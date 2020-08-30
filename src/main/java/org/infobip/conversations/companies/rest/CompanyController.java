package org.infobip.conversations.companies.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.companies.repository.model.Company;
import org.infobip.conversations.companies.repository.CompanyRepository;
import org.infobip.conversations.companies.service.CompanyService;
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
@RequestMapping(value = "/api/companies")
public class CompanyController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody Company company) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(companyRepository.save(company)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> read(@PathVariable Long id) {
        logger.info("id : " + id);
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(companyRepository.findById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> readAll(Pageable pageable) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
           .setResult(companyRepository.findAll(pageable)), HttpStatus.OK);
    }

   @GetMapping("/all")
   public ResponseEntity<Response> readAllCompanies() {
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(companyRepository.findAll()), HttpStatus.OK);
   }

   @PutMapping
   public ResponseEntity<Response> update(@RequestBody Company company) {
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(companyRepository.save(company)), HttpStatus.OK);
   }

   @DeleteMapping("/{id}")
   public void delete(@PathVariable Long id) {
      companyRepository.deleteById(id);
   }
}
