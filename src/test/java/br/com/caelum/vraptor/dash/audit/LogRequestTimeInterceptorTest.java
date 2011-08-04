package br.com.caelum.vraptor.dash.audit;

import static br.com.caelum.vraptor.dash.matchers.IsOfResourceMatcher.isOpenRequestOfResource;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequest;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequestInterceptor;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequests;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.ResourceClass;
import br.com.caelum.vraptor.resource.ResourceMethod;

@RunWith(MockitoJUnitRunner.class)
public class LogRequestTimeInterceptorTest {

	private OpenRequestInterceptor interceptor;
	private @Mock OpenRequests requests;
	private @Mock InterceptorStack stack;
	private @Mock ResourceMethod resourceMethod;

	@Before
	public void setUp() throws Exception {
		configureMockResourceMethod();

		OpenRequest openRequest = new OpenRequest(resourceMethod);
		when(requests.add(resourceMethod)).thenReturn(openRequest);

		interceptor = new OpenRequestInterceptor(requests);
	}

	private void configureMockResourceMethod() {
		Method method = Class.class.getDeclaredMethods()[0];
		when(resourceMethod.getMethod()).thenReturn(method);

		ResourceClass resourceClass = new DefaultResourceClass(Class.class);
		when(resourceMethod.getResource()).thenReturn(resourceClass);
	}

	@Test
	public void addsAndRemovesResourceMethodsInOpenRequestsBeforeAndAfterStackExecutionRespectively() throws Exception {
		interceptor.intercept(stack, resourceMethod, null);

		InOrder inOrder = inOrder(requests, stack);
		inOrder.verify(requests).add(resourceMethod);
		inOrder.verify(stack).next(resourceMethod, null);
		inOrder.verify(requests).remove(argThat(isOpenRequestOfResource(resourceMethod)));
	}

	@Test(expected=InterceptionException.class)
	public void addsAndRemovesResourceMethodsInOpenRequestsEvenWhenStackExecutionThrowsException() throws Exception {
		doThrow(new InterceptionException("execution failed")).when(stack).next(resourceMethod, null);

		interceptor.intercept(stack, resourceMethod, null);

		verify(requests).add(resourceMethod);
		verify(requests).remove(argThat(isOpenRequestOfResource(resourceMethod)));
	}
}
