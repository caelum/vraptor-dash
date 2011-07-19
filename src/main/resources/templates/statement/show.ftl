<html>
<head>
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("jquery", "1.6.2");
	google.load("visualization", "1", {packages:["linechart"]});
    google.setOnLoadCallback(function() {
        $(function() {
			$('#graph').hide();
			$('#cumulativeGraph').click(drawChart);
			$('#graphShow').click(graphDisplay);
			<#if resultado[0]?size==2 || resultado[0]?size==3>
				$('#cumulativeGraphForm').show();
			</#if>
			drawChart();
        });
	});
	
    function drawChart() {
        var acumular = $('#cumulativeGraph').attr('checked');
        var chartOptions = {width: 800, height: 450, title: 'Statement'};
        var chartDrawArea = document.getElementById('graphCanvas');
		var data = new google.visualization.DataTable();
		<#if resultado[0]?size == 2>
			data.addColumn('string', 'Column 1');
			data.addColumn('number', 'Column 2');

			data.addRows(${resultado.size});
			var acumulador = 0;
			<#assign status = -1>
			<#list resultado as linha>
				<#assign status = status + 1>
				data.setCell(${status}, 0, '${linha[0]}');
				if(acumular == true) {
					acumulador += ${linha[1]};
					data.setCell(${status}, 1, acumulador);
				} else {
					data.setCell(${status}, 1, ${linha[1]});
				}
			</#list>

		</#if>
		<#if resultado[0]?size == 3>
			var indiceDaLinha = {};
			var colunas = {};
			data.addColumn('string', 'Coluna 1');
			<#list resultado as linha>
				indiceDaLinha['${linha[0]}'] = 0;
			</#list>
			var counter = 1;
			for (var key in indiceDaLinha) {
				data.addColumn('number', key);
				indiceDaLinha[key] = counter++;
			}
			
			
			<#list resultado as linha>
				colunas['${linha[1]}'] = 0;
			</#list>
			counter = 0;
			for(var k in colunas) { 
				counter++;
			}
			data.addRows(counter);
			
			counter = 0;
			for (var key in colunas) {
				data.setCell(counter, 0, key);
				colunas[key] = counter++;
			}
			var acumulador = {};
			for (var index in indiceDaLinha) {
				acumulador[index] = 0;
			}
			<#list resultado as linha>
				if (acumular == true) {
					acumulador['${linha[0]}'] += ${linha[2]};
					data.setCell(colunas['${linha[1]}'], indiceDaLinha['${linha[0]}'], acumulador['${linha[0]}']);
				} else {
					data.setCell(colunas['${linha[1]}'], indiceDaLinha['${linha[0]}'], ${linha[2]});
				}
			</#list>
		</#if>
		var columnCount = $('table#resultado tr:first-child td').size();
		if (columnCount == 2 || columnCount == 3) {
			var chart = new google.visualization.LineChart(chartDrawArea);
			chart.draw(data, chartOptions);
		}
	}

	function graphDisplay() {
		$("#graph").toggle("slow");
	}
</script>
</head>
<body>

<fieldset style="padding: 4px; float:left; width:100%" class="fieldsetChique">
	<legend>${statement.name}</legend>

	<button id="graphShow">Graph</button>
	<div id="graph" class="hideme">
		<div id="graphCanvas">
			To generate a graph, the query must contain only two or three columns, being the last one a numeric value.
		</div>
		<form id="cumulativeGraphForm" style="display:none">
			<input type="checkbox" id="cumulativeGraph" name="cumulativeGraph" />
			<label for="cumulativeGraph">cumulative graph</label>
		</form>
	</div>
	<table id="resultado">
	<#list resultado as row>
		<tr>
		<#list row as value>
			<td>value</td>
		</#list>
		</tr>
	</#list>
	</table>
</fieldset>
</body>
</html>