package org.infobip.conversations.communicationreviews.service;

import org.infobip.conversations.communicationreviews.repository.CommunicationReviewRepository;
import org.infobip.conversations.communicationreviews.repository.model.CommunicationReview;
import org.infobip.conversations.companies.repository.model.Company;
import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.service.UserService;
import org.infobip.conversations.users.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommunicationReviewService {
   private final CommunicationReviewRepository communicationReviewRepository;
   private UserService userService;

   public CommunicationReviewService(CommunicationReviewRepository communicationReviewRepository) {
      this.communicationReviewRepository = communicationReviewRepository;
   }

   public List<CommunicationReview> findAllCommunicationReviewsForUser() throws Exception {
      Optional<User> oCurrentUser = this.userService.getUserWithAuthorities();
      if (oCurrentUser.isEmpty()) {
         throw new Exception("Trying to get communication review for wrong user");
      }

      User currentUser = oCurrentUser.get();
      boolean isAgent = SecurityUtils.userHasRole(currentUser, AvailableRoles.Agent);
      boolean isCompanyAdmin = SecurityUtils.userHasRole(currentUser, AvailableRoles.CompanyAdmin);
      boolean isCustomerUser = SecurityUtils.userHasRole(currentUser, AvailableRoles.User);
      List<CommunicationReview> communicationReviewsList = new ArrayList<>();

      if (isAgent || isCompanyAdmin) {
         if (isAgent) {
            communicationReviewsList = communicationReviewRepository.findAllCommunicationReviewsForUser(currentUser.getId(), null);
         } else {
            Company adminCompany = this.userService.getCurrentUserCompany();
            if (adminCompany != null) {
               communicationReviewsList = communicationReviewRepository.findAllCommunicationReviewsForCompany(adminCompany.getId());
            }
         }
      } else if (isCustomerUser) {
         communicationReviewsList = communicationReviewRepository.findAllCommunicationReviewsForUser(null, currentUser.getId());
      }
      return communicationReviewsList;
   }

}
