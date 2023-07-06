
package acme.features.student.enrolment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import filter.SpamFilter;

@Service
public class EnrolmentUpdateService extends AbstractService<Student, Enrolment> {

	@Autowired
	protected EnrolmentRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {

		Enrolment object;
		int id;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findEnrolmentById(id);
		final int userId = super.getRequest().getPrincipal().getAccountId();

		status = object.getStudent().getUserAccount().getId() == userId && object.getDraftMode();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Enrolment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findEnrolmentById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {

		assert object != null;
		String cardNumber;
		String lowerNibble;

		cardNumber = super.getRequest().getData("cardNumber", String.class);

		super.bind(object, "code", "motivation", "goals", "holderName");

		final Course course = this.repository.findCourseById(super.getRequest().getData("course", int.class));
		object.setCourse(course);

		if (cardNumber.length() == 16) {
			lowerNibble = cardNumber.substring(12);
			object.setLowerNibble(lowerNibble);
		}
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Enrolment enrolment;

			enrolment = this.repository.findEnrolmentByCode(object.getCode());
			super.state(enrolment == null || enrolment.equals(object), "code", "student.enrolment.form.error.code-duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("motivation"))
			super.state(!SpamFilter.antiSpamFilter(object.getMotivation(), this.repository.findThreshold()), "motivation", "student.enrolment.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(!SpamFilter.antiSpamFilter(object.getGoals(), this.repository.findThreshold()), "goals", "student.enrolment.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("holderName"))
			if (object.getHolderName() != "")
				super.state(!SpamFilter.antiSpamFilter(object.getHolderName(), this.repository.findThreshold()), "holderName", "student.enrolment.error.spam");

		if (!super.getBuffer().getErrors().hasErrors("cardNumber")) {
			String creditCardNumber;
			creditCardNumber = super.getRequest().getData("cardNumber", String.class);
			if (creditCardNumber != "")
				super.state(creditCardNumber.matches("\\d{16}"), "cardNumber", "student.enrolment.form.error.wrong-cardNumber");
		}

		if (!super.getBuffer().getErrors().hasErrors("expiryDate")) {
			String expiryDate;
			Date date = null;

			expiryDate = super.getRequest().getData("expiryDate", String.class);

			if (expiryDate != "")
				if (super.getRequest().getLocale().toString().equals("es")) {
					final DateFormat format = new SimpleDateFormat("MM/yy");
					format.setLenient(false);
					super.state(expiryDate.matches("\\d{2}/\\d{2}") && expiryDate.matches("(0?[1-9]|[1][0-2])/[0-9]+"), "expiryDate", "student.enrolment.form.error.wrong-expiryDate");

					if (expiryDate.matches("\\d{2}/\\d{2}") && expiryDate.matches("(0?[1-9]|[1][0-2])/[0-9]+")) {
						try {
							date = format.parse(expiryDate);
						} catch (final ParseException e) {
							e.printStackTrace();
						}
						super.state(MomentHelper.isFuture(date), "expiryDate", "student.enrolment.form.error.card-expired");
					}
				} else {
					final DateFormat format = new SimpleDateFormat("yy/MM");
					format.setLenient(false);
					super.state(expiryDate.matches("\\d{2}/\\d{2}") && expiryDate.matches("[0-9]+/(0?[1-9]|[1][0-2])"), "expiryDate", "student.enrolment.form.error.wrong-expiryDate");

					if (expiryDate.matches("\\d{2}/\\d{2}") && expiryDate.matches("[0-9]+/(0?[1-9]|[1][0-2])")) {
						try {
							date = format.parse(expiryDate);
						} catch (final ParseException e) {
							e.printStackTrace();
						}
						super.state(MomentHelper.isFuture(date), "expiryDate", "student.enrolment.form.error.card-expired");
					}
				}
		}
		if (!super.getBuffer().getErrors().hasErrors("cvc")) {
			String cvc;
			cvc = super.getRequest().getData("cvc", String.class);

			if (cvc != "")
				super.state(cvc.matches("\\d{3}"), "cvc", "student.enrolment.form.error.wrong-cvc");
		}

	}

	@Override
	public void perform(final Enrolment object) {

		assert object != null;

		this.repository.save(object);

	}

	@Override
	public void unbind(final Enrolment object) {

		assert object != null;
		Tuple tuple;
		String cardNumber;
		String lowerNibble;
		String expiryDate;
		String cvc;

		cardNumber = super.getRequest().getData("cardNumber", String.class);
		expiryDate = super.getRequest().getData("expiryDate", String.class);
		cvc = super.getRequest().getData("cvc", String.class);

		final Collection<Course> courses = this.repository.findPublishedCourses();
		final SelectChoices s = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, "code", "motivation", "goals", "draftMode", "holderName");
		tuple.put("course", s.getSelected().getKey());
		tuple.put("courses", s);
		tuple.put("cardNumber", cardNumber);
		tuple.put("expiryDate", expiryDate);
		tuple.put("cvc", cvc);

		if (cardNumber.length() == 16) {
			lowerNibble = cardNumber.substring(12);
			tuple.put("lowerNibble", lowerNibble);
		}

		super.getResponse().setData(tuple);

	}

}
