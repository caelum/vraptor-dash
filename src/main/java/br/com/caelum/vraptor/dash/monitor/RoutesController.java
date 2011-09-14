package br.com.caelum.vraptor.dash.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.freemarker.FreemarkerView;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.http.route.Router;
import freemarker.template.TemplateException;

@Resource
public class RoutesController {

	private static final String PRODUCTION = "production";
	private static final String INDEX = "routes/index";

	private final Router router;
	private final Environment environment;
	private final Result result;

	public RoutesController(Router router, Result result, Environment environment) {
		this.router = router;
		this.result = result;
		this.environment = environment;
	}

	@Path("/dash/routes") @Get
	public void allRoutes() throws IOException, TemplateException {
		if (PRODUCTION.equals(environment.getName())) {
			throw new UnsupportedOperationException();
		}

		List<Route> routes = orderRoutesByURI(router.allRoutes());
		List<FreemarkerRoute> freemarkerRoutes = createRoutesForFreeMarker(routes);
		result.include("routes", freemarkerRoutes);
		result.use(FreemarkerView.class).withTemplate(INDEX);
	}

	private List<FreemarkerRoute> createRoutesForFreeMarker(List<Route> routes) {
		List<FreemarkerRoute> freemakerRoutes = new ArrayList<FreemarkerRoute>();
		for (Route route : routes) {
			freemakerRoutes.add(new FreemarkerRoute(route));
		}
		return freemakerRoutes;
	}

	private List<Route> orderRoutesByURI(List<Route> allRoutes) {
		List<Route> routes = new ArrayList<Route>(allRoutes);
		Collections.sort(routes, new RouteUriComparator());
		return routes;
	}

	private final class RouteUriComparator implements Comparator<Route> {

		public int compare(Route r1, Route r2) {
			return r1.getOriginalUri().compareTo(r2.getOriginalUri());
		}
	}
}
