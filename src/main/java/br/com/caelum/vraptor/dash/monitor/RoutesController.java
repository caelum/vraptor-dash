package br.com.caelum.vraptor.dash.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.http.route.Router;
import freemarker.template.TemplateException;

@Resource
public class RoutesController {

	private static final String PRODUCTION = "production";
	private static final String INDEX = "routes/index";

	private final Router router;
	private final Freemarker marker;
	private final MutableRequest request;
	private final Environment environment;

	public RoutesController(Router router, MutableRequest request, Freemarker marker, Environment environment) {
		this.router = router;
		this.request = request;
		this.marker = marker;
		this.environment = environment;
	}

	@Path("/dash/routes") @Get
	public void allRoutes() throws IOException, TemplateException {
		if (PRODUCTION.equals(environment.getName())) {
			throw new UnsupportedOperationException();
		}

		List<Route> routes = orderRoutesByURI(router.allRoutes());
		List<FreemarkerRoute> freemakerRoutes = createRoutesForFreeMarker(routes);
		this.marker.use(INDEX).with("routes", freemakerRoutes).render();
	}

	private List<FreemarkerRoute> createRoutesForFreeMarker(List<Route> routes) {
		List<FreemarkerRoute> freemakerRoutes = new ArrayList<FreemarkerRoute>();
		for (Route route : routes) {
			freemakerRoutes.add(new FreemarkerRoute(route, this.request));
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
