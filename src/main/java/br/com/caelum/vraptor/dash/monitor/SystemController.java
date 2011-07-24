package br.com.caelum.vraptor.dash.monitor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.dash.audit.Audit;
import br.com.caelum.vraptor.dash.hibernate.AuditoriaController;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;

public class SystemController {

	private static Logger log = LoggerFactory.getLogger(AuditoriaController.class);

	@Path("/dash/system_properties")
	public InputStreamDownload listaPropriedades() throws IOException {
		StringBuilder sb = new StringBuilder();

		Set<Entry<Object, Object>> entrySet = System.getProperties().entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			sb.append(String.format("%-50s %s\n", entry.getKey(), entry.getValue()));
		}
		sb.append(String.format("%-50s %s\n", "Charset.defaultCharset()", Charset.defaultCharset()));

		return new InputStreamDownload(new ByteArrayInputStream(sb.toString().getBytes()), "text/plain", "log.log");
	}

	@Path("/auditoria/log")
	@Audit
	public InputStreamDownload listaLogPadrao() throws IOException {
		File file = new File("padrao.log");
		log.info("acessando arquivo de log: " + file.getCanonicalPath());
		if (!file.exists()) {
			throw new IllegalStateException("arquivo de log nao encontrado");
		}
		log.info("fazendo upload do arquivo de log: " + file.length());
		return new InputStreamDownload(new FileInputStream(file), "text/plain", "log.log");
	}

	@Path("/auditoria/acesso")
	@Audit
	public InputStreamDownload listaLogDeAcessoImportante() throws IOException {
		File file = new File("importante.log");
		log.info("acessando arquivo de log: " + file.getCanonicalPath());
		if (!file.exists()) {
			throw new IllegalStateException("arquivo de log nao encontrado");
		}
		log.info("fazendo upload do arquivo de log: " + file.length());

		return new InputStreamDownload(new FileInputStream(file), "text/plain", "log.log");
	}

	@Path("/auditoria/threads")
	public void threads() {
		result.forwardTo("/WEB-INF/jsp/auditoria/basic_monitor.jsp");
	}
}
