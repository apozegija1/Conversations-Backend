package org.infobip.conversations.users.security.rest;

import org.infobip.conversations.users.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.infobip.conversations.users.security.repository.model.User;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

   private final UserService userService;

   public UserRestController(UserService userService) {
      this.userService = userService;
   }

   @GetMapping("/user")
   public ResponseEntity<User> getActualUser() {
      return ResponseEntity.ok(userService.getUserWithAuthorities().get());
   }

   @GetMapping("/users")
   public ResponseEntity<List<User>> get() {
      return ResponseEntity.ok(userService.get());
   }
}
