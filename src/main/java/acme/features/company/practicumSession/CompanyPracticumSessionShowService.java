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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Practicum;
import acme.entities.PracticumSession;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionShowService extends AbstractService<Company, PracticumSession> {
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
		int sessionId;
		final PracticumSession session;

		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findOneSessionById(sessionId);
		status = session != null && super.getRequest().getPrincipal().getActiveRoleId() == session.getPracticum().getCompany().getId();

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
	public void unbind(final PracticumSession object) {
		assert object != null;
		final Collection<Practicum> practicas;
		final SelectChoices select;
		final int companyId = super.getRequest().getPrincipal().getActiveRoleId();

		practicas = this.repository.findManyPracticaByCompanyId(companyId);
		select = SelectChoices.from(practicas, "code", object.getPracticum());
		Tuple tuple;

		tuple = super.unbind(object, "title", "summary", "initialPeriod", "finalPeriod", "link", "draftMode", "addendum");
		tuple.put("practicum", select.getSelected().getKey());
		tuple.put("practica", select);

		final int selectedId = Integer.parseInt(select.getSelected().getKey());
		final Practicum selectedPracticum = this.repository.findOnePracticaById(selectedId);

		tuple.put("practicum.code", selectedPracticum.getCode());

		super.getResponse().setData(tuple);
	}
}
