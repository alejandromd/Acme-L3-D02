
package acme.features.administrator.offer;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AdministratorOfferShowService extends AbstractService<Administrator, Offer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorOfferRepository repository;

	// AbstractService interface ----------------------------------------------


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
		Offer object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneOfferById(id);

		super.getBuffer().setData(object);

	}

	@Override
	public void unbind(final Offer object) {
		assert object != null;

		Tuple tuple;
		int masterId;
		masterId = super.getRequest().getData("id", int.class);
		final Offer offer = this.repository.findOneOfferById(masterId);
		final Date date = Date.from(Instant.now());
		final boolean bool = offer.getStartAvaliabilityPeriod().before(date) && offer.getEndAvaliabilityPeriod().after(date);

		tuple = super.unbind(object, "instantiationMoment", "heading", "summary", "startAvaliabilityPeriod", "endAvaliabilityPeriod", "price", "furtherInformation");
		tuple.put("isViewable", !bool);

		super.getResponse().setData(tuple);
	}

}
