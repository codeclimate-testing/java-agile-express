package com.express.domain;

import com.express.testutils.SetterGetterInvoker;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import java.util.ArrayList;
import java.util.List;

public class UserTest extends UnitilsJUnit4 {
   private static final Log LOG = LogFactory.getLog(BacklogTest.class);
   private User user;
   
   @Before
   public void setUp() {
      user = new User();
      LOG.info(user);
   }
   
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<User> setterGetterInvoker = new SetterGetterInvoker<User>(new User());
       setterGetterInvoker.invokeSettersAndGetters();
   }
   
   @Test
   public void checkConstructedProperly() {
      assertEquals(user.getActive(), false);
      assertNotNull(user.toString());
   }
   
   @Test
   public void testAddRemoveAccessRequests() {
      assertNotNull(user.getAccessRequests());
      Assert.assertEquals(0, user.getAccessRequests().size());
      AccessRequest request = new AccessRequest();
      user.addAccessRequest(request);
      Assert.assertEquals(1, user.getAccessRequests().size());
      user.removeAccessRequest(request);
      Assert.assertEquals(0, user.getAccessRequests().size());
   }

   @Test
   public void testGetSetAccessRequest() {
      List<AccessRequest> accessList = new ArrayList<AccessRequest>();
      AccessRequest request = new AccessRequest();
      accessList.add(request);
      Assert.assertEquals(0, user.getAccessRequests().size());
      user.setAccessRequests(accessList);
      Assert.assertEquals(1, user.getAccessRequests().size());
      Assert.assertEquals(request, user.getAccessRequests().get(0));
   }

   @Test
   public void testGetUsername() {
      String email = "test@test.com";
      user.setEmail(email);
      Assert.assertEquals(email, user.getUsername());
   }

}
