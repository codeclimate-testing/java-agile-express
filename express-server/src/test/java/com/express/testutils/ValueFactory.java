package com.express.testutils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;


public class ValueFactory {

   private Map<Class<?>, Object> typeValues;

   /**
    * Constructor.
    */
   public ValueFactory() {
      typeValues = new HashMap<Class<?>, Object>();
      typeValues.put(String.class, "test");
      typeValues.put(Long.class, new Long(42));
      typeValues.put(Long.TYPE, new Long(42));
      typeValues.put(Integer.class, Integer.valueOf(21));
      typeValues.put(Integer.TYPE, Integer.valueOf(21));
      typeValues.put(Boolean.class, Boolean.TRUE);
      typeValues.put(Boolean.TYPE, Boolean.TRUE);
      
   }

   /**
    * Create a test value.
    * @param type Desired type of value.
    * @return Created value.
    */
   public Object createValue(Class<?> type) {
      Object value = null;
      if (typeValues.containsKey(type)) {
         value = typeValues.get(type);
      } else if (type.isInterface()) {
         value = Proxy.newProxyInstance(type.getClassLoader(), new Class[] {type},
               new TestInvocationHandler(type.getName()));
      }
      return value;
   }

   private static class TestInvocationHandler implements InvocationHandler {
      
      private String name;
      
      
      public TestInvocationHandler(String name) {
         super();
         this.name = name;
      }

      
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         Object result = null;
         if ("toString".equals(method.getName())) {
            result = "proxy for " + name;
         }
         return result;
      }
      
   }
}
