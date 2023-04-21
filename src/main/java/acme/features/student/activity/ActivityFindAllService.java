
package acme.features.student.activity;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Activity;
import acme.entities.Enrolment;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class ActivityFindAllService extends AbstractService<Student, Activity> {

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
		status = enrolment != null && super.getRequest().getPrincipal().hasRole(enrolment.getStudent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Activity> objects;

		final int id = super.getRequest().getData("enrolmentId", int.class);
		objects = this.repository.findActivitiesByEnrolmentId(id);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "activityType", "startPeriod", "endPeriod");

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<Activity> objects) {
		assert objects != null;

		Enrolment enrolment;
		boolean showCreate;

		final int id = super.getRequest().getData("enrolmentId", int.class);
		enrolment = this.repository.findEnrolmentById(id);
		showCreate = !enrolment.getDraftMode() && super.getRequest().getPrincipal().hasRole(enrolment.getStudent());

		super.getResponse().setGlobal("enrolmentId", id);
		super.getResponse().setGlobal("showCreate", showCreate);
	}

}
