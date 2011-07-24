package br.com.caelum.vraptor.dash.hibernate.stats;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;

@ApplicationScoped
@Component
public class OpenRequests {

	private final Map<Long, OpenRequest> requestsAbertas = new ConcurrentHashMap<Long, OpenRequest>();

	public OpenRequest add(ResourceMethod method) {
		Method metodo = method.getMethod();
		Class<?> classe = method.getResource().getType();
		OpenRequest req = new OpenRequest(metodo, classe);
		return requestsAbertas.put(req.getId(), req);
	}

	public void remove(OpenRequest request) {
		requestsAbertas.remove(request.getId());
	}

	public Map<Long, OpenRequest> toMap() {
		return requestsAbertas;
	}

}
