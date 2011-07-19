<html>

<body>
	<h2 class="pagetitle">Statement</h2>
	<div style="float:left" class="formulario">
		<form id="frmStatement" action='' method="post" >
			<div style="float:left; padding: 3px;margin: 2px;" >
				<label class="strong">Name</label>: &nbsp;
				<input id="assuntoStmt" type="text" name="statement.name" value=""/><br/><br/>
				<label class="strong">Password</label>: &nbsp;
				<input id="passwordStmt" type="password" name="statement.password" value=""/><br/><br/>
				<textarea id="hqlToExecute" style="width: 600px; height: 200px" name="statement.hql"></textarea>
			</div>
	
			<div style="padding-top:3px; clear:left">
				<input type="submit" value="Create" />
			</div>
	
		</form>
	</div>
	
	<br/>
	<fieldset style="padding: 4px; float:left; width:100%" class="fieldsetChique">
		<legend id="meusStmts" style="cursor: pointer">All statements</legend>
		<table id="statements" class="tabela" >
			<thead>
				<tr class="even">
					<th>Name</th>
					<th>Password protected?</th>
					<th>Hql</th>
					<th/>
	
				</tr>
			</thead>
			
	
			<tbody>
				<#assign even = "even">
				<#list statements as stmt>
					<#if even=="even">
						<#assign even = "odd">
					<#else>
						<#assign even = "even">
					</#if>
					<tr id="trStatement_${stmt.id}" class="${even}">
						<td>
							<a id="tituloStmt_${stmt.id}" href="statements/${stmt.id}">${stmt.name}</a>
						</td>
						<td id=""><#if stmt.passwordProtected>password</#if></td>
						<td id="corpoStmt_${stmt.id}">${stmt.hql}</td>
						<td>
							<a href="" onclick="alert('todo: support to remove, just link and do a method DELETE on it');" id="remove_${stmt.id}">remove</a>
						</td>
	
					</tr>
				</#list>
			</tbody>
		</table>
	</fieldset>
	<br/>
</body>
</html>