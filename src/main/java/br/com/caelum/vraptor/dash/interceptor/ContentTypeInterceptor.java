package br.com.caelum.vraptor.dash.interceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.BeforeCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;

@Intercepts
public class ContentTypeInterceptor {

	private final HttpServletResponse response;

	@Inject
	public ContentTypeInterceptor(HttpServletResponse response) {
		this.response = response;
	}
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected ContentTypeInterceptor() {
		this(null);
	}

	@Accepts
	public boolean accepts(ControllerMethod method) {
		return method.getController().getType().getPackage()
			.getName().startsWith("br.com.caelum.vraptor.dash");
	}

	@BeforeCall
	public void intercept() {
        response.setContentType("text/html");
	}

}