package br.com.caelum.vraptor.dash.hibernate.stats;

import javax.enterprise.inject.Vetoed;

@Vetoed
public class EntityCacheStatsWrapper {

	private EntityStatsWrapper entityStatsWrapper;
	private CacheStatsWrapper cacheStatsWrapper;

	public void setEntityStatsWrapper(EntityStatsWrapper entityStatsWrapper) {
		this.entityStatsWrapper = entityStatsWrapper;
	}

	public void setCacheStatsWrapper(CacheStatsWrapper cacheStatsWrapper) {
		this.cacheStatsWrapper = cacheStatsWrapper;
	}

	public String getName() {
		if ("".equals(getEntityName())) {
			return getCacheName();
		}
		return getEntityName();
	}
	
	public long getFetchCount() {
		return entityStatsWrapper == null ? 0 : entityStatsWrapper.getFetchCount();
	}

	public long getLoadCount() {
		return  entityStatsWrapper == null ? 0 : entityStatsWrapper.getLoadCount();
	}
	
	public String getEntityName() {
		return  entityStatsWrapper == null ? "" : entityStatsWrapper.getEntityName();
	}
	
	public long getHitCount() {
		return cacheStatsWrapper == null ? 0 : cacheStatsWrapper.getHitCount();
	}

	public long getMissCount() {
		return cacheStatsWrapper == null ? 0 : cacheStatsWrapper.getMissCount();
	}

	public long getPutCount() {
		return cacheStatsWrapper == null ? 0 : cacheStatsWrapper.getPutCount();
	}
	
	public String getCacheName() {
		return cacheStatsWrapper == null ? "" : cacheStatsWrapper.getCacheName();
	}

}
