package br.com.caelum.vraptor.dash.monitor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.dash.audit.Audit;
import br.com.caelum.vraptor.dash.hibernate.AuditController;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;
import freemarker.template.TemplateException;

@Resource
public class SystemController {

	public static final String ALLOWED_LOG_REGEX = "br.com.caelum.vraptor.dash.log.allowed";
	private static Logger log = LoggerFactory.getLogger(AuditController.class);
	private final Freemarker marker;
	private final Environment environment;
	private final Result result;

	public SystemController(Freemarker marker, Environment environment, Result result) {
		this.marker = marker;
		this.environment = environment;
		this.result = result;
	}

	@Path("/dash/system_properties")
	public InputStreamDownload properties() throws IOException {
		StringBuilder sb = new StringBuilder();

		Set<Entry<Object, Object>> entrySet = System.getProperties().entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			sb.append(String.format("%-50s %s\n", entry.getKey(), entry.getValue()));
		}
		sb.append(String.format("%-50s %s\n", "Charset.defaultCharset()", Charset.defaultCharset()));

		return new InputStreamDownload(new ByteArrayInputStream(sb.toString().getBytes()), "text/plain", "log.log");
	}

	@Path("/dash/log/{name}")
	@Audit
	public InputStreamDownload log(String name) throws IOException {
		File file = new File(name);
		String allowedPattern = environment.get(SystemController.ALLOWED_LOG_REGEX);
		if(!name.matches(allowedPattern)) {
			result.use(Results.status()).forbidden("This file is not a log");
			return null;
		}
		log.info("accessing file: " + file.getCanonicalPath	());
		if (!file.exists()) {
			throw new IllegalStateException("file not found");	
		}
		return new InputStreamDownload(new FileInputStream(file), "text/plain", "log.log");
	}

	@Path("/dash/threads")
	public void threads() throws IOException, TemplateException {
		marker.use("audit/basicMonitor").render();
	}
}
