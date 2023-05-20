
package acme.features.lecturer.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.CourseLecture;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseDeleteService extends AbstractService<Lecturer, Course> {

	@Autowired
	protected LecturerCourseRepository repository;

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
		Course object;
		int id;
		Lecturer lecturer;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findCourseById(id);
		lecturer = object == null ? null : object.getLecturer();
		status = object != null && object.isDraftMode() && super.getRequest().getPrincipal().hasRole(lecturer);

		super.getResponse().setAuthorised(status);
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
		super.bind(object, "id", "code", "title", "summary", "retailPrice", "link");
	}

	@Override
	public void validate(final Course object) {
		assert object != null;
	}

	@Override
	public void perform(final Course object) {
		assert object != null;
		Collection<CourseLecture> courseLectures;

		courseLectures = this.repository.findCourseLecturesByCourse(object);
		for (final CourseLecture cl : courseLectures)
			this.repository.delete(cl);
		this.repository.delete(object);
	}
}
