<#list stackTraces as entry>
	<h1>Thread name: ${entry.key.name}</h1>
	<h3>Status: ${entry.key.state}</h3>

	<div class="stacktrace">
		<#if entry.value??>
			<#list entry.value as ste>
				[${ste.fileName}: ${ste.lineNumber}] ${ste.className}.${ste.methodName}<br/>
			</#list>
		<#else>
			Empty stacktrace
		</#if>
	</div>
</#list>