package org.infobip.conversations.users.rest;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.users.AvailableRoles;
import org.infobip.conversations.users.repository.model.User;
import org.infobip.conversations.users.rest.dto.JwtToken;
import org.infobip.conversations.users.security.jwt.JWTFilter;
import org.infobip.conversations.users.security.jwt.TokenProvider;
import org.infobip.conversations.users.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.infobip.conversations.users.rest.dto.AuthDto;

import javax.validation.Valid;

import static org.infobip.conversations.common.Constant.SUCCESS;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class AuthenticationRestController {

   private final TokenProvider tokenProvider;

   private final AuthenticationManagerBuilder authenticationManagerBuilder;

   private final UserService userService;

   public AuthenticationRestController(TokenProvider tokenProvider,
                                       AuthenticationManagerBuilder authenticationManagerBuilder,
                                       UserService userService) {
      this.tokenProvider = tokenProvider;
      this.authenticationManagerBuilder = authenticationManagerBuilder;
      this.userService = userService;
   }

   @PostMapping("/authenticate")
   public ResponseEntity<JwtToken> authorize(@Valid @RequestBody AuthDto authDto) {

      UsernamePasswordAuthenticationToken authenticationToken =
         new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());

      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      boolean rememberMe = authDto.isRememberMe() != null && authDto.isRememberMe();
      String jwt = tokenProvider.createToken(authentication, rememberMe);

      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

      return new ResponseEntity<>(new JwtToken(jwt), httpHeaders, HttpStatus.OK);
   }

   @PostMapping("/register")
   public ResponseEntity<Response> register(@RequestBody User user) {
      User savedUser = userService.saveUserWithRole(user);
      return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS)
         .setResult(savedUser), HttpStatus.OK);
   }
}
