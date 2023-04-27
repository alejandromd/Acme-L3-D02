
package acme.features.administrator.bulletin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Bulletin;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import filter.SpamFilter;

@Service
public class AdministratorBulletinPostService extends AbstractService<Administrator, Bulletin> {

	@Autowired
	protected AdministratorBulletinRepository repository;

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
		Bulletin object;

		object = new Bulletin();
		object.setInstantiationMoment(MomentHelper.getCurrentMoment());
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Bulletin object) {
		assert object != null;
		super.bind(object, "title", "message", "critical", "link");
	}

	@Override
	public void validate(final Bulletin object) {
		assert object != null;
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "administrator.bulletin.error.confirmation");
		if (!super.getBuffer().getErrors().hasErrors("message"))
			super.state(!SpamFilter.antiSpamFilter(object.getMessage(), this.repository.findThreshold()), "message", "administrator.bulletin.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(!SpamFilter.antiSpamFilter(object.getTitle(), this.repository.findThreshold()), "title", "administrator.bulletin.error.spam");
	}

	@Override
	public void perform(final Bulletin object) {
		assert object != null;
		object.setInstantiationMoment(MomentHelper.getCurrentMoment());
		this.repository.save(object);
	}

	@Override
	public void unbind(final Bulletin object) {
		assert object != null;
		Tuple tuple;

		tuple = super.unbind(object, "title", "message", "critical", "link");
		tuple.put("confirmation", false);
		super.getResponse().setData(tuple);

	}
}
