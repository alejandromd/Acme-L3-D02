/*
 * AuthenticatedConsumerController.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.company.practicumSession;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Practicum;
import acme.entities.PracticumSession;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionPublishService extends AbstractService<Company, PracticumSession> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Company company;
		PracticumSession session;
		int sessionId;

		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findOneSessionById(sessionId);
		company = session == null ? null : session.getPracticum().getCompany();
		status = session != null && session.isDraftMode() && super.getRequest().getPrincipal().hasRole(company);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		PracticumSession object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSessionById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final PracticumSession object) {
		assert object != null;

		int practicumId;
		Practicum practicum;

		practicumId = super.getRequest().getData("practicum", int.class);
		practicum = this.repository.findOnePracticaById(practicumId);

		super.bind(object, "title", "summary", "link", "initialPeriod", "finalPeriod");

		object.setPracticum(practicum);
	}

	@Override
	public void validate(final PracticumSession object) {
		assert object != null;

		//Date Validations

		final Date inicial = super.getRequest().getData("initialPeriod", Date.class);
		final Date f_final = super.getRequest().getData("finalPeriod", Date.class);
		final Date inicioValido = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
		final Date finalValido = MomentHelper.deltaFromMoment(inicial, 7, ChronoUnit.DAYS);

		final boolean inicio_bool = inicial.getTime() >= inicioValido.getTime();
		super.state(inicio_bool, "initialPeriod", "company.practicum-session.validation.startDate.error.AtLeastOneWeekAntiquity");

		final boolean final_bool = f_final.getTime() >= finalValido.getTime();
		super.state(final_bool, "finalPeriod", "company.practicum-session.validation.endDate.error.AtLeastOneWeekDuration");

		//Practicum Validation
		final Collection<Practicum> practicas;
		final SelectChoices select;
		final int companyId = super.getRequest().getPrincipal().getActiveRoleId();

		practicas = this.repository.findManyPrivatePracticaByCompanyId(companyId);
		select = SelectChoices.from(practicas, "code", object.getPracticum());

		final int selectedId = Integer.parseInt(select.getSelected().getKey());
		final Practicum selectedPracticum = this.repository.findOnePracticaById(selectedId);

		final boolean valid = selectedPracticum.getDraftMode();
		super.state(valid, "practicum", "company.practicum-session.validation.practicum.error.Published");

	}

	@Override
	public void perform(final PracticumSession object) {
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final PracticumSession object) {
		assert object != null;
		final Collection<Practicum> practicas;
		final SelectChoices select;
		final int companyId = super.getRequest().getPrincipal().getActiveRoleId();

		practicas = this.repository.findManyPrivatePracticaByCompanyId(companyId);
		select = SelectChoices.from(practicas, "code", object.getPracticum());
		Tuple tuple;

		tuple = super.unbind(object, "title", "summary", "initialPeriod", "finalPeriod", "draftMode", "addendum", "link");
		tuple.put("practicum", select.getSelected().getKey());
		tuple.put("practica", select);

		super.getResponse().setData(tuple);
	}
}
