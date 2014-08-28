package br.com.caelum.vraptor.dash.config;


import java.io.IOException;
import java.util.List;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.dash.uristats.IdeableUser;
import br.com.caelum.vraptor.freemarker.FreemarkerView;
import br.com.caelum.vraptor.view.HttpResult;
import freemarker.template.TemplateException;

@Alternative
@Controller
public class ConfigController {

	private static final String JS = "config/config.js";

	private final Session session;
	private final IdeableUser currentUser;
	private final Result result;

	private final ConfigurationsAwareUser user;
    private final HttpServletResponse response;

    @Inject
    public ConfigController(Session session, IdeableUser currentUser, Result result, ConfigurationsAwareUser user,
                            HttpServletResponse response) {
		this.session = session;
		this.currentUser = currentUser;
		this.result = result;
		this.user = user;
        this.response = response;
    }
    
    /**
	 * @deprecated CDI eyes only
	 */
	protected ConfigController() {
		this(null, null, null, null, null);
	}

	@SuppressWarnings("unchecked")
	private List<UserConfig> all(String key) {
		if(! user.canSeeConfigurations()) {
			result.use(HttpResult.class).sendError(401);
			return null;
		}
		Query all = session.createQuery("from DashUserConfig where userId = :id and key like :key").setCacheable(true);
		all.setParameter("id", currentUser.getId());
		all.setParameter("key", key);
		return all.list();
	}

	@Path("/dash/config")
	@Post
	public void create(String key, String value) throws IOException, TemplateException {
		if(! user.canSeeConfigurations()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		session.save(new UserConfig(key, value, currentUser.getId().toString()));
		result.nothing();
	}

	@Path("/dash/config.js")
	@Get
	public void js(String key) throws IOException, TemplateException {
		if(! user.canSeeConfigurations()) {
			result.use(HttpResult.class).sendError(401);
			return;
		}
		result.include("configs", all(key));
        response.setContentType("text/javascript");
		result.use(FreemarkerView.class).withTemplate(JS);
	}
}
