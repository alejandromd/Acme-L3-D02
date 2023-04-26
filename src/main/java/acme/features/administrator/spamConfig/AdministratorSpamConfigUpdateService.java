
package acme.features.administrator.spamConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.SpamConfig;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AdministratorSpamConfigUpdateService extends AbstractService<Administrator, SpamConfig> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorSpamConfigRepository repository;

	// AbstractService<Administrator, SpamConfig> -------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		SpamConfig object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSpamConfigById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final SpamConfig object) {
		assert object != null;

		super.bind(object, "threshold");
	}

	@Override
	public void validate(final SpamConfig object) {
		assert object != null;
	}

	@Override
	public void perform(final SpamConfig object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final SpamConfig object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "threshold");

		super.getResponse().setData(tuple);
	}

}
