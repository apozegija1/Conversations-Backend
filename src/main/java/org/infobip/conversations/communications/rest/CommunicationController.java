package org.infobip.conversations.communications.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.utils.LongUtils;
import org.infobip.conversations.communications.repository.CommunicationRepository;
import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.communications.service.CommunicationService;
import org.infobip.conversations.communicationtypes.repository.CommunicationTypeRepository;
import org.infobip.conversations.communicationtypes.repository.model.CommunicationType;
import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.UserRepository;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.service.UserService;
import org.infobip.conversations.users.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.infobip.conversations.common.Constant.SUCCESS;

@RestController
@RequestMapping(value = "/api/communications")
public class CommunicationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommunicationService communicationService;
    private UserService userService;

    @Autowired
    private CommunicationRepository communicationRepository;

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody Communication communication) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationRepository.save(communication)), HttpStatus.OK);
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

    /*@GetMapping
    public ResponseEntity<Response> readAll(Pageable pageable) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationRepository.findAll(pageable)), HttpStatus.OK);
    }*/

   @DeleteMapping("/{id}")
   public void delete(@PathVariable Long id) {
      communicationRepository.deleteById(id);
   }


   // If agent is null, we will get all communications for a specific user
   // If customer is null, we will get all communications for agent

   @GetMapping("/users")
   public ResponseEntity<Response> getAllCommunicationsForUser(@RequestParam Map<String, String> queryParameters) {
       Long agentId = LongUtils.stringToLong(queryParameters.getOrDefault("agentId", null));
       Long customerId = LongUtils.stringToLong(queryParameters.getOrDefault("customerId", null));
       List<Communication> list = communicationRepository.findAllCommunicationsForUser(agentId, customerId);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(list), HttpStatus.OK);
   }


   //all communications for company by role

   @GetMapping()
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
   
   /*
    * STATISTICS ROUTES
    * */

   // average duration (end_time - start_time) by company, types, agents, and date
      //1. avg duration for agents
      //2. avg duration for company
      //3. avg duration for date range
      //4. avg duration for agents in company in specific date range
//date se prosljeđuje u formatu: yyyy-mm-dd hh:mm:ss.sss, npr: 2012-05-16 00:00:00.000
   @GetMapping("/statistics/avgduration")
   public ResponseEntity<Response> getAverageDuration(@RequestParam Map<String, String> queryParameters) {
      Long companyId = LongUtils.stringToLong(queryParameters.getOrDefault("companyId", null));
      Long agentId = LongUtils.stringToLong(queryParameters.getOrDefault("agentId", null));
      Timestamp fromDate = Timestamp.valueOf((queryParameters.getOrDefault("fromDate", null)));
      Timestamp toDate = Timestamp.valueOf((queryParameters.getOrDefault("toDate", null)));

      Float average = communicationRepository.findAverageDurationInSeconds(companyId, agentId, fromDate, toDate);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(average), HttpStatus.OK);
   }

   // number of calls in specific date range for one company (frequency)

   @GetMapping("/statistics/count")
   public ResponseEntity<Response> getCommunicationCountForPeriod(@RequestParam Map<String, String> queryParameters) {
      Long companyId = LongUtils.stringToLong(queryParameters.getOrDefault("companyId", null));
      Long agentId = LongUtils.stringToLong(queryParameters.getOrDefault("agentId", null));
      Timestamp fromDate = Timestamp.valueOf((queryParameters.getOrDefault("fromDate", null)));
      Timestamp toDate = Timestamp.valueOf((queryParameters.getOrDefault("toDate", null)));

      Long average = communicationRepository.findCommunicationCountForPeriod(companyId, agentId, fromDate, toDate);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(average), HttpStatus.OK);
   }
}
