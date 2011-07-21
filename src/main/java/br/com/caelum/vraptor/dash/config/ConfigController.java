package br.com.caelum.vraptor.dash.config;


import java.io.IOException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.dash.uristats.IdeableUser;
import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.view.HttpResult;
import freemarker.template.TemplateException;

@Resource
public class ConfigController {

	private static final String JS = "config/config.js";

	private final Freemarker marker;
	private final Session session;
	private final IdeableUser currentUser;
	private final Result result;

	public ConfigController(Session session, IdeableUser currentUser, Freemarker marker, Result result) {
		this.session = session;
		this.currentUser = currentUser;
		this.marker = marker;
		this.result = result;
	}

	@SuppressWarnings("unchecked")
	private List<UserConfig> all(String key) {
		Query all = session.createQuery("from DashUserConfig where user = :id and key like :key").setCacheable(true);
		all.setParameter("id", currentUser.getId());
		all.setParameter("key", key);
		return all.list();
	}

	@Path("/dash/config")
	@Post
	public void create(String key, String value) throws IOException, TemplateException {
		session.save(new UserConfig(key, value, currentUser.getId().toString()));
		result.use(HttpResult.class).setStatusCode(200);
	}

	@Path("/dash/config.js")
	@Get
	public void js(String key) throws IOException, TemplateException {
		marker.use(JS).with("configs", all(key)).render();
	}
}
