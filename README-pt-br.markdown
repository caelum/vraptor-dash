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

# Renderizando emails

String body = freemarker.use("notificacao_email_enviado").with("usuarioLogado", usuario).getContent();

# Ajuda

Receba assistência dos desenvolvedores do vraptor e da comunidade na lista de emails do vraptor.
