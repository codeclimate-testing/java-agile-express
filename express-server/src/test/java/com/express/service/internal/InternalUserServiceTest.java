package com.express.service.internal;

import org.junit.Test;
import org.junit.Before;
import static org.easymock.EasyMock.expect;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.UnitilsJUnit4;
import com.express.domain.User;
import com.express.service.internal.UserService;
import com.express.service.internal.InternalUserService;
import com.express.dao.UserDao;
import net.sf.ehcache.Element;
import net.sf.ehcache.Ehcache;

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
      userService = new InternalUserService(userDao, cache);
   }

   @Test
   public void checkLoadUserByUsernameReturnsUserFromCache() {
      String uname = "AbCde";
      User user = new User();
      user.setEmail(uname);
      expect(cache.get(uname.toLowerCase())).andReturn(new Element(uname, user));
      EasyMockUnitils.replay();
      userService.loadUserByUsername(uname);
   }
}
