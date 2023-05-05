
package acme.features.lecturer.lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureListService extends AbstractService<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("masterId", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Course object;
		int masterId;
		Principal principal;
		int userAccountId;

		masterId = super.getRequest().getData("masterId", int.class);
		object = this.repository.findCourseById(masterId);
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getLecturer().getUserAccount().getId() == userAccountId);
	}

	@Override
	public void load() {
		Collection<Lecture> objects;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findLecturesByCourse(masterId);
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		Tuple tuple;
		int masterId;
		String payload;

		tuple = super.unbind(object, "title", "summary", "estimatedLearningTime");
		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().setGlobal("masterId", masterId);
		tuple.put("masterId", masterId);
		payload = String.format("%s;%s;%s;%s", object.getBody(), object.getLink(), object.getLectureType(), object.isDraftMode());
		tuple.put("payload", payload);
		super.getResponse().setGlobal("showCreate", false);
		super.getResponse().setData(tuple);
	}

}
