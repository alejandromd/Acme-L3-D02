
package acme.features.administrator.spamConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.SpamConfig;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AdministratorSpamConfigListService extends AbstractService<Administrator, SpamConfig> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AdministratorSpamConfigRepository repository;

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
		SpamConfig object;

		object = this.repository.findOneSpamConfig();
		super.getBuffer().setData(object);

	}

	@Override
	public void unbind(final SpamConfig object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "threshold");

		super.getResponse().setData(tuple);
	}

}
