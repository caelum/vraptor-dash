package br.com.caelum.vraptor.dash.hibernate.stats;

import javax.enterprise.inject.Vetoed;

import org.hibernate.stat.SecondLevelCacheStatistics;

@Vetoed
public class CacheStatsWrapper {

	private final String cacheName;
	private final SecondLevelCacheStatistics statistics;

	public CacheStatsWrapper(String cacheName, SecondLevelCacheStatistics statistics) {
		this.cacheName = cacheName;
		this.statistics = statistics;
	}

	public long getHitCount() {
		return statistics.getHitCount();
	}

	public long getMissCount() {
		return statistics.getMissCount();
	}

	public long getPutCount() {
		return statistics.getPutCount();
	}
	
	public String getCacheName() {
		return cacheName;
	}

}
