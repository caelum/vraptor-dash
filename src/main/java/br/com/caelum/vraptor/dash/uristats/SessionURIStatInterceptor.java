package br.com.caelum.vraptor.dash.uristats;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.vraptor.ioc.Container;

public class SessionURIStatInterceptor extends BaseURIStatInterceptor{

	private final Session session;

	public SessionURIStatInterceptor(Container container, Session session,
			HttpServletRequest request, HttpServletResponse response) {
		super(container, request, response);
		this.session = session;
	}
	
	protected void saveStat(Stat stat) {
		Transaction tx = session.getTransaction();
		boolean created = false;
		if(tx==null || !tx.isActive()) {
			tx = session.beginTransaction();
			created = true;
		}
		try {
			session.save(stat);
			if(created) {
				tx.commit();
			}
		} finally {
			if(created && tx.isActive()) {
				tx.rollback();
			}
		}
	}

}
