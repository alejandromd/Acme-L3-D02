
package acme.features.authenticated.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import filter.SpamFilter;

@Service
public class CreateStudentService extends AbstractService<Authenticated, Student> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		status = !super.getRequest().getPrincipal().hasRole(Student.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Student object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Student();
		object.setUserAccount(userAccount);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Student object) {
		assert object != null;
		super.bind(object, "statement", "link", "strongFeatures", "weakFeatures");
	}

	@Override
	public void validate(final Student object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("statement"))
			super.state(!SpamFilter.antiSpamFilter(object.getStatement(), this.repository.findThreshold()), "statement", "authenticated.student.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("strongFeatures"))
			super.state(!SpamFilter.antiSpamFilter(object.getStrongFeatures(), this.repository.findThreshold()), "strongFeatures", "authenticated.student.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("weakFeatures"))
			super.state(!SpamFilter.antiSpamFilter(object.getWeakFeatures(), this.repository.findThreshold()), "weakFeatures", "authenticated.student.error.spam");
	}

	@Override
	public void perform(final Student object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Student object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "statement", "link", "strongFeatures", "weakFeatures");
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
