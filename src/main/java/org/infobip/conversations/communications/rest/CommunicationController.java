package org.infobip.conversations.communications.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.utils.LongUtils;
import org.infobip.conversations.communications.repository.CommunicationRepository;
import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.communications.service.CommunicationService;
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
@RequestMapping(value = "/api/communications")
public class CommunicationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommunicationService communicationService;

    @Autowired
    private CommunicationRepository communicationRepository;

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody Communication communication) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationRepository.save(communication)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Response> update(@RequestBody Communication communication) {
        Optional<Communication> dbAdmin = communicationRepository.findById(communication.getId());

        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationRepository.save(communication)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> read(@PathVariable Long id) {
        logger.info("id : " + id);
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationRepository.findById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> readAll(Pageable pageable) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationRepository.findAll(pageable)), HttpStatus.OK);
    }

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

}
