package br.com.caelum.vraptor.dash.statement;

import java.io.IOException;
import java.util.List;

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
import freemarker.template.TemplateException;

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

	public StatementController(Result result, Validator validator, StatementDao statementDao, StatementAwareUser currentUser, 
			Freemarker marker) {
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
			validateStatement(statement,null);
			List<Object[]> results = statements.execute(statement,null);
			List<String> columns = statement.getColumns();
			renderResponse(statement, results, columns);
		} else {
			result.use(HttpResult.class).sendError(401);
		}
	}

	private void renderResponse(Statement statement, List<Object[]> results,
			List<String> columns) throws IOException, TemplateException {
		if(results.isEmpty()) {
			marker.use(NONE).render();
		} else {
			marker.use(SHOW)
				.with("statement", statement)
				.with("result", results)
				.with("columns", columns)
				.render();
		}
	}
	
	
	@Path("/dash/statements/execute")
	@Post
	public void execute(Statement statement, List<String> parameters) throws IOException, TemplateException {
		
		validateStatement(statement,parameters);
		List<Object[]> results = statements.execute(statement,parameters);
		List<String> columns = statement.getColumns();
		renderResponse(statement, results, columns);
	}
	

	@Path("/dash/statements")
	@Post
	public void create(Statement statement) throws IOException, TemplateException {
		if (!currentUser.canCreateStatements()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		validateStatement(statement,null);
		statements.save(statement);
		result.use(Results.logic()).redirectTo(getClass()).show(statement, statement.getPassword());
	}

	@Path("/dash/statements/{statement.id}")
	@Put
	public void update(Statement statement) throws IOException, TemplateException {
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
		result.use(Results.logic()).forwardTo(getClass()).show(loaded, loaded.getPassword());
	}

	@Path("/dash/statements/{statement.id}")
	@Delete
	public void delete(Statement statement) {
		statements.delete(statement);
		result.nothing();
	}

	private void validateStatement(Statement statement, List<String> parameters) throws IOException, TemplateException {
		try {
			statement.validate(statements,parameters);
		} catch (IllegalArgumentException e) {
			validator.add(new ValidationMessage("invalid_hql", "invalid_hql", e.getCause().getMessage()));
			validator.onErrorUse(Results.logic()).forwardTo(getClass()).index();
		}
	}
}
