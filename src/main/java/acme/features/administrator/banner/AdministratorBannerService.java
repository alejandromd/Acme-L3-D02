
package acme.features.administrator.banner;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Banner;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorBannerService extends AbstractService<Administrator, Banner> {

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
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Banner object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findBannerById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;
		int masterId;
		masterId = super.getRequest().getData("id", int.class);
		final Banner banner = this.repository.findBannerById(masterId);
		final Date date = MomentHelper.getCurrentMoment();
		final boolean bool = banner.getDisplayPeriodBegin().before(date) && banner.getDisplayPeriodFinish().after(date);

		final Tuple tuple = super.unbind(object, "instantiationMoment", "slogan", "displayPeriodBegin", "displayPeriodFinish", "picture", "linkWeb");
		super.getResponse().setGlobal("isViewable", !bool);
		super.getResponse().setData(tuple);
	}

}
