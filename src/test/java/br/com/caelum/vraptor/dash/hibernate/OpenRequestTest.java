package br.com.caelum.vraptor.dash.hibernate;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.Method;
import java.util.Calendar;

import org.junit.Test;

import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequest;

public class OpenRequestTest {

	@Test
	public void openRequestIdsMustBeDifferent() throws Exception {
		Class<?> clazz = Class.class;
		Method method = clazz.getDeclaredMethods()[0];
		Calendar now = Calendar.getInstance();
		OpenRequest request1 = new OpenRequest(method, clazz, now);
		OpenRequest request2 = new OpenRequest(method, clazz, now);

		assertFalse(request1.getId().equals(request2.getId()));

	}
}
