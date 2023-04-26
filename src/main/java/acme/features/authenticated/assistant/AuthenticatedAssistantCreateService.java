
package acme.features.authenticated.assistant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;
import filter.SpamFilter;

@Service
public class AuthenticatedAssistantCreateService extends AbstractService<Authenticated, Assistant> {

	@Autowired
	protected AuthenticatedAssistantRepository repository;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Assistant.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Assistant object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		object = new Assistant();
		object.setUserAccount(userAccount);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Assistant object) {
		assert object != null;

		super.bind(object, "supervisor", "expertiseFields", "resume", "link");
	}

	@Override
	public void validate(final Assistant object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("supervisor"))
			super.state(!SpamFilter.antiSpamFilter(object.getSupervisor(), this.repository.findThreshold()), "supervisor", "authenticated.assistant.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("expertiseFields"))
			super.state(!SpamFilter.antiSpamFilter(object.getExpertiseFields(), this.repository.findThreshold()), "expertiseFields", "authenticated.assistant.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("resume"))
			super.state(!SpamFilter.antiSpamFilter(object.getResume(), this.repository.findThreshold()), "resume", "authenticated.assistant.form.error.spam");
	}

	@Override
	public void perform(final Assistant object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Assistant object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "supervisor", "expertiseFields", "resume", "link");

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}
