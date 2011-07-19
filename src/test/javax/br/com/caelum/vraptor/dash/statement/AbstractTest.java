package br.com.caelum.vraptor.dash.statement;

public class AbstractTest {

	public Statement novoStatement() {
		final Statement stmt = new Statement("select id, name from Statement");
		stmt.setName("Hql name");
		session.save(stmt);
		return stmt;
	}

}
