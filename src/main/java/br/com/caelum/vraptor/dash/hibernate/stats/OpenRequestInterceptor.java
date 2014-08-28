package br.com.caelum.vraptor.dash.hibernate.stats;

import javax.inject.Inject;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;

@Intercepts
public class OpenRequestInterceptor {

	private final OpenRequests requests;
	private final ControllerMethod method;
	
	@Inject
	public OpenRequestInterceptor(OpenRequests requests, ControllerMethod method) {
		this.requests = requests;
		this.method = method;
	}
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected OpenRequestInterceptor() {
		this(null, null);
	}
	
	@AroundCall
	public void intercept(SimpleInterceptorStack stack) {
		OpenRequest openRequest = requests.add(method);
		try {
			stack.next();
		} finally {
			requests.remove(openRequest);
		}
	}

}
