package br.com.caelum.vraptor.dash.monitor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.util.test.MockResult;

@RunWith(MockitoJUnitRunner.class)
public class RoutesControllerTest {

	private @Mock Router router;
	private @Mock Environment environment;

	private RoutesController controller;

	@Before
	public void setUp() throws Exception {
		this.controller = new RoutesController(router, new MockResult(), environment);
	}

	@Test
	public void showsAllRegisteredRoutesWhenAccessedOutOfProductionEnvironment() throws Exception {
		when(environment.getName()).thenReturn("notProduction");

		controller.allRoutes();

		verify(router).allRoutes();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void throwsExceptionWhenAccessedInProductionEnvironment() throws Exception {
		when(environment.getName()).thenReturn("production");

		controller.allRoutes();
	}
}
