package br.com.caelum.vraptor.dash.audit;

import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.dash.uristats.BaseURIStatInterceptor;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.ioc.Container;

@Intercepts
public class AuditLogInterceptor  {

	private static final Logger LOG = getLogger(AuditLogInterceptor.class);
	private final HttpServletRequest request;
	private final Container container;

	@Inject
	public AuditLogInterceptor(Container container, HttpServletRequest request) {
		this.container = container;
		this.request = request;
	}
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected AuditLogInterceptor() {
		this(null, null);
	}

	@Accepts
	public boolean accepts(ControllerMethod method) {
		return method.containsAnnotation(Audit.class);
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack, ControllerMethod method) {
		try {
			if (LOG.isInfoEnabled()) {
				StringBuilder builder = new StringBuilder(String.format(
						"audit [%s][%s][%s][%s][from %s]", method.toString(),
						BaseURIStatInterceptor.userKey(container),
						request.getRemoteAddr(), request.getRemoteHost(),
						request.getHeader("X_FORWARDED_FOR")));
				if("GET".equalsIgnoreCase(request.getMethod())) {
					Audit audit = method.getMethod().getAnnotation(Audit.class);
					for (String value : audit.value()) {
						builder.append("{"+value + "->" + request.getParameter(value)+"}");
					}
				}
				LOG.info(builder.toString());
			}
			stack.next();
		} catch (Exception ex) {
			LOG.error("error:",ex);
		}
	}
}
