
package acme.features.auditor.auditingRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.entities.auditingRecord.AuditingRecord;
import acme.entities.auditingRecord.Mark;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordShowService extends AbstractService<Auditor, AuditingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordRepository repository;

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
		Audit audit;
		Principal principal;
		int userId;

		id = super.getRequest().getData("id", int.class);
		audit = this.repository.findOneAuditByAuditingRecordId(id);
		principal = super.getRequest().getPrincipal();
		userId = principal.getAccountId();
		status = audit.getAuditor().getUserAccount().getId() == userId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditingRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditingRecordById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final AuditingRecord object) {
		assert object != null;

		Tuple tuple;
		String message;
		SelectChoices choices;
		choices = SelectChoices.from(Mark.class, object.getMark());

		message = "[X]";
		tuple = super.unbind(object, "subject", "assessment", "periodStartDate", "mark", "periodEndDate", "link");
		tuple.put("masterId", object.getAudit().getId());
		tuple.put("draftMode", object.getAudit().isDraftMode());
		tuple.put("correction", object.isCorrection());
		tuple.put("message", message);
		tuple.put("marks", choices);
		tuple.put("mark", choices.getSelected().getKey());

		super.getResponse().setData(tuple);
	}

}
