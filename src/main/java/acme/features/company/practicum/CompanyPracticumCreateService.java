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
public class CompanyPracticumCreateService extends AbstractService<Company, Practicum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepository repository;

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
		Practicum object;
		Company c;
		object = new Practicum();
		c = this.repository.findOneCompanyById(super.getRequest().getPrincipal().getActiveRoleId());
		object.setCompany(c);
		object.setDraftMode(true);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;

		Course course;
		int courseId;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);
		object.setCourse(course);
		super.bind(object, "code", "title", "summary", "goals");
	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Practicum e;
			e = this.repository.findPracticumByCode(object.getCode());
			super.state(e == null, "code", "company.practicum.form.error.duplicated");
		}

	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;
		Tuple tuple;
		Collection<PracticumSession> sessions;
		Collection<Course> courses;
		SelectChoices select;
		Double et;

		sessions = this.repository.findPracticumSessionsByPracticumId(object.getId());
		et = 0.;
		if (!sessions.isEmpty())
			et = object.estimatedTime(sessions);

		courses = this.repository.findAllCourses();
		select = SelectChoices.from(courses, "code", object.getCourse());

		tuple = super.unbind(object, "code", "title", "summary", "goals", "draftMode");
		tuple.put("course", select.getSelected().getKey());
		tuple.put("courses", select);
		tuple.put("estimatedTime", et);

		super.getResponse().setData(tuple);
	}
}
