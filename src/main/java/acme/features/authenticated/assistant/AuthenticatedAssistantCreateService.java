
package acme.features.authenticated.assistant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

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
		boolean auth;
		auth = !super.getRequest().getPrincipal().hasRole(Assistant.class);
		super.getResponse().setAuthorised(auth);
	}

	@Override
	public void load() {
		Assistant object;
		final int userAccountId = super.getRequest().getPrincipal().getAccountId();
		final UserAccount userAccount = this.repository.findUserAccountById(userAccountId);
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
		// TODO
	}

	@Override
	public void perform(final Assistant object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Assistant object) {
		assert object != null;
		final Tuple tuple;
		tuple = super.unbind(object, "supervisor", "expertiseFields", "resume", "link");
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}
