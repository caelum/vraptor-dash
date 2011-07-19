package br.com.caelum.vraptor.dash.statement;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;

@Resource
public class StatementController {

	private final Result result;
	private final Validator validator;
	private final StatementDao statements;
	private final StatementAwareUser currentUser;

	public StatementController(Result result, Validator validator, StatementDao statementDao, StatementAwareUser currentUser) {
		this.result = result;
		this.validator = validator;
		this.statements = statementDao;
		this.currentUser = currentUser;
	}

	@Path("/dash/statements")
	@Get
	public void index() {
		if (currentUser.canCreateStatements()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		result.include("statements", statements.all());
	}

	@Path("/dash/statements/{statement.id}")
	@Get
	public void show(Statement statement, String password) {
		statement = statements.load(statement.getId());
		boolean canView = currentUser.canCreateStatements() || statement.canBeAccessedWithKey(password);
		if (canView) {
			validaStatement(statement);
			result.include("statement", statement);
			result.include("resultado", statements.execute(statement));
		} else {
			result.use(HttpResult.class).sendError(401);
		}
	}

	@Path("/dash/statements/novo")
	@Post
	public void create(Statement statement) {
		if (currentUser.canCreateStatements()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		validaStatement(statement);
		statements.save(statement);
		result.use(Results.logic()).redirectTo(getClass()).show(statement, statement.getPassword());
	}

	@Path("/dash/statements/{statement.id}")
	@Put
	public void update(Statement statement) {
		validaStatement(statement);
		if (currentUser.canCreateStatements()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		Statement loaded = statements.load(statement.getId());
		loaded.setHql(statement.getHql());
		loaded.setName(statement.getName());
		loaded.setPassword(statement.getPassword());
		validaStatement(loaded);
		statements.merge(loaded);
		result.use(Results.logic()).forwardTo(getClass()).show(loaded, loaded.getPassword());
	}

	private void validaStatement(Statement statement) {
		try {
			statement.valida(statements);
		} catch (IllegalArgumentException e) {
			validator.add(new ValidationMessage("hql_invalido", "hql_invalido", e.getCause().getMessage()));
			validator.onErrorUse(Results.logic()).forwardTo(getClass()).index();
		}
	}
}
