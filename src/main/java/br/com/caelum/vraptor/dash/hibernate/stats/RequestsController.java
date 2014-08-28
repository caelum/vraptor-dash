package br.com.caelum.vraptor.dash.hibernate.stats;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.HttpResult;

@Alternative
@Controller
public class RequestsController {

	private final Result result;
	private final OpenRequests requests;
	private final HibernateStatsAwareUser user;
	
	@Inject
	public RequestsController(Result result, OpenRequests requests, HibernateStatsAwareUser user) {
		this.result = result;
		this.requests = requests;
		this.user = user;
	}
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected RequestsController() {
		this(null, null, null);
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
