package org.infobip.conversations.users.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.RoleRepository;
import org.infobip.conversations.users.repository.UserRepository;
import org.infobip.conversations.users.repository.model.Role;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.service.UserService;
import org.infobip.conversations.users.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.infobip.conversations.common.Constant.SUCCESS;

@RestController
@RequestMapping("/api/roles")
public class RoleRestController {

   @Autowired
   private RoleRepository roleRepository;

   @PostMapping
   public ResponseEntity<Response> create(@RequestBody Role role) {
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(roleRepository.save(role)), HttpStatus.OK);
   }

   @PutMapping
   public ResponseEntity<Response> update(@RequestBody Role role) {
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(roleRepository.save(role)), HttpStatus.OK);
   }

   @GetMapping("/{id}")
   public ResponseEntity<Response> read(@PathVariable Long id) {
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(roleRepository.findById(id)), HttpStatus.OK);
   }

   @GetMapping
   public ResponseEntity<Response> readAll() {
      boolean isSuperAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.SuperAdmin);
      boolean isCompanyAdmin = SecurityUtils.loggedInUserHasRole(AvailableRoles.CompanyAdmin);
      List<Role> roles = null;
      if (isSuperAdmin) {
         roles = roleRepository.findAll();
      } else if (isCompanyAdmin) {
         roles = roleRepository.findAll().stream()
            .filter((r -> r.getName().equals(AvailableRoles.Agent.name()) ||
                     r.getName().equals(AvailableRoles.CompanyAdmin.name())))
            .collect(Collectors.toList());
      }
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(roles), HttpStatus.OK);
   }

   @DeleteMapping("{id}")
   public void delete(@PathVariable Long id) {
      roleRepository.deleteById(id);
   }
}
