package br.com.caelum.vraptor.dash.uristats;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;

public class BaseURIStatInterceptorTest {

	@Test
	public void should_ignore_annotated_controllers() {
		BaseURIStatInterceptor interceptor = new BaseURIStatInterceptor(null, null, null);
		
		boolean accept = interceptor.accepts(
				new DefaultResourceMethod(new DefaultResourceClass(NotLoggedResource.class), 
				null));
		
		assertFalse(accept);
	}
}
