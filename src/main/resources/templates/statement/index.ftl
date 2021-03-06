<html>
<head>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
</head>
<body>
	<h2 class="pagetitle">Statement</h2>
	<div style="float:left" class="formulario">
		<#if errors??>
			<div id="errors">
				<ul>
				<#list errors as error>
					<#-- if vraptor 2 message -->
					<#if error.class.name = "org.vraptor.i18n.FixedMessage">
						<li>${error.category}</li>
					<#-- it's vraptor 3 -->
					<#else>
						<li>${error.category} - ${error.message}</li>
					</#if>
				</#list>
				</ul>
			</div>
		</#if>
		<form id="frmStatement" action='statements/execute' method="post" >
			<div style="float:left; padding: 3px;margin: 2px;" >
				<label class="strong">Name</label>: &nbsp;
				<input id="assuntoStmt" type="text" name="statement.name" value=""/><br/><br/>
				<label class="strong">Password</label>: &nbsp;
				<input id="passwordStmt" type="password" name="statement.password" value=""/><br/><br/>
				<label class="strong">Max results</label>: &nbsp;
				<input id="size" type="text" name="maxResults" value="100"/><br/><br/>
				
				<div id="query">
					<textarea id="hqlToExecute" cols="100" rows="15" name="statement.hql"></textarea>
					<ul id="parameters" style="float: right; list-style:none; margin: 0px; padding-left:10px; width:600px;">
					</ul>
				</div>
				
			</div>
	
			<div style="padding-top:3px; clear:left">
				<input type="submit" id="execute" value="Execute Only" />
				<input type="button" id="create" value="Save and Execute" />
			</div>
	
		</form>
	</div>
	
	<br/>
	<fieldset style="padding: 4px; float:left; width:100%" class="fieldsetChique">
		<legend id="meusStmts" style="cursor: pointer">All statements</legend>
		<div style="padding-top:3px; clear:left">
			<form id="resultsSize" action='statements'>
				<input type="text" id="stmtListSize" name="size" value="${size}" />
				<input type="submit" id="submitSize" value="List statements" />	
			</form>
		</div>
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
						<td id="">
							<#if stmt.openForOthersWithPassword>
								open for password
							<#else>
								totally closed
							</#if>
						</td>
						<td id="corpoStmt_${stmt.id}">${stmt.escapedHql}</td>
						<td>
							<a style="cursor:pointer" class="remove_stmt" id="remove_${stmt.id}" data-stmtid="${stmt.id}">remove</a>
						</td>
					</tr>
				</#list>
			</tbody>
		</table>
	</fieldset>
	<br/>
	
	<script type="text/javascript">
	
		$(function() {
			$(".remove_stmt").click(function() {
				if(confirm("do you really want to delete it?")){
					var stmtId = $(this).attr('data-stmtid');
					$.ajax({
						url:'statements/'+ stmtId,
						type:'DELETE',
						success: function() {
							link = $("#trStatement_"+ stmtId);
							$(link).fadeOut(700, function() {
								link.remove();
							});
						}
					});
				}
				return false;
			});
			
			$('#create').click(function(){
				$('#frmStatement').attr('action','statements').submit();
			});
			
			
			var parameterCount = 1;
			
			$('#hqlToExecute').keypress(function(event){
    
    			// 63 is the code for '?'
			    if(event.which == 63) {
			      var input = '<li style="padding-bottom:3px; float:left; margin-right:6px; margin-right:6px;" id="parameter{p}">'+
			      			  '<label class="strong">Parameter {p}:</label>'+
			      			  '<input type="text" name="parameters[{p}]" style="margin-left:5px"></li>';
			      
			      $('#parameters').append(input.replace(/{([{p}]*)}/g,parameterCount));
			      parameterCount++;
			    }

			});
			
		});
	</script>
</body>
</html>