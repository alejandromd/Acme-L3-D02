
package acme.testing.student.activity;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Activity;
import acme.testing.TestHarness;

public class StudentActivityShowTest extends TestHarness {

	@Autowired
	protected StudentActivityTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int eRecordIndex, final int aRecordIndex, final String title, final String summary, final String activityType, final String startPeriod, final String endPeriod, final String link) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Students", "My enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(eRecordIndex);
		super.clickOnButton("Activities");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(aRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("summary", summary);
		super.checkInputBoxHasValue("activityType", activityType);
		super.checkInputBoxHasValue("startPeriod", startPeriod);
		super.checkInputBoxHasValue("endPeriod", endPeriod);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {

		final Collection<Activity> activities;
		String param;

		activities = this.repository.findManyActivitiesByStudentUsername("student1");
		for (final Activity a : activities) {
			param = String.format("id=%d", a.getId());

			super.checkLinkExists("Sign in");

			super.signIn("administrator", "administrator");
			super.request("/student/activity/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student2", "student2");
			super.request("/student/activity/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/student/activity/show", param);
			super.checkPanicExists();
			super.signOut();

			if (a.getEnrolment().getDraftMode()) {
				super.signIn("student1", "student1");
				super.request("/student/activity/show", param);
				super.checkPanicExists();
				super.signOut();
			}

		}
	}

}
