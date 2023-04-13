
package acme.features.student.enrolment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.Enrolment;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class EnrolmentCreateService extends AbstractService<Student, Enrolment> {

	@Autowired
	protected EnrolmentRepository repository;

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
		Enrolment object;
		object = new Enrolment();

		final Student student = this.repository.findStudentById(super.getRequest().getPrincipal().getActiveRoleId());
		final Course course = this.repository.findCourseById(super.getRequest().getData("course", int.class));

		object.setStudent(student);
		object.setCourse(course);
		object.setDraftMode(true);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;
		super.bind(object, "code", "motivation", "goals");
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;
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
		tuple = super.unbind(object, "code", "motivation", "goals", "course", "student");
		super.getResponse().setData(tuple);
	}

}
