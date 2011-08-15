package br.com.caelum.vraptor.dash.statement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StatementTest extends DatabaseIntegrationTest {
	
	@Test
	public void shouldEscapeLowerThanSymbol() {
		String hql = "select login from User where id < 10";
		Statement stmt = new Statement("doni",hql);
		
		
		assertEquals("select login from User where id &lt; 10",stmt.getEscapedHql());
		assertEquals("select login from User where id < 10",stmt.getHql());
	}
	
	@Test
	public void shouldEscapeGreaterThanSymbol() {
		String hql = "select login from User where id > 10";
		Statement stmt = new Statement("doni",hql);
		
		
		assertEquals("select login from User where id &gt; 10",stmt.getEscapedHql());
		assertEquals("select login from User where id > 10",stmt.getHql());
	}

	@Test(expected=IllegalArgumentException.class)
	public void deleteIsNotValid(){
		String hql = "delete from DashStatement";
		Statement stmt = new Statement(hql, hql);
		stmt.validate(new StatementDao(session));
	}

	@Test(expected=IllegalArgumentException.class)
	public void updateIsNotValid(){
		String hql = "update from DashStatement";
		Statement stmt = new Statement(hql, hql);
		stmt.validate( new StatementDao(session));
	}

	@Test(expected=IllegalArgumentException.class)
	public void hqlReferencingUnexistingEntityIsNotValid() {
		Statement statement = new Statement("inexisting", "from SomethingThatDoesNotExist");
		statement.validate(new StatementDao(session));
	}

	@Test
	public void canAccessAStatementWithTheCorrectKey() {
		Statement statement = new Statement("inexisting", "from SomethingThatDoesNotExist");
		statement.setPassword("mykey");
		assertTrue(statement.canBeAccessedWithKey("mykey"));
	}

	@Test
	public void cannotAccessIfThereIsNoPassword() {
		Statement statement = new Statement("inexisting", "from SomethingThatDoesNotExist");
		assertFalse(statement.canBeAccessedWithKey(null));
		assertFalse(statement.canBeAccessedWithKey("myKey"));
	}

	@Test
	public void cannotAccessWithWrongKey() {
		Statement statement = new Statement("inexisting", "from SomethingThatDoesNotExist");
		statement.setPassword("mykey");
		assertFalse(statement.canBeAccessedWithKey(null));
		assertFalse(statement.canBeAccessedWithKey("myKEY"));
	}

}
