package org.infobip.conversations.users.repository;

import org.infobip.conversations.users.repository.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Role} entity.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
