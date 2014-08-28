package br.com.caelum.vraptor.dash.monitor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.dash.audit.Audit;
import br.com.caelum.vraptor.dash.hibernate.AuditController;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.freemarker.FreemarkerView;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;
import freemarker.template.TemplateException;

@Controller
public class SystemController {

	public static final String ALLOWED_LOG_REGEX = "br.com.caelum.vraptor.dash.log.allowed";
	private static final Logger LOG = LoggerFactory.getLogger(AuditController.class);
	private final Environment environment;
	private final Result result;
	private final MonitorAwareUser user;

	@Inject
	public SystemController(Environment environment, Result result, MonitorAwareUser user) {
		this.environment = environment;
		this.result = result;
		this.user = user;
	}
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected SystemController() {
		this(null, null, null);
	}

	@Get("/dash/system_properties")
	public InputStreamDownload properties() throws IOException {
		if(! user.canSeeMonitorStats()) {
			result.use(HttpResult.class).sendError(401);
			return null;
		}
		StringBuilder sb = new StringBuilder();

		Set<Entry<Object, Object>> entrySet = System.getProperties().entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			sb.append(String.format("%-50s %s\n", entry.getKey(), entry.getValue()));
		}
		sb.append(String.format("%-50s %s\n", "Charset.defaultCharset()", Charset.defaultCharset()));

		return new InputStreamDownload(new ByteArrayInputStream(sb.toString().getBytes()), "text/plain", "log.log");
	}

	@Get("/dash/log/{name}")
	@Audit
	public InputStreamDownload log(String name) throws IOException {
		if(! user.canSeeMonitorStats()) {
			result.use(HttpResult.class).sendError(401);
			return null;
		}
		File file = new File(name);
		String allowedPattern = environment.get(SystemController.ALLOWED_LOG_REGEX);
		if(!name.matches(allowedPattern)) {
			result.use(Results.status()).forbidden("This file is not a log");
			return null;
		}
		LOG.debug("accessing file: " + file.getCanonicalPath());
		if (!file.exists()) {
			throw new IllegalStateException("file not found");
		}
		return new InputStreamDownload(new FileInputStream(file), "text/plain", "log.log");
	}

	@Get("/dash/threads")
	public void threads() throws IOException, TemplateException {
		if(! user.canSeeMonitorStats()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		result.include("stackTraces", Thread.getAllStackTraces().entrySet());
		result.use(FreemarkerView.class).withTemplate("audit/basicMonitor");
	}

	@Get("/dash/threads/stats")
	public void threadStats() {
		if(! user.canSeeMonitorStats()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		new BasicMonitor().logStats();
		result.use(Results.http()).body("Statistics logged").setStatusCode(200);
	}
}
