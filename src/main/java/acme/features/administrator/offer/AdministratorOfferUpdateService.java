
package acme.features.administrator.offer;

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
import filter.SpamFilter;

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
		int masterId;
		Offer offer;
		masterId = super.getRequest().getData("id", int.class);
		offer = this.repository.findOneOfferById(masterId);
		final Date date = MomentHelper.getCurrentMoment();
		super.getResponse().setAuthorised(offer != null && !(offer.getStartAvaliabilityPeriod().before(date) && offer.getEndAvaliabilityPeriod().after(date)));
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

		if (object.getStartAvaliabilityPeriod() != null && object.getEndAvaliabilityPeriod() != null) {

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
		}
		if (object.getPrice() != null) {
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
		if (!super.getBuffer().getErrors().hasErrors("heading"))
			super.state(!SpamFilter.antiSpamFilter(object.getHeading(), this.repository.findThreshold()), "heading", "administrator.offer.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("summary"))
			super.state(!SpamFilter.antiSpamFilter(object.getSummary(), this.repository.findThreshold()), "summary", "administrator.offer.error.spam");

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
