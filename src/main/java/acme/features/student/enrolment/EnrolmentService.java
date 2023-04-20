
package acme.features.student.enrolment;

import java.time.Duration;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Activity;
import acme.entities.Course;
import acme.entities.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class EnrolmentService extends AbstractService<Student, Enrolment> {

	@Autowired
	protected EnrolmentRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(super.getRequest().getPrincipal().hasRole(Student.class));
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
	public void unbind(final Enrolment object) {
		assert object != null;
		Tuple tuple;

		final Collection<Course> courses = this.repository.findPublishedCourses();
		final SelectChoices s = SelectChoices.from(courses, "title", object.getCourse());

		final int workTime = this.getWorkTime(object);

		tuple = super.unbind(object, "code", "motivation", "goals", "draftMode", "holderName", "lowerNibble");
		tuple.put("course", s.getSelected().getKey());
		tuple.put("courses", s);
		tuple.put("student", object.getStudent().getUserAccount().getUsername());
		tuple.put("workTime", workTime);

		super.getResponse().setData(tuple);
	}

	public int getWorkTime(final Enrolment e) {
		int result = 0;
		final Collection<Activity> activities = this.repository.findActivitiesByEnrolment(e);
		for (final Activity a : activities) {
			final Duration d = MomentHelper.computeDuration(a.getStartPeriod(), a.getEndPeriod());
			final int hours = (int) d.toHours();
			result += hours;
		}
		return result;
	}

}
