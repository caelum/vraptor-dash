## vraptor-dash

Um dashboard com diversas ferramentas para seu projeto vraptor.

# instalação

É possível fazer o download do jar do repositório do Maven, ou configurado em qualquer ferramenta compatível:

		<dependency>
			<groupId>br.com.caelum.vraptor</groupId>
			<artifactId>vraptor-dash</artifactId>
			<version>1.0.0</version>
			<scope>compile</scope>
		</dependency>


# Configuração

Para configurar, coloque em seu hibernate.cfg.xml a entidade:

	br.com.caelum.vraptor.dash.statement.Statement
	br.com.caelum.vraptor.dash.uristats.Stat

Habilite os controllers do vraptor-dash no beans.xml:

```
<beans ...>
    <alternatives>
        <class>br.com.caelum.vraptor.dash.config.ConfigController</class>
        <class>br.com.caelum.vraptor.dash.hibernate.AuditController</class>
        <class>br.com.caelum.vraptor.dash.hibernate.stats.RequestsController</class>
        <class>br.com.caelum.vraptor.dash.monitor.RoutesController</class>
        <class>br.com.caelum.vraptor.dash.monitor.SystemController</class>
    </alternatives>
</beans>
```

	
Crie também um componente que implemente a interface StatementAwareUser:

	@Component
	public class StatementCheck implements StatementAwareUser, IdeableUser {
	
		private final User user;
		public StatementCheck(User user) {
			this.user = user;
		}

		public Serializable getId() {
			return user.getId();
		}
	
		public boolean canCreateStatements() {
			return true; // somente se ele eh um usuario admin que pode acessar todos os statements e criar novos
		}
	}

# Acessando

Acesse a URI /dash/statements
Faça queries como:

	select count(id), uri from DashUriStat group by uri
	select count(id), userId from DashUriStat group by userId

# Auditando acesso a determinados métodos

Se você deseja logar quem acessa determinados métodos, registre o

	AuditLogInterceptor
	
E então anote seu método para auditoria:

	@Audit("client")
	public void edit(Client client) {
		...
	}

Lembre-se que sua classe de usuário precisa implementar IdeableUser.

# Otimizando estratégias de cache

Adicione o seguinte filtro ao seu web.xml e configure os DashUriStats:

	<filter>
		<filter-name>vraptor-dash-cache</filter-name>
		<filter-class>br.com.caelum.vraptor.dash.cache.CacheCheckFilter</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>vraptor-dash-cache</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

Agora o objeto Stat possui os campos *size*, *etag*. Caso hasEtag seja false, a etag é gerada mas não
devolvida ao cliente, permitindo que você verifique qual seria a otimização em kbytes e em segundos
 possível:

	select uri, verb, etag, size*(count(uri)-1)/1024, time*(count(uri)-1)/1000 from DashUriStat where hasEtag=false and (cache is null or cache=="") group by uri, verb, etag

Ou por método, que mostra quão importante é melhorar o método em si, uma medida mais importante:

select resource, method, (sum(size)-   (select sum(avg(d.size)) from DashUriStat as d where d.resource=resource and d.method=method group by etag, sum(avg(d.size)))      )/1024 as ssss,(sum(time)-avg(time))/1000 as tttt from DashUriStat where cache = '' and hadEtag = 'false' and verb='GET' group by resource, method order by resource, method

Analise qual otimização de cache de método você pode ganhar mais e otimize.

# Ajuda

Receba assistência dos desenvolvedores do vraptor e da comunidade na lista de emails do vraptor.
