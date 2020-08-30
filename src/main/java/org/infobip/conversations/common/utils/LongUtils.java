package org.infobip.conversations.common.utils;

public class LongUtils {

   public static Long stringToLong(String value) {
      try {
         return value != null ? Long.parseLong(value) : null;
      } catch (NumberFormatException e) {
         return null;
      }
   }
}
