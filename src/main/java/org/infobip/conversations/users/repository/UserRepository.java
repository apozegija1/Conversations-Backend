package org.infobip.conversations.users.repository;

import org.infobip.conversations.users.repository.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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
   Page<User> findAllUsersForCompany(Long companyId, Pageable pageable);

   @Query(value = "SELECT count(u.id) AS Users, MONTH(u.created_at) AS 'Month' " +
      "FROM users u " +
      "WHERE YEAR(u.created_at) = YEAR(NOW()) " +
      "GROUP BY MONTH(u.created_at) " +
      "ORDER BY MONTH(u.created_at) ASC", nativeQuery = true)
   List<Long> findAllUsersByMonthsForCurrentYear();

   @Query(value = "SELECT count(u.id) AS Users, count(cp.id) AS Companies, CAST(AVG(res.c) AS INTEGER) AS AvgInYearByMonths " +
      "FROM users u, companies cp, " +
      "(SELECT count(us.id) c " +
      "FROM users us " +
      "WHERE YEAR(uS.created_at) = YEAR(NOW()) " +
      "GROUP BY MONTH(uS.created_at)) as res " +
      "WHERE u.company_id = cp.id " +
      "AND YEAR(u.created_at) = YEAR(NOW())", nativeQuery = true)
   List<Integer> findAllStatisticOverviewsForSuperAgent();
}
