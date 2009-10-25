package com.express.service.dto;

import org.junit.Test;
import com.express.testutils.SetterGetterInvoker;

/**
 * @author Adam Boas
 *         Created on Mar 23, 2009
 */
public class ThemeDtoTest {

   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<ThemeDto> setterGetterInvoker = new SetterGetterInvoker<ThemeDto>(new ThemeDto());
       setterGetterInvoker.invokeSettersAndGetters();
   }
}