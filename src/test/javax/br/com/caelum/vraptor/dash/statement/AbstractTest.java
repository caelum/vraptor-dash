package br.com.caelum.vraptor.dash.statement;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class AbstractTest {

	private SessionFactory factory;
	protected Session session;

	@BeforeClass
	public void setup() {
		this.factory = new AnnotationConfiguration().configure()
				.buildSessionFactory();
	}

	@Before
	public void setupSession() {
		this.session = factory.openSession();
	}

	@After
	public void shutdownSession() {
		if (this.session != null) {
			this.session.close();
		}
	}

	@AfterClass
	public void shutdown() {
		if (factory != null) {
			factory.close();
		}
	}

}
