package br.com.caelum.vraptor.dash.monitor;

import java.lang.reflect.Field;
import java.util.EnumSet;

import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.controller.HttpMethod;
import br.com.caelum.vraptor.http.route.FixedMethodStrategy;
import br.com.caelum.vraptor.http.route.Route;

public class FreemarkerRoute {

	private final Route route ;

	public FreemarkerRoute(Route route) {
		this.route = route;
	}

	public String getAllowedMethods() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");

		if(routeSupportsAllHttpMethods(this.route)) {
			builder.append("ALL");
		} else {
			builder.append(httpMethodsToString(this.route.allowedMethods()));
		}

		builder.append("]");
		return builder.toString();
	}


	public String getControllerAndMethodName() {
		Field resourceMethodField = new Mirror().on(FixedMethodStrategy.class).reflect().field("resourceMethod");
		resourceMethodField.setAccessible(true);
		try {
			ControllerMethod resourceMethod = (ControllerMethod) resourceMethodField.get(route);
			return resourceMethod.getMethod().toString();
		} catch (Exception e) {
			return "Unknown: " + e.getMessage();
		}
	}

	public String getOriginalUri() {
		return route.getOriginalUri();
	}


	private void deleteLastCharFrom(StringBuilder builder) {
		builder.deleteCharAt(builder.length() - 1);
	}

	private String httpMethodsToString(EnumSet<HttpMethod> httpMethods) {
		StringBuilder builder = new StringBuilder();
		for (HttpMethod httpMethod : httpMethods) {
			builder.append(httpMethod.name());
			builder.append(" ");
		}
		deleteLastCharFrom(builder);
		return builder.toString();
	}

	private boolean routeSupportsAllHttpMethods(Route route) {
		return route.allowedMethods().size() == HttpMethod.values().length;
	}
}
