package br.com.caelum.vraptor.dash.uristats;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.dash.cache.ObservableResponse;
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
	private final HttpServletResponse response;

	public URIStatInterceptor(Container container, Session session,
			HttpServletRequest request, HttpServletResponse response) {
		this.container = container;
		this.session = session;
		this.request = request;
		this.response = response;
	}

	public boolean accepts(ResourceMethod arg0) {
		return true;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object instance) throws InterceptionException {
		long before = System.currentTimeMillis();
		stack.next(method, instance);
		long time = System.currentTimeMillis() - before;

		String key = userKey(container);

		String resource = method.getResource().getType().getName();
		String methodName = method.getMethod().getName();

		String etag = response.getHeader("etag");
		boolean hadEtag = etag != null;
		int size = 0;
		if (response instanceof ObservableResponse && !hadEtag) {
			ObservableResponse resp = (ObservableResponse) response;
			etag = resp.getMd5();
			size = resp.getBufferSize();
		}

		Stat stat = new Stat(key, request.getRequestURI(), time,
				request.getMethod(), resource, methodName,
				response.getHeader("etag"), response.getStatus(), hadEtag,
				response.getHeader("cache-control"), size);
		session.save(stat);
	}

	public static String userKey(Container container) {
		String key = "";
		try {
			IdeableUser user = container.instanceFor(IdeableUser.class);
			key = user.getId().toString();
		} catch (Exception e) {
			key = "";
		}
		return key;
	}

}
