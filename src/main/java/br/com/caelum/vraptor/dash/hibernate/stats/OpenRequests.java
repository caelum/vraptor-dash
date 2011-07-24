package br.com.caelum.vraptor.dash.hibernate.stats;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@ApplicationScoped
@Component
public class OpenRequests {

	private final Map<Long, OpenRequest> requestsAbertas = new ConcurrentHashMap<Long, OpenRequest>();

	public void add(OpenRequest request) {
		requestsAbertas.put(request.getId(), request);
	}

	public void remove(OpenRequest request) {
		requestsAbertas.remove(request.getId());
	}

	public Map<Long, OpenRequest> toMap() {
		return requestsAbertas;
	}

}
