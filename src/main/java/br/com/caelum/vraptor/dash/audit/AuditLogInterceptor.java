package br.com.caelum.vraptor.dash.audit;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.dash.uristats.BaseURIStatInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Component
public class AuditLogInterceptor implements Interceptor {

	private static final Logger LOG = Logger.getLogger(AuditLogInterceptor.class);
	private final HttpServletRequest request;
	private final Container container;

	public AuditLogInterceptor(Container container, HttpServletRequest request) {
		this.container = container;
		this.request = request;
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(Audit.class);
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object object) throws InterceptionException {
		try {
			if (LOG.isInfoEnabled()) {
				StringBuilder builder = new StringBuilder(String.format(
						"audit [%s][%s][%s][%s][from %s]", method.toString(),
						BaseURIStatInterceptor.userKey(container),
						request.getRemoteAddr(), request.getRemoteHost(),
						request.getHeader("X_FORWARDED_FOR")));
				Audit audit = method.getMethod().getAnnotation(Audit.class);
				for (String value : audit.value()) {
					builder.append(value + "->" + request.getParameter(value)
							+ "\n");
				}
				LOG.info(builder.toString());
			}
			stack.next(method, object);
		
		
		} catch (Exception ex) {
			LOG.error("error:",ex);
		}
	}
}
