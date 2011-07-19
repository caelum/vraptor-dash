package br.com.caelum.vraptor.dash.statement;

import org.hibernate.jdbc.Expectations;
import org.junit.Test;

public class StatementTest extends AbstractTest {

	@Test(expected=IllegalArgumentException.class)
	public void testSeNaoPermiteClausulaDelete(){
		String hql = "delete from DashStatement";
		Statement r = new Statement(hql, hql);
		r.valida(new StatementDao(session));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSeNaoPermiteClausulaUpdate(){
		String hql = "update from DashStatement";
		Statement r = new Statement(hql, hql);
		r.valida( new StatementDao(session));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testValidaHqlInvalido() {
		Statement statement = new Statement("inexisting", "from SomethingThatDoesNotExist");
		statement.valida(new StatementDao(session));
	}

}
