if(!dash) {
	dash = {}
}
dash.addConfig = function(key, value, options = {}) {
		$.post('ajax/test.html', options);
}
dash.configs = {}
<#list configs as cfg>
dash.configs['${cfg.key}'] = ${cfg.value}
</#list>
