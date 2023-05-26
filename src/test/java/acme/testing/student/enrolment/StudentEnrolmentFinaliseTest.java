
package acme.testing.student.enrolment;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Enrolment;
import acme.testing.TestHarness;

public class StudentEnrolmentFinaliseTest extends TestHarness {

	@Autowired
	protected StudentEnrolmentTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/finalise-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String motivation, final String goals, final String course, final String holderName, final String cardNumber, final String expiryDate, final String cvc) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Students", "My enrolments");
		super.checkListingExists();

		super.clickOnButton("Create");
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("motivation", motivation);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("course", course);

		super.clickOnSubmit("Create");
		super.checkNotPanicExists();

		Enrolment enrolment;
		enrolment = this.repository.findEnrolmentByCode(code);
		String param;
		param = String.format("id=%d", enrolment.getId());
		super.request("/student/enrolment/show", param);

		super.fillInputBoxIn("holderName", holderName);
		super.fillInputBoxIn("cardNumber", cardNumber);
		super.fillInputBoxIn("expiryDate", expiryDate);
		super.fillInputBoxIn("cvc", cvc);
		super.clickOnSubmit("Finalise");
		
		super.checkNotPanicExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/finalise-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String holderName, final String cardNumber, final String expiryDate, final String cvc) {

		super.signIn("student1", "student1");

		Enrolment enrolment;
		enrolment = this.repository.findEnrolmentByCode("AAA444");
		String param;
		param = String.format("id=%d", enrolment.getId());
		super.request("/student/enrolment/show", param);

		super.fillInputBoxIn("holderName", holderName);
		super.fillInputBoxIn("cardNumber", cardNumber);
		super.fillInputBoxIn("expiryDate", expiryDate);
		super.fillInputBoxIn("cvc", cvc);

		super.clickOnSubmit("Finalise");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Enrolment> enrolments;
		String param;

		enrolments = this.repository.findManyEnrolemntsByStudentUsername("student1");
		for (final Enrolment e : enrolments) {
			param = String.format("id=%d", e.getId());

			super.checkLinkExists("Sign in");

			super.signIn("administrator", "administrator");
			super.request("/student/enrolment/finalise", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student2", "student2");
			super.request("/student/enrolment/finalise", param);
			super.checkPanicExists();
			super.signOut();

			if (!e.getDraftMode()) {
				super.signIn("student1", "student1");
				super.request("/student/enrolment/finalise", param);
				super.checkPanicExists();
				super.signOut();
			}
		}

	}

}
