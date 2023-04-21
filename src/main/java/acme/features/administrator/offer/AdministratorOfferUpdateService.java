
package acme.features.administrator.offer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorOfferUpdateService extends AbstractService<Administrator, Offer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorOfferRepository repository;

	// AbstractService<Employer, Job> -------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Offer offer;
		masterId = super.getRequest().getData("id", int.class);
		offer = this.repository.findOneOfferById(masterId);
		status = offer != null && super.getRequest().getPrincipal().hasRole(Administrator.class);
		final Date date = Date.from(Instant.now());
		final boolean bool = offer.getStartAvaliabilityPeriod().before(date) && offer.getEndAvaliabilityPeriod().after(date);

		super.getResponse().setAuthorised(status && !bool);
	}

	@Override
	public void load() {
		Offer object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneOfferById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Offer object) {
		assert object != null;

		super.bind(object, "instantiationMoment", "heading", "summary", "startAvaliabilityPeriod", "endAvaliabilityPeriod", "price", "furtherInformation");

	}

	@Override
	public void validate(final Offer object) {
		assert object != null;

		Calendar calendar;
		final Date latestDate;

		calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2100);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		latestDate = new Date(calendar.getTimeInMillis());

		final Date earliestDate;

		calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2000);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		earliestDate = new Date(calendar.getTimeInMillis());

		if (!super.getBuffer().getErrors().hasErrors("moment") && !super.getBuffer().getErrors().hasErrors("startAvailable")) {
			boolean isValid;

			isValid = MomentHelper.isLongEnough(object.getInstantiationMoment(), object.getStartAvaliabilityPeriod(), 1, ChronoUnit.DAYS) && MomentHelper.isFuture(object.getStartAvaliabilityPeriod());

			super.state(isValid, "startAvailable", "administrator.offer.error.onedayfuture");
		}

		if (!super.getBuffer().getErrors().hasErrors("endAvailable") && !super.getBuffer().getErrors().hasErrors("startAvailable")) {
			boolean isValid;

			isValid = MomentHelper.isLongEnough(object.getStartAvaliabilityPeriod(), object.getEndAvaliabilityPeriod(), 1, ChronoUnit.WEEKS) && MomentHelper.isBefore(object.getStartAvaliabilityPeriod(), object.getEndAvaliabilityPeriod());

			super.state(isValid, "startAvailable", "administrator.offer.error.oneweeklong");
			super.state(isValid, "endAvailable", "administrator.offer.error.oneweeklong");

		}

		if (!super.getBuffer().getErrors().hasErrors("startAvailable")) {

			boolean isValid;

			isValid = MomentHelper.isBeforeOrEqual(object.getStartAvaliabilityPeriod(), latestDate);
			super.state(isValid, "startAvailable", "administrator.offer.error.datelimits");
		}

		if (!super.getBuffer().getErrors().hasErrors("endAvailable")) {

			boolean isValid;

			isValid = MomentHelper.isAfterOrEqual(object.getEndAvaliabilityPeriod(), earliestDate) && MomentHelper.isBeforeOrEqual(object.getEndAvaliabilityPeriod(), latestDate);
			super.state(isValid, "endAvailable", "administrator.offer.error.datelimits");
		}
	}

	@Override
	public void perform(final Offer object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Offer object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "instantiationMoment", "heading", "summary", "startAvaliabilityPeriod", "endAvaliabilityPeriod", "price", "furtherInformation");

		super.getResponse().setData(tuple);
	}
}
