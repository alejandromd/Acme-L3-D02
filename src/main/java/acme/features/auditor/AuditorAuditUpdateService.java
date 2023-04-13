
package acme.features.auditor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.entities.Course;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditUpdateService extends AbstractService<Auditor, Audit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditRepository repository;

	// AbstractService<Auditor, Audit> -------------------------------------


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
		status = object.getAuditor().getUserAccount().getId() == userId;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Audit object;
		int id;
		final int courseId;
		final Course course;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {

		assert object != null;

		int courseId;
		Course course;

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
		tuple.put("courses", courses);
		tuple.put("course", choices.getSelected().getKey());

		super.getResponse().setData(tuple);

	}

}
