
package acme.features.company.practicumSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Practicum;
import acme.entities.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanySessionCreateService extends AbstractService<Company, PracticumSession> {

	@Autowired
	protected CompanySessionRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int practicumId;
		Practicum practicum;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		status = practicum != null && practicum.getDraftMode() && super.getRequest().getPrincipal().hasRole(practicum.getCompany()) && super.getRequest().getPrincipal().getUsername().equals(practicum.getCompany().getUserAccount().getUsername());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		PracticumSession object;
		Practicum practicum;
		int practicumId;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		object = new PracticumSession();
		object.setPracticum(practicum);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final PracticumSession object) {
		assert object != null;

		Practicum practicum;
		int practicumId;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);

		super.bind(object, "title", "summary", "initialPeriod", "finalPeriod", "link");
		object.setPracticum(practicum);

	}

	@Override
	public void validate(final PracticumSession object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("initialPeriod") && !super.getBuffer().getErrors().hasErrors("endTime"))
			if (!MomentHelper.isBefore(object.getInitialPeriod(), object.getFinalPeriod()))
				super.state(false, "finalPeriod", "company.session.form.error.end-before-start");
			else {
				final int days = (int) MomentHelper.computeDuration(MomentHelper.getCurrentMoment(), object.getInitialPeriod()).toDays();
				if (days < 1)
					super.state(false, "initialPeriod", "company.session.form.error.day-ahead");
				else {
					final int hours = (int) MomentHelper.computeDuration(object.getInitialPeriod(), object.getFinalPeriod()).toHours();
					if (!(1 <= hours && hours <= 5))
						super.state(false, "finalPeriod", "company.session.form.error.duration");
				}
			}
	}

	@Override
	public void perform(final PracticumSession object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final PracticumSession object) {
		assert object != null;

		Practicum practicum;
		int practicumId;

		Tuple tuple;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		tuple = super.unbind(object, "title", "summary", "initialPeriod", "finalPeriod", "link");
		tuple.put("practicum", practicum);
		tuple.put("masterId", super.getRequest().getData("masterId", int.class));
		tuple.put("draftMode", object.getPracticum().getDraftMode());

		super.getResponse().setData(tuple);
	}

}
