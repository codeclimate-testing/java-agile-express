package com.express.service.mapping;

import net.sf.dozer.util.mapping.converters.CustomConverter;

/**
 * @author Adam Boas
 *
 */
public class IdConverter implements CustomConverter
{

   /** 
    * Enforces that when an id of zero passed in it is mapped as null instead.
    * 
    * @see net.sf.dozer.util.mapping.converters.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
    */
   @SuppressWarnings("unchecked")
   public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
            Class destinationClass, Class sourceClass)
   {
      Long longValue = (Long)sourceFieldValue;
      if(longValue == null || longValue == 0) {
         return null;
      }
      else {
         return longValue;
      }
   }

}
