package br.com.caelum.vraptor.dash.statement;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.caelumweb2.logica.AbstractDaoTest;

public class StatementTest extends AbstractDaoTest {

	private StatementDao dao;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		dao = controller.getStatementDao();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSeNaoPermiteClausulaDelete(){
		String hql = "delete from Matricula as m";
		Statement r = new Statement(hql);
		r.valida(dao);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSeNaoPermiteClausulaUpdate(){
		String hql = "update from Matricula as m";
		Statement r = new Statement(hql);
		r.valida( dao);
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
