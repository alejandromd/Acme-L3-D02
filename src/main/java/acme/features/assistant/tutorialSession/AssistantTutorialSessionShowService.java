
package acme.features.assistant.tutorialSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Tutorial;
import acme.entities.tutorialSession.SessionType;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionShowService extends AbstractService<Assistant, TutorialSession> {

	@Autowired
	protected AssistantTutorialSessionRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int sessionId;
		Tutorial tutorial;

		sessionId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialBySessionId(sessionId);
		status = tutorial != null && (!tutorial.isDraftMode() || super.getRequest().getPrincipal().hasRole(tutorial.getAssistant()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TutorialSession object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSessionById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final TutorialSession object) {
		assert object != null;

		Tuple tuple;
		SelectChoices choices;

		choices = SelectChoices.from(SessionType.class, object.getType());

		tuple = super.unbind(object, "title", "informativeAbstract", "type", "startTimestamp", "endTimestamp", "furtherInfo");
		tuple.put("masterId", object.getTutorial().getId());
		tuple.put("draftMode", object.getTutorial().isDraftMode());
		tuple.put("types", choices);

		super.getResponse().setData(tuple);
	}

}
