
package acme.features.student.enrolment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.Enrolment;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class EnrolmentFinaliseService extends AbstractService<Student, Enrolment> {

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
		Enrolment object;
		int id;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findEnrolmentById(id);
		final int userId = super.getRequest().getPrincipal().getAccountId();

		status = object.getStudent().getUserAccount().getId() == userId && object.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findEnrolmentById(id);
		final Course course = this.repository.findCourseById(super.getRequest().getData("course", int.class));

		object.setCourse(course);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;
		super.bind(object, "code", "motivation", "goals", "draftMode", "holderName", "lowerNibble");
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

		if (object.getHolderName() == null)
			super.state(false, "holderName", "message");
		if (object.getLowerNibble() == null || object.getLowerNibble().toString().length() != 4)
			super.state(false, "lowerNibble", "message");

	}

	@Override
	public void perform(final Enrolment object) {
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		Tuple tuple;

		tuple = super.unbind(object, "code", "motivation", "goals", "draftMode", "holderName", "lowerNibble");

		super.getResponse().setData(tuple);
	}

}
