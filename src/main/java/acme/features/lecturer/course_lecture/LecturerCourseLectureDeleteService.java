
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
public class LecturerCourseLectureDeleteService extends AbstractService<Lecturer, CourseLecture> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseLectureRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("lectureId", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Lecture object;
		int id;
		Principal principal;
		int userAccountId;

		id = super.getRequest().getData("lectureId", int.class);
		object = this.repository.findLectureById(id);
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getLecturer().getUserAccount().getId() == userAccountId);
	}

	@Override
	public void load() {
		CourseLecture object;
		Lecture lecture;
		int lectureId;

		object = new CourseLecture();
		lectureId = super.getRequest().getData("lectureId", int.class);
		lecture = this.repository.findLectureById(lectureId);
		object.setLecture(lecture);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final CourseLecture object) {
		assert object != null;
		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);
		super.bind(object, "id");
		object.setCourse(course);
	}

	@Override
	public void validate(final CourseLecture object) {
		assert object != null;
		super.state(object.getCourse() != null, "course", "lecturer.courseLecture.form.error.course.null");
		if (!super.getBuffer().getErrors().hasErrors("course"))
			super.state(object.getCourse().isDraftMode(), "course", "lecturer.courseLecture.form.error.course");
	}

	@Override
	public void perform(final CourseLecture object) {
		assert object != null;
		CourseLecture courseLecture;
		courseLecture = this.repository.findCourseLectureByLectureAndCourse(object.getCourse(), object.getLecture());

		this.repository.delete(courseLecture);
	}

	@Override
	public void unbind(final CourseLecture object) {
		assert object != null;
		Tuple tuple;
		int lectureId;
		Collection<Course> courses;
		Lecture lecture;

		tuple = super.unbind(object, "lecture", "course");
		lectureId = super.getRequest().getData("lectureId", int.class);
		courses = this.repository.findCourseByLecture(object.getLecture());
		lecture = this.repository.findLectureById(lectureId);

		final SelectChoices choices;
		choices = SelectChoices.from(courses, "code", object.getCourse());
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("cursos", courses);
		tuple.put("draftMode", lecture.isDraftMode());
		tuple.put("lectureId", super.getRequest().getData("lectureId", int.class));

		super.getResponse().setData(tuple);
	}
}
