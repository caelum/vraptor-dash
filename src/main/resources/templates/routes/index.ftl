<html>
<body>
	
	<fieldset style="padding: 4px; float:left; width:100%" class="fieldsetChique">
		<legend id="meusStmts" style="cursor: pointer">All routes</legend>
		<table id="statements" class="tabela" >
			<thead>
				<tr class="even">
					<th>URI</th>
					<th>Allowed HTTP Methods</th>
					<th>Controller Method</th>
				</tr>
			</thead>
	
			<tbody>
				<#assign even = "even">
				<#list routes as route>
					<#if even=="even">
						<#assign even = "odd">
					<#else>
						<#assign even = "even">
					</#if>
					<tr class="${even}">
						<td>${route.originalUri}</td>
						<td>${route.allowedMethods}</td>
						<td>${route.controllerAndMethodName}</td>
					</tr>
				</#list>
			</tbody>
		</table>
	</fieldset>
</body>
</html>