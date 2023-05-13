
package acme.features.lecturer.course_lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.CourseLecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseLectureListService extends AbstractService<Lecturer, CourseLecture> {

	@Autowired
	protected LecturerCourseLectureRepository repository;


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

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		courseId = super.getRequest().getData("courseId", int.class);
		course = this.repository.findCourseById(courseId);
		status = course != null && course.getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<CourseLecture> objects;
		int courseId;

		courseId = super.getRequest().getData("courseId", int.class);
		objects = this.repository.findCourseLectureByCourseId(courseId);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final CourseLecture object) {
		assert object != null;
		Tuple tuple;

		tuple = super.unbind(object, "lecture.title", "lecture.estimatedLearningTime", "lecture.lectureType");
		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<CourseLecture> objects) {
		assert objects != null;
		int courseId;
		Course course;

		courseId = super.getRequest().getData("courseId", int.class);
		course = this.repository.findCourseById(courseId);

		super.getResponse().setGlobal("courseId", courseId);
		super.getResponse().setGlobal("showCreate", course.isDraftMode());
	}
}
