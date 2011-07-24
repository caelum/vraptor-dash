package br.com.caelum.vraptor.dash.hibernate;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequest;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequests;

public class RequestsAbertasTest {

	private Class type;
	private Method method;

	@Before
	public void setup() {
		this.type = Class.class;
		this.method = type.getDeclaredMethods()[0];
	}

	@Test
	public void adicionaCorretamenteAoMapa() throws Exception {
		OpenRequest request = new OpenRequest(method, type);
		OpenRequests requestsAbertas = new OpenRequests();

		assertEquals(0, requestsAbertas.toMap().size());
		requestsAbertas.add(request);
		assertEquals(1, requestsAbertas.toMap().size());
	}

	@Test
	public void removeCorretamenteAoMapa() throws Exception {
		OpenRequest request = new OpenRequest(method, type);
		OpenRequests requestsAbertas = new OpenRequests();

		requestsAbertas.add(request);
		requestsAbertas.remove(request);
		assertEquals(0, requestsAbertas.toMap().size());
	}

}
