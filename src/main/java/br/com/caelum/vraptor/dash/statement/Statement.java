package br.com.caelum.vraptor.dash.statement;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "DashStatement")
@Table(name="DashStatement")
public class Statement {

	@Id
	@GeneratedValue(generator="statement-uuid")
	@GenericGenerator(name="statement-uuid",strategy = "uuid")
	private String id;

	private String name;

	private String hql;

	private String password;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
	protected Statement() {
	}

	public Statement(String name, String hql) {
		this.name = name;
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
		return isOpenForOthersWithPassword() && password.equals(key);
	}
	
	public boolean isOpenForOthersWithPassword() {
		return password!=null && !password.isEmpty();
	}

}
