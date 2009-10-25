package com.express.domain;

import org.junit.Test;
import com.express.testutils.SetterGetterInvoker;

/**
 * @author Adam Boas
 *         Created on Mar 28, 2009
 */
public class ProjectWorkerTest {

   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<ProjectWorker> setterGetterInvoker = new SetterGetterInvoker<ProjectWorker>(new ProjectWorker());
       setterGetterInvoker.invokeSettersAndGetters();
   }
}
