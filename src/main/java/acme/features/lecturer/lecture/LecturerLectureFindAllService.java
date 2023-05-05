
package acme.features.lecturer.lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Lecture;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureFindAllService extends AbstractService<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Lecture> objects;
		Lecturer lecturer;

		lecturer = this.repository.findLecturerById(super.getRequest().getPrincipal().getActiveRoleId());
		objects = this.repository.findLecturesByLecturer(lecturer);
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		Tuple tuple;
		String payload;

		tuple = super.unbind(object, "title", "summary", "estimatedLearningTime");
		payload = String.format("%s;%s;%s;%s", object.getBody(), object.getLink(), object.getLectureType(), object.isDraftMode());
		tuple.put("payload", payload);
		super.getResponse().setGlobal("showCreate", true);
		super.getResponse().setData(tuple);
	}

}
