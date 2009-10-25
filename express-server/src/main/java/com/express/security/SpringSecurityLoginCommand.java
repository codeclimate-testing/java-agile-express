package com.express.security;

import flex.messaging.FlexContext;
import flex.messaging.io.MessageIOConstants;
import flex.messaging.security.LoginCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.AbstractAuthenticationManager;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public class SpringSecurityLoginCommand implements LoginCommand {
   private static final Log LOG = LogFactory.getLog(SpringSecurityLoginCommand.class);
   
   private AbstractAuthenticationManager authenticationManager;

   public Principal doAuthentication(String username, Object credentials) {
      String password = extractPassword(credentials);
      if (password == null) {
         return null;
      }
      
      if(authenticationManager == null) {
         init();
      }

      Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
      try {
         SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(auth));
         LOG.info("User [" + username + "] logged in.");
         return SecurityContextHolder.getContext().getAuthentication();
      }
      catch (RuntimeException e) {
         LOG.info("User [" + username + "] failed authentication.");
         throw e;
      }
   }

   /**
    * We are not going to perform Authorization at this level since we would like SpringSecurity
    * to manage Authorization at the method level.
    */
   @SuppressWarnings("unchecked")
   public boolean doAuthorization(Principal principal, List roles) {
      return true;
   }

   public boolean logout(Principal principal) {
      HttpServletRequest request = FlexContext.getHttpRequest();
      if (request != null && request.getSession(false) != null) {
         try {
            request.getSession().invalidate();
         }
         catch (IllegalStateException e) {
            // Expected.
         }
      }
      request.getSession(true); // Session re-created to avoid Flex error when
      // it also attempts to invalidate the session.
      LOG.info("User [" + principal.getName() + "] logged out.");
      return true;
   }

   public void start(ServletConfig config) {
      //My tests show this never gets called
   }

   public void stop() {
      authenticationManager = null;
   }

   @SuppressWarnings("unchecked")
   protected String extractPassword(Object credentials)
   {
       String password = null;
       if (credentials instanceof String)
       {
           password = (String)credentials;
       }
       else if (credentials instanceof Map)
       {
           password = (String)((Map)credentials).get(MessageIOConstants.SECURITY_CREDENTIALS);
       }
       return password;
   }
   
   private void init() {
      HttpServletRequest request = FlexContext.getHttpRequest();
      ApplicationContext ctx = 
         WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
      authenticationManager = (AbstractAuthenticationManager) ctx.getBean("_authenticationManager");
   }

}
