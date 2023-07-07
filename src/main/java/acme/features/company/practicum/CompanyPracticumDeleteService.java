/*
 * AuthenticatedConsumerController.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.Practicum;
import acme.entities.PracticumSession;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumDeleteService extends AbstractService<Company, Practicum> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Company c;
		boolean status;
		int practicumId;
		Practicum practicum;

		practicumId = super.getRequest().getData("id", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		c = practicum == null ? null : practicum.getCompany();
		status = practicum != null && practicum.getDraftMode() && super.getRequest().getPrincipal().hasRole(c);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		Practicum object;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOnePracticumById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;

		Course course;
		int courseId;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(object, "code", "title", "summary", "goals", "estimatedTime");

		object.setCourse(course);

	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;

	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;
		Collection<PracticumSession> practicumSessions;

		practicumSessions = this.repository.findPracticumSessionsByPracticumId(object.getId());
		this.repository.deleteAll(practicumSessions);

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		Double et;
		Collection<Course> courses;
		Tuple tuple;
		SelectChoices select;
		Collection<PracticumSession> practicumSessions;

		practicumSessions = this.repository.findPracticumSessionsByPracticumId(object.getId());
		et = 0.;
		if (!practicumSessions.isEmpty())
			et = object.estimatedTime(practicumSessions);

		courses = this.repository.findAllCourses();
		select = SelectChoices.from(courses, "code", object.getCourse());

		tuple = super.unbind(object, "code", "title", "summary", "goals", "draftMode");
		tuple.put("course", select.getSelected().getKey());
		tuple.put("courses", select);
		tuple.put("estimatedTime", et);

		super.getResponse().setData(tuple);
	}
}
