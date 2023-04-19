
package acme.features.authenticated.note;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Note;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import filter.SpamFilter;

@Service
public class AuthenticatedNoteCreateService extends AbstractService<Authenticated, Note> {

	@Autowired
	protected AuthenticatedNoteRepository repository;


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
		Note object;
		Principal principal;
		int userId;
		UserAccount userAccount;
		Date moment;

		principal = super.getRequest().getPrincipal();
		userId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userId);
		moment = MomentHelper.getCurrentMoment();

		object = new Note();
		object.setInstantiationMoment(moment);
		object.setAuthor(userAccount.getUsername() + " - " + userAccount.getIdentity().getSurname() + ", " + userAccount.getIdentity().getName());

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Note object) {
		assert object != null;

		super.bind(object, "instantiationMoment", "title", "author", "message", "email", "link");

	}

	@Override
	public void validate(final Note object) {
		assert object != null;

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");
		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(!SpamFilter.antiSpamFilter(object.getTitle(), this.repository.findThreshold()), "title", "auditor.audit.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("message"))
			super.state(!SpamFilter.antiSpamFilter(object.getMessage(), this.repository.findThreshold()), "message", "auditor.audit.form.error.spam");

	}

	@Override
	public void perform(final Note object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Note object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "instantiationMoment", "title", "author", "message", "email", "link");
		tuple.put("confirmation", false);

		super.getResponse().setData(tuple);
	}
}
