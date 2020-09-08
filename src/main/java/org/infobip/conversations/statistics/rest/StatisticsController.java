package org.infobip.conversations.statistics.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.utils.LongUtils;
import org.infobip.conversations.communicationreviews.repository.CommunicationReviewRepository;
import org.infobip.conversations.communications.repository.CommunicationRepository;
import org.infobip.conversations.statistics.models.IStatisticsOverview;
import org.infobip.conversations.statistics.models.StatisticsOverview;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.infobip.conversations.common.Constant.SUCCESS;

@RestController
@RequestMapping(value = "/api/statistics")
public class StatisticsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

   @Autowired
    private CommunicationRepository communicationRepository;
   @Autowired
   private CommunicationReviewRepository communicationReviewRepository;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private UserService userService;

   //date se prosljeđuje u formatu: yyyy-mm-dd hh:mm:ss.sss, npr: 2012-05-16 00:00:00.000

   @GetMapping("/avgCommunicationDuration")
   public ResponseEntity<Response> getAverageDuration(@RequestParam Map<String, String> queryParameters) {
      Long companyId = LongUtils.stringToLong(queryParameters.getOrDefault("companyId", null));
      Long agentId = LongUtils.stringToLong(queryParameters.getOrDefault("agentId", null));
      Timestamp fromDate = Timestamp.valueOf((queryParameters.getOrDefault("fromDate", null)));
      Timestamp toDate = Timestamp.valueOf((queryParameters.getOrDefault("toDate", null)));

      Float average = communicationRepository.findAverageDurationInSeconds(companyId, agentId, fromDate, toDate);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(average), HttpStatus.OK);
   }

   // number of calls in specific date range for one company (frequency)

   @GetMapping("/communicationCount")
   public ResponseEntity<Response> getCommunicationCountForPeriod(@RequestParam Map<String, String> queryParameters) {
      Long companyId = LongUtils.stringToLong(queryParameters.getOrDefault("companyId", null));
      Long agentId = LongUtils.stringToLong(queryParameters.getOrDefault("agentId", null));
      Timestamp fromDate = Timestamp.valueOf((queryParameters.getOrDefault("fromDate", null)));
      Timestamp toDate = Timestamp.valueOf((queryParameters.getOrDefault("toDate", null)));

      Long average = communicationRepository.findCommunicationCountForPeriod(companyId, agentId, fromDate, toDate);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(average), HttpStatus.OK);
   }

   @GetMapping("/company")
   public ResponseEntity<Response> getAverageRatingForCompanyByCommunicationType(@RequestParam Map<String, String> queryParameters) {
      Long companyId = LongUtils.stringToLong(queryParameters.getOrDefault("companyId", null));
      Long typeId = LongUtils.stringToLong(queryParameters.getOrDefault("typeId", null));
      Float average = communicationReviewRepository.findAverageRatingForCompanybyCommunicationType(companyId, typeId);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(average), HttpStatus.OK);
   }


   // statistics for home page
   @GetMapping("/overview")
   public ResponseEntity<Response> getStatisticsOverviewForHomePage() {
      boolean isSuperAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.SuperAdmin);


      List<IStatisticsOverview> statisticsOverviews = new ArrayList<>();
      if (isSuperAdmin) {
         //data for superAdmin: number od users, number of companies and average number of registered users in this year
            List<Integer> statisticsOverviewsForSuperAgent = null;
         statisticsOverviewsForSuperAgent = userRepository.findAllStatisticOverviewsForSuperAgent();
         System.out.println(statisticsOverviewsForSuperAgent + " << ");
         return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
            .setResult(statisticsOverviewsForSuperAgent), HttpStatus.OK);
      } else {
         boolean isCompanyAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.CompanyAdmin);
         boolean isAgent = SecurityUtils.loggedInUserHasRole(AvailableRoles.Agent);
         if (isCompanyAdmin) {
            User user = this.userService.getUserWithAuthorities().get();
            // Check company of the user and pass it to get statistics for company
            if (user.getCompany() != null) {
               statisticsOverviews = communicationRepository.findAllStatisticOverviewsForCompanyOrAgent(user.getCompany().getId(), null);
            } else {
               return ResponseEntity
                  .status(HttpStatus.BAD_REQUEST)
                  .body(new Response(ResultCode.ERROR, "Invalid user company"));
            }
         } else if (isAgent) {
            User user = this.userService.getUserWithAuthorities().get();
            statisticsOverviews = communicationRepository.findAllStatisticOverviewsForCompanyOrAgent(null, user.getId());
         }

         return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
            .setResult(statisticsOverviews), HttpStatus.OK);
      }
   }

   @GetMapping("/chartOverview")
   public ResponseEntity<Response> getNumberOfElementsForChartOnHomepage() {
      boolean isSuperAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.SuperAdmin);
      boolean isCompanyAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.CompanyAdmin);
      boolean isAgent = SecurityUtils.loggedInUserHasRole(AvailableRoles.Agent);

      List<Long> elementsCount = null;
      if (isSuperAdmin) {
         //superAdmin can only see how many users registered by month
         elementsCount = userRepository.findAllUsersByMonthsForCurrentYear();
      } else if (isCompanyAdmin) {
         //company can see how many calls in company are there by month, and agent can see only his calls by months
         User user = this.userService.getUserWithAuthorities().get();
         // Check company of the user and pass it to get statistics for company
         if (user.getCompany() != null) {
            elementsCount = communicationRepository.findAllCallsByMonthsForCurrentYear(user.getCompany().getId(), null);
         } else {
            return ResponseEntity
               .status(HttpStatus.BAD_REQUEST)
               .body(new Response(ResultCode.ERROR, "Invalid user company"));
         }
      } else if(isAgent) {
         User user = this.userService.getUserWithAuthorities().get();
         elementsCount = communicationRepository.findAllCallsByMonthsForCurrentYear(null, user.getId());
      }

      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(elementsCount), HttpStatus.OK);

   }


}
