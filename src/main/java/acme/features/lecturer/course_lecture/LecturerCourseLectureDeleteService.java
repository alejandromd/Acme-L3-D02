
package acme.features.lecturer.course_lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.CourseLecture;
import acme.framework.components.accounts.Principal;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseLectureDeleteService extends AbstractService<Lecturer, CourseLecture> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseLectureRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int courseLectureId;
		CourseLecture courseLecture;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		courseLectureId = super.getRequest().getData("id", int.class);
		courseLecture = this.repository.findOneCourseLectureById(courseLectureId);
		status = courseLecture != null && courseLecture.getCourse().isDraftMode() && courseLecture.getCourse().getLecturer().getUserAccount().getId() == userAccountId && courseLecture.getLecture().getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CourseLecture object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseLectureById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final CourseLecture object) {
		assert object != null;

	}

	@Override
	public void validate(final CourseLecture object) {
		assert object != null;

		super.state(object.getCourse().isDraftMode(), "course", "lecturer.courseLecture.form.error.course");
	}

	@Override
	public void perform(final CourseLecture object) {
		assert object != null;

		CourseLecture courseLecture;

		courseLecture = this.repository.findOneCourseLectureByIds(object.getCourse().getId(), object.getLecture().getId());

		this.repository.delete(courseLecture);
	}
}
