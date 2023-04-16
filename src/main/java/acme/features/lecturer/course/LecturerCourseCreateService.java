
package acme.features.lecturer.course;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseCreateService extends AbstractService<Lecturer, Course> {

	@Autowired
	protected LecturerCourseRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		final Lecturer lecturer = this.repository.findLecturerByIdUserAccount(userAccountId);
		super.getResponse().setAuthorised(lecturer != null);
	}

	@Override
	public void load() {
		final Course object = new Course();
		final Lecturer lecturer = this.repository.findLecturerById(super.getRequest().getPrincipal().getActiveRoleId());
		object.setLecturer(lecturer);
		object.setDraftMode(true);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Course object) {
		assert object != null;
		super.bind(object, "code", "title", "summary", "retailPrice", "link");
	}

	@Override
	public void validate(final Course object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("retailPrice")) {
			final Double amount = object.getRetailPrice().getAmount();
			super.state(amount >= 0 && amount < 1000000, "retailPrice", "lecturer.course.error.price");
		}
		if (!super.getBuffer().getErrors().hasErrors("retailPrice")) {
			final String aceptedCurrencies = this.repository.findSystemConfiguration().getAceptedCurrencies();
			final List<String> currencies = Arrays.asList(aceptedCurrencies.split(","));
			super.state(currencies.contains(object.getRetailPrice().getCurrency()), "retailPrice", "lecturer.course.error.currency");
			super.state(currencies.contains(object.getRetailPrice().getCurrency()), "retailPrice", aceptedCurrencies);
		}
	}

	@Override
	public void perform(final Course object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		final Tuple tuple = super.unbind(object, "code", "title", "summary", "retailPrice", "link", "draftMode", "lecturer");
		super.getResponse().setData(tuple);
	}
}
