package org.infobip.conversations.communications.service;

import org.infobip.conversations.communications.models.UserCommunication;
import org.infobip.conversations.communications.repository.CommunicationRepository;
import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.companies.repository.model.Company;
import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.service.UserService;
import org.infobip.conversations.users.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class CommunicationService {
   private final CommunicationRepository communicationRepository;

   private final UserService userService;

   public CommunicationService(CommunicationRepository communicationRepository,
                               UserService userService) {
      this.communicationRepository = communicationRepository;
      this.userService = userService;
   }

   List<UserCommunication> findAllCommunicationsForUser(Long agentId, Long customerId, Pageable pageable) {
      boolean isAgent = SecurityUtils.loggedInUserHasRole(AvailableRoles.Agent);
      boolean isCompanyAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.CompanyAdmin);
      boolean isCustomerUser = SecurityUtils.loggedInUserHasRole(AvailableRoles.User);
      List<UserCommunication> userCommunications = new ArrayList<>();
      if (isAgent || isCompanyAdmin) {
         List<Communication> communications;
         if (isAgent) {
            communications = communicationRepository.findAllCommunicationsForUser(agentId, null);
         } else {
            Company adminCompany = this.userService.getCurrentUserCompany();
            communications = communicationRepository.findAllCommunicationsForCompany(adminCompany.getId());
         }

         userCommunications = communications.stream()
            .collect(groupingBy(Communication::getAgent))
            .entrySet()
            .stream()
            .map((s-> new UserCommunication(s.getKey(), s.getValue())))
            .collect(toList());
      } else if (isCustomerUser) {
         List<Communication> list = communicationRepository.findAllCommunicationsForUser(null, customerId);
         userCommunications = list.stream()
            .collect(groupingBy(Communication::getCustomer))
            .entrySet()
            .stream()
            .map((s-> new UserCommunication(s.getKey(), s.getValue())))
            .collect(toList());
      }
      return userCommunications;
   }


}
