package org.infobip.conversations.statistics.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.utils.LongUtils;
import org.infobip.conversations.communicationreviews.repository.model.CommunicationReview;
import org.infobip.conversations.communications.models.UserCommunication;
import org.infobip.conversations.communications.repository.CommunicationRepository;
import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.communications.service.CommunicationService;
import org.infobip.conversations.statistics.models.StatisticsOverview;
import org.infobip.conversations.statistics.repository.StatisticsRepository;
import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.service.UserService;
import org.infobip.conversations.users.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static org.infobip.conversations.common.Constant.SUCCESS;

@RestController
@RequestMapping(value = "/api/statistics")
public class StatisticsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommunicationService communicationService;
    private UserService userService;

    @Autowired
    private StatisticsRepository statisticsRepository;
   /*
    * STATISTICS ROUTES
    * */

   // average duration (end_time - start_time) by company, types, agents, and date
      //1. avg duration for agents
      //2. avg duration for company
      //3. avg duration for date range
      //4. avg duration for agents in company in specific date range
//date se prosljeđuje u formatu: yyyy-mm-dd hh:mm:ss.sss, npr: 2012-05-16 00:00:00.000
   @GetMapping("/avgCommunicationDuration")
   public ResponseEntity<Response> getAverageDuration(@RequestParam Map<String, String> queryParameters) {
      Long companyId = LongUtils.stringToLong(queryParameters.getOrDefault("companyId", null));
      Long agentId = LongUtils.stringToLong(queryParameters.getOrDefault("agentId", null));
      Timestamp fromDate = Timestamp.valueOf((queryParameters.getOrDefault("fromDate", null)));
      Timestamp toDate = Timestamp.valueOf((queryParameters.getOrDefault("toDate", null)));

      Float average = statisticsRepository.findAverageDurationInSeconds(companyId, agentId, fromDate, toDate);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(average), HttpStatus.OK);
   }

   // number of calls in specific date range for one company (frequency)

   @GetMapping("/communicationCount")
   public ResponseEntity<Response> getCommunicationCountForPeriod(@RequestParam Map<String, String> queryParameters) {
      Long companyId = LongUtils.stringToLong(queryParameters.getOrDefault("companyId", null));
      Long agentId = LongUtils.stringToLong(queryParameters.getOrDefault("agentId", null));
      Timestamp fromDate = Timestamp.valueOf((queryParameters.getOrDefault("fromDate", null)));
      Timestamp toDate = Timestamp.valueOf((queryParameters.getOrDefault("toDate", null)));

      Long average = statisticsRepository.findCommunicationCountForPeriod(companyId, agentId, fromDate, toDate);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(average), HttpStatus.OK);
   }

   @GetMapping("/company")
   public ResponseEntity<Response> getAverageRatingForCompanyByCommunicationType(@RequestParam Map<String, String> queryParameters) {
      Long companyId = LongUtils.stringToLong(queryParameters.getOrDefault("companyId", null));
      Long typeId = LongUtils.stringToLong(queryParameters.getOrDefault("typeId", null));
      Float average = statisticsRepository.findAverageRatingForCompanybyCommunicationType(companyId, typeId);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(average), HttpStatus.OK);
   }


   // statistics for home page
   @GetMapping("/overview")
   public ResponseEntity<Response> getStatisticsOverviewForHomePage() {
      boolean isSuperAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.SuperAdmin);
      boolean isCompanyAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.CompanyAdmin);
      boolean isAgent = SecurityUtils.loggedInUserHasRole(AvailableRoles.Agent);

      List<StatisticsOverview> statisticsOverviews = null;
      if (isSuperAdmin) {
         statisticsOverviews = statisticsRepository.findAllStatisticOverviewsForCompanyOrAgent(null, null);
      } else if (isCompanyAdmin) {
         User user = this.userService.getUserWithAuthorities().get();
         // Check company of the user and pass it to get statistics for company
         if (user.getCompany() != null) {
            statisticsOverviews = statisticsRepository.findAllStatisticOverviewsForCompanyOrAgent(user.getCompany().getId(), null);
         } else {
            return ResponseEntity
               .status(HttpStatus.BAD_REQUEST)
               .body(new Response(ResultCode.ERROR, "Invalid user company"));
         }
      } else if(isAgent) {
         User user = this.userService.getUserWithAuthorities().get();
         statisticsOverviews = statisticsRepository.findAllStatisticOverviewsForCompanyOrAgent(null, user.getId());
      }
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(statisticsOverviews), HttpStatus.OK);
   }

   @GetMapping("/callsByMonths")
   public ResponseEntity<Response> getCallsByMonthsForCurrentYear() {
      boolean isSuperAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.SuperAdmin);
      boolean isCompanyAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.CompanyAdmin);
      boolean isAgent = SecurityUtils.loggedInUserHasRole(AvailableRoles.Agent);

      List<Long> numberOfCalls = null;
      if (isSuperAdmin) {
         numberOfCalls = statisticsRepository.findAllCallsByMonthsForCurrentYearForCompanyOrAgent(null, null);
      } else if (isCompanyAdmin) {
         User user = this.userService.getUserWithAuthorities().get();
         // Check company of the user and pass it to get statistics for company
         if (user.getCompany() != null) {
            numberOfCalls = statisticsRepository.findAllCallsByMonthsForCurrentYearForCompanyOrAgent(user.getCompany().getId(), null);
         } else {
            return ResponseEntity
               .status(HttpStatus.BAD_REQUEST)
               .body(new Response(ResultCode.ERROR, "Invalid user company"));
         }
      } else if(isAgent) {
         User user = this.userService.getUserWithAuthorities().get();
         numberOfCalls = statisticsRepository.findAllCallsByMonthsForCurrentYearForCompanyOrAgent(null, user.getId());
      }
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(numberOfCalls), HttpStatus.OK);
   }


}
