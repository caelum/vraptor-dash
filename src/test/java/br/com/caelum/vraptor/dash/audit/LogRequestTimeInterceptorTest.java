package br.com.caelum.vraptor.dash.audit;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.dash.hibernate.stats.LogRequestTimeInterceptor;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequest;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequests;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.ResourceClass;
import br.com.caelum.vraptor.resource.ResourceMethod;

public class LogRequestTimeInterceptorTest {

	@Test
	public void insereRecursoCorretamenteEmRequestsAbertas() throws Exception {
		InterceptorStack stack = mock(InterceptorStack.class);

		ResourceMethod resourceMethod = mock(ResourceMethod.class);

		final Method method = Class.class.getDeclaredMethods()[0];
		when(resourceMethod.getMethod()).thenReturn(method);

		final ResourceClass resourceClass = new DefaultResourceClass(Class.class);
		when(resourceMethod.getResource()).thenReturn(resourceClass);

		OpenRequests requests = mock(OpenRequests.class);
		final OpenRequest requestAberta = new OpenRequest(method, resourceClass.getType());

		LogRequestTimeInterceptor interceptor = new LogRequestTimeInterceptor(requests);
		interceptor.intercept(stack, resourceMethod, null);

		// Verifica que o elemento adicionado no RequestsAbertas tem o "recurso"
		// (Nome do metodo e da classe) esperados
		verify(requests).add(argThat(new VerificaRequestApenasPorRecurso(requestAberta)));

	}

	private final class VerificaRequestApenasPorRecurso extends TypeSafeMatcher<OpenRequest> {
		private final OpenRequest requestAberta;

		private VerificaRequestApenasPorRecurso(OpenRequest requestAberta) {
			this.requestAberta = requestAberta;
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("That t");
		}

		@Override
		protected boolean matchesSafely(OpenRequest item) {
			return requestAberta.getRecurso().equals(item.getRecurso());
		}

		@Override
		protected void describeMismatchSafely(OpenRequest item, Description mismatchDescription) {
			// TODO Auto-generated method stub

		}
	}

}
