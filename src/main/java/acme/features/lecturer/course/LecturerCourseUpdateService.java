
package acme.features.lecturer.course;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.Nature;
import acme.entities.Course;
import acme.entities.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;
import filter.SpamFilter;

@Service
public class LecturerCourseUpdateService extends AbstractService<Lecturer, Course> {

	@Autowired
	protected LecturerCourseRepository repository;


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Course object;
		final int id = super.getRequest().getData("id", int.class);
		object = this.repository.findCourseById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getLecturer().getUserAccount().getId() == userAccountId && object.isDraftMode());
	}

	@Override
	public void load() {
		Course object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findCourseById(id);
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
		if (!super.getBuffer().getErrors().hasErrors("summary"))
			super.state(!SpamFilter.antiSpamFilter(object.getSummary(), this.repository.findThreshold()), "summary", "lecturer.course.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(!SpamFilter.antiSpamFilter(object.getTitle(), this.repository.findThreshold()), "title", "lecturer.course.error.spam");

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
		final List<Lecture> lectures = this.repository.findLecturesByCourse(object.getId()).stream().collect(Collectors.toList());
		final Nature nature = object.courseTypeNature(lectures);
		tuple.put("nature", nature);
		super.getResponse().setData(tuple);
	}
}
