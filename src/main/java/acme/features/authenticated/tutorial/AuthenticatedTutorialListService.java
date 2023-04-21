
package acme.features.authenticated.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Tutorial;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedTutorialListService extends AbstractService<Authenticated, Tutorial> {

	@Autowired
	protected AuthenticatedTutorialRepository repository;


	@Override
	public void check() {
		boolean probe;
		probe = super.getRequest().hasData("courseId", int.class);
		super.getResponse().setChecked(probe);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Tutorial> objects;
		final int courseId = super.getRequest().getData("courseId", int.class);
		objects = this.repository.findTutorialsByCourseId(courseId);
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "code", "title", "informativeAbstract");
		super.getResponse().setData(tuple);
	}

}
