package br.com.caelum.vraptor.dash.statement;

import java.util.List;

import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;

public class StatementDaoTest extends AbstractTest{

	@Test(expected=IllegalArgumentException.class)
	public void hqlBizarroEhInvalido() {
		new StatementDao(session).valida("select");
	}

	@Test(expected=IllegalArgumentException.class)
	public void hqlQueNaoExecutaEhInvalido() throws Exception {
		new StatementDao(session).valida("from DashStatement as d where e = f");
	}

	@Test
	public void verificaRetornoDeQueries() {
		Transaction tx = session.beginTransaction();
		session.save(new Statement("first", "from First"));
		session.save(new Statement("second", "from Second"));
		tx.commit();
		List<Object[]> result = new StatementDao(session).execute(new Statement("statements", "select name, hql from DashStatement"));

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(2, result.get(0).length);
		Assert.assertEquals("first", result.get(0)[0]);
		Assert.assertEquals("second", result.get(1)[0]);
	}
}
