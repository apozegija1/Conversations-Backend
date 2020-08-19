package org.infobip.conversations.users.rest;

import org.infobip.conversations.util.AbstractRestControllerTest;
import org.infobip.conversations.util.LogInUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRestControllerTest extends AbstractRestControllerTest {

   @Before
   public void setUp() {
      SecurityContextHolder.clearContext();
   }

   @Test
   public void getActualUserForUserWithToken() throws Exception {
      final String token = LogInUtils.getTokenForLogin("user", "password", getMockMvc());

      getMockMvc().perform(get("/api/user")
         .contentType(MediaType.APPLICATION_JSON)
         .header("Authorization", "Bearer " + token))
         .andExpect(status().isOk())
         .andExpect(content().json(
            "{\n" +
               "  \"username\" : \"user\",\n" +
               "  \"firstname\" : \"user\",\n" +
               "  \"lastname\" : \"user\",\n" +
               "  \"email\" : \"enabled@user.com\",\n" +
               "  \"authorities\" : [ {\n" +
               "    \"name\" : \"ROLE_USER\"\n" +
               "  } ]\n" +
               "}"
         ));
   }

   @Test
   public void getActualUserForUserWithoutToken() throws Exception {
      getMockMvc().perform(get("/api/user")
         .contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isUnauthorized());
   }

}
