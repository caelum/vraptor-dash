package br.com.caelum.vraptor.dash.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.http.route.Router;
import freemarker.template.TemplateException;

@Resource
public class RoutesController {
	
	private static final String INDEX = "routes/index";

	private final Router router;
	private final Freemarker maker;
	private final MutableRequest request;

	public RoutesController(Router router, MutableRequest request, Freemarker maker) {
		this.router = router;
		this.request = request;
		this.maker = maker;
	}

	@Path("/dash/routes") @Get
	public void allRoutes() throws IOException, TemplateException {
		List<Route> routes = orderRoutesByURI(router.allRoutes());
		List<FreeMakerRoute> freemakerRoutes = createRoutesForFreeMaker(routes);
		this.maker.use(INDEX).with("routes", freemakerRoutes).render();
	}

	private List<FreeMakerRoute> createRoutesForFreeMaker(List<Route> routes) {
		List<FreeMakerRoute> freemakerRoutes = new ArrayList<FreeMakerRoute>();
		for (Route route : routes) {
			freemakerRoutes.add(new FreeMakerRoute(route, this.request));
		}
		return freemakerRoutes;
	}

	private List<Route> orderRoutesByURI(List<Route> allRoutes) {
		List<Route> routes = new ArrayList<Route>(allRoutes);
		Collections.sort(routes, new CompareByRouteToString());
		return routes;
	}
	
	private final class CompareByRouteToString implements Comparator<Route> {
		
		public int compare(Route r1, Route r2) {
			return r1.getOriginalUri().compareTo(r2.getOriginalUri());
		}
	}
}
