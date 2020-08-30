package org.infobip.conversations.users.utils;

import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class SecurityUtils {

   private static final Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);

   private SecurityUtils() {
   }

   /**
    * Get the login of the current user.
    *
    * @return the login of the current user.
    */
   public static Optional<String> getCurrentUsername() {
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null) {
         LOG.debug("no authentication in security context found");
         return Optional.empty();
      }

      String username = null;
      if (authentication.getPrincipal() instanceof UserDetails) {
         UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
         username = springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
         username = (String) authentication.getPrincipal();
      }

      LOG.debug("found username '{}' in security context", username);

      return Optional.ofNullable(username);
   }

   public static boolean loggedInUserHasRole(AvailableRoles role) {
      Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)
         SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities();

      return authorities.stream()
         .anyMatch(a -> a.getAuthority().equals(role.name()));
   }

   public static boolean loggedInUserHasAnyRole(AvailableRoles[] roles) {
      Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)
         SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities();

      return authorities.stream()
         .anyMatch(a -> Arrays.stream(roles)
            .anyMatch(passedRole -> passedRole.name().equals(a.getAuthority())));
   }

   public static boolean userHasRole(User user, AvailableRoles role) {
      return user.getRoles().stream()
         .anyMatch(r -> role.name().equals(r.getName()));
   }

   public static boolean userHasAnyRole(User user, AvailableRoles[] roles) {
      return user.getRoles().stream()
         .anyMatch(r -> Arrays.stream(roles)
            .anyMatch(passedRole -> passedRole.name().equals(r.getName())));
   }
}
