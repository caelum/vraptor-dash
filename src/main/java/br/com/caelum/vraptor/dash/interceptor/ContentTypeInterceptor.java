package br.com.caelum.vraptor.dash.interceptor;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Lazy;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
@RequestScoped
@Lazy
public class ContentTypeInterceptor implements Interceptor {


	private final HttpServletResponse response;

	public ContentTypeInterceptor(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.getResource().getType().getPackage().getName().startsWith("br.com.caelum.vraptor.dash");
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object object) throws InterceptionException {
        response.setContentType("text/html");
        stack.next(method, object);
	}

}
