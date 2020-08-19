package org.infobip.conversations.users.service;

import org.infobip.conversations.users.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

   private final UserRepository userRepository;

   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Transactional(readOnly = true)
   public Optional<User> getUserWithAuthorities() {
      return SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
   }
}
