package com.express.service.mapping;


import com.express.domain.Methodology;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.CustomConverter;

/**
 * @author Adam Boas
 * 
 */
public class MethodologyConverter implements CustomConverter {

   private static final Log LOG = LogFactory.getLog(MethodologyConverter.class);

   /**
    * Performs a bi-directional conversion between methodology and it's string
    * representation.
    */
   @SuppressWarnings("unchecked")
   public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class destinationClass,
         Class sourceClass) {
      if(LOG.isDebugEnabled()) {
         LOG.debug("Converting " + sourceFieldValue);
      }
      if (sourceFieldValue instanceof Methodology) {
         return ((Methodology) sourceFieldValue).getTitle();
      }
      else {
         return Methodology.getMethodology((String) sourceFieldValue);
      }
   }

}
