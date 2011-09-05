package br.com.caelum.vraptor.dash.monitor;

import java.util.EnumSet;

import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.resource.HttpMethod;
import br.com.caelum.vraptor.resource.ResourceMethod;

public class FreemarkerRoute {

	private final Route route ;
	private final MutableRequest request;

	public FreemarkerRoute(Route route, MutableRequest request) {
		this.route = route;
		this.request = request;
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
		ResourceMethod resourceMethod = route.resourceMethod(this.request, this.route.getOriginalUri());
		String controllerName = resourceMethod.getResource().getType().getSimpleName();
		String methodName = resourceMethod.getMethod().getName();
		return controllerName + "." + methodName;
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
