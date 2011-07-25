## vraptor-dash

A dashboard with several tools for your vraptor project.

# installing

It's possible to download its jar from maven's repository (see mvnrepository.com) or any other compatible tool such as maven (gradle, ivy and so on):

		<dependency>
			<groupId>br.com.caelum.vraptor</groupId>
			<artifactId>vraptor-dash</artifactId>
			<version>1.0.0</version>
			<scope>compile</scope>
		</dependency>


# Configuring

Add the entity to your hibernate.cfg.xml:

	br.com.caelum.vraptor.dash.statement.Statement
	br.com.caelum.vraptor.dash.uristats.Stat
	
Create a component that implements StatementAwareUser:

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
			return true; // this is the admin user who is capable of seeing all statements and creating new ones. this is "GOD"
		}
	}

# Accessing

Go to /dash/statements
Do queries as:

	select count(id), uri from DashUriStat group by uri
	select count(id), userId from DashUriStat group by userId

# Auditing access to specific methods

If you want to log who access some methods, register:

	AuditLogInterceptor

And annotate your method with the parameters to be logged:

	@Audit("client")
	public void edit(Client client) {
		...
	}
	
Remember that you need the logged in user to implement the interface IdeableUser

# Optimizing cache strategies

Add the following filter to your web.xml and remember to configure DashUriStat:

	<filter>
		<filter-name>vraptor-dash-cache</filter-name>
		<filter-class>br.com.caelum.vraptor.dash.cache.CacheCheckFilter</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>vraptor-dash-cache</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

Now the Stat object has the fields *size*, *etag*. 
If hasEtag = false, the etag is automatically generated (but not returned to the client). So now its possible to check how much kbytes and seconds one could save from using cache in not-yet-cached pages:

	select uri, verb, etag, size*(count(uri)-1)/1024, time*(count(uri)-1)/1000 from DashUriStat where hasEtag=false and (cache is null or cache=="") group by uri, verb, etag

Per method, which shows how important it is to improve one method:

	select resource, method, (sum(size)-   (select sum(avg(d.size)) from DashUriStat as d where d.resource=resource and d.method=method group by etag, sum(avg(d.size)))      )/1024 as ssss,(sum(time)-avg(time))/1000 as tttt from DashUriStat where cache = '' and hadEtag = 'false' and verb='GET' group by resource, method order by resource, method
	
Analyze where you can gain the most and optimize it.

# Help

Get help at VRaptor's mailing list