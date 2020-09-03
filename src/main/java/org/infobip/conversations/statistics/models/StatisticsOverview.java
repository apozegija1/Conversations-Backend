package org.infobip.conversations.statistics.models;

import java.sql.Time;

public class StatisticsOverview {
   public Long numberOfElementsOfEntityOne;
   public Long numberOfElementsOfEntityTwo;
   public Time numberOfElementsOfEntityThree;

   public StatisticsOverview(Long numberOfElementsOfEntityOne, Long numberOfElementsOfEntityTwo, Time numberOfElementsOfEntityThree) {
      this.numberOfElementsOfEntityOne = numberOfElementsOfEntityOne;
      this.numberOfElementsOfEntityTwo = numberOfElementsOfEntityTwo;
      this.numberOfElementsOfEntityThree = numberOfElementsOfEntityThree;
   }
}
