package com.express.service.mapping;import com.express.domain.Status;import org.apache.commons.logging.Log;import org.apache.commons.logging.LogFactory;import org.dozer.CustomConverter;/** * @author Adam Boas *  */public class StatusConverter implements CustomConverter {   private static final Log LOG = LogFactory.getLog(StatusConverter.class);   /**    * Performs a bi-directional conversion between status and it's string    * representation.    */   @SuppressWarnings("unchecked")   public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class destinationClass,         Class sourceClass) {      if(LOG.isDebugEnabled()) {         LOG.debug("Converting " + sourceFieldValue);      }      if (sourceFieldValue instanceof Status)         return ((Status) sourceFieldValue).getTitle();      else         return Status.getStatus((String) sourceFieldValue);   }}