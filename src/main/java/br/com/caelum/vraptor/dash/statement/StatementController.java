package br.com.caelum.vraptor.dash.statement;

import java.util.List;

import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.freemarker.FreemarkerView;
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

	public StatementController(Result result, Validator validator, StatementDao statementDao, StatementAwareUser currentUser) {
		this.result = result;
		this.validator = validator;
		this.statements = statementDao;
		this.currentUser = currentUser;
	}

	@Path("/dash/statements")
	@Get
	public void index(Integer size) {
		if (!currentUser.canCreateStatements()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		if(size == null) {
			size = 100;
		}
		result.include("statements", statements.all(size));
		result.include("size", size);
		result.use(FreemarkerView.class).withTemplate(INDEX);
	}

	@Path(value = "/dash/statements/{statement.id}", priority = Path.LOWEST)
	@Post
	public void show(Statement statement, String password, Integer maxResults) {
		statement = statements.load(statement.getId());
		maxResults = (maxResults == null ? 100 : maxResults);
		if (canView(statement, password)) {
			List<Object[]> results = executeStatement(statement, maxResults);
			List<String> columns = statement.getColumns();
			renderResponse(statement, results, columns, maxResults);
		} else {
			result.use(HttpResult.class).sendError(401);
		}
	}

	private boolean canView(Statement statement, String password) {
		return currentUser.canCreateStatements() || statement.canBeAccessedWithKey(password);
	}

	@Path("/dash/statements/{statement.id}")
	@Get
	public void form(Statement statement, Integer maxResults) {
		if(canView(statement, "")) {
			result.forwardTo(this).show(statement, "", maxResults);
		} else {
			result.include("maxResults", maxResults);
			result.include("statement", statement);
			result.use(FreemarkerView.class).withTemplate("statement/form");
		}
	}

	@Path("/dash/statements/{statement.id}/json")
	@Post
	public void showJSON(Statement statement, String password, Integer maxResults) {
		statement = statements.load(statement.getId());
		if (canView(statement, password)) {
			List<Object[]> results = executeStatement(statement, maxResults);
			result.use(Results.json()).from(results).serialize();
		} else {
			result.use(HttpResult.class).sendError(401);
		}
	}

	private List<Object[]> executeStatement(Statement statement, Integer maxResults) {
		if(maxResults == null) {
			maxResults = 100;
		}
		validateStatement(statement,null);
		List<Object[]> results = statements.execute(statement,null,maxResults);
		return results;
	}

	private void renderResponse(Statement statement, List<Object[]> results,
			List<String> columns, Integer maxResults) {
		result.include("statement", statement);
		result.include("result", results);
		result.include("columns", columns);
		result.include("maxResults", maxResults);
		result.use(FreemarkerView.class).withTemplate(SHOW);
	}


	@Path("/dash/statements/execute")
	@Post
	public void execute(Statement statement, List<String> parameters, Integer maxResults) {
		if (!currentUser.canCreateStatements()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}

		if(maxResults == null) {
			maxResults = 100;
		}

		validateStatement(statement,parameters);
		List<Object[]> results = statements.execute(statement,parameters,maxResults);
		List<String> columns = statement.getColumns();
		renderResponse(statement, results, columns, maxResults);
	}


	@Path("/dash/statements")
	@Post
	public void create(Statement statement, Integer maxResults) {
		if (!currentUser.canCreateStatements()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		validateStatement(statement,null);
		statements.save(statement);
		result.redirectTo(this).form(statement, maxResults);
	}

	@Path("/dash/statements/{statement.id}")
	@Put
	public void update(Statement statement) {
		validateStatement(statement,null);
		if (!currentUser.canCreateStatements()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		Statement loaded = statements.load(statement.getId());
		loaded.setHql(statement.getHql());
		loaded.setName(statement.getName());
		loaded.setPassword(statement.getPassword());
		validateStatement(loaded,null);
		statements.merge(loaded);
		result.forwardTo(this).show(loaded, loaded.getPassword(), null);
	}

	@Path("/dash/statements/{statement.id}")
	@Delete
	public void delete(Statement statement) {
		statements.delete(statement);
		result.nothing();
	}

	private void validateStatement(Statement statement, List<String> parameters) {
		try {
			statement.validate(statements,parameters);
		} catch (IllegalArgumentException e) {
			validator.add(new ValidationMessage("invalid_hql", "invalid_hql", e.getCause().getMessage()));
			validator.onErrorUse(Results.logic()).redirectTo(getClass()).index(null);
		}
	}
}
