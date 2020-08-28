package org.infobip.conversations.users.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.utils.LongUtils;
import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.companies.repository.CompanyRepository;
import org.infobip.conversations.companies.repository.model.Company;
import org.infobip.conversations.users.repository.UserRepository;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.service.UserService;
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
@RequestMapping("/api")
public class UserRestController {

   private final UserService userService;

   @Autowired
   private UserRepository userRepository;
   private CompanyRepository companyRepository;

   public UserRestController(UserService userService) {
      this.userService = userService;
   }

   @GetMapping("/user")
   public ResponseEntity<User> getActualUser() {
      return ResponseEntity.ok(userService.getUserWithAuthorities().get());
   }

   @GetMapping("/user/companies")
   public ResponseEntity<List<User>> getCompanyUsers() {
      return ResponseEntity.ok(userService.getCompanyUsers());
   }

   @PostMapping
   public ResponseEntity<Response> create(@RequestBody User user) {
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(userRepository.save(user)), HttpStatus.OK);
   }

   @PostMapping("/users/role/{roleName}")
   public ResponseEntity<Response> createUserWithRole(@RequestBody User user, @PathVariable String roleName) {
      User savedUser = userService.saveUserWithRole(user, roleName);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(savedUser), HttpStatus.OK);
   }

   @PutMapping("/users")
   public ResponseEntity<Response> update(@RequestBody User user) {
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(userRepository.save(user)), HttpStatus.OK);
   }

   @GetMapping("/users/{id}")
   public ResponseEntity<Response> read(@PathVariable Long id) {
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(userRepository.findById(id)), HttpStatus.OK);
   }

   @GetMapping("/users")
   public ResponseEntity<Response> readAll(Pageable pageable) {
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(userRepository.findAll(pageable)), HttpStatus.OK);
   }

   @DeleteMapping("/users/{id}")
   public void delete(@PathVariable Long id) {
      userRepository.deleteById(id);
   }

   //all users in company
   @GetMapping("/users/company/{id}")
   public ResponseEntity<Response> getAllUsersForCompany(@PathVariable Long id) {
      List<User> list = userRepository.findAllUsersForCompany(id);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(list), HttpStatus.OK);
   }

}
