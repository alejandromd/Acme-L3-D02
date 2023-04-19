
package acme.features.auditor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.entities.Course;
import acme.entities.auditingRecord.AuditingRecord;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditDeleteService extends AbstractService<Auditor, Audit> {

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

		Audit object;
		int id;
		Principal principal;
		int userId;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditById(id);
		principal = super.getRequest().getPrincipal();
		userId = principal.getAccountId();
		status = object.getAuditor().getUserAccount().getId() == userId && object.isDraftMode();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Audit object;
		int id;
		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditById(id);
		object.setCourse(course);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {

		assert object != null;

		super.bind(object, "code", "conclusion", "strongPoints", "weakPoints");

	}

	@Override
	public void validate(final Audit object) {

		assert object != null;

	}

	@Override
	public void perform(final Audit object) {

		assert object != null;

		Collection<AuditingRecord> auditingRecords;

		auditingRecords = this.repository.findManyAuditingRecordsByAuditId(object.getId());

		this.repository.deleteAll(auditingRecords);
		this.repository.delete(object);

	}

	@Override
	public void unbind(final Audit object) {

		assert object != null;

		Tuple tuple;

		Collection<Course> courses;
		SelectChoices choices;

		courses = this.repository.findCoursesNotDraftMode();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints", "draftMode");
		tuple.put("courses", courses);
		tuple.put("course", choices.getSelected().getKey());

		super.getResponse().setData(tuple);

	}
}
