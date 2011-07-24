package br.com.caelum.vraptor.dash.hibernate;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequest;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequests;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;

public class RequestsAbertasTest {

	private DefaultResourceMethod wrapped;

	@Before
	public void setup() {
		Class<?> type = Class.class;
		Method method = type.getDeclaredMethods()[0];
		this.wrapped = new DefaultResourceMethod(new DefaultResourceClass(type), method);
	}

	@Test
	public void adicionaCorretamenteAoMapa() throws Exception {
		OpenRequests requestsAbertas = new OpenRequests();

		assertEquals(0, requestsAbertas.toMap().size());
		requestsAbertas.add(wrapped);
		assertEquals(1, requestsAbertas.toMap().size());
	}

	@Test
	public void removeCorretamenteAoMapa() throws Exception {
		OpenRequests requestsAbertas = new OpenRequests();

		OpenRequest request = requestsAbertas.add(wrapped);
		requestsAbertas.remove(request);
		assertEquals(0, requestsAbertas.toMap().size());
	}

}
