package org.infobip.conversations.users.repository;

import org.infobip.conversations.users.repository.model.Role;
import org.infobip.conversations.users.repository.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Role} entity.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
   Optional<Role> findOneByName(String rolename);
}
