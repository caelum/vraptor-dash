package br.com.caelum.vraptor.dash.statement;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.caelumweb2.logica.AbstractDaoTest;
import br.com.caelum.caelumweb2.modelo.deus.Statement;
import br.com.caelum.caelumweb2.modelo.pessoas.Usuario;
import br.com.caelum.vraptor.dash.statement.StatementDao;

public class StatementDaoTest extends AbstractTest{

	@Test(expected=IllegalArgumentException.class)
	public void hqlBizarroEhInvalido() {
		new StatementDao(session).valida("qualquer coisa from qualquer lugar");
	}

	@Test(expected=IllegalArgumentException.class)
	public void hqlQueNaoExecutaEhInvalido() throws Exception {
		new StatementDao(session).valida("select year(data), month(data), valorTotal from Movimentacao where data is not null group by year(data), month(data) order by month(data)");
	}

	@Test
	public void aoListarStatementsListaStatementDoUsuarioCriador() {
		Usuario usuarioCriador = mother.novoUsuario();
		Statement stmtDoUsuario = mother.novoStatement(usuarioCriador);
		List<Statement> lista = new StatementDao(session).listaStatementsDo(usuarioCriador);

		Assert.assertTrue("A lista deveria conter o statement do usuario criador", lista.contains(stmtDoUsuario));
	}

	@Test
	public void aoListarStatementsNaoMostraStatementDeOutroUsuario() {
		Usuario usuarioCriador = mother.novoUsuario();
		Statement stmtDeOutroUsuario = mother.novoStatement(mother.novoUsuario());
		List<Statement> lista = new StatementDao(session).listaStatementsDo(usuarioCriador);

		Assert.assertFalse("A lista nao deveria conter o statement de outro usuario", lista.contains(stmtDeOutroUsuario));
	}

	@Test
	public void trazStatementsQueOUsuarioPossui() {
		Usuario usuarioCriador = mother.novoUsuario();
		Statement statement = mother.novoStatement(usuarioCriador);
		Usuario caire = mother.novoUsuario("caire");
		statement.setVisualizadores(Arrays.asList(caire));
		session.flush();

		List<Statement> meusStatements = new StatementDao(session).meusStatements(caire);
		Assert.assertThat("Nao contem statement para o usuario", meusStatements, Matchers.hasItem(statement));
	}

	@Test
	public void verificaRetornoDeQueries() {
		mother.umAluno().chamado("Ipa").persistido();
		mother.umAluno().chamado("Gargamel").persistido();
		List<Object[]> result = new StatementDao(session).execute("select codigoCaelumweb1, id from Aluno");

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(2, result.get(0).length);
		Assert.assertEquals(2, result.get(1).length);
	}
}
