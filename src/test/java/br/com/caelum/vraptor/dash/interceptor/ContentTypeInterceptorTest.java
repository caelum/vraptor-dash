package br.com.caelum.vraptor.dash.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import net.vidageek.mirror.dsl.Mirror;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.controller.DefaultBeanClass;
import br.com.caelum.vraptor.controller.DefaultControllerMethod;
import br.com.caelum.vraptor.dash.statement.StatementController;
import br.com.caelum.vraptor.validator.Validator;

@RunWith(MockitoJUnitRunner.class)
public class ContentTypeInterceptorTest {

	private @Mock HttpServletResponse response;

	@Test
	public void acceptsOnlyControllersInsideDashPackage() throws Exception {
		ContentTypeInterceptor interceptor = new ContentTypeInterceptor(response);
		ControllerMethod vraptorDashMethod = resourceMethodInClass(StatementController.class, "index", Integer.class);
		assertTrue(interceptor.accepts(vraptorDashMethod));
		ControllerMethod vraptorRandomMethod = resourceMethodInClass(Validator.class, "validate", Object.class);
		assertFalse(interceptor.accepts(vraptorRandomMethod));
	}

	private ControllerMethod resourceMethodInClass(Class<?> type, String methodName, Class<?>... methodArgs) {
		DefaultBeanClass vraptorDashController = new DefaultBeanClass(type);
		Method dashMethod = new Mirror().on(vraptorDashController.getClass()).reflect().method(methodName).withArgs(methodArgs);
		DefaultControllerMethod vraptorDashMethod = new DefaultControllerMethod(vraptorDashController, dashMethod);
		return vraptorDashMethod;
	}
}
