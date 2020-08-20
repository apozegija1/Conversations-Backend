package org.infobip.conversations.communicationreviews.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.communicationreviews.model.CommunicationReview;
import org.infobip.conversations.communicationreviews.model.CommunicationReviewRepository;
import org.infobip.conversations.communicationreviews.service.CommunicationReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.infobip.conversations.common.Constant.SUCCESS;

@RestController
@RequestMapping(value = "/api/communicationreview")
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


}
