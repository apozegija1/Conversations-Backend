package org.infobip.conversations.statistics.models;

import java.sql.Time;

public class StatisticsOverview {
   public Long numberOfAgents;
   public Long numberOfCalls;
   public Time avgDurationOfCalls;

   public StatisticsOverview(Long numberOfAgents, Long numberOfCalls, Time avgDurationOfCalls) {
      this.numberOfAgents = numberOfAgents;
      this.numberOfCalls = numberOfCalls;
      this.avgDurationOfCalls = avgDurationOfCalls;
   }

}
