
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.Practicum;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumCreateService extends AbstractService<Company, Practicum> {

	@Autowired
	protected CompanyPracticumRepository repository;


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
		Company company;
		final int courseId;
		final Course course;

		object = new Practicum();
		company = this.repository.findCompanyById(super.getRequest().getPrincipal().getActiveRoleId());
		object.setDraftMode(true);
		object.setCompany(company);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;

		int companyId;
		Company company;
		int courseId;
		Course course;

		companyId = super.getRequest().getPrincipal().getActiveRoleId();
		company = this.repository.findCompanyById(companyId);
		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);

		super.bind(object, "code", "title", "summary", "goals", "draftMode");
		object.setCompany(company);
		object.setDraftMode(true);
		object.setCourse(course);
	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Practicum practicum;
			String code;

			code = object.getCode();
			practicum = this.repository.findPracticumByCode(code);
			super.state(practicum == null, "code", "company.practicum.form.error.duplicated-code");
		}
		//if (!super.getBuffer().getErrors().hasErrors("title"))
		//super.state(!SpamFilter.antiSpamFilter(object.getTitle(), this.repository.findThreshold()), "conclusion", "auditor.audit.form.error.spam");
	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		final Tuple tuple;
		Collection<Course> courses;
		SelectChoices choices;

		courses = this.repository.findAllCoursesNotPublished();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		tuple = super.unbind(object, "title", "summary", "goals", "draftMode");
		tuple.put("courses", choices);
		tuple.put("course", choices.getSelected().getKey());

		super.getResponse().setData(tuple);
	}

}
