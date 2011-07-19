package br.com.caelum.vraptor.dash.statement;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class AbstractTest {

	private static SessionFactory factory;
	protected Session session;

	@BeforeClass
	public static void setup() {
		factory = new AnnotationConfiguration().configure().addAnnotatedClass(Statement.class)
				.buildSessionFactory();
	}

	@Before
	public void setupSession() {
		this.session = factory.openSession();
	}

	@After
	public void shutdownSession() {
		if (this.session != null) {
			session.createQuery("delete from DashStatement").executeUpdate();
			this.session.close();
		}
	}

	@AfterClass
	public static void shutdown() {
		if (factory != null) {
			factory.close();
		}
	}

}
