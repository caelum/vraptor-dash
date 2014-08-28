package br.com.caelum.vraptor.dash.hibernate.stats;

import javax.enterprise.inject.Vetoed;

import org.hibernate.stat.EntityStatistics;

@Vetoed
public class EntityStatsWrapper {

	private final String entityName;
	private final EntityStatistics entityStatistics;

	public EntityStatsWrapper(String entityName, EntityStatistics entityStatistics) {
		this.entityName = entityName;
		this.entityStatistics = entityStatistics;
	}

	public long getFetchCount() {
		return entityStatistics.getFetchCount();
	}

	public long getLoadCount() {
		return entityStatistics.getLoadCount();
	}
	
	public String getEntityName() {
		return entityName;
	}

}
