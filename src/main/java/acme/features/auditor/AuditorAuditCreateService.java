
package acme.features.auditor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.entities.Course;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;
import filter.SpamFilter;

@Service
public class AuditorAuditCreateService extends AbstractService<Auditor, Audit> {

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
		Audit object;
		Auditor auditor;
		String mark;
		int auditorId;

		auditorId = super.getRequest().getPrincipal().getActiveRoleId();
		auditor = this.repository.findOneAuditorById(auditorId);
		mark = "N/A";

		object = new Audit();
		object.setAuditor(auditor);
		object.setDraftMode(true);
		object.setMark(mark);

		super.getBuffer().setData(object);

	}

	@Override
	public void bind(final Audit object) {
		assert object != null;

		Course course;
		int courseId;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(object, "code", "conclusion", "strongPoints", "weakPoints");
		object.setCourse(course);
	}

	@Override
	public void validate(final Audit object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(this.repository.findAuditByCode(object.getCode()) == null, "code", "auditor.audit.form.error.code");
		if (!super.getBuffer().getErrors().hasErrors("conclusion"))
			super.state(!SpamFilter.antiSpamFilter(object.getConclusion(), this.repository.findThreshold()), "conclusion", "auditor.audit.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("strongPoints"))
			super.state(!SpamFilter.antiSpamFilter(object.getStrongPoints(), this.repository.findThreshold()), "strongPoints", "auditor.audit.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("weakPoints"))
			super.state(!SpamFilter.antiSpamFilter(object.getWeakPoints(), this.repository.findThreshold()), "weakPoints", "auditor.audit.form.error.spam");

	}

	@Override
	public void perform(final Audit object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		Tuple tuple;

		Collection<Course> courses;
		SelectChoices choices;

		courses = this.repository.findCoursesWithoutAudit();
		choices = SelectChoices.from(courses, "code", object.getCourse());

		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints", "draftMode");
		tuple.put("courses", choices);
		tuple.put("course", choices.getSelected().getKey());

		super.getResponse().setData(tuple);
	}

}
