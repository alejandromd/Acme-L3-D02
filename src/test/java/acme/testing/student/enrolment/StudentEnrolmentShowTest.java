
package acme.testing.student.enrolment;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Enrolment;
import acme.testing.TestHarness;

public class StudentEnrolmentShowTest extends TestHarness {

	@Autowired
	protected StudentEnrolmentTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String motivation, final String goals, final String draftMode, final String course, final String workTime) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Students", "My enrolments");
		super.checkListingExists();

		Enrolment enrolment;
		enrolment = this.repository.findEnrolmentByCode(code);
		String param;
		param = String.format("id=%d", enrolment.getId());
		super.request("/student/enrolment/show", param);

		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("motivation", motivation);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("draftMode", draftMode);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("workTime", workTime);
		

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a deletion that
		// HINT+ doesn't involve entering any data into any forms.
	}

	@Test
	public void test300Hacking() {

		Collection<Enrolment> enrolments;
		String param;

		enrolments = this.repository.findManyEnrolemntsByStudentUsername("student1");
		for (final Enrolment e : enrolments)
			if (e.getDraftMode()) {
				param = String.format("id=%d", e.getId());

				super.checkLinkExists("Sign in");
				super.request("/student/enrolment/show", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/student/enrolment/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student2", "student2");
				super.request("/student/enrolment/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/student/enrolment/show", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
