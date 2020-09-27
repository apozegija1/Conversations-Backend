package org.infobip.conversations.statistics.models;

public class Reports implements IReports{
   public Long value;
   public String agent;

   public Reports(Long value, String agent) {
      this.value = value;
      this.agent = agent;
   }

   public Long getValue() {
      return value;
   }

   public void setValue(Long value) {
      this.value = value;
   }

   public String getAgent() {
      return agent;
   }

   public void setAgent(String agent) {
      this.agent = agent;
   }
}
