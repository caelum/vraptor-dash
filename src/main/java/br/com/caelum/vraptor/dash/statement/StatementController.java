package br.com.caelum.vraptor.dash.statement;

import java.io.IOException;
import java.util.List;

import freemarker.template.TemplateException;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;

@Resource
public class StatementController {

	private static final String SHOW = "statement/show";
	private static final String NONE = "statement/none";
	private static final String INDEX = "statement/index";

	private final Result result;
	private final Validator validator;
	private final StatementDao statements;
	private final StatementAwareUser currentUser;
	private final Freemarker marker;

	public StatementController(Result result, Validator validator, StatementDao statementDao, StatementAwareUser currentUser, Freemarker marker) {
		this.result = result;
		this.validator = validator;
		this.statements = statementDao;
		this.currentUser = currentUser;
		this.marker = marker;
	}

	@Path("/dash/statements")
	@Get
	public void index() throws IOException, TemplateException {
		if (!currentUser.canCreateStatements()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		marker.use(INDEX).with("statements", statements.all()).render();
	}

	@Path("/dash/statements/{statement.id}")
	@Get
	public void show(Statement statement, String password) throws IOException, TemplateException {
		statement = statements.load(statement.getId());
		boolean canView = currentUser.canCreateStatements() || statement.canBeAccessedWithKey(password);
		if (canView) {
			validaStatement(statement);
			List<Object[]> results = statements.execute(statement);
			List<String> columns = statement.getColumns();
			if(results.isEmpty()) {
				marker.use(NONE).render();
			} else {
				marker.use(SHOW)
					.with("statement", statement)
					.with("resultado", results)
					.with("columns", columns)
					.render();
			}
		} else {
			result.use(HttpResult.class).sendError(401);
		}
	}

	@Path("/dash/statements")
	@Post
	public void create(Statement statement) throws IOException, TemplateException {
		if (!currentUser.canCreateStatements()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		validaStatement(statement);
		statements.save(statement);
		result.use(Results.logic()).redirectTo(getClass()).show(statement, statement.getPassword());
	}

	@Path("/dash/statements/{statement.id}")
	@Put
	public void update(Statement statement) throws IOException, TemplateException {
		validaStatement(statement);
		if (!currentUser.canCreateStatements()) {
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
	
	@Path("/dash/statements/{statement.id}")
	@Delete
	public void delete(Statement statement) {
		statements.delete(statement);
		result.nothing();
	}

	private void validaStatement(Statement statement) throws IOException, TemplateException {
		try {
			statement.valida(statements);
		} catch (IllegalArgumentException e) {
			validator.add(new ValidationMessage("hql_invalido", "hql_invalido", e.getCause().getMessage()));
			validator.onErrorUse(Results.logic()).forwardTo(getClass()).index();
		}
	}
}
