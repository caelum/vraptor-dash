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
			return user==null ? "$not_logged_in$" : user.getId();
		}
	
		public boolean canCreateStatements() {
			return true; // this is the admin user who is capable of seeing all statements and creating new ones. this is "GOD"
		}
	}

# Accessing

Go to /dash/statements

# Help

Get help at VRaptor's mailing list