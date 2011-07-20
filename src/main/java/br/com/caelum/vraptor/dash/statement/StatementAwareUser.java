package br.com.caelum.vraptor.dash.statement;

/**
 * An user who might be able to access the statement dashboard
 * 
 * @author guilherme silveira
 */
public interface StatementAwareUser {

	boolean canCreateStatements();

}
