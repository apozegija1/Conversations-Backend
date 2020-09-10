package org.infobip.conversations.communications.service;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.model.Message;
import org.infobip.conversations.common.model.MessageType;
import org.infobip.conversations.common.service.MessageService;
import org.infobip.conversations.communications.models.AvailableCommunicationType;
import org.infobip.conversations.communications.models.UserCommunication;
import org.infobip.conversations.communications.repository.CommunicationRepository;
import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.communications.utils.MessageUtils;
import org.infobip.conversations.communicationtypes.repository.CommunicationTypeRepository;
import org.infobip.conversations.communicationtypes.repository.model.CommunicationType;
import org.infobip.conversations.companies.repository.model.Company;
import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.service.UserService;
import org.infobip.conversations.users.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.sql.Timestamp;
import java.util.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class CommunicationService {
   private final CommunicationRepository communicationRepository;
   private final CommunicationTypeRepository communicationTypeRepository;
   private final MessageService messageService;
   private final UserService userService;

   public CommunicationService(CommunicationRepository communicationRepository,
                               CommunicationTypeRepository communicationTypeRepository,
                               UserService userService,
                               MessageService messageService) {
      this.communicationRepository = communicationRepository;
      this.communicationTypeRepository = communicationTypeRepository;
      this.userService = userService;
      this.messageService = messageService;
   }

   public Communication save(Communication communication) {
      Optional<CommunicationType> oType = communicationTypeRepository.findOneByType(communication.getType().getType());
      if (oType.isEmpty()) {
         throw new IllegalArgumentException("Invalid communication type");
      }

      CommunicationType type = oType.get();
      communication.setType(type);
      communication.setEndTime(new Timestamp(System.currentTimeMillis()));

      // Check if type is sms and if user has entered his phone number
      if (type.getType().equals(AvailableCommunicationType.SMS.name())) {
         if (communication.getCustomer().getPhone() == null) {
            throw new IllegalArgumentException("User doesn't have phone to which to send message");
         }

         Response response = this.sendMessage(type, communication);
         // If there was error in sending message return exception
         if (response.status == ResultCode.ERROR) {
            throw new ResolutionException(response.message);
         }
      }

      return communicationRepository.save(communication);
   }

   public List<UserCommunication> findAllCommunicationsForUser() throws Exception {
      Optional<User> oCurrentUser = this.userService.getUserWithAuthorities();
      if (oCurrentUser.isEmpty()) {
         throw new Exception("Trying to get communication for wrong user");
      }

      User currentUser = oCurrentUser.get();
      boolean isAgent = SecurityUtils.userHasRole(currentUser, AvailableRoles.Agent);
      boolean isCompanyAdmin = SecurityUtils.userHasRole(currentUser, AvailableRoles.CompanyAdmin);
      boolean isCustomerUser = SecurityUtils.userHasRole(currentUser, AvailableRoles.User);
      Map<User, List<Communication>> userCommunicationsMapping = new HashMap<>();

      if (isAgent || isCompanyAdmin) {
         List<Communication> communications = new ArrayList<>();
         if (isAgent) {
            communications = communicationRepository.findAllCommunicationsForUser(currentUser.getId(), null);
         } else {
            Company adminCompany = this.userService.getCurrentUserCompany();
            if (adminCompany != null) {
               communications = communicationRepository.findAllCommunicationsForCompany(adminCompany.getId());
            }
         }

         userCommunicationsMapping = communications.stream()
            .collect(groupingBy(Communication::getCustomer));
      } else if (isCustomerUser) {
         List<Communication> communications = communicationRepository.findAllCommunicationsForUser(null, currentUser.getId());
         userCommunicationsMapping = communications.stream()
            .collect(groupingBy(Communication::getAgent));
      }

      return userCommunicationsMapping
         .entrySet()
         .stream()
         .map((s-> new UserCommunication(s.getKey(), s.getValue())))
         .collect(toList());
   }

   private Response sendMessage(CommunicationType type, Communication communication) {
      MessageType messageType = MessageUtils.getMessageTypeFromCommunicationType(type);
      String agentCompany = communication.getAgent().getCompany().getName();
      String userPhone = communication.getCustomer().getPhone();

      String messageId = UUID.randomUUID().toString();
      Message message = new Message(messageId, agentCompany, userPhone,
         "", communication.getText());
      communication.setMessageId(messageId);
      return this.messageService.sendMessage(messageType, message);
   }
}
