package br.com.caelum.vraptor.dash.statement;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Transaction;
import org.junit.Test;

public class StatementDaoTest extends DatabaseIntegrationTest{

	@Test(expected=IllegalArgumentException.class)
	public void throwsExceptionWhenValidatingInvalidHql() {
		new StatementDao(session).validate("select",null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void throwsExceptionWhenValidatingHqlThatDoesNotExecute() throws Exception {
		new StatementDao(session).validate("from DashStatement as d where d.a_b_c__d = 1",null);
	}

	@Test
	public void returnsAnEmptyListForAStatementWithoutResults() throws Exception {
		List<Object[]> result = new StatementDao(session).execute(new Statement("statements", "select name from DashStatement"), null);
		assertEquals(0, result.size());
	}

	@Test
	public void assemblesResultListWithLinesFromQueryResultWhenEachLineHasOnlyOneColumn() {
		Transaction tx = session.beginTransaction();
		session.save(new Statement("first", "from First"));
		session.save(new Statement("second", "from Second"));
		tx.commit();
		List<Object[]> result = new StatementDao(session).execute(new Statement("statements", "select name from DashStatement"),null);

		assertEquals(2, result.size());
		assertEquals(1, result.get(0).length);
		assertEquals(1, result.get(1).length);
		assertEquals("first", result.get(0)[0]);
		assertEquals("second", result.get(1)[0]);
	}

	@Test
	public void assemblesResultListWithLinesFromQueryResultWhenEachLineHasManyColumns() {
		Transaction tx = session.beginTransaction();
		session.save(new Statement("first", "from First"));
		session.save(new Statement("second", "from Second"));
		tx.commit();
		List<Object[]> result = new StatementDao(session).execute(new Statement("statements", "select name, hql from DashStatement"), null);

		assertEquals(2, result.size());
		assertEquals(2, result.get(0).length);
		assertEquals(2, result.get(1).length);
		assertEquals("first", result.get(0)[0]);
		assertEquals("second", result.get(1)[0]);
		assertEquals("from First", result.get(0)[1]);
		assertEquals("from Second", result.get(1)[1]);
	}
	
	@Test
	public void shouldOnlyReturn1000Itens() {
		Transaction tx = session.beginTransaction();
		for (int i = 1 ; i <= 1010; i++) {
			session.save(new Statement("" + i, "from First"));
		}
		tx.commit();
		List<Object[]> results = new StatementDao(session).execute(new Statement("statements", "select name, hql from DashStatement"), null);
		assertEquals(1000, results.size());
	}
	
	@Test
	public void shouldExecuteAQueryWithParameters() {
		Transaction tx = session.beginTransaction();
		session.save(new Statement("first", "from First"));
		session.save(new Statement("last", "from Last"));
		tx.commit();
		
		List<Object[]> results = new StatementDao(session).execute(new Statement("statements", "select name, hql from DashStatement where name = ?"), Arrays.<String>asList("first"));
		assertEquals(1,results.size());
		assertEquals("first",results.get(0)[0]);
		assertEquals("from First",results.get(0)[1]);
	}
}
