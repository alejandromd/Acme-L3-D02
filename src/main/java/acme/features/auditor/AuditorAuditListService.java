
package acme.features.auditor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditListService extends AbstractService<Auditor, Audit> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuditorAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Audit> objects;
		int auditorId;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		auditorId = principal.getAccountId();
		objects = this.repository.findAuditsByAuditorId(auditorId);
		super.getBuffer().setData(objects);

	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		Tuple tuple;

		String courseTitle;
		courseTitle = object.getCourse().getTitle();

		tuple = super.unbind(object, "code", "conclusion");
		tuple.put("courseTitle", courseTitle);

		super.getResponse().setData(tuple);
	}

}
