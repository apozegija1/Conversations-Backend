package org.infobip.conversations.statistics.repository;

import org.infobip.conversations.statistics.models.StatisticsOverview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<StatisticsOverview, Long> {


   @Query(value = "SELECT AVG(TIME_TO_SEC(TIMEDIFF(cm.end_time, cm.start_time))) " +
      "FROM communications cm, users ua, users uc, companies cp " +
      "WHERE cm.agent_id = ua.id AND cm.customer_id = uc.id AND ua.company_id = cp.id " +
      "AND (cp.id = ?1 OR ua.id = ?2 OR (cm.start_time >= unix_timestamp(?3) AND cm.start_time < unix_timestamp(?4))) ", nativeQuery = true)
   Float findAverageDurationInSeconds(Long companyId, Long agentId, Timestamp fromDate, Timestamp toDate);

   @Query(value = "SELECT COUNT(cm.id) " +
      "FROM communications cm, users ua, users uc, companies cp " +
      "WHERE cm.agent_id = ua.id AND cm.customer_id = uc.id AND ua.company_id = cp.id " +
      "AND (cp.id = ?1 OR ua.id = ?2 OR (cm.start_time >= unix_timestamp(?3) AND cm.start_time < unix_timestamp(?4))) ", nativeQuery = true)
   Long findCommunicationCountForPeriod(Long companyId, Long agentId, Timestamp fromDate, Timestamp toDate);

   @Query(value = "SELECT AVG(cr.rating) " +
      "FROM communicationreviews cr, communications cm, users ua, users uc, companies cp, communicationtypes ct " +
      "WHERE cr.communication_id = cm.id AND cm.agent_id = ua.id AND cm.customer_id = uc.id " +
      "AND ua.company_id = cp.id AND cm.type_id = ct.id " +
      "AND (cp.id = ?1 OR ct.id = ?2) " +
      "GROUP BY ct.type", nativeQuery = true)
   Float findAverageRatingForCompanybyCommunicationType(Long companyId, Long typeId);


   @Query(value = "SELECT count(ua.id) AS Agents, count(cm.id) AS Calls, SEC_TO_TIME(AVG(TIME_TO_SEC(TIMEDIFF(cm.end_time, cm.start_time)))) AS AvgDuration " +
      "FROM communications cm, users ua, users uc, companies cp, communicationtypes ct " +
      "WHERE cm.agent_id = ua.id AND cm.customer_id = uc.id AND ua.company_id = cp.id AND cm.type_id = ct.id " +
      "AND (cp.id = ?1 OR ua.id =?2) AND (cm.type_id = 2 OR cm.type_id = 3)", nativeQuery = true)
   List<StatisticsOverview> findAllStatisticOverviewsForCompanyOrAgent(Long companyId, Long userId);

   @Query(value = "SELECT count(cm.id) AS Calls, MONTH(cm.start_time) AS Month " +
      "FROM communications cm, users ua, users uc, companies cp, communicationtypes ct " +
      "WHERE cm.agent_id = ua.id AND cm.customer_id = uc.id AND ua.company_id = cp.id AND cm.type_id = ct.id " +
      "AND (cp.id = ?1 OR ua.id =?2) AND (cm.type_id = 2 OR cm.type_id = 3) " +
      "AND YEAR(cm.start_time) = YEAR(NOW()) " +
      "GROUP BY MONTH(cm.start_time) " +
      "ORDER BY MONTH(cm.start_time) ASC", nativeQuery = true)
   List<Long> findAllCallsByMonthsForCurrentYearForCompanyOrAgent(Long companyId, Long userId);

   @Query(value = "SELECT count(u.id) AS Users, MONTH(u.created_at) AS 'Month' " +
      "FROM users u " +
      "WHERE YEAR(u.created_at) = YEAR(NOW()) " +
      "GROUP BY MONTH(u.created_at) " +
      "ORDER BY MONTH(u.created_at) ASC", nativeQuery = true)
   List<Long> findAllUsersByMonthsForCurrentYearForSuperAgent();

   @Query(value = "SELECT count(u.id) AS Users, count(cp.id) AS Companies, AVG(COUNT(u.id)) AS AvgUsersRegistered " +
      "FROM users u, companies cp " +
      "WHERE u.company_id = cp.id " +
      "AND YEAR(u.created_at) = YEAR(NOW())", nativeQuery = true)
   List<Long> findAllStatisticOverviewsForSuperAgent();
}

