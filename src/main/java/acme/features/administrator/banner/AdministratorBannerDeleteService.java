
package acme.features.administrator.banner;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Banner;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AdministratorBannerDeleteService extends AbstractService<Administrator, Banner> {

	@Autowired
	protected AdministratorBannerRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		int masterId;
		masterId = super.getRequest().getData("id", int.class);
		final Banner banner = this.repository.findBannerById(masterId);
		final Date date = Date.from(Instant.now());
		final boolean bool = banner.getDisplayPeriodBegin().before(date) && banner.getDisplayPeriodFinish().after(date);
		super.getResponse().setAuthorised(!bool);
	}

	@Override
	public void load() {
		Banner object;

		final int id = super.getRequest().getData("id", int.class);
		object = this.repository.findBannerById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Banner object) {
		assert object != null;

		super.bind(object, "instantiationMoment", "slogan", "displayPeriodBegin", "displayPeriodFinish", "picture", "linkWeb");
	}

	@Override
	public void validate(final Banner object) {
		assert object != null;
	}

	@Override
	public void perform(final Banner object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "instantiationMoment", "slogan", "displayPeriodBegin", "displayPeriodFinish", "picture", "linkWeb");

		super.getResponse().setData(tuple);
	}

}
