
package acme.features.student.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.Nature;
import acme.entities.Activity;
import acme.entities.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import filter.SpamFilter;

@Service
public class ActivityCreateService extends AbstractService<Student, Activity> {

	@Autowired
	protected ActivityRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("enrolmentId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Enrolment enrolment;

		final int id = super.getRequest().getData("enrolmentId", int.class);
		enrolment = this.repository.findEnrolmentById(id);
		status = enrolment != null && !enrolment.getDraftMode() && super.getRequest().getPrincipal().hasRole(enrolment.getStudent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Activity object;
		Enrolment enrolment;

		final int id = super.getRequest().getData("enrolmentId", int.class);
		enrolment = this.repository.findEnrolmentById(id);

		object = new Activity();
		object.setEnrolment(enrolment);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Activity object) {
		assert object != null;

		super.bind(object, "title", "summary", "activityType", "startPeriod", "endPeriod", "link");
	}

	@Override
	public void validate(final Activity object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("endPeriod"))
			super.state(MomentHelper.isBefore(object.getStartPeriod(), object.getEndPeriod()), "endPeriod", "student.activity.form.error.wrong-dates");
		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(!SpamFilter.antiSpamFilter(object.getTitle(), this.repository.findThreshold()), "title", "student.activity.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("summary"))
			super.state(!SpamFilter.antiSpamFilter(object.getSummary(), this.repository.findThreshold()), "summary", "student.activity.error.spam");

	}

	@Override
	public void perform(final Activity object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;

		Tuple tuple;

		final SelectChoices s = SelectChoices.from(Nature.class, object.getActivityType());

		tuple = super.unbind(object, "title", "summary", "startPeriod", "endPeriod", "link");
		tuple.put("enrolmentId", super.getRequest().getData("enrolmentId", int.class));
		tuple.put("draftMode", object.getEnrolment().getDraftMode());
		tuple.put("activityTypes", s);
		tuple.put("activityType", s.getSelected().getKey());

		super.getResponse().setData(tuple);
	}

}
