package com.express.service.mapping;

import net.sf.dozer.util.mapping.converters.CustomConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.express.domain.Status;

/**
 * @author Adam Boas
 * 
 */
public class StatusConverter implements CustomConverter {

   private static final Log LOG = LogFactory.getLog(StatusConverter.class);

   /**
    * Performs a bi-directional conversion between status and it's string
    * representation.
    */
   @SuppressWarnings("unchecked")
   public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class destinationClass,
         Class sourceClass) {
      if(LOG.isDebugEnabled()) {
         LOG.debug("Converting " + sourceFieldValue);
      }
      if (sourceFieldValue instanceof Status)
         return ((Status) sourceFieldValue).getTitle();
      else
         return Status.getStatus((String) sourceFieldValue);

   }

}
