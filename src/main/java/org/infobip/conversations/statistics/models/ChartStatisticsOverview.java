package org.infobip.conversations.statistics.models;

public class ChartStatisticsOverview implements IChartStatisticsOverview{
   public int number;
   public String month;

   public String getMonth() {
      return month;
   }

   public void setMonth(String month) {
      this.month = month;
   }

   public int getNumber() {
      return number;
   }

   public void setNumber(int number) {
      this.number = number;
   }
}
