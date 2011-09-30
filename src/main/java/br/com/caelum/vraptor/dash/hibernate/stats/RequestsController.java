package br.com.caelum.vraptor.dash.hibernate.stats;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.HttpResult;

@Resource
public class RequestsController {

	private final Result result;
	private final OpenRequests requests;
	private final HibernateStatsAwareUser user;
	

	public RequestsController(Result result, OpenRequests requests, HibernateStatsAwareUser user) {
		this.result = result;
		this.requests = requests;
		this.user = user;
	}

	@Path("/dash/requests")
	public void show() {
		if(! user.canSeeHibernateStats()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		result.include("requests", requests.toMap());
	}
}
