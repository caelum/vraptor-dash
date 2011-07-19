package br.com.caelum.vraptor.dash.statement;

import org.junit.Assert;
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

	@Test
	public void testCanAccessAStatementWithTheCorrectKey() {
		Statement statement = new Statement("inexisting", "from SomethingThatDoesNotExist");
		statement.setPassword("mykey");
		Assert.assertTrue(statement.canBeAccessedWithKey("mykey"));
	}

	@Test
	public void testCannotAccessIfThereIsNoPassword() {
		Statement statement = new Statement("inexisting", "from SomethingThatDoesNotExist");
		Assert.assertFalse(statement.canBeAccessedWithKey(null));
		Assert.assertFalse(statement.canBeAccessedWithKey("myKey"));
	}

	@Test
	public void testCannotAccessWithWrongKey() {
		Statement statement = new Statement("inexisting", "from SomethingThatDoesNotExist");
		statement.setPassword("mykey");
		Assert.assertFalse(statement.canBeAccessedWithKey(null));
		Assert.assertFalse(statement.canBeAccessedWithKey("myKEY"));
	}

}
