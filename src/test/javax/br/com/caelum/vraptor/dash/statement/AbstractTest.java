package br.com.caelum.vraptor.dash.statement;

import org.hibernate.jdbc.Expectations;
import org.junit.Before;

public class AbstractTest {

	private Mockery mockery;

	@Before
	public void before() {
		this.mockery = new Mockery() {
			{
				setImposteriser(ClassImposteriser.INSTANCE);
			}
		};
	}

	public <T> T mock(Class<T> type) {
		return mockery.mock(type);
	}

	public <T> T mock(Class<T> type, String name) {
		return mockery.mock(type, name);
	}

	public <T> T stub(Class<T> type) {
		final T mock = mockery.mock(type);
		mockery.checking(new Expectations() {{
			allowing(mock);
		}});
		return mock;
	}

	public void assertIsSatisfied() {
		mockery.assertIsSatisfied();
	}

	public void checking(Expectations expectations) {
		mockery.checking(expectations);
	}


	public Statement novoStatement() {
		final Statement stmt = new Statement("select id, name from Statement");
		stmt.setName("Hql name");
		session.save(stmt);
		return stmt;
	}

}
