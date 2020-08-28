package org.infobip.conversations.users.service;

import org.infobip.conversations.companies.repository.model.Company;
import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.RoleRepository;
import org.infobip.conversations.users.repository.model.Role;
import org.infobip.conversations.users.utils.PasswordUtils;
import org.infobip.conversations.users.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.repository.UserRepository;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.Option;
import java.util.*;

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
   public List<User> getCompanyUsers() {
      Optional<User> user = SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
      return new ArrayList<>();
   }

   public User saveUserWithRole(User user, String roleName) {
      Optional<User> existingUser = this.userRepository.findByUsername(user.getUsername());
      if (existingUser.isPresent()) {
         throw new IllegalArgumentException("User with specified username already exists. Choose another username");
      }
      Optional<User> oCurrentUser = this.getUserWithAuthorities();
      user.setRoles(this.getRolesByCurrentUser(oCurrentUser, roleName));
      user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
      // For now set activated by default until we enable email activation
      user.setActivated(true);
      return userRepository.save(user);
   }

   private Set<Role> getRolesByCurrentUser(Optional<User> oCurrentUser, String roleName) {
      // If current user is empty then someone is trying to register who isn't in our system so it is basic user
      if (oCurrentUser.isEmpty()) {
         roleName = AvailableRoles.User.name();
      } else {
         User currentUser = oCurrentUser.get();
         Set<Role> currentUserRoles = currentUser.getRoles();
         boolean userRoleCompanyAdmin = SecurityUtils.userHasRole(currentUser, AvailableRoles.CompanyAdmin);

         // If user is company admin he can add only agents
         if (userRoleCompanyAdmin) {
            boolean hasSuperAdminRole = SecurityUtils.userHasRole(currentUser, AvailableRoles.SuperAdmin);
            // Assign agent role if current user is not super admin as super admin can add any role but company admin only agent
            if (!hasSuperAdminRole) {
               roleName = AvailableRoles.Agent.name();
            }
         }
      }

      Optional<Role> oRole = roleRepository.findOneByName(roleName);

      if (oRole.isEmpty()) {
         throw new IllegalArgumentException("Invalid role");
      }
      Role role = oRole.get();

      Set<Role> roles = new HashSet<>();
      roles.add(role);
      return roles;
   }

}
