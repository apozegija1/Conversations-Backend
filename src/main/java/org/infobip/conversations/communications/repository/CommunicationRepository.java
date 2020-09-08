package org.infobip.conversations.communications.repository;

import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.statistics.models.IStatisticsOverview;
import org.infobip.conversations.statistics.models.StatisticsOverview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Long> {

   @Query("SELECT c " +
      "FROM  CommunicationType t, Communication c, User ua, User uc " +
      "WHERE t.id = c.type.id AND uc.id = c.customer.id AND ua.id = c.agent.id " +
      "AND (c.agent.id = ?1 OR c.customer.id = ?2)")
      //if agent is null, we will get all communications for a specific user,
      //and if customer is null, we will get all communications for agent
    List<Communication> findAllCommunicationsForUser(Long agentId, Long customerId);


   @Query("SELECT cm " +
      "FROM Communication cm, User ua, User uc, Company cp " +
      "WHERE cm.agent.id = ua.id AND cm.customer.id = uc.id AND ua.company.id = cp.id " +
      "AND cp.id = ?1")
   List<Communication> findAllCommunicationsForCompany(Long companyId);


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


   @Query(value = "SELECT count(cm.id) AS Calls, MONTH(cm.start_time) AS Month " +
      "FROM communications cm, users ua, users uc, companies cp, communicationtypes ct " +
      "WHERE cm.agent_id = ua.id AND cm.customer_id = uc.id AND ua.company_id = cp.id AND cm.type_id = ct.id " +
      "AND (cp.id = ?1 OR ua.id =?2) AND (cm.type_id = 2 OR cm.type_id = 3) " +
      "AND YEAR(cm.start_time) = YEAR(NOW()) " +
      "GROUP BY MONTH(cm.start_time) " +
      "ORDER BY MONTH(cm.start_time) ASC", nativeQuery = true)
   List<Long> findAllCallsByMonthsForCurrentYear(Long companyId, Long userId);

   @Query(value = "SELECT count(ua.id) AS numberOfElementsOfEntityOne, count(cm.id) AS numberOfElementsOfEntityTwo, " +
      "CAST(sec_to_time(avg(TIME_TO_SEC(TIMEDIFF(cm.end_time, cm.start_time)))) AS CHAR) AS numberOfElementsOfEntityThree " +
      "FROM communications cm, users ua, users uc, companies cp, communicationtypes ct " +
      "WHERE cm.agent_id = ua.id AND cm.customer_id = uc.id AND ua.company_id = cp.id AND cm.type_id = ct.id " +
      "AND (cp.id = ?1 OR ua.id =?2) AND (cm.type_id = 2 OR cm.type_id = 3)", nativeQuery = true)
   List<IStatisticsOverview> findAllStatisticOverviewsForCompanyOrAgent(Long companyId, Long userId);

}

