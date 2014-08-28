package br.com.caelum.vraptor.dash.cache;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheCheckFilter implements Filter {

	private final static Logger LOGGER = LoggerFactory.getLogger(CacheCheckFilter.class);

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		try {
			res = new ObservableResponse((HttpServletResponse) res);
		} catch (Exception ex) {
			LOGGER.error("Observable response was not injected", ex);
		}

		chain.doFilter(req, res);

	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
