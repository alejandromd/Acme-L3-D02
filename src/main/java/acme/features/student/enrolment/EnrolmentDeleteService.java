
package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Activity;
import acme.entities.Course;
import acme.entities.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class EnrolmentDeleteService extends AbstractService<Student, Enrolment> {

	@Autowired
	protected EnrolmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Enrolment object;
		int id;
		id = super.getRequest().getData("id", int.class);

		object = this.repository.findEnrolmentById(id);
		final int userId = super.getRequest().getPrincipal().getAccountId();

		super.getResponse().setAuthorised(object.getStudent().getUserAccount().getId() == userId && object.getDraftMode());
	}

	@Override
	public void load() {
		Enrolment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findEnrolmentById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;
		super.bind(object, "code", "motivation", "goals");
		final Course course = this.repository.findCourseById(super.getRequest().getData("course", int.class));
		object.setCourse(course);
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;
	}

	@Override
	public void perform(final Enrolment object) {

		assert object != null;
		final Collection<Activity> activities = this.repository.findActivitiesByEnrolment(object);
		for (final Activity a : activities)
			this.repository.delete(a);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		Tuple tuple;

		final Collection<Course> courses = this.repository.findAllCourses();
		final SelectChoices s = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, "code", "motivation", "goals", "draftMode", "holderName", "lowerNibble");
		tuple.put("course", s.getSelected().getKey());
		tuple.put("courses", s);

		super.getResponse().setData(tuple);
	}

}
