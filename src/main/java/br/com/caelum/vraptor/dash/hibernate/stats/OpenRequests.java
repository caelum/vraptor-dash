package br.com.caelum.vraptor.dash.hibernate.stats;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.vraptor.controller.ControllerMethod;

@ApplicationScoped
public class OpenRequests {

	private final Map<Long, OpenRequest> openRequests = new ConcurrentHashMap<Long, OpenRequest>();

	public OpenRequest add(ControllerMethod controllerMethod) {
		OpenRequest req = new OpenRequest(controllerMethod);
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
