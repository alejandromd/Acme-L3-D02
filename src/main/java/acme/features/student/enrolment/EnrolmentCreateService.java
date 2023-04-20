
package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import filter.SpamFilter;

@Service
public class EnrolmentCreateService extends AbstractService<Student, Enrolment> {

	@Autowired
	protected EnrolmentRepository repository;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(super.getRequest().getPrincipal().hasRole(Student.class));
	}

	@Override
	public void load() {
		Enrolment object;
		object = new Enrolment();

		final Student student = this.repository.findStudentById(super.getRequest().getPrincipal().getActiveRoleId());

		object.setStudent(student);
		object.setDraftMode(true);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;

		final Course course = this.repository.findCourseById(super.getRequest().getData("course", int.class));
		object.setCourse(course);

		super.bind(object, "code", "motivation", "goals", "holderName", "lowerNibble");
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Enrolment enrolment;

			enrolment = this.repository.findEnrolmentByCode(object.getCode());
			super.state(enrolment == null || enrolment.equals(object), "code", "student.enrolment.form.error.code-duplicated");
		}
		if (!super.getBuffer().getErrors().hasErrors("motivation"))
			super.state(!SpamFilter.antiSpamFilter(object.getMotivation(), this.repository.findThreshold()), "motivation", "student.enrolment.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(!SpamFilter.antiSpamFilter(object.getGoals(), this.repository.findThreshold()), "goals", "student.enrolment.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("holderName"))
			if (object.getHolderName() != "")
				super.state(!SpamFilter.antiSpamFilter(object.getHolderName(), this.repository.findThreshold()), "holderName", "student.enrolment.error.spam");

	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		Tuple tuple;

		final Collection<Course> courses = this.repository.findPublishedCourses();
		final SelectChoices s = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, "code", "motivation", "goals", "draftMode", "holderName", "lowerNibble");
		tuple.put("course", s.getSelected().getKey());
		tuple.put("courses", s);

		super.getResponse().setData(tuple);
	}

}
