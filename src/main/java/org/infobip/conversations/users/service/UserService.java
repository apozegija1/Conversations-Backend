package org.infobip.conversations.users.service;

import org.infobip.conversations.companies.repository.model.Company;
import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.RoleRepository;
import org.infobip.conversations.users.repository.model.Role;
import org.infobip.conversations.users.utils.PasswordUtils;
import org.infobip.conversations.users.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

   private final UserRepository userRepository;

   private final RoleRepository roleRepository;

   public UserService(UserRepository userRepository, RoleRepository roleRepository) {
      this.userRepository = userRepository;
      this.roleRepository = roleRepository;
   }

   @Transactional(readOnly = true)
   public Optional<User> getUserWithAuthorities() {
      return SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
   }

   @Transactional(readOnly = true)
   public Company getCurrentUserCompany() {
      Optional<User> user = SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
      return user.map(User::getCompany).orElse(null);
   }

   @Transactional(readOnly = true)
   public Page<User> getUsersByRole(AvailableRoles role, Pageable pageable) {
      return userRepository.findAllUsersByRole(role.name(), pageable);
   }

   public User saveUserWithRole(User user) {
      Optional<User> existingUser = this.userRepository.findByUsername(user.getUsername());
      if (existingUser.isPresent()) {
         throw new IllegalArgumentException("User with specified username already exists. Choose another username");
      }

      return this.saveOrUpdateUser(user, null);
   }

   public User saveOrUpdateUser(User user, User existing) {
      Optional<User> oCurrentUser = this.getUserWithAuthorities();
      user.setRoles(this.getRolesByCurrentUser(oCurrentUser, user.getRoles()));

      this.checkUserCompany(user);

      if (existing == null) {
         user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
         // For now set activated by default until we enable email activation
         user.setActivated(true);
      } else {
         user.setPassword(existing.getPassword());
         user.setActivated(existing.isActivated());
      }

      return userRepository.save(user);
   }

   private Set<Role> getRolesByCurrentUser(Optional<User> oCurrentUser, Set<Role> newRoles) {
      // If current user is empty then someone is trying to register who isn't in our system so it is basic user
      String roleName = null;
      if (oCurrentUser.isEmpty()) {
         roleName = AvailableRoles.User.name();
      } else {
         User currentUser = oCurrentUser.get();
         boolean userRoleCompanyAdmin = SecurityUtils.userHasRole(currentUser, AvailableRoles.CompanyAdmin);

         // If user is company admin and tries to assign some other role then return to agent role
         if (userRoleCompanyAdmin) {
            boolean hasSuperAdmin = SecurityUtils.hasRole(newRoles, AvailableRoles.SuperAdmin);
            if (hasSuperAdmin) {
               roleName = AvailableRoles.Agent.name();
            }
         }
      }

      if (roleName != null) {
         Optional<Role> oRole = roleRepository.findOneByName(roleName);
         if (oRole.isEmpty()) {
            throw new IllegalArgumentException("Invalid role");
         }
         Role role = oRole.get();
         newRoles.add(role);
      }

      return newRoles;
   }

   private void checkUserCompany(User user) {
      boolean isCompanyAdmin = SecurityUtils.userHasRole(user, AvailableRoles.CompanyAdmin);
      boolean isAgent = SecurityUtils.userHasRole(user, AvailableRoles.Agent);
      // If user is missing company or company passed is missing id throw error
      if ((isCompanyAdmin || isAgent) && (user.getCompany() == null || user.getCompany().getId() == null)) {
         throw new IllegalArgumentException("User is missing company");
      }
   }
}
