package org.infobip.conversations.communications.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.model.WebRtcTokenResponse;
import org.infobip.conversations.communications.models.UserCommunication;
import org.infobip.conversations.communications.repository.CommunicationRepository;
import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.communications.service.CommunicationService;
import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.UserRepository;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.service.UserService;
import org.infobip.conversations.users.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.infobip.conversations.common.Constant.SUCCESS;

@RestController
@RequestMapping(value = "/api/communications")
public class CommunicationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommunicationService communicationService;

    private final UserService userService;

    private final CommunicationRepository communicationRepository;

    private final UserRepository userRepository;

    public CommunicationController(CommunicationService communicationService,
                                   UserService userService,
                                   CommunicationRepository communicationRepository,
                                   UserRepository userRepository) {
       this.communicationService = communicationService;
       this.userService = userService;
       this.communicationRepository = communicationRepository;
       this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody Communication communication) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationService.save(communication)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Response> update(@RequestBody Communication communication) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationRepository.save(communication)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> read(@PathVariable Long id) {
        logger.info("id : " + id);
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationRepository.findById(id)), HttpStatus.OK);
    }

   @DeleteMapping("/{id}")
   public void delete(@PathVariable Long id) {
      communicationRepository.deleteById(id);
   }

   @GetMapping("/users")
   public ResponseEntity<Response> getAllCommunicationsForUser() throws Exception {
       List<UserCommunication> userCommunications = communicationService.findAllCommunicationsForUser();
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(userCommunications), HttpStatus.OK);
   }

   //all communications for company by role, as this is not pageable we use /all route
   @GetMapping("/all")
   public ResponseEntity<Response> readAll() {
      boolean isSuperAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.SuperAdmin);
      boolean isCompanyAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.CompanyAdmin);
      List<Communication> communications = null;
      if (isSuperAdmin) {
         communications = communicationRepository.findAll();
      } else if (isCompanyAdmin) {
         User user = this.userService.getUserWithAuthorities().get();
         // Check company of the user and pass it to get users for company
         if (user.getCompany() != null) {
            communications = communicationRepository.findAllCommunicationsForCompany(user.getCompany().getId());
         } else {
            return ResponseEntity
               .status(HttpStatus.BAD_REQUEST)
               .body(new Response(ResultCode.ERROR, "Invalid user company"));
         }
      }
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(communications), HttpStatus.OK);
   }

   @GetMapping("/webrtc/{userId}")
   public ResponseEntity<Response> getWebrtcToken(@PathVariable Long userId) throws Exception {
      Optional<User> oUser = userRepository.findById(userId);
      if (oUser.isEmpty()) {
         new ResponseEntity<>(new Response(ResultCode.ERROR, "Invalid user"), HttpStatus.NOT_FOUND);
      }

      WebRtcTokenResponse response = this.communicationService.getWebrtcToken(oUser.get());
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(response), HttpStatus.OK);
   }
}
