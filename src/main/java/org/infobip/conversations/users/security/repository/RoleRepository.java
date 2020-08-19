package org.infobip.conversations.users.security.repository;

import org.infobip.conversations.users.security.repository.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Role} entity.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
