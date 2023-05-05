
package acme.features.authenticated.offer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Offer;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedOfferListService extends AbstractService<Authenticated, Offer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedOfferRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().isAuthenticated();
		super.getResponse().setAuthorised(status);
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
