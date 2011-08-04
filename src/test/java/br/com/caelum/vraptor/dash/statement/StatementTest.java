package br.com.caelum.vraptor.dash.statement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StatementTest extends DatabaseIntegrationTest {

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
