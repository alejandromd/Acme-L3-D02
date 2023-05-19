
package acme.features.lecturer.course;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.CourseType;
import acme.entities.Course;
import acme.entities.Lecture;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseShowService extends AbstractService<Lecturer, Course> {

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
		status = object != null && super.getRequest().getPrincipal().hasRole(lecturer);
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
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;
		List<Lecture> lectures;
		CourseType courseType;

		tuple = super.unbind(object, "id", "code", "title", "summary", "retailPrice", "link", "draftMode", "lecturer");
		lectures = this.repository.findLecturesByCourse(object.getId()).stream().collect(Collectors.toList());
		courseType = object.courseTypeNature(lectures);
		tuple.put("nature", courseType);
		super.getResponse().setData(tuple);
	}
}
