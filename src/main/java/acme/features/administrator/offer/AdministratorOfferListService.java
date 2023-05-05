
package acme.features.administrator.offer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AdministratorOfferListService extends AbstractService<Administrator, Offer> {

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
		final Collection<Offer> objects;

		objects = this.repository.findAllOffers();

		super.getBuffer().setData(objects);

	}

	@Override
	public void unbind(final Offer object) {
		assert object != null;

		Tuple tuple;
		String payload;

		tuple = super.unbind(object, "heading", "startAvaliabilityPeriod", "endAvaliabilityPeriod", "price");
		payload = String.format("%s;%s;%s", object.getInstantiationMoment(), object.getSummary(), object.getFurtherInformation());
		tuple.put("payload", payload);
		super.getResponse().setData(tuple);
	}

}
