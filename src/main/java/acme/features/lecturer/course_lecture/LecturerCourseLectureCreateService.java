
package acme.features.lecturer.course_lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.CourseLecture;
import acme.entities.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseLectureCreateService extends AbstractService<Lecturer, CourseLecture> {

	@Autowired
	protected LecturerCourseLectureRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("courseId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int courseId;
		Course course;
		Principal principal;
		int userAccountId;

		courseId = super.getRequest().getData("courseId", int.class);
		course = this.repository.findCourseById(courseId);
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		status = course != null && course.isDraftMode() && course.getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CourseLecture object;
		int courseId;
		Course course;

		courseId = super.getRequest().getData("courseId", int.class);
		course = this.repository.findCourseById(courseId);
		object = new CourseLecture();
		object.setCourse(course);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final CourseLecture object) {
		assert object != null;

		int lectureId;
		Lecture lecture;

		lectureId = super.getRequest().getData("lecture", int.class);
		lecture = this.repository.findLectureById(lectureId);

		object.setLecture(lecture);
	}

	@Override
	public void validate(final CourseLecture object) {
		assert object != null;

		int lectureId;
		int courseId;
		CourseLecture courseLecture = null;

		if (object.getLecture() != null) {
			Lecture lecture;
			lectureId = object.getLecture().getId();
			lecture = this.repository.findLectureById(lectureId);

			super.state(!lecture.isDraftMode(), "lecture", "lecturer.courseLecture.form.error.draftMode");
			courseId = super.getRequest().getData("courseId", int.class);
			courseLecture = this.repository.findOneCourseLectureByIds(courseId, lectureId);

			super.state(object.getLecture().getLecturer().equals(object.getCourse().getLecturer()), "lecture", "lecturer.courseLecture.form.error.lecture");
			super.state(courseLecture == null, "lecture", "lecturer.courseLecture.form.error.lecture");

		}

		super.state(object.getLecture() != null, "lecture", "lecturer.courseLecture.form.lecture.empty");

	}

	@Override
	public void perform(final CourseLecture object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final CourseLecture object) {
		assert object != null;

		int lecturerId;
		Collection<Lecture> lectures;
		Tuple tuple;
		final SelectChoices choices;
		int courseId;

		lecturerId = super.getRequest().getPrincipal().getActiveRoleId();
		lectures = this.repository.findLecturesByLecturerId(lecturerId);

		choices = SelectChoices.from(lectures, "title", object.getLecture());

		tuple = super.unbind(object, "lecture", "course");
		tuple.put("lecture", choices.getSelected().getKey());
		tuple.put("lectures", choices);
		courseId = super.getRequest().getData("courseId", int.class);
		super.getResponse().setGlobal("courseId", courseId);
		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<CourseLecture> objects) {
		assert objects != null;
		int courseId;

		courseId = super.getRequest().getData("courseId", int.class);
		super.getResponse().setGlobal("courseId", courseId);
	}

}
