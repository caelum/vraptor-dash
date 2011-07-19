package br.com.caelum.vraptor.dash.statement;

import org.hibernate.jdbc.Expectations;
import org.junit.Test;

public class StatementTest extends AbstractTest {

	@Test(expected=IllegalArgumentException.class)
	public void testSeNaoPermiteClausulaDelete(){
		String hql = "delete from Matricula as m";
		Statement r = new Statement(hql);
		r.valida(new StatementDao(session));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSeNaoPermiteClausulaUpdate(){
		String hql = "update from Matricula as m";
		Statement r = new Statement(hql);
		r.valida( new StatementDao(session));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testValidaHqlInvalido() {
		final StatementDao mock = mockery.mock(StatementDao.class);
		Statement statement = new Statement("qualquer coisa");
		mockery.checking(new Expectations() {{
			one(mock).valida("qualquer coisa"); will(throwException(new IllegalArgumentException()));
		}});
		statement.valida(mock);

		mockery.assertIsSatisfied();
	}

}
