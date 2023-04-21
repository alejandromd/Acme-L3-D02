
package acme.features.authenticated.auditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;
import filter.SpamFilter;

@Service
public class AuthenticatedAuditorUpdateService extends AbstractService<Authenticated, Auditor> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedAuditorRepository repository;

	// AbstractService interface ----------------------------------------------ç


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
		Auditor object;
		Principal principal;
		int userId;

		principal = super.getRequest().getPrincipal();
		userId = principal.getAccountId();
		object = this.repository.findOneAuditorByUserAccountId(userId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Auditor object) {
		assert object != null;

		super.bind(object, "firm", "professionalId", "certifications", "link");
	}

	@Override
	public void validate(final Auditor object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("certifications"))
			super.state(!SpamFilter.antiSpamFilter(object.getCertifications(), this.repository.findThreshold()), "certifications", "auditor.audit.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("firm"))
			super.state(!SpamFilter.antiSpamFilter(object.getFirm(), this.repository.findThreshold()), "firm", "auditor.audit.form.error.spam");
	}

	@Override
	public void perform(final Auditor object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Auditor object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "firm", "professionalId", "certifications", "link");
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
