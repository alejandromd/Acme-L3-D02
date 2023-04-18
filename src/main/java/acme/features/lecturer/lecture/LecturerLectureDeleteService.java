
package acme.features.lecturer.lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.Nature;
import acme.entities.CourseLecture;
import acme.entities.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureDeleteService extends AbstractService<Lecturer, Lecture> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Lecture object;
		final int id = super.getRequest().getData("id", int.class);
		object = this.repository.findLectureById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getLecturer().getUserAccount().getId() == userAccountId && object.isDraftMode());
	}

	@Override
	public void load() {
		Lecture object;
		final int id = super.getRequest().getData("id", int.class);
		object = this.repository.findLectureById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecture object) {
		assert object != null;
		super.bind(object, "title", "summary", "estimatedLearningTime", "body", "lectureType", "link");
	}

	@Override
	public void validate(final Lecture object) {
		assert object != null;
	}

	@Override
	public void perform(final Lecture object) {
		assert object != null;
		final Collection<CourseLecture> courseLectures = this.repository.findCourseLecturesByLecture(object);
		for (final CourseLecture cl : courseLectures)
			this.repository.delete(cl);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		final Tuple tuple = super.unbind(object, "title", "summary", "estimatedLearningTime", "body", "lectureType", "link", "draftMode", "lecturer");
		final SelectChoices choices;
		choices = SelectChoices.from(Nature.class, object.getLectureType());
		tuple.put("lectureType", choices.getSelected().getKey());
		tuple.put("lectureTypes", choices);
		super.getResponse().setData(tuple);
	}
}
