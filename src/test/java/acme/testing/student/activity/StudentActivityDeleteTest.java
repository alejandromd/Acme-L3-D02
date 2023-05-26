
package acme.testing.student.activity;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Activity;
import acme.testing.TestHarness;

public class StudentActivityDeleteTest extends TestHarness {

	@Autowired
	protected StudentActivityTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int eRecordIndex, final int aRecordIndex, final String title, final String summary, final String activityType, final String startPeriod, final String endPeriod, final String link) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Students", "My enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(eRecordIndex);
		super.clickOnButton("Activities");

		super.clickOnButton("Create");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("activityType", activityType);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("endPeriod", endPeriod);
		super.fillInputBoxIn("link", link);

		super.clickOnSubmit("Create");

		super.checkListingExists();

		super.clickOnMenu("Students", "My enrolments");
		super.sortListing(0, "asc");

		super.clickOnListingRecord(eRecordIndex);
		super.clickOnButton("Activities");

		super.clickOnListingRecord(aRecordIndex);
		super.clickOnSubmit("Delete");

		super.checkNotPanicExists();

		super.signOut();

	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a deletion that
		// HINT+ doesn't involve entering any data into any forms.
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
			super.request("/student/activity/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student2", "student2");
			super.request("/student/activity/delete", param);
			super.checkPanicExists();
			super.signOut();

			if (!a.getDraftMode()) {
				super.signIn("student1", "student1");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
		}
	}

}
