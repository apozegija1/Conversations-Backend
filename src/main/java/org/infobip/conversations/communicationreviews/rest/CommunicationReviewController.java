package org.infobip.conversations.communicationreviews.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.utils.LongUtils;
import org.infobip.conversations.communicationreviews.repository.CommunicationReviewRepository;
import org.infobip.conversations.communicationreviews.repository.model.CommunicationReview;
import org.infobip.conversations.communicationreviews.service.CommunicationReviewService;
import org.infobip.conversations.communications.models.UserCommunication;
import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.service.UserService;
import org.infobip.conversations.users.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.infobip.conversations.common.Constant.SUCCESS;

@RestController
@RequestMapping(value = "/api/communicationreviews")
public class CommunicationReviewController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserService userService;

    @Autowired
    private CommunicationReviewService communicationReviewService;

    @Autowired
    private CommunicationReviewRepository communicationReviewRepository;

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody CommunicationReview communicationReview) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationReviewRepository.save(communicationReview)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Response> update(@RequestBody CommunicationReview communicationReview) {
        Optional<CommunicationReview> dbAdmin = communicationReviewRepository.findById(communicationReview.getId());

        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationReviewRepository.save(communicationReview)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> read(@PathVariable Long id) {
        logger.info("id : " + id);
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationReviewRepository.findById(id)), HttpStatus.OK);
    }

   @DeleteMapping("/{id}")
   public void delete(@PathVariable Long id) {
      communicationReviewRepository.deleteById(id);
   }

   @GetMapping("/users")
   public ResponseEntity<Response> getAllCommunicationReviewsForUser() throws Exception {
      List<CommunicationReview> userCommunicationReviews = communicationReviewService.findAllCommunicationReviewsForUser();
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(userCommunicationReviews), HttpStatus.OK);
   }


   //all communicationreviews for company by role
   @GetMapping()
   public ResponseEntity<Response> readAll() {
      boolean isSuperAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.SuperAdmin);
      boolean isCompanyAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.CompanyAdmin);
      List<CommunicationReview> communicationReviews = null;
      if (isSuperAdmin) {
         communicationReviews = communicationReviewRepository.findAll();
      } else if (isCompanyAdmin) {
         User user = this.userService.getUserWithAuthorities().get();
         // Check company of the user and pass it to get reviews for company
         if (user.getCompany() != null) {
            communicationReviews = communicationReviewRepository.findAllCommunicationReviewsForCompany(user.getCompany().getId());
         } else {
            return ResponseEntity
               .status(HttpStatus.BAD_REQUEST)
               .body(new Response(ResultCode.ERROR, "Invalid user company"));
         }
      }
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(communicationReviews), HttpStatus.OK);
   }


}
