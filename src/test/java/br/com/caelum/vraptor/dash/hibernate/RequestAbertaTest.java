package br.com.caelum.vraptor.dash.hibernate;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.Method;
import java.util.Calendar;

import org.junit.Test;

import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequest;

public class RequestAbertaTest {

	@Test
	public void verificaSeIdsDeRequestsAbertasSaoDiferentes() throws Exception {
		Class<?> classe = Class.class;
		Method metodo = classe.getDeclaredMethods()[0];
		Calendar agora = Calendar.getInstance();
		OpenRequest request1 = new OpenRequest(metodo, classe, agora);
		OpenRequest request2 = new OpenRequest(metodo, classe, agora);
		
		assertFalse(request1.getId().equals(request2.getId()));
		
	}
}
