package org.infobip.conversations.communications.repository;

import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.statistics.models.IChartStatisticsOverview;
import org.infobip.conversations.statistics.models.IReports;
import org.infobip.conversations.statistics.models.IStatisticsOverview;
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


   @Query(value = "SELECT  ua.username as agent, AVG(TIME_TO_SEC(TIMEDIFF(cm.end_time, cm.start_time))) as value " +
      "FROM communications cm, users ua, users uc, companies cp " +
      "WHERE cm.agent_id = ua.id AND cm.customer_id = uc.id AND ua.company_id = cp.id " +
      "AND (ua.company_id = ?1 or (cm.start_time >= unix_timestamp(?2) AND cm.start_time < unix_timestamp(?3))) " +
      "group by ua.username", nativeQuery = true)
   List<IReports> findAverageDurationInSeconds(Long companyId, Timestamp fromDate, Timestamp toDate);

   @Query(value = "SELECT  ua.username as agent, COUNT(cm.id) as value " +
      "FROM communications cm, users ua, users uc, companies cp " +
      "WHERE cm.agent_id = ua.id AND cm.customer_id = uc.id AND ua.company_id = cp.id " +
      "AND (ua.company_id = ?1 or (cm.start_time >= unix_timestamp(?2) AND cm.start_time < unix_timestamp(?3))) " +
      "group by ua.username", nativeQuery = true)
   List<IReports> findCommunicationCountForPeriod(Long companyId, Timestamp fromDate, Timestamp toDate);


   @Query(value = "SELECT count(cm.id) AS 'number', MONTHNAME(cm.start_time) AS 'month' " +
      "FROM communications cm, users ua, users uc, companies cp, communicationtypes ct " +
      "WHERE cm.agent_id = ua.id AND cm.customer_id = uc.id AND ua.company_id = cp.id AND cm.type_id = ct.id " +
      "AND (cp.id = ?1 OR ua.id =?2) AND (cm.type_id = 2 OR cm.type_id = 3) " +
      "AND YEAR(cm.start_time) = YEAR(NOW()) " +
      "GROUP BY MONTH(cm.start_time) " +
      "ORDER BY MONTH(cm.start_time) ASC", nativeQuery = true)
   List<IChartStatisticsOverview> findAllCallsByMonthsForCurrentYear(Long companyId, Long userId);

   @Query(value = "SELECT count(cm.id) AS 'number', MONTHNAME(cm.start_time) AS 'month' " +
      "FROM communications cm, users ua, users uc, communicationtypes ct " +
      "WHERE cm.agent_id = ua.id AND cm.customer_id = uc.id AND cm.type_id = ct.id " +
      "AND uc.id =?1 AND (cm.type_id = 2 OR cm.type_id = 3) " +
      "AND YEAR(cm.start_time) = YEAR(NOW()) " +
      "GROUP BY MONTH(cm.start_time) " +
      "ORDER BY MONTH(cm.start_time) ASC", nativeQuery = true)
   List<IChartStatisticsOverview> findAllCallsByMonthsForCurrentYearForUser(Long userId);


   @Query(value = "SELECT agents.one AS numberOfElementsOfEntityOne, calls.two AS numberOfElementsOfEntityTwo, average.three AS numberOfElementsOfEntityThree " +
      "FROM (SELECT count(ua.id) as one " +
      "      FROM users ua, companies cp " +
      "      WHERE ua.company_id = cp.id AND ua.company_id = ?1) as agents, " +
      "(SELECT count(cm.id) as two" +
      "     FROM communications cm, communicationtypes ct, users ua, users uc, companies cp" +
      "     WHERE cm.type_id = ct.id AND (cm.type_id = 2 OR cm.type_id = 3) and cm.agent_id = ua.id AND cm.customer_id = uc.id AND ua.company_id = cp.id AND ua.company_id = ?1) as calls," +
      "(SELECT CAST(time_format(CAST(sec_to_time(avg(TIME_TO_SEC(TIMEDIFF(cm.end_time, cm.start_time)))) AS CHAR), '%H:%i:%s') AS CHAR) AS three" +
      "     FROM communications cm, communicationtypes ct, users ua, users uc, companies cp" +
      "     WHERE cm.type_id = ct.id AND (cm.type_id = 2 OR cm.type_id = 3) and cm.agent_id = ua.id AND cm.customer_id = uc.id AND ua.company_id = cp.id AND ua.company_id = ?1) as average", nativeQuery = true)
   List<IStatisticsOverview> findAllStatisticOverviewsForCompany(Long companyId);

   @Query(value = "SELECT sms.one AS numberOfElementsOfEntityOne, calls.two AS numberOfElementsOfEntityTwo, average.three AS numberOfElementsOfEntityThree " +
      "FROM (SELECT count(cm.id) as one" +
      "      FROM communications cm, communicationtypes ct, users ua, users uc " +
      "      WHERE cm.type_id = ct.id AND cm.type_id = 1  AND cm.agent_id = ua.id AND cm.customer_id = uc.id AND (ua.id = ?1 OR uc.id = ?2)) as sms," +
      " (SELECT count(cm.id) as two" +
      "     FROM communications cm, communicationtypes ct, users ua, users uc" +
      "     WHERE cm.type_id = ct.id AND (cm.type_id = 2 OR cm.type_id = 3) and cm.agent_id = ua.id AND cm.customer_id = uc.id and (ua.id = ?1 OR uc.id = ?2)) as calls," +
      " (SELECT CAST(time_format(CAST(sec_to_time(avg(TIME_TO_SEC(TIMEDIFF(cm.end_time, cm.start_time)))) AS CHAR), '%H:%i:%s') AS CHAR) AS three" +
      "     FROM communications cm, communicationtypes ct, users ua, users uc " +
      "     WHERE cm.type_id = ct.id AND (cm.type_id = 2 OR cm.type_id = 3) and cm.agent_id = ua.id AND cm.customer_id = uc.id and (ua.id = ?1 OR uc.id = ?2)) as average", nativeQuery = true)
   List<IStatisticsOverview> findAllStatisticOverviewsForAgentOrUser(Long agentId, Long userId);

//cm.type_id 1-sms 2-video 3-audio
}

