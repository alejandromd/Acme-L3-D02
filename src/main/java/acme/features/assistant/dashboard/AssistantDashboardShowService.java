
package acme.features.assistant.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.forms.AssistantDashboard;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantDashboardShowService extends AbstractService<Assistant, AssistantDashboard> {

	@Autowired
	protected AssistantDashboardRepository repository;


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
		Principal principal;
		int userAccountId;
		Assistant assistant;
		int assistantId;

		AssistantDashboard dashboard;
		Integer numberOfTutorials;
		Double averageTimeOfSessions;
		Double stdDevTimeOfSessions;
		Double minTimeOfSessions;
		Double maxTimeOfSessions;
		Double averageTimeOfTutorials;
		Double stdDevOfTutorials;
		Double minTimeOfTutorials;
		Double maxTimeOfTutorials;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		assistant = this.repository.findOneAssistantByUserAccountId(userAccountId);
		assistantId = assistant.getId();

		numberOfTutorials = this.repository.findNumberOfTutorialsByAssistantId(assistantId);
		averageTimeOfSessions = this.repository.findAverageTimeOfSessionsByAssistantId(assistantId);
		stdDevTimeOfSessions = this.repository.findStdDevTimeOfSessionsByAssistantId(assistantId);
		minTimeOfSessions = this.repository.findMinTimeOfSessionsByAssistantId(assistantId);
		maxTimeOfSessions = this.repository.findMaxTimeOfSessionsByAssistantId(assistantId);
		averageTimeOfTutorials = this.repository.findAverageTimeOfTutorialsByAssistantId(assistantId);
		stdDevOfTutorials = this.repository.findStdDevTimeOfTutorialsByAssistantId(assistantId);
		minTimeOfTutorials = this.repository.findMinTimeOfTutorialsByAssistantId(assistantId);
		maxTimeOfTutorials = this.repository.findMaxTimeOfTutorialsByAssistantId(assistantId);

		dashboard = new AssistantDashboard();
		dashboard.setNumberOfTutorials(numberOfTutorials);
		dashboard.setAverageTimeOfSessions(averageTimeOfSessions);
		dashboard.setDeviationTimeOfSessions(stdDevTimeOfSessions);
		dashboard.setMinimumTimeOfSessions(minTimeOfSessions);
		dashboard.setMaximumTimeOfSessions(maxTimeOfSessions);
		dashboard.setAverageTimeOfTutorials(averageTimeOfTutorials);
		dashboard.setDeviationTimeOfTutorials(stdDevOfTutorials);
		dashboard.setMinimumTimeOfTutorials(minTimeOfTutorials);
		dashboard.setMaximumTimeOfTutorials(maxTimeOfTutorials);

		super.getBuffer().setData(dashboard);
	}

	@Override
	public void unbind(final AssistantDashboard object) {
		Tuple tuple;

		tuple = super.unbind(object, "numberOfTutorials", "averageTimeOfSessions", "deviationTimeOfSessions", "minimumTimeOfSessions", "maximumTimeOfSessions", "averageTimeOfTutorials", "deviationTimeOfTutorials", "minimumTimeOfTutorials",
			"maximumTimeOfTutorials");

		super.getResponse().setData(tuple);
	}
}
