package org.infobip.conversations.statistics.models;

import java.sql.Time;

public interface IStatisticsOverview {

   Long getNumberOfElementsOfEntityOne();
   void setNumberOfElementsOfEntityOne(Long numberOfElementsOfEntityOne);
   Long getNumberOfElementsOfEntityTwo();
   void setNumberOfElementsOfEntityTwo(Long numberOfElementsOfEntityTwo);
   Time getNumberOfElementsOfEntityThree();
   void setNumberOfElementsOfEntityThree(Time numberOfElementsOfEntityThree);
}
