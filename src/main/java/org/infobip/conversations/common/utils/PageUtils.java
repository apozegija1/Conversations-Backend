package org.infobip.conversations.common.utils;

import org.springframework.data.domain.Pageable;

public class PageUtils {
   public static Pageable getAllDataPage() {
      return Pageable.unpaged();
   }
}
