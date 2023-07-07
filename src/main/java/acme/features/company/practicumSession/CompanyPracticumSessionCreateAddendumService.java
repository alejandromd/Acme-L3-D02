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
public class CompanyPracticumSessionCreateAddendumService extends AbstractService<Company, PracticumSession> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionRepository repository;

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
		PracticumSession object;

		object = new PracticumSession();
		object.setAddendum(true);
		object.setDraftMode(true);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final PracticumSession object) {
		assert object != null;
		super.bind(object, "title", "summary", "link", "initialPeriod", "finalPeriod");

	}

	@Override
	public void validate(final PracticumSession object) {
		assert object != null;

		final Date inicial = super.getRequest().getData("initialPeriod", Date.class);
		final Date f_final = super.getRequest().getData("finalPeriod", Date.class);
		final Date inicioValido = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
		final Date finalValido = MomentHelper.deltaFromMoment(inicial, 7, ChronoUnit.DAYS);

		final boolean inicio_bool = inicial.getTime() >= inicioValido.getTime();
		super.state(inicio_bool, "initialPeriod", "company.practicum-session.validation.startDate.error.AtLeastOneWeekAntiquity");

		final boolean final_bool = f_final.getTime() >= finalValido.getTime();
		super.state(final_bool, "finalPeriod", "company.practicum-session.validation.endDate.error.AtLeastOneWeekDuration");

		boolean confirm;
		confirm = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirm, "confirmation", "company.practicum-session.validation.confirmation");

		final Collection<Practicum> practicas;
		Collection<PracticumSession> addendums;
		final SelectChoices choices;
		final int companyId = super.getRequest().getPrincipal().getActiveRoleId();

		practicas = this.repository.findManyPublishedPracticaByCompanyId(companyId);
		choices = SelectChoices.from(practicas, "code", object.getPracticum());

		final int selectedId = Integer.parseInt(choices.getSelected().getKey());
		addendums = this.repository.findAddendumSessionsByPracticumId(selectedId);

		final boolean valid = addendums.size() == 0;
		super.state(valid, "practicum", "company.practicum-session.validation.practicum.error.AddendumUsed");
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

		practicas = this.repository.findManyPublishedPracticaByCompanyId(companyId);
		select = SelectChoices.from(practicas, "code", object.getPracticum());
		Tuple tuple;

		tuple = super.unbind(object, "title", "summary", "initialPeriod", "finalPeriod", "draftMode", "addendum", "link");
		tuple.put("practicum", select.getSelected().getKey());
		tuple.put("practica", select);
		tuple.put("confirmation", false);
		tuple.put("practicumId", super.getRequest().getData("practicumId", int.class));

		super.getResponse().setData(tuple);
	}
}
