
package acme.features.assistant.tutorialSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Tutorial;
import acme.entities.tutorialSession.SessionType;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionCreateService extends AbstractService<Assistant, TutorialSession> {

	@Autowired
	protected AssistantTutorialSessionRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Tutorial tutorial;

		masterId = super.getRequest().getData("masterId", int.class);
		tutorial = this.repository.findOneTutorialById(masterId);
		status = tutorial != null && !tutorial.isDraftMode() && super.getRequest().getPrincipal().hasRole(tutorial.getAssistant());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TutorialSession object;
		int masterId;
		Tutorial tutorial;

		masterId = super.getRequest().getData("masterId", int.class);
		tutorial = this.repository.findOneTutorialById(masterId);

		object = new TutorialSession();
		object.setTutorial(tutorial);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final TutorialSession object) {
		assert object != null;

		super.bind(object, "title", "informativeAbstract", "type", "startTimestamp", "endTimestamp", "furtherInfo");
	}

	@Override
	public void validate(final TutorialSession object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("startTimestamp")) {
			final int daysBetween = (int) MomentHelper.computeDuration(MomentHelper.getCurrentMoment(), object.getStartTimestamp()).toDays();
			super.state(daysBetween > 1, "startTimestamp", "assistant.tutorialSession.form.error.start-not-ahead");
		}

		if (!super.getBuffer().getErrors().hasErrors("endTimestamp"))
			super.state(MomentHelper.isBefore(object.getStartTimestamp(), object.getEndTimestamp()), "endTimestamp", "assistant.tutorialSession.form.error.end-not-after");

		if (!super.getBuffer().getErrors().hasErrors("startTimestamp") && !super.getBuffer().getErrors().hasErrors("endTimestamp")) {
			final int hoursBetween = (int) MomentHelper.computeDuration(object.getStartTimestamp(), object.getEndTimestamp()).toHours();
			super.state(hoursBetween >= 1 && hoursBetween <= 5, "endTimestamp", "assistant.tutorialSession.form.error.invalid-duration");
		}

	}

	@Override
	public void perform(final TutorialSession object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TutorialSession object) {
		assert object != null;

		Tuple tuple;
		SelectChoices choices;

		choices = SelectChoices.from(SessionType.class, object.getType());

		tuple = super.unbind(object, "title", "informativeAbstract", "type", "startTimestamp", "endTimestamp", "furtherInfo");
		tuple.put("masterId", super.getRequest().getData("masterId", int.class));
		tuple.put("draftMode", object.getTutorial().isDraftMode());
		tuple.put("types", choices);

		super.getResponse().setData(tuple);
	}

}
