
package acme.testing.lecturer.course;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Course;
import acme.testing.TestHarness;

public class LecturerCoursePublishTest extends TestHarness {

	@Autowired
	protected LecturerCourseTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code) {

		Course course;
		course = this.repository.findCourseByCode(code);
		String param;
		param = String.format("id=%d", course.getId());

		super.signIn("lecturer1", "lecturer1");
		super.request("/lecturer/course/show", param);
		super.clickOnSubmit("Publish");
		super.checkNotPanicExists();
		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final int recordIndex, final String code, final String title, final String summary, final String retailPrice, final String link) {

		Course course;
		course = this.repository.findCourseByCode("KFF561");
		String param;
		param = String.format("id=%d", course.getId());

		super.signIn("lecturer1", "lecturer1");
		super.request("/lecturer/course/show", param);

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("retailPrice", retailPrice);
		super.fillInputBoxIn("link", link);

		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test101Negative() {

		Course course;
		course = this.repository.findCourseByCode("KFF561");
		String param;
		param = String.format("id=%d", course.getId());

		super.signIn("lecturer1", "lecturer1");
		super.request("/lecturer/course/show", param);
		super.clickOnSubmit("Publish");
		super.checkErrorsExist();

		super.request("/lecturer/course/show", param);
		super.clickOnButton("Lectures");
		super.checkListingExists();
		super.clickOnListingRecord(0);
		super.clickOnSubmit("Delete lecture from course");
		super.checkNotPanicExists();
		super.request("/lecturer/course/show", param);
		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Course> courses;
		String param;

		courses = this.repository.findCoursesByUsername("lecturer1");
		for (final Course course : courses) {
			param = String.format("id=%d", course.getId());

			super.checkLinkExists("Sign in");

			super.signIn("administrator", "administrator");
			super.request("/lecturer/course/publish", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/course/publish", param);
			super.checkPanicExists();
			super.signOut();

			if (!course.isDraftMode()) {
				super.signIn("lecturer1", "lecturer1");
				super.request("/lecturer/course/publish", param);
				super.checkPanicExists();
				super.signOut();
			}
		}

	}

}
