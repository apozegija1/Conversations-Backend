package org.infobip.conversations.users.rest;

import org.infobip.conversations.users.repository.UserRepository;
import org.infobip.conversations.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.infobip.conversations.users.repository.model.User;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

   private final UserService userService;

   @Autowired
   private UserRepository userRepository;

   public UserRestController(UserService userService) {
      this.userService = userService;
   }

   @GetMapping("/user")
   public ResponseEntity<User> getActualUser() {
      return ResponseEntity.ok(userService.getUserWithAuthorities().get());
   }

   @GetMapping("/users")
   public ResponseEntity<List<User>> get() {
      return ResponseEntity.ok(userRepository.findAll());
   }
}
