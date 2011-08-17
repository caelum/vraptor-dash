package br.com.caelum.vraptor.dash.hibernate.stats;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RequestsController {

	private final Result result;
	private final OpenRequests requests;
	private final HttpServletResponse response;
	

	public RequestsController(Result result, OpenRequests requests, HttpServletResponse response) {
		this.result = result;
		this.requests = requests;
		this.response = response;
	}

	@Path("/dash/requests")
	public void show() {
		result.include("requests", requests.toMap());
		response.setContentType("text/html");
	}
}
