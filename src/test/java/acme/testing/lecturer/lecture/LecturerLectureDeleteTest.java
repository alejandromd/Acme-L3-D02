
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Course;
import acme.entities.Lecture;
import acme.testing.TestHarness;

public class LecturerLectureDeleteTest extends TestHarness {

	@Autowired
	protected LecturerLectureTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String summary, final String estimatedLearningTime, final String body, final String lectureType, final String link) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My lectures");

		super.checkListingExists();

		super.clickOnButton("Create lecture");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("estimatedLearningTime", estimatedLearningTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("lectureType", lectureType);
		super.fillInputBoxIn("link", link);

		super.clickOnSubmit("Create");

		super.checkListingExists();

		super.clickOnMenu("Lecturer", "My lectures");
		super.sortListing(2, "desc");

		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, summary);
		super.checkColumnHasValue(recordIndex, 2, estimatedLearningTime);

		super.clickOnListingRecord(recordIndex);
		super.clickOnSubmit("Delete");

		super.checkListingExists();

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test101Positive(final int recordIndex, final String title, final String summary, final String estimatedLearningTime, final String body, final String lectureType, final String link, final String code) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My lectures");

		super.checkListingExists();

		super.clickOnButton("Create lecture");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("estimatedLearningTime", estimatedLearningTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("lectureType", lectureType);
		super.fillInputBoxIn("link", link);

		super.clickOnSubmit("Create");

		super.checkListingExists();

		Course course;
		course = this.repository.findCourseByCode(code);
		String param;
		param = String.format("courseId=%d", course.getId());
		super.request("/lecturer/course-lecture/add", param);
		super.fillInputBoxIn("lecture", "test");
		super.clickOnSubmit("Confirm");
		super.checkNotErrorsExist();

		super.clickOnMenu("Lecturer", "My lectures");
		super.sortListing(2, "desc");

		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, summary);
		super.checkColumnHasValue(recordIndex, 2, estimatedLearningTime);

		super.clickOnListingRecord(recordIndex);
		super.clickOnSubmit("Delete");

		super.checkListingExists();

		super.signOut();

	}

	@Test
	public void test100Negative() {
		// HINT: there aren't any negative tests for this feature since it's a listing that
		// HINT+ doesn't involve entering any data into any forms.
	}

	@Test
	public void test300Hacking() {

		Collection<Lecture> lectures;
		String param;

		lectures = this.repository.findLecturesByUsername("lecturer1");
		for (final Lecture lecture : lectures) {
			param = String.format("id=%d", lecture.getId());

			super.checkLinkExists("Sign in");

			super.signIn("administrator", "administrator");
			super.request("/lecturer/lecture/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/lecture/delete", param);
			super.checkPanicExists();
			super.signOut();

			if (!lecture.isDraftMode()) {
				super.signIn("lecturer1", "lecturer1");
				super.request("/lecturer/lecture/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
		}

	}

}
