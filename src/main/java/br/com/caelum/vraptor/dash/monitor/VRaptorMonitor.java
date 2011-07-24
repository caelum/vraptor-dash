package br.com.caelum.vraptor.dash.monitor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;

public class VRaptorMonitor {

	@Path("/auditoria/rotas")
	public InputStreamDownload listaRotas() throws IOException {
		List<Route> lista = new ArrayList<Route>(router.allRoutes());
		Collections.sort(lista, new Comparator<Route>() {
			public int compare(Route r1, Route r2) {
				return r1.toString().compareTo(r2.toString());
			}
		});
		StringBuilder sb = new StringBuilder();
		for (Route route : lista) {
			sb.append(route.toString() + "\n");
		}
		return new InputStreamDownload(new ByteArrayInputStream(sb.toString().getBytes()), "text/plain", "log.log");
	}
	
}
