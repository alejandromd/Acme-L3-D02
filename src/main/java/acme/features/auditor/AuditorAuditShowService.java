
package acme.features.auditor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.entities.Course;
import acme.entities.auditingRecord.Mark;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditShowService extends AbstractService<Auditor, Audit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditRepository repository;

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
		Principal principal;
		int auditorId;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditById(id);
		principal = super.getRequest().getPrincipal();
		auditorId = principal.getAccountId();
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
		Collection<Course> courses;
		SelectChoices choice;
		Collection<Mark> marks;
		String markList;
		int auditId;

		courses = this.repository.findCoursesWithoutAudit();
		auditId = object.getId();
		marks = this.repository.findMarkByAuditId(auditId);

		if (marks.isEmpty())
			markList = "N/A";
		else
			markList = marks.toString();

		choice = SelectChoices.from(courses, "code", object.getCourse());

		tuple = super.unbind(object, "code", "strongPoints", "weakPoints", "conclusion");
		tuple.put("course", choice.getSelected().getKey());
		tuple.put("courses", choice);
		tuple.put("mark", markList);

		super.getResponse().setData(tuple);

	}

}
