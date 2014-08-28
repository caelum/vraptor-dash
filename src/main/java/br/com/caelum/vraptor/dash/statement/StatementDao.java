package br.com.caelum.vraptor.dash.statement;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;

public class StatementDao {

	private final Session session;
	
	@Inject
	public StatementDao(Session session) {
		this.session = session;
	}
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected StatementDao() {
		this(null);
	}

	public void validate(String hql, List<String> parameters) {
		try {
			createQuery(hql,parameters,1000).list();
		} catch (Exception exception) {
			throw new IllegalArgumentException(exception);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> execute(Statement st, List<String> parameters, Integer size) {
		
		List results = createQuery(st.getHql(),parameters,size).list();
		
		if (!results.isEmpty() && results.get(0).getClass().isArray()) {
			return results;
		}
		List<Object[]> wrappedResults = new ArrayList<Object[]>();
		for (Object o : results) { 
			wrappedResults.add(new Object[] { o });
		}
		return wrappedResults;
	}

	public void save(Statement statement) {
		session.save(statement);
	}

	public void merge(Statement statement) {
		session.merge(statement);
	}

	public Statement load(String id) {
		return (Statement) session.load(Statement.class, id);
	}
	
	public void delete(Statement statement) {
		session.delete(statement);
	}

	@SuppressWarnings("unchecked")
	public List<Statement> all(Integer size) {
		return createQuery("from DashStatement",null, size).setCacheable(true).list();
	}
	
	private Query createQuery(String hql, List<String> parameters, Integer size) {
		
		Query query = session.createQuery(hql);

		if(parameters != null) {
			for(int i = 0; i < parameters.size(); i++) {
				query.setParameter(i,parameters.get(i));
			}
		}
		
		return query.setMaxResults(size);
	}

}
