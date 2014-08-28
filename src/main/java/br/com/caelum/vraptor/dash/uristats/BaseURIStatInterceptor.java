package br.com.caelum.vraptor.dash.uristats;

import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.dash.cache.ObservableResponse;
import br.com.caelum.vraptor.http.VRaptorResponse;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.ioc.Container;

/**
 * Saves all request information as statistics
 * 
 * @author guilherme silveira
 */
@Intercepts
public class BaseURIStatInterceptor {
	
	private static final Logger LOG = LoggerFactory.getLogger(BaseURIStatInterceptor.class);
	private final HttpServletRequest request;
	private final Container container;
	private final HttpServletResponse response;
	private final ControllerMethod method;

	@Inject
	public BaseURIStatInterceptor(Container container, 
			HttpServletRequest request, HttpServletResponse response, 
			ControllerMethod method) {
		this.container = container;
		this.request = request;
		this.response = response;
		this.method = method;
	}
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected BaseURIStatInterceptor() {
		this(null, null, null, null);
	}


	@Accepts
	public boolean accepts(ControllerMethod method) {
		return !method.getController().getType().isAnnotationPresent(NoURIStats.class);
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack) {
		
		long before = System.currentTimeMillis();
		stack.next();

		try {
			long time = System.currentTimeMillis() - before;
	
			String key = userKey(container);
	
			String resource = method.getController().getType().getName();
			String methodName = method.getMethod().getName();
	
			
			String etag = "";
			String cacheControl = "";
			String hadEtag = "unknown";
			long size = 0;
			int status = 200;
			if (response instanceof VRaptorResponse) {
				VRaptorResponse r = (VRaptorResponse) response;
				ServletResponse sr = r.getResponse();
				if(sr instanceof ObservableResponse) {
					ObservableResponse resp = (ObservableResponse) sr;
					etag = resp.getEtagHeader();
					if(etag.equals("")) {
						etag = resp.getMd5();
						hadEtag = "false";
					} else {
						hadEtag = "true";
					}
					size = resp.size();
					cacheControl = resp.getCacheControlHeader();
					status = resp.getGivenStatus();
				}
			}
			
			String queryString = extractQueryString(request.getMethod());
			
			String ip = extractIpAdress();
			
			Stat stat = new Stat(key, request.getRequestURI(), queryString, time,
					request.getMethod(), resource, methodName,
					etag, status, hadEtag,
					cacheControl, size,extractIpAdress());
			
			saveStat(stat);
			
		} catch (Exception ex) {
			LOG.error("Unable to prepare stat to save:", ex);
		}
	}

	private String extractIpAdress() {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	private String extractQueryString(String method) {
		// security, do not log parameters on passwords and so on
		if(!method.equalsIgnoreCase("GET")) return "";
		
		String queryString = "";
		Enumeration<String> paramNames = request.getParameterNames();
		boolean hadParameter = false;
		while(paramNames.hasMoreElements()) {
			if(hadParameter) {
				queryString += "&";
			}
			String name = paramNames.nextElement();
			queryString += name + "=" + request.getParameter(name);
			hadParameter = true;
		}
		return queryString;
	}

	/**
	 * Saves this stat where you want
	 * @param stat
	 */
	protected void saveStat(Stat stat) {
		stat.log();
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
