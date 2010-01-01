package com.express.service.internal;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.UnitilsJUnit4;
import com.express.domain.User;
import com.express.service.internal.UserService;
import com.express.service.internal.InternalUserService;
import com.express.dao.UserDao;
import net.sf.ehcache.Element;
import net.sf.ehcache.Ehcache;

import static org.mockito.BDDMockito.given;

/**
 * @author Adam Boas
 *         Created on Apr 11, 2009
 */
public class InternalUserServiceTest extends UnitilsJUnit4 {
   @Mock
   UserDao userDao;

   @Mock
   Ehcache cache;

   UserService userService;

   @Before
   public void setUp() {
      MockitoAnnotations.initMocks(this);
      userService = new InternalUserService(userDao, cache);
   }

   @Test
   public void checkLoadUserByUsernameReturnsUserFromCache() {
      String uname = "AbCde";
      User user = new User();
      user.setEmail(uname);
      given(cache.get(uname.toLowerCase())).willReturn(new Element(uname, user));
      userService.loadUserByUsername(uname);
   }
}
