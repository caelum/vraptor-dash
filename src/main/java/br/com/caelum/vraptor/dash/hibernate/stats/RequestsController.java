package br.com.caelum.vraptor.dash.hibernate.stats;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RequestsController {
	
	private Result result;
	private final OpenRequests requests;

	public RequestsController(Result result, OpenRequests requests) {
		this.result = result;
		this.requests = requests;
	}
	
	@Path("/auditoria/requests")
	public void visualiza() {
		result.include("requests", requests.toMap());
	}
}
