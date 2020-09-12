package org.infobip.conversations.statistics.models;

public class StatisticsOverview implements IStatisticsOverview{
   public Long numberOfElementsOfEntityOne;
   public Long numberOfElementsOfEntityTwo;
   public String numberOfElementsOfEntityThree;

   public StatisticsOverview(Long numberOfElementsOfEntityOne, Long numberOfElementsOfEntityTwo, String numberOfElementsOfEntityThree) {
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

   public String getNumberOfElementsOfEntityThree() {
      return numberOfElementsOfEntityThree;
   }

   public void setNumberOfElementsOfEntityThree(String numberOfElementsOfEntityThree) {
      this.numberOfElementsOfEntityThree = numberOfElementsOfEntityThree;
   }
}
