package org.infobip.conversations.users.service;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.infobip.conversations.users.security.UserNotActivatedException;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserModelDetailsService implements UserDetailsService {

   private final Logger log = LoggerFactory.getLogger(UserModelDetailsService.class);

   private final UserRepository userRepository;

   public UserModelDetailsService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String login) {
      log.debug("Authenticating user '{}'", login);

      if (new EmailValidator().isValid(login, null)) {
         return userRepository.findOneWithAuthoritiesByEmailIgnoreCase(login)
            .map(user -> createSpringSecurityUser(login, user))
            .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
      }

      return userRepository.findOneWithAuthoritiesByUsername(login)
         .map(user -> createSpringSecurityUser(login, user))
         .orElseThrow(() -> new UsernameNotFoundException("User " + login + " was not found in the database"));

   }

   private org.springframework.security.core.userdetails.User createSpringSecurityUser(String login, User user) {
      if (!user.isActivated()) {
         throw new UserNotActivatedException("User " + login + " was not activated");
      }

      List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
         .map(authority -> new SimpleGrantedAuthority(authority.getName()))
         .collect(Collectors.toList());

      return new org.springframework.security.core.userdetails.User(user.getUsername(),
         user.getPassword(),
         grantedAuthorities);
   }
}
