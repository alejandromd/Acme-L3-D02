
package acme.features.lecturer.course;

import java.util.Arrays;
import java.util.Collection;
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
public class LecturerCoursePostService extends AbstractService<Lecturer, Course> {

	@Autowired
	protected LecturerCourseRepository repository;

	// AbstractService<Employer, Job> -------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Course object;
		int id;
		Principal principal;
		int userAccountId;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findCourseById(id);
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
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
		Collection<Lecture> lectures;

		lectures = this.repository.findLecturesByCourse(object.getId());
		super.state(!lectures.isEmpty(), "draftMode", "lecturer.course.error.lecture");

		if (!lectures.isEmpty()) {
			boolean existHandOn;
			boolean lecturesInDraftMode;

			lecturesInDraftMode = lectures.stream().allMatch(x -> x.isDraftMode() == false);
			super.state(lecturesInDraftMode, "draftMode", "lecturer.course.error.draftMode");

			existHandOn = lectures.stream().anyMatch(x -> x.getLectureType().equals(Nature.HANDS_ON));
			super.state(existHandOn, "nature", "lecturer.course.error.handsOn");
		}
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Course course;

			course = this.repository.findCourseByCode(object.getCode());
			super.state(course == null || course.equals(object), "code", "lecturer.course.form.error.code-duplicated");
		}
		if (!super.getBuffer().getErrors().hasErrors("retailPrice")) {
			Double amount;

			amount = object.getRetailPrice().getAmount();
			super.state(amount >= 0 && amount < 1000000, "retailPrice", "lecturer.course.error.price");
		}
		if (!super.getBuffer().getErrors().hasErrors("retailPrice")) {
			String aceptedCurrencies;
			List<String> currencies;

			aceptedCurrencies = this.repository.findSystemConfiguration().getAceptedCurrencies();
			currencies = Arrays.asList(aceptedCurrencies.split(","));
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
		if (object.isDraftMode()) {
			object.setDraftMode(false);
			this.repository.save(object);
		}
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;
		List<Lecture> lectures;
		Nature nature;

		tuple = super.unbind(object, "code", "title", "summary", "retailPrice", "link", "draftMode");
		lectures = this.repository.findLecturesByCourse(object.getId()).stream().collect(Collectors.toList());
		nature = object.courseTypeNature(lectures);
		tuple.put("nature", nature);
		super.getResponse().setData(tuple);
	}
}
