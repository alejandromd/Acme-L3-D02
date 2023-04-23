
package acme.features.administrator.offer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorOfferCreateService extends AbstractService<Administrator, Offer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorOfferRepository repository;

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
		final Offer object;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		object = new Offer();
		object.setInstantiationMoment(moment);

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

			super.state(isValid, "startAvaliabilityPeriod", "administrator.offer.error.onedayfuture");
		}

		if (!super.getBuffer().getErrors().hasErrors("endAvaliabilityPeriod") && !super.getBuffer().getErrors().hasErrors("startAvailable")) {
			boolean isValid;

			isValid = MomentHelper.isLongEnough(object.getStartAvaliabilityPeriod(), object.getEndAvaliabilityPeriod(), 1, ChronoUnit.WEEKS) && MomentHelper.isBefore(object.getStartAvaliabilityPeriod(), object.getEndAvaliabilityPeriod());

			super.state(isValid, "startAvaliabilityPeriod", "administrator.offer.error.oneweeklong");
			super.state(isValid, "endAvaliabilityPeriod", "administrator.offer.error.oneweeklong");

		}

		if (!super.getBuffer().getErrors().hasErrors("startAvaliabilityPeriod")) {

			boolean isValid;

			isValid = MomentHelper.isBeforeOrEqual(object.getStartAvaliabilityPeriod(), latestDate);
			super.state(isValid, "startAvaliabilityPeriod", "administrator.offer.error.datelimits");
		}

		if (!super.getBuffer().getErrors().hasErrors("endAvaliabilityPeriod")) {

			boolean isValid;

			isValid = MomentHelper.isAfterOrEqual(object.getEndAvaliabilityPeriod(), earliestDate) && MomentHelper.isBeforeOrEqual(object.getEndAvaliabilityPeriod(), latestDate);
			super.state(isValid, "endAvaliabilityPeriod", "administrator.offer.error.datelimits");
		}
		if (!super.getBuffer().getErrors().hasErrors("price")) {
			final Double amount = object.getPrice().getAmount();
			super.state(amount >= 0 && amount < 1000000, "price", "administrator.offer.error.price");
		}
		if (!super.getBuffer().getErrors().hasErrors("price")) {
			final String aceptedCurrencies = this.repository.findSystemConfiguration().getAceptedCurrencies();
			final List<String> currencies = Arrays.asList(aceptedCurrencies.split(","));
			super.state(currencies.contains(object.getPrice().getCurrency()), "price", "administrator.offer.error.currency");
			super.state(currencies.contains(object.getPrice().getCurrency()), "price", aceptedCurrencies);
		}
	}

	@Override
	public void perform(final Offer object) {
		assert object != null;
		object.setInstantiationMoment(Date.from(Instant.now()));
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
