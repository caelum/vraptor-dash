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
	
Crie também um componente que implemente a interface StatementAwareUser:

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
			return true; // somente se ele eh um usuario admin que pode acessar todos os statements e criar novos
		}
	}

# Acessando

Acesse a URI /dash/statements
Faça queries como:

	select count(id), uri from DashUriStat group by uri
	select count(id), userId from DashUriStat group by userId

# Ajuda

Receba assistência dos desenvolvedores do vraptor e da comunidade na lista de emails do vraptor.
