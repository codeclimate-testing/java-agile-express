package com.express.testutils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;

public class SetterGetterInvoker<T> {

   private static final Log LOG = LogFactory.getLog(SetterGetterInvoker.class);

   private T testTarget;
   private ValueFactory valueFactory;

   /**
    * Constructor.
    * @param testTarget Target to be tested.
    */
   public SetterGetterInvoker(T testTarget) {
      super();
      this.testTarget = testTarget;
      this.valueFactory = new ValueFactory();
   }

   /**
    * Invoke setters and getters.
    */
   public void invokeSettersAndGetters() {
      Class<? extends Object> targetClass = testTarget.getClass();
      Method[] methods = targetClass.getMethods();
      for (int i = 0; i < methods.length; i++) {
         Method method = methods[i];
         if (method.getName().startsWith("set")) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1 && canEvaluate(method, parameterTypes[0])) {
               Object testValue = valueFactory.createValue(parameterTypes[0]);
               try {
                  if (LOG.isDebugEnabled()) {
                     LOG.debug("invoke set method: " + method.toString() + " with argument: " + testValue);
                  }
                  method.invoke(testTarget, testValue);
                  if (testValue instanceof Boolean) {
                     invokeGetter(targetClass, testValue, "is" + method.getName().substring(3));
                  } else {
                     invokeGetter(targetClass, testValue, "get" + method.getName().substring(3));
                  }
               } catch (IllegalAccessException e) {
                  Assert.fail("failed to access setter method: " + method.toString() + " - " + e.getMessage());
               } catch (InvocationTargetException e) {
                  Assert.fail("failed to invoke setter method: " + method.toString() + " - " + e.getMessage());
               }
            }
         }
      }
   }

   /**
    * @param targetClass Class of target object.
    * @param expectedValue Expected value.
    * @param getterName Name of getter.
    */
   private void invokeGetter(Class<?> targetClass, Object expectedValue, String getterName) {
      try {
         Method getterMethod = targetClass.getMethod(getterName);
         if (LOG.isDebugEnabled()) {
            LOG.debug("invoke get method: " + getterMethod.toString());
         }
         Object retrievedValue = getterMethod.invoke(testTarget);
         Class<?> returnType = getterMethod.getReturnType();
         if (returnType.isPrimitive()) {
            Assert.assertEquals("return value of " + getterName + " incorrect", expectedValue, retrievedValue);
         }
         else {
            Assert.assertSame("return value of " + getterName + " incorrect", expectedValue, retrievedValue);
         }
      } catch (NoSuchMethodException ignore) {
         // ignore if getter does not exist
         if (LOG.isDebugEnabled()) {
            LOG.debug("getter does not exist: " + getterName);
         }
      } catch (IllegalAccessException e) {
         Assert.fail("failed to access getter method: " + getterName + " - " + e.getMessage());
      } catch (InvocationTargetException e) {
         Assert.fail("failed to invoke getter method: " + getterName + " - " + e.getMessage());
      }
   }

   /**
    * This method will exclude cases where the get / set methods are wrappping a field with a
    * different type, such as presenting a List when the internal structure is a Set
    * @param method the setter method
    * @param parameterClass the class of the parameter being set
    * @return true or false measured by the parameter class being the same as the internal field
    * class
    */
   private boolean canEvaluate(Method method, Class<?> parameterClass) {
      String fieldName = method.getName().substring(3);
      fieldName = fieldName.replaceFirst(fieldName.substring(0,1),fieldName.substring(0,1).toLowerCase());
      try {
         Field field = testTarget.getClass().getDeclaredField(fieldName);
         LOG.debug(field.getType().getName() + ":" + parameterClass.getName());
         if(parameterClass.getName().equals(field.getType().getName())) {
             return true;
         }
      }
      catch (NoSuchFieldException e) {
         LOG.error(e);
      }
      return false;
   }

}