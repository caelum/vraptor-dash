package br.com.caelum.vraptor.dash.monitor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;
import br.com.caelum.vraptor.resource.HttpMethod;

public class FreemakerRouteTest {

	private FreeMakerRoute route;
	private Route routeMock;
	private MutableRequest requestMock;
	private DefaultResourceMethod resourceMethod;

	@Before
	public void setup() {
		this.requestMock = mock(MutableRequest.class);
		this.routeMock = mock(Route.class);
		this.route = new FreeMakerRoute(routeMock, requestMock);
	}
	
	@Test
	public void returnsControllerAndMethodName() throws SecurityException, NoSuchMethodException  {
		Class<?> type = RoutesController.class;
		Method method = type.getDeclaredMethods()[0];
		this.resourceMethod = new DefaultResourceMethod(new DefaultResourceClass(type), method);
		String uri = "/dash/routes";
		when(routeMock.getOriginalUri()).thenReturn(uri);
		when(routeMock.resourceMethod(this.requestMock, uri)).thenReturn(this.resourceMethod);
		Assert.assertEquals("RoutesController.allRoutes", route.getControllerAndMethodName());
	}
	
	
	@Test
	public void returnsGETandPOSTWhenHttpMethodsGETAndPOSTAllowed() {
		when(routeMock.allowedMethods()).thenReturn(EnumSet.of(HttpMethod.GET, HttpMethod.POST));
		Assert.assertEquals("[GET POST]", route.getAllowedMethods());
	}	
	
	@Test
	public void returnsPUTandDeleteWhenHttpMethodsPutAndDeleteAllowed() {
		when(routeMock.allowedMethods()).thenReturn(EnumSet.of(HttpMethod.PUT, HttpMethod.DELETE));
		Assert.assertEquals("[PUT DELETE]", route.getAllowedMethods());
	}
	
	@Test
	public void returnsALLWhenAllHttpMethodsAllowed() {
		when(routeMock.allowedMethods()).thenReturn(EnumSet.allOf(HttpMethod.class));
		Assert.assertEquals("[ALL]", route.getAllowedMethods());
	}
	
	
}
