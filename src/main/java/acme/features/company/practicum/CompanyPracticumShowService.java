
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Practicum;
import acme.entities.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumShowService extends AbstractService<Company, Practicum> {

	@Autowired
	protected CompanyPracticumRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int practicumId;
		Practicum practicum;

		practicumId = super.getRequest().getData("id", int.class);
		practicum = this.repository.findPracticumById(practicumId);
		status = practicum != null && super.getRequest().getPrincipal().hasRole(practicum.getCompany());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Practicum object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findPracticumById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		Tuple tuple;
		int id;
		Collection<PracticumSession> sessions;
		int nHours;

		id = super.getRequest().getData("id", int.class);

		sessions = this.repository.findPracticumSessionsByPracticumId(id);

		nHours = sessions.stream().mapToInt(s -> (int) MomentHelper.computeDuration(s.getInitialPeriod(), s.getFinalPeriod()).toHours()).sum();

		tuple = super.unbind(object, "code", "title", "summary", "goals", "draftMode");
		tuple.put("totalTime", nHours);

		super.getResponse().setData(tuple);
	}
}
