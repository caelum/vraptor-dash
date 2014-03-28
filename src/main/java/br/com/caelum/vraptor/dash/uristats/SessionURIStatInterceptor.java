package br.com.caelum.vraptor.dash.uristats;

import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.ioc.Container;

@Specializes
public class SessionURIStatInterceptor extends BaseURIStatInterceptor{

	private final Session session;

	@Inject
	public SessionURIStatInterceptor(Container container, Session session,
			HttpServletRequest request, HttpServletResponse response, 
			ControllerMethod method) {
		super(container, request, response, method);
		this.session = session;
	}
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected SessionURIStatInterceptor() {
		this(null, null, null, null, null);
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
