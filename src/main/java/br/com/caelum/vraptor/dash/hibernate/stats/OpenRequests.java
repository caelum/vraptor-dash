package br.com.caelum.vraptor.dash.hibernate.stats;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;

@ApplicationScoped
@Component
public class OpenRequests {

	private final Map<Long, OpenRequest> openRequests = new ConcurrentHashMap<Long, OpenRequest>();

	public OpenRequest add(ResourceMethod resourceMethod) {
		OpenRequest req = new OpenRequest(resourceMethod);
		openRequests.put(req.getId(), req);
		return req;
	}

	public void remove(OpenRequest request) {
		openRequests.remove(request.getId());
	}

	public Map<Long, OpenRequest> toMap() {
		return openRequests;
	}

}
