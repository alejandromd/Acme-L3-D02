
package acme.features.administrator.banner;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Banner;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import filter.SpamFilter;

@Service
public class AdministratorBannerCreateService extends AbstractService<Administrator, Banner> {

	@Autowired
	protected AdministratorBannerRepository repository;


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
		Banner object;
		Date moment;
		Date start;
		Date end;
		Calendar calendar;
		final int MILLIS_IN_ONE_MINUTE = 60 * 1000;

		moment = MomentHelper.getCurrentMoment();

		start = Date.from(Instant.now());
		start.setTime(start.getTime() + MILLIS_IN_ONE_MINUTE);

		calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		end = calendar.getTime();

		object = new Banner();
		object.setInstantiationMoment(moment);
		object.setDisplayPeriodBegin(start);
		object.setDisplayPeriodFinish(end);
		object.setPicture("");
		object.setSlogan("");
		object.setLinkWeb("");

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

		if (!super.getBuffer().getErrors().hasErrors("displayPeriodBegin"))
			super.state(MomentHelper.isFuture(object.getDisplayPeriodBegin()), "displayPeriodBegin", "administrator.banner.form.error.wrong-displayStart");

		if (!super.getBuffer().getErrors().hasErrors("displayPeriodFinish")) {
			Date start;
			Date startOneWeek;
			Calendar calendar;

			start = object.getDisplayPeriodBegin();
			calendar = Calendar.getInstance();
			calendar.setTime(start);
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			startOneWeek = calendar.getTime();

			super.state(MomentHelper.isAfterOrEqual(object.getDisplayPeriodFinish(), startOneWeek), "displayPeriodFinish", "administrator.banner.form.error.wrong-displayEnd");
		}
		if (!super.getBuffer().getErrors().hasErrors("slogan"))
			super.state(!SpamFilter.antiSpamFilter(object.getSlogan(), this.repository.findThreshold()), "slogan", "administrator.banner.error.spam");

	}

	@Override
	public void perform(final Banner object) {
		assert object != null;

		Date d;

		d = Date.from(Instant.now());
		object.setInstantiationMoment(d);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "instantiationMoment", "slogan", "displayPeriodBegin", "displayPeriodFinish", "picture", "linkWeb");

		super.getResponse().setData(tuple);
	}

}
