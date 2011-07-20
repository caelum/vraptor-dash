package br.com.caelum.vraptor.dash.uristats;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;

/**
 * Saves all request information as statistics
 * 
 * @author guilherme silveira
 */
@Intercepts(before = ExecuteMethodInterceptor.class)
public class URIStatInterceptor implements Interceptor {

	private final Session session;
	private final HttpServletRequest request;
	private final Container container;

	public URIStatInterceptor(Container container, Session session,
			HttpServletRequest request) {
		this.container = container;
		this.session = session;
		this.request = request;
	}

	public boolean accepts(ResourceMethod arg0) {
		return true;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object instance) throws InterceptionException {
		long before = System.currentTimeMillis();
		stack.next(method, instance);
		String key = "";
		try {
			IdeableUser user = container.instanceFor(IdeableUser.class);
			key = user.getId().toString();
		} catch (Exception e) {
			key = "";
		}
		Stat stat = new Stat(key, request.getRequestURI(),
				System.currentTimeMillis() - before, request.getMethod());
		session.save(stat);
	}

}
