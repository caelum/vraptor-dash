<html>
<body>

<fieldset>
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
	<br/>
	<br/>
	<div style="padding-top:3px; clear:left">
		<form id="frmStatement" action="execute" method="post">
			<label class="strong">Name:</label>
			<input type="text" name="statement.name" value="${statement.name}"/>
			<br/>
			<label class="strong">Hql:</label>
			<textarea name="statement.hql" cols="100" rows="15">${statement.hql}</textarea>
			<br/>
			<label for="stmtListSize">Max results: </label>
			<input type="text" id="stmtListSize" name="maxResults" value="${maxResults?c}" />
			<input type="submit" id="execute" value="Execute" />
			<input type="button" id="create" value="Save and Execute" />
		</form>
	</div>
	<table id="result_data">
	<#if result?size == 0>
		<tr><td>No results</td></tr>
	<#else>
	
		<thead>
			<tr>
				<#list columns as column>
					<th>${column}</th>
				</#list>
			</tr>
		</thead>
		<tbody>
			<#list result as row>
				<tr>
				<#list row as value>
					<td>${value!"null"}</td>
				</#list>
				</tr>
			</#list>
		</tbody>
	</#if>
	</table>
</fieldset>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
    
    function TwoColumnChartDrawer(data, headers) {
        var graphData = [headers];
        for(var i = 0; i < data.length; i++) {
            graphData.push(data[i]);
        }
        return function(options, canvas, columnsToHide) {
            if(columnsToHide != undefined && columnsToHide.length > 0) {
                throw Exception("Can't filter 2 columns chart");
            }
            var chart = new google.visualization.LineChart(canvas);
            var dataTable = google.visualization.arrayToDataTable(graphData);
            chart.draw(dataTable, options);
        }
    }
    
    function ThreeColumnChartDrawer(data, headers) {
        // depende da ordenação total dos dados
        function createLine(begin, end, data, columns) {
            var line = [data[begin][1]];
            var j = begin;
            var i = 0;
            for(;j < end && i < columns.length; i++) {
                if(data[j][0] == columns[i]) {
                    line.push(data[j][2]);
                    j++;
                } else {
                    line.push(null);
                }
            }
            while(i < columns.length) {
                line.push(null);
                i++;
            }   
            return line;
        }
    
        var columns = [];
        var _columns = {};
        // ordena as colunas sem repetição
        for(var i = 0; i < data.length; i++) {
            if(_columns[data[i][0]] != 1) {
                _columns[data[i][0]] = 1;
                columns.push(data[i][0]);
            }
        }
        columns.sort();
        // faz a ordenação total dos dados
        data.sort(function(a, b) {
            if(a[1] == b[1]) {
                if(a[0] < b[0]) return -1;
                else if(a[0] > b[0]) return 1;
                else return 0;
            } else {
                if(a[1] < b[1]) return -1;
                else if(a[1] > b[1]) return 1;
                else return 0;
            }
        });
        
        var firstline = [headers[1]];
        for(var i = 0; i < columns.length; i++) {
            firstline.push(columns[i]);
        }
        var lines = [firstline];
        
        for(i = 0; i < data.length;) {
            var valor = data[i][1];
            var j = i;
            // enquanto eu estiver na mesma linha
            for(; j <= data.length; j++) {
                if(j == data.length || data[j][1] != valor) {
                    break;
                }
            }
            lines.push(createLine(i, j, data, columns));
            i = j;
        }
       
        return function(options, canvas, columnsToHide) {
            var chartData = [];
            var indicesToHide = [];
            for(var i = 0; i < columnsToHide.length; i++){
                indicesToHide.push(columns.indexOf(columnsToHide[i]) + 1);
            }
            
            for(var i = 0; i < lines.length; i++) {
                var line = [];
                for(var j = 0; j < lines[i].length; j++) {
                    if(indicesToHide.indexOf(j) == -1) {
                        line.push(lines[i][j]);
                    }
                }
                chartData.push(line);
            }
            var dataTable = google.visualization.arrayToDataTable(chartData);
            var chart = new google.visualization.LineChart(canvas);
            chart.draw(dataTable, options);
        }
    }
    
    function readTableData(tableId) {
        var data = [];
        var headers = [];
        $("#" + tableId + " thead tr th").each(function() {
            headers.push($(this).text());
        });
        var columns = headers.length;
        $("#" + tableId + " tbody tr").each(function() {
            var line = [];
            $(this).find("td").each(function() {
                var valor = parseFloat($(this).text());
                if(valor) {
                    line.push(valor);
                } else {
                    line.push($(this).text());
                }
            });
            if(columns > line.length) {
            	columns = line.length;
            }
            data.push(line);
        });
        return [data, headers, columns];
    }
    
    function getGraphDrawer() {
        var dataAndHeaders = readTableData("result_data");
        var data = dataAndHeaders[0];
        var headers = dataAndHeaders[1];
    
        if(dataAndHeaders[2] == 2) {
            return TwoColumnChartDrawer(data, headers);
        } else if(dataAndHeaders[2] == 3) {
            return ThreeColumnChartDrawer(data, headers);
        }
    }

    google.load("jquery", "1.6.2");
    google.load("visualization", "1", {packages:["linechart"]});
    google.setOnLoadCallback(function() {
        var draw = getGraphDrawer();
        var canvas = document.getElementById('graph');
        console.log(draw);
        draw({width: 800, height: 450, title: 'Statement'}, canvas, []);
    });
</script>
</body>
</html>