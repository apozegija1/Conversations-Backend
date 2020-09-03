package org.infobip.conversations.statistics.models;

import java.sql.Time;

public class StatisticsOverview implements IStatisticsOverview {
   public Long numberOfElementsOfEntityOne;
   public Long numberOfElementsOfEntityTwo;
   public Time numberOfElementsOfEntityThree;

   public StatisticsOverview(Long numberOfElementsOfEntityOne, Long numberOfElementsOfEntityTwo, Time numberOfElementsOfEntityThree) {
      this.numberOfElementsOfEntityOne = numberOfElementsOfEntityOne;
      this.numberOfElementsOfEntityTwo = numberOfElementsOfEntityTwo;
      this.numberOfElementsOfEntityThree = numberOfElementsOfEntityThree;
   }

   public Long getNumberOfElementsOfEntityOne() {
      return numberOfElementsOfEntityOne;
   }

   public void setNumberOfElementsOfEntityOne(Long numberOfElementsOfEntityOne) {
      this.numberOfElementsOfEntityOne = numberOfElementsOfEntityOne;
   }

   public Long getNumberOfElementsOfEntityTwo() {
      return numberOfElementsOfEntityTwo;
   }

   public void setNumberOfElementsOfEntityTwo(Long numberOfElementsOfEntityTwo) {
      this.numberOfElementsOfEntityTwo = numberOfElementsOfEntityTwo;
   }

   public Time getNumberOfElementsOfEntityThree() {
      return numberOfElementsOfEntityThree;
   }

   public void setNumberOfElementsOfEntityThree(Time numberOfElementsOfEntityThree) {
      this.numberOfElementsOfEntityThree = numberOfElementsOfEntityThree;
   }
}
