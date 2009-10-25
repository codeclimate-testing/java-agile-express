package com.express.service;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;

import com.express.dao.UserDao;
import com.express.domain.User;
import com.express.service.dto.LoginRequest;
import com.express.service.dto.UserDto;
import com.express.service.mapping.DomainFactory;
import com.express.service.mapping.Policy;
import com.express.service.mapping.RemoteObjectFactory;
import com.express.service.notification.NotificationService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Ehcache;

public class UserManagerImplLoginTest extends UnitilsJUnit4 {
   static final String USERNAME = "test@test.com";
   static final String PASSWORD = "password";
   @Mock
   RemoteObjectFactory mockRemoteObjectFactory;
   @Mock
   UserDao mockUserDao;
   @Mock
   PasswordEncoder mockPasswordEncoder;
   @Mock
   DomainFactory mockDomainFactory;
   @Mock
   NotificationService mockNotificationService;

   UserManager userManager;
   
   @Before
   public void setUp() {
      userManager = new UserManagerImpl(mockUserDao, mockPasswordEncoder,mockRemoteObjectFactory,
               mockDomainFactory, mockNotificationService);
   }
   
   @Test
   public void checkLoginUserHappyDays(){
      LoginRequest request = new LoginRequest();
      request.setUsername(USERNAME);
      request.setPassword(PASSWORD);
      User user = new User();
      user.setEmail(USERNAME);
      user.setPassword(PASSWORD);
      user.setActive(true);
      expect(mockUserDao.findByUsername(USERNAME)).andReturn(user);
      expect(mockPasswordEncoder.isPasswordValid(PASSWORD, PASSWORD, USERNAME)).andReturn(Boolean.TRUE);
      expect(mockRemoteObjectFactory.createUserDto(user, Policy.DEEP)).andReturn(new UserDto());
      
      EasyMockUnitils.replay();
      userManager.login(request);
   }
   
   @Test
   public void checkLoginUserInactiveThrowsExeption(){
      LoginRequest request = new LoginRequest();
      request.setUsername(USERNAME);
      request.setPassword(PASSWORD);
      User user = new User();
      user.setEmail(USERNAME);
      user.setPassword(PASSWORD);
      expect(mockUserDao.findByUsername(USERNAME)).andReturn(user);
      expect(mockPasswordEncoder.isPasswordValid(PASSWORD, PASSWORD, USERNAME)).andReturn(Boolean.FALSE);
      
      EasyMockUnitils.replay();
      try {
         userManager.login(request);
         fail("Login should fai for inactive users");
      }
      catch(RemoteAccessException e) {
         //This is expected
      }
   }
   
   @Test
   public void checkLoginUserPassswordReminderRequestReturnsNull(){
      LoginRequest request = new LoginRequest();
      request.setUsername(USERNAME);
      request.setPassword(PASSWORD);
      request.setPasswordReminderRequest(true);
      User user = new User();
      user.setEmail(USERNAME);
      user.setPassword(PASSWORD);
      user.setActive(false);
      expect(mockUserDao.findByUsername(USERNAME)).andReturn(user);
      mockNotificationService.sendPasswordReminderNotification(user);
      
      EasyMockUnitils.replay();
      assertNull(userManager.login(request));

   }
   
   @Test
   public void checkLoginNonExistingUserThrowsExceptionl(){
      LoginRequest request = new LoginRequest();
      request.setUsername(USERNAME);
      request.setPassword(PASSWORD);
      request.setPasswordReminderRequest(true);
      User user = new User();
      user.setEmail(USERNAME);
      user.setPassword(PASSWORD);
      expect(mockUserDao.findByUsername(USERNAME)).andThrow(new ObjectRetrievalFailureException(User.class,USERNAME));
      
      EasyMockUnitils.replay();
      try {
         userManager.login(request);
         fail("Login should fai for inactive users");
      }
      catch(RemoteAccessException e) {
         //This is expected
      }
   }

}
