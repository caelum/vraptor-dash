package br.com.caelum.vraptor.dash.uristats;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import br.com.caelum.vraptor.controller.DefaultBeanClass;
import br.com.caelum.vraptor.controller.DefaultControllerMethod;

public class BaseURIStatInterceptorTest {

	@Test
	public void should_ignore_annotated_controllers() {
		BaseURIStatInterceptor interceptor = new BaseURIStatInterceptor(null, null, null, null);
		
		boolean accept = interceptor.accepts(
				new DefaultControllerMethod(new DefaultBeanClass(NotLoggedResource.class), 
				null));
		
		assertFalse(accept);
	}
}
