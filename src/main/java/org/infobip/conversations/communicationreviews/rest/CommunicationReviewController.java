package org.infobip.conversations.communicationreviews.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.utils.LongUtils;
import org.infobip.conversations.communicationreviews.repository.model.CommunicationReview;
import org.infobip.conversations.communicationreviews.repository.CommunicationReviewRepository;
import org.infobip.conversations.communicationreviews.service.CommunicationReviewService;
import org.infobip.conversations.communications.repository.model.Communication;
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

    @GetMapping
    public ResponseEntity<Response> readAll(Pageable pageable) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationReviewRepository.findAll(pageable)), HttpStatus.OK);
    }

   @DeleteMapping("/{id}")
   public void delete(@PathVariable Long id) {
      communicationReviewRepository.deleteById(id);
   }


   @GetMapping("/users")
   public ResponseEntity<Response> getAllReviewsForUser(@RequestParam Map<String, String> queryParameters) {
      Long agentId = LongUtils.stringToLong(queryParameters.getOrDefault("agentId", null));
      Long customerId = LongUtils.stringToLong(queryParameters.getOrDefault("customerId", null));
      List<CommunicationReview> list = communicationReviewRepository.findAllReviewsForUser(agentId, customerId);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(list), HttpStatus.OK);
   }

   @GetMapping("/usersByUsername")
   public ResponseEntity<Response> getAllReviewsForUserByUsername(@RequestParam Map<String, String> queryParameters) {
      String agentUsername = queryParameters.getOrDefault("agentUsername", null);
      String customerUsername = queryParameters.getOrDefault("customerUsername", null);
      List<CommunicationReview> list = communicationReviewRepository.findAllReviewsForUserByUsername(agentUsername, customerUsername);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(list), HttpStatus.OK);
   }


   /*
   * STATISTICS ROUTES
   * */

   // average ratings by company and types
      //1. ratings for company
      //2. ratings for types
      //3. ratings for types in company
   @GetMapping("/statistics/company")
   public ResponseEntity<Response> getAverageRatingForCompanyType(@RequestParam Map<String, String> queryParameters) {
      Long companyId = LongUtils.stringToLong(queryParameters.getOrDefault("companyId", null));
      Long typeId = LongUtils.stringToLong(queryParameters.getOrDefault("typeId", null));
      Float average = communicationReviewRepository.findAverageRatingForCompany(companyId, typeId);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(average), HttpStatus.OK);
   }


}
