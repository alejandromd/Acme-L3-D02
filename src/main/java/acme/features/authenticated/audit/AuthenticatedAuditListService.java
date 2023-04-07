
package acme.features.authenticated.audit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedAuditListService extends AbstractService<Authenticated, Audit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Audit> objects;
		int courseId;

		courseId = super.getRequest().getData("id", int.class);
		objects = this.repository.findAuditsByCourse(courseId);
		super.getBuffer().setData(objects);

	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "code", "mark", "conclusion");

		super.getResponse().setData(tuple);
	}

}
