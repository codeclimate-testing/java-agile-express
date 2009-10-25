package com.express.service;

import com.express.dao.UserDao;
import com.express.domain.User;
import com.express.service.dto.UserDto;
import com.express.service.mapping.DomainFactory;
import com.express.service.mapping.RemoteObjectFactory;
import com.express.service.notification.NotificationService;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;

public class UserManagerImplRegisterTest extends UnitilsJUnit4 {
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
   public void checkRegisterHappyDays(){
      UserDto dto = new UserDto();
      dto.setEmail(USERNAME);
      dto.setPassword(PASSWORD);
      User user = new User();
      user.setEmail(USERNAME);
      user.setPassword(PASSWORD);
      user.setActive(true);
      expect(mockUserDao.findByUsername(USERNAME)).andThrow(new ObjectRetrievalFailureException(User.class,USERNAME));
      expect(mockDomainFactory.createUser(dto)).andReturn(user);
      expect(mockPasswordEncoder.encodePassword(PASSWORD, USERNAME)).andReturn("rubbish");
      mockUserDao.save(user);
      mockNotificationService.sendConfirmationNotification(user);
      
      EasyMockUnitils.replay();
      userManager.register(dto);
   }

   @Test
   public void checkRegisterConfirm() {
      User user = new User();
      expect(mockUserDao.findById(1l)).andReturn(user);
      EasyMockUnitils.replay();
      userManager.confirmRegistration(1l);
      assertTrue(user.getActive());
   }


}
