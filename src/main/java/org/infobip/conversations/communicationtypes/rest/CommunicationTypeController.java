package org.infobip.conversations.communicationtypes.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.communicationtypes.repository.CommunicationTypeRepository;
import org.infobip.conversations.communicationtypes.repository.model.CommunicationType;
import org.infobip.conversations.communicationtypes.service.CommunicationTypeService;
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
@RequestMapping(value = "/api/communicationtypes")
public class CommunicationTypeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommunicationTypeService communicationTypeService;

    @Autowired
    private CommunicationTypeRepository communicationTypeRepository;

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody CommunicationType communicationType) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationTypeRepository.save(communicationType)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Response> update(@RequestBody CommunicationType communicationType) {
        Optional<CommunicationType> dbAdmin = communicationTypeRepository.findById(communicationType.getId());

        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationTypeRepository.save(communicationType)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> read(@PathVariable Long id) {
        logger.info("id : " + id);
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationTypeRepository.findById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> readAll(Pageable pageable) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(communicationTypeRepository.findAll(pageable)), HttpStatus.OK);
    }

   @DeleteMapping("/{id}")
   public void delete(@PathVariable Long id) {
      communicationTypeRepository.deleteById(id);
   }
}
