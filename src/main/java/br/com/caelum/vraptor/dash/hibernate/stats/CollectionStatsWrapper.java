package br.com.caelum.vraptor.dash.hibernate.stats;

import javax.enterprise.inject.Vetoed;

import org.hibernate.stat.CollectionStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;

@Vetoed
public class CollectionStatsWrapper {

	private final String collectionRoleName;
	private final CollectionStatistics collectionStatistics;
	private SecondLevelCacheStatistics collectionSecondLevel;

	public CollectionStatsWrapper(String collectionRoleName, Statistics statistics) {
		this.collectionRoleName = collectionRoleName;
		this.collectionStatistics = statistics.getCollectionStatistics(collectionRoleName);
		this.collectionSecondLevel = statistics.getSecondLevelCacheStatistics(collectionRoleName);
	}
	
	public String getCollectionRoleName() {
		return collectionRoleName;
	}

	public long getFetchCount() {
		return collectionStatistics == null ? 0 : collectionStatistics.getFetchCount();
	}

	public long getLoadCount() {
		return collectionStatistics == null ? 0 : collectionStatistics.getLoadCount();
	}
	
	public long getHitCount() {
		return collectionSecondLevel == null ? 0 : collectionSecondLevel.getHitCount();
	}

	public long getMissCount() {
		return collectionSecondLevel == null ? 0 : collectionSecondLevel.getMissCount();
	}

	public long getPutCount() {
		return collectionSecondLevel == null ? 0 : collectionSecondLevel.getPutCount();
	}
	
}
