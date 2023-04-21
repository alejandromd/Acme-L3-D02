
package acme.features.authenticated.tutorial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Tutorial;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedTutorialShowService extends AbstractService<Authenticated, Tutorial> {

	@Autowired
	protected AuthenticatedTutorialRepository repository;


	@Override
	public void check() {
		boolean probe;
		probe = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(probe);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Tutorial object;
		final int id = super.getRequest().getData("id", int.class);
		object = this.repository.findTutorialById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "code", "title", "informativeAbstract", "goals");
		tuple.put("assistant", object.getAssistant().getSupervisor());
		super.getResponse().setData(tuple);
	}

}
