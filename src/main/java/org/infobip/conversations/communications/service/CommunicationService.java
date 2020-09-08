package org.infobip.conversations.communications.service;

import org.infobip.conversations.common.model.MessageType;
import org.infobip.conversations.common.service.MessageService;
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

      MessageType messageType = MessageUtils.getMessageTypeFromCommunicationType(type);
      String agentCompany = communication.getAgent().getCompany().getName();
      this.messageService.sendMessage(messageType, agentCompany, agentCompany,
         "", communication.getText());
      communication.setType(type);
      communication.setEndTime(new Timestamp(System.currentTimeMillis()));
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
}
