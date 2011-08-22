package br.com.caelum.vraptor.dash.hibernate.stats;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RequestsController {

	private final Result result;
	private final OpenRequests requests;
	

	public RequestsController(Result result, OpenRequests requests) {
		this.result = result;
		this.requests = requests;
	}

	@Path("/dash/requests")
	public void show() {
		result.include("requests", requests.toMap());
	}
}
