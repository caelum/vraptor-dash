package br.com.caelum.vraptor.dash.statement;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Statement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String hql;

	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Deprecated
	private Statement() {
	}

	public Statement(String hql) {
		this.hql = hql;
	}

	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public void valida(StatementDao dao) {
		if (hql.contains("delete") || hql.contains("update")) {
			throw new IllegalArgumentException(
					"O hql nao pode conter a clausula de delete ou update");
		}
		try {
			dao.valida(hql);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public boolean canBeAccessedWithKey(String key) {
		return !isPasswordProtected() || password.equals(key);
	}
	
	public boolean isPasswordProtected() {
		return !(password==null || password.isEmpty());
	}

}
