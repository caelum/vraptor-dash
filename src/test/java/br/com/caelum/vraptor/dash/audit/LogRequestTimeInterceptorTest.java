package br.com.caelum.vraptor.dash.audit;

import static br.com.caelum.vraptor.dash.matchers.IsOfControllerMatcher.isOpenRequestOfController;
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
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.controller.DefaultBeanClass;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequest;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequestInterceptor;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequests;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;

@RunWith(MockitoJUnitRunner.class)
public class LogRequestTimeInterceptorTest {

	private OpenRequestInterceptor interceptor;
	private @Mock OpenRequests requests;
	private @Mock SimpleInterceptorStack stack;
	private @Mock ControllerMethod controllerMethod;

	@Before
	public void setUp() throws Exception {
		configureMockControllerMethod();

		OpenRequest openRequest = new OpenRequest(controllerMethod);
		when(requests.add(controllerMethod)).thenReturn(openRequest);

		interceptor = new OpenRequestInterceptor(requests, controllerMethod);
	}

	private void configureMockControllerMethod() {
		Method method = Class.class.getDeclaredMethods()[0];
		when(controllerMethod.getMethod()).thenReturn(method);

		DefaultBeanClass resourceClass = new DefaultBeanClass(Class.class);
		when(controllerMethod.getController()).thenReturn(resourceClass);
	}

	@Test
	public void addsAndRemovesControllerMethodsInOpenRequestsBeforeAndAfterStackExecutionRespectively() throws Exception {
		interceptor.intercept(stack);

		InOrder inOrder = inOrder(requests, stack);
		inOrder.verify(requests).add(controllerMethod);
		inOrder.verify(stack).next();
		inOrder.verify(requests).remove(argThat(isOpenRequestOfController(controllerMethod)));
	}

	@Test(expected=InterceptionException.class)
	public void addsAndRemovesControllerMethodsInOpenRequestsEvenWhenStackExecutionThrowsException() throws Exception {
		doThrow(new InterceptionException("execution failed")).when(stack).next();

		interceptor.intercept(stack);

		verify(requests).add(controllerMethod);
		verify(requests).remove(argThat(isOpenRequestOfController(controllerMethod)));
	}
}
