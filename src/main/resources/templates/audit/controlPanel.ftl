<html>
	<body>
		<fieldset>
			<legend>Connections</legend>
			<ul>
				Connection count: ${connectionCount}
			</ul>
		</fieldset>
		
		<fieldset>
			<legend>Second level cache</legend>
			<ul>
				Second level cache hit count: ${secondLevelCacheHitCount}
			</ul>
			<ul>
				Second level cache miss count: ${secondLevelCacheMissCount}
			</ul>
			<ul>
				Second level cache put count: ${secondLevelCachePutCount}
			</ul>
		</fieldset>
		
		<fieldset>
			<legend>Memory usage</legend>
			<ul>
				Total memory: ${totalMemory}
			</ul>
			<ul>
				Used memory: ${usedMemory} (${usedMemoryPerCent})
			</ul>
			<ul>
				Free memory: ${freeMemory} (${freeMemoryPerCent})
			</ul>
		</fieldset>
		
		<fieldset>
			<legend>Conection pool</legend>
			<ul>
				Initial pool size: ${initPoolSize}
			</ul>
			<ul>
				Max pool size: ${maxPoolSize}
			</ul>
			<ul>
				Min pool size: ${minPoolSize}
			</ul>
			<ul>
				Number of busy connections: ${numBusyCon}
			</ul>
			<ul>
				Number of connections: ${numCon}
			</ul>
			<ul>
				Number of idle connections: ${numIdleCon}
			</ul>
			<ul>
				Number of user pools: ${numUserPools}
			</ul>
		</fieldset>
		
		<fieldset>
			<legend>Query Statistics</legend>
			
			<table id="queries">
			<thead>
				<tr>
					<th>Query</th>
					<th>Count</th>
					<th>Average Time (ms)</th>
					<th>Put</th>
					<th>Hits</th>
					<th>Miss</th>
				</tr>
			</thead>
			<tbody>
				<#list queryStatsList as q>
					<tr>
						<td>${q.query}</td>
						<td>${q.executionCount}</td>
						<td>${q.executionAvgTime}</td>
						<td>${q.cachePutCount}</td>
						<td>${q.cacheHitCount}</td>
						<td>${q.cacheMissCount}</td>
					</tr>
				</#list>
			</tbody>
			</table>
		</fieldset>
		
		<fieldset>
			<legend>Entity Statistics</legend>
			
			<table id="entities">
			<thead>
				<tr>
					<th>Entity</th>
					<th>Fetch</th>
					<th>Load</th>
					<th>Put</th>
					<th>Miss</th>
					<th>Hit</th>
				</tr>
			</thead>
			<tbody>
				<#list entityCacheStats?keys as k>
					<tr>
						<td>${entityCacheStats[k].name}</td>
						<td>${entityCacheStats[k].fetchCount}</td>
						<td>${entityCacheStats[k].loadCount}</td>
						<td>${entityCacheStats[k].putCount}</td>
						<td>${entityCacheStats[k].missCount}</td>
						<td>${entityCacheStats[k].hitCount}</td>
					</tr>
				</#list>
			</tbody>
			</table>
		</fieldset>
		
		<fieldset>
			<legend>Collections Statistics</legend>
			
			<table id="collections">
			<thead>
				<tr>
					<th>Collection Role Name</th>
					<th>Fetch count</th>
					<th>Load count</th>
					<th>Put</th>
					<th>Miss</th>
					<th>Hit</th>
				</tr>
			</thead>
			<tbody>
				<#list collectionsStatsList as e>
					<tr>
						<td>${e.collectionRoleName}</td>
						<td>${e.fetchCount}</td>
						<td>${e.loadCount}</td>
						<td>${e.putCount}</td>
						<td>${e.missCount}</td>
						<td>${e.hitCount}</td>
					</tr>
				</#list>
			</tbody>
			</table>
		</fieldset>
		
		<fieldset>
			<legend>EhCache Statistics</legend>
			
			<table id="ehCache">
			<thead>
				<tr>
					<th>Cache Name</th>
					<th>Cache hits</th>
					<th>Cache misses</th>
					<th>Object count</th>
				</tr>
			</thead>
			<tbody>
				<#list ehCacheStatsList as e>
					<tr>
						<td>${e.associatedCacheName}</td>
						<td>${e.cacheHits}</td>
						<td>${e.cacheMisses}</td>
						<td>${e.objectCount}</td>
					</tr>
				</#list>
			</tbody>
			</table>
		</fieldset>
	</body>
</html>