package br.com.caelum.vraptor.dash.simplemail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.util.hibernate.SessionCreator;

@RequestScoped
public class DefaultMailer extends
		br.com.caelum.vraptor.simplemail.DefaultMailer {

	private final SessionCreator sessions;

	public DefaultMailer(Environment env, SessionCreator sessions) {
		super(env);
		this.sessions = sessions;
	}

	protected void wrapUpAndSend(Email email) throws EmailException {
		Session session = sessions.getInstance();

		Transaction tx = session.beginTransaction();
		try {
			try {
				email.send();
				session.save(EmailSent.from(email));
			} catch (RuntimeException ex) {
				session.save(EmailSent.from(email, ex));
				throw ex;
			} catch (EmailException ex) {
				session.save(EmailSent.from(email, ex));
				throw ex;
			} finally {
				tx.commit();
				tx = null;
			}
		} finally {
			if (tx != null) {
				tx.rollback();
			}
		}
	}

}
