package org.infobip.conversations.users.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
   public static String hashPassword(String password_plaintext) {
      return BCrypt.hashpw(password_plaintext, BCrypt.gensalt());
   }

   public static boolean checkPassword(String password_plaintext, String stored_hash) {
      return BCrypt.checkpw(password_plaintext, stored_hash);
   }

   public static String generateMD5(String text) {
      MessageDigest md;
      try {
         md = MessageDigest.getInstance("MD5");
         byte[] messageDigest = md.digest(text.getBytes());
         BigInteger number = new BigInteger(1, messageDigest);
         String hashtext = number.toString(16);
         return hashtext;
      } catch (NoSuchAlgorithmException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return text;
   }
}
