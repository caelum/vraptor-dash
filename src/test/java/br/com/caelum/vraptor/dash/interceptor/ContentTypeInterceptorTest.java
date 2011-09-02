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

import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.dash.statement.StatementController;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;
import br.com.caelum.vraptor.resource.ResourceMethod;

@RunWith(MockitoJUnitRunner.class)
public class ContentTypeInterceptorTest {

	private @Mock HttpServletResponse response;

	@Test
	public void acceptsOnlyControllersInsideDashPackage() throws Exception {
		ContentTypeInterceptor interceptor = new ContentTypeInterceptor(response);
		ResourceMethod vraptorDashMethod = resourceMethodInClass(StatementController.class, "index", Integer.class);
		assertTrue(interceptor.accepts(vraptorDashMethod));
		ResourceMethod vraptorRandomMethod = resourceMethodInClass(Validator.class, "validate", Object.class);
		assertFalse(interceptor.accepts(vraptorRandomMethod));
	}

	private ResourceMethod resourceMethodInClass(Class<?> type, String methodName, Class<?>... methodArgs) {
		DefaultResourceClass vraptorDashController = new DefaultResourceClass(type);
		Method dashMethod = new Mirror().on(vraptorDashController.getClass()).reflect().method(methodName).withArgs(methodArgs);
		DefaultResourceMethod vraptorDashMethod = new DefaultResourceMethod(vraptorDashController, dashMethod);
		return vraptorDashMethod;
	}
}
