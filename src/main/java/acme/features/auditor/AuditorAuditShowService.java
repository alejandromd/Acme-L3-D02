
package acme.features.auditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.features.authenticated.audit.AuthenticatedAuditRepository;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditShowService extends AbstractService<Auditor, Audit> {

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

		boolean status;
		int id;
		Audit object;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int auditorId = principal.getAccountId();
		status = object.getAuditor().getUserAccount().getId() == auditorId;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Audit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Audit object) {

		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "code", "strongPoints", "weakPoints", "mark", "conclusion");

		super.getResponse().setData(tuple);

	}

}
