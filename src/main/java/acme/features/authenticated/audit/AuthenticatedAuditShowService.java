
package acme.features.authenticated.audit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.entities.auditingRecord.Mark;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedAuditShowService extends AbstractService<Authenticated, Audit> {

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
		String auditor;
		Collection<Mark> marks;
		String markList;
		int auditId;

		auditId = object.getId();
		marks = this.repository.findMarkByAuditId(auditId);
		auditor = object.getAuditor().getUserAccount().getUsername();

		if (marks.isEmpty())
			markList = "N/A";
		else
			markList = marks.toString();

		tuple = super.unbind(object, "code", "strongPoints", "weakPoints", "conclusion");
		tuple.put("auditor", auditor);
		tuple.put("mark", markList);

		super.getResponse().setData(tuple);

	}

}
