package org.infobip.conversations.users.repository;

import org.infobip.conversations.communications.repository.model.Communication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.infobip.conversations.users.repository.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   @EntityGraph(attributePaths = "roles")
   Optional<User> findOneWithAuthoritiesByUsername(String username);

   @EntityGraph(attributePaths = "roles")
   Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

   Optional<User> findByUsername(String username);

   @Query("SELECT u " +
      "FROM User u, Company cp " +
      "WHERE u.company.id = cp.id " +
      "AND cp.id = ?1")
    List<User> findAllUsersForCompany(Long companyId);
}
