
package acme.testing.lecturer.course;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Course;
import acme.testing.TestHarness;

public class LecturerCourseDeleteTest extends TestHarness {

	@Autowired
	protected LecturerCourseTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String summary, final String retailPrice, final String link) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My courses");

		super.checkListingExists();

		super.clickOnButton("Create");

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("retailPrice", retailPrice);
		super.fillInputBoxIn("link", link);

		super.clickOnSubmit("Create");

		super.checkListingExists();

		Course course;
		course = this.repository.findCourseByCode(code);
		String param;
		param = String.format("id=%d", course.getId());

		super.request("/lecturer/course/show", param);
		super.clickOnSubmit("Delete");
		super.checkNotPanicExists();

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test101Positive(final int recordIndex, final String code, final String title, final String summary, final String retailPrice, final String link) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My courses");

		super.checkListingExists();

		super.clickOnButton("Create");

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("retailPrice", retailPrice);
		super.fillInputBoxIn("link", link);

		super.clickOnSubmit("Create");

		super.checkListingExists();

		Course course;
		course = this.repository.findCourseByCode(code);
		String param;
		param = String.format("courseId=%d", course.getId());

		super.request("/lecturer/course-lecture/add", param);
		super.fillInputBoxIn("lecture", "L");
		super.clickOnSubmit("Confirm");
		super.checkNotErrorsExist();

		param = String.format("id=%d", course.getId());
		super.request("/lecturer/course/show", param);
		super.clickOnSubmit("Delete");
		super.checkNotPanicExists();

		super.signOut();

	}

	@Test
	public void test100Negative() {
		// HINT: there aren't any negative tests for this feature since it's a listing that
		// HINT+ doesn't involve entering any data into any forms.
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
			super.request("/lecturer/course/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/course/delete", param);
			super.checkPanicExists();
			super.signOut();

			if (!course.isDraftMode()) {
				super.signIn("lecturer1", "lecturer1");
				super.request("/lecturer/course/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
		}

	}

}
