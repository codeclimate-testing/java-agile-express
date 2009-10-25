package com.express.service.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public enum Policy {
   SHALLOW(".shallow"), MEDIUM(".medium"), DEEP(".deep");
   
   private static final Log LOG = LogFactory.getLog(Policy.class);
   
   private final String suffix;
   
   Policy(String suffix) {
      this.suffix = suffix;
   }
   
   public String getSuffix() {
      return suffix;
   }
   
   @SuppressWarnings("unchecked")
   public String getMapId(Class clazz) {
      String mapId = clazz.getSimpleName() + suffix;
      if(LOG.isDebugEnabled()) {
         LOG.debug("returning mapId:[" + mapId + "]");
      }
      return mapId;
   }

}
