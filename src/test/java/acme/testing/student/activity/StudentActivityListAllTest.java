
package acme.testing.student.activity;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Enrolment;
import acme.testing.TestHarness;

public class StudentActivityListAllTest extends TestHarness {

	@Autowired
	protected StudentActivityTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int eRecordIndex, final int aRecordIndex, final String title, final String activityType, final String startPeriod, final String endPeriod) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Students", "My enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(eRecordIndex);
		super.clickOnButton("Activities");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(aRecordIndex, 0, title);
		super.checkColumnHasValue(aRecordIndex, 1, activityType);
		super.checkColumnHasValue(aRecordIndex, 2, startPeriod);
		super.checkColumnHasValue(aRecordIndex, 3, endPeriod);

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

		enrolments = this.repository.findManyEnrolmentsByStudentUsername("student1");
		for (final Enrolment e : enrolments) {

			param = String.format("enrolmentId=%d", e.getId());

			super.checkLinkExists("Sign in");
			super.request("/student/activity/list", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/student/activity/list", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/student/activity/list", param);
			super.checkPanicExists();
			super.signOut();

			if (e.getDraftMode()) {
				super.signIn("student1", "student1");
				super.request("/student/activity/list", param);
				super.checkPanicExists();
				super.signOut();
			}

		}
	}

}
