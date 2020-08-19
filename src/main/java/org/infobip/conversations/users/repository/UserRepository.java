package org.infobip.conversations.users.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.infobip.conversations.users.repository.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   @EntityGraph(attributePaths = "roles")
   Optional<User> findOneWithAuthoritiesByUsername(String username);

   @EntityGraph(attributePaths = "roles")
   Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

}
