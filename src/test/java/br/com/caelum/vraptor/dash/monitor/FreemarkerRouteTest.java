package br.com.caelum.vraptor.dash.monitor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.controller.HttpMethod;
import br.com.caelum.vraptor.http.route.Route;

@RunWith(MockitoJUnitRunner.class)
public class FreemarkerRouteTest {

	private @Mock Route routeMock;
	private FreemarkerRoute route;

	@Before
	public void setUp() throws Exception {
		route = new FreemarkerRoute(routeMock);
	}

	@Test
	public void returnsGETandPOSTWhenHttpMethodsGETAndPOSTAllowed() {
		when(routeMock.allowedMethods()).thenReturn(EnumSet.of(HttpMethod.GET, HttpMethod.POST));
		assertEquals("[GET POST]", route.getAllowedMethods());
	}

	@Test
	public void returnsPUTandDeleteWhenHttpMethodsPutAndDeleteAllowed() {
		when(routeMock.allowedMethods()).thenReturn(EnumSet.of(HttpMethod.PUT, HttpMethod.DELETE));
		assertEquals("[PUT DELETE]", route.getAllowedMethods());
	}

	@Test
	public void returnsALLWhenAllHttpMethodsAllowed() {
		when(routeMock.allowedMethods()).thenReturn(EnumSet.allOf(HttpMethod.class));
		assertEquals("[ALL]", route.getAllowedMethods());
	}


}
