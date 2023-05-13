
package acme.features.lecturer.course_lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.CourseLecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseLectureShowService extends AbstractService<Lecturer, CourseLecture> {

	@Autowired
	protected LecturerCourseLectureRepository repository;


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
		courseLecture = this.repository.findOneLectureCourseById(courseLectureId);
		status = courseLecture != null && courseLecture.getCourse().getLecturer().getUserAccount().getId() == userAccountId && courseLecture.getLecture().getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CourseLecture object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneLectureCourseById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final CourseLecture object) {
		assert object != null;
		Tuple tuple;
		int id;
		Course course;

		id = super.getRequest().getData("id", int.class);
		course = this.repository.findOneLectureCourseById(id).getCourse();
		tuple = super.unbind(object, "lecture.title", "course.code");
		super.getResponse().setGlobal("showCreate", course.isDraftMode());

		super.getResponse().setData(tuple);
	}

}
