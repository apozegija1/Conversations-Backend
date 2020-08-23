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
   public List<User> getCompanyUsers() {
      Optional<User> user = SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
      return new ArrayList<>();
   }

   public User saveUserWithRole(User user, String roleName) {
      Optional<User> oCurrentUser = this.getUserWithAuthorities();
      if(oCurrentUser.isEmpty()) {
         throw new IllegalArgumentException("Invalid user");
      }

      User currentUser = oCurrentUser.get();
      user.setRoles(this.getRolesByCurrentUser(currentUser, roleName));
      user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
      // For now set activated by default until we enable email activation
      user.setActivated(true);
      return userRepository.save(user);
   }

   private Set<Role> getRolesByCurrentUser(User currentUser, String roleName) {
      Set<Role> currentUserRoles = currentUser.getRoles();
      Optional<Role> userRoleCompanyAdmin = currentUserRoles.stream()
         .filter(r -> AvailableRoles.CompanyAdmin.name().equals(r.getName()))
         .findFirst();

      // If user is company admin he can add only agents
      if (userRoleCompanyAdmin.isPresent()) {
         Optional<Role> userRoleSuperAdmin = currentUserRoles.stream()
            .filter(r -> AvailableRoles.SuperAdmin.name().equals(r.getName()))
            .findFirst();
         // Assign agent role if current user is not super admin as super admin can add any role but company admin only agent
         if (userRoleSuperAdmin.isEmpty()) {
            roleName = AvailableRoles.Agent.name();
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
