package br.com.caelum.vraptor.dash.uristats;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts(before=ExecuteMethodInterceptor.class)
public class URIStatInterceptor implements Interceptor {
	
	private final IdeableUser user;
	private final Session session;
	private final HttpServletRequest request;

	public URIStatInterceptor(IdeableUser user, Session session, HttpServletRequest request) {
		this.user = user;
		this.session = session;
		this.request = request;
	}

	public boolean accepts(ResourceMethod arg0) {
		return true;
	}

	public void intercept(InterceptorStack arg0, ResourceMethod method,
			Object arg2) throws InterceptionException {
		session.save(new Stat(user.getId().toString(), request.getRequestURI()));
	}

}
