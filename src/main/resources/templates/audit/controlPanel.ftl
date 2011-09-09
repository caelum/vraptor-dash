<html>
	<body>
		<fieldset>
			<legend>Conex&otilde;es</legend>
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
			
			<p>
				Segure Shift para ordenar por m&uacute;ltiplas colunas
			</p>
			
			<table class="tabela sortable paged" id="queries">
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
				<caelum:forEach items="${queryStatsList}" var="q" varStatus="status">
					<tr id="query_${status.index}">
						<td>${q.query}</td>
						<td class="stats">${q.executionCount}</td>
						<td class="stats">${q.executionAvgTime}</td>
						<td class="stats">${q.cachePutCount}</td>
						<td class="stats">${q.cacheHitCount}</td>
						<td class="stats">${q.cacheMissCount}</td>
					</tr>
				</caelum:forEach>
			</tbody>
			</table>
		</fieldset>
		
		<fieldset>
			<legend>Entity Statistics</legend>
			
			<p>
				Segure Shift para ordenar por m&uacute;ltiplas colunas
			</p>
			
			<table class="tabela sortable paged" id="entities">
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
				<caelum:forEach items="${entityCacheStats}" var="e" varStatus="status">
					<tr id="entity_${status.index}">
						<td>${e.value.name}</td>
						<td class="stats">${e.value.fetchCount}</td>
						<td class="stats">${e.value.loadCount}</td>
						<td class="stats">${e.value.putCount}</td>
						<td class="stats">${e.value.missCount}</td>
						<td class="stats">${e.value.hitCount}</td>
					</tr>
				</caelum:forEach>
			</tbody>
			</table>
		</fieldset>
		
		<fieldset>
			<legend>Collections Statistics</legend>
			
			<p>
				Segure Shift para ordenar por m&uacute;ltiplas colunas
			</p>
			
			<table class="tabela sortable paged" id="collections">
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
				<caelum:forEach items="${collectionsStatsList}" var="e" varStatus="status">
					<tr id="collections_${status.index}">
						<td>${e.collectionRoleName}</td>
						<td class="stats">${e.fetchCount}</td>
						<td class="stats">${e.loadCount}</td>
						<td class="stats">${e.putCount}</td>
						<td class="stats">${e.missCount}</td>
						<td class="stats">${e.hitCount}</td>
					</tr>
				</caelum:forEach>
			</tbody>
			</table>
		</fieldset>
		
		<fieldset>
			<legend>EhCache Statistics</legend>
			
			<p>
				Segure Shift para ordenar por m&uacute;ltiplas colunas
			</p>
			
			<table class="tabela sortable paged" id="ehCache">
			<thead>
				<tr>
					<th>Cache Name</th>
					<th>Cache hits</th>
					<th>Cache misses</th>
					<th>Object count</th>
				</tr>
			</thead>
			<tbody>
				<caelum:forEach items="${ehCacheStatsList}" var="e" varStatus="status">
					<tr id="ehCache_${status.index}">
						<td>${e.associatedCacheName}</td>
						<td class="stats">${e.cacheHits}</td>
						<td class="stats">${e.cacheMisses}</td>
						<td class="stats">${e.objectCount}</td>
					</tr>
				</caelum:forEach>
			</tbody>
			</table>
		</fieldset>
	</body>
</html>