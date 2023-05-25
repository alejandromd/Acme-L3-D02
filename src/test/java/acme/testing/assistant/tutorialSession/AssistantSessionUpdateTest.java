
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorialSession.TutorialSession;
import acme.testing.TestHarness;

public class AssistantSessionUpdateTest extends TestHarness {

	@Autowired
	protected AssistantSessionTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String informativeAbstract, final String type, final String startTimestamp, final String endTimestamp, final String furtherInfo) {

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("Show tutorials");
		super.checkListingExists();
		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("informativeAbstract", informativeAbstract);
		super.fillInputBoxIn("type", type);
		super.fillInputBoxIn("startTimestamp", startTimestamp);
		super.fillInputBoxIn("endTimestamp", endTimestamp);
		super.fillInputBoxIn("furtherInfo", furtherInfo);
		super.clickOnSubmit("Update");

		super.checkListingExists();
		super.checkColumnHasValue(sessionRecordIndex, 0, title);
		super.checkColumnHasValue(sessionRecordIndex, 1, type);
		super.checkColumnHasValue(sessionRecordIndex, 2, startTimestamp);
		super.checkColumnHasValue(sessionRecordIndex, 3, endTimestamp);
		super.clickOnListingRecord(sessionRecordIndex);

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("informativeAbstract", informativeAbstract);
		super.checkInputBoxHasValue("type", type);
		super.checkInputBoxHasValue("startTimestamp", startTimestamp);
		super.checkInputBoxHasValue("endTimestamp", endTimestamp);
		super.checkInputBoxHasValue("furtherInfo", furtherInfo);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String informativeAbstract, final String type, final String startTimestamp, final String endTimestamp, final String furtherInfo) {
		// HINT: this test attempts to update a session with wrong data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("Show tutorials");
		super.checkListingExists();
		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("informativeAbstract", informativeAbstract);
		super.fillInputBoxIn("type", type);
		super.fillInputBoxIn("startTimestamp", startTimestamp);
		super.fillInputBoxIn("endTimestamp", endTimestamp);
		super.fillInputBoxIn("furtherInfo", furtherInfo);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<TutorialSession> sessions;
		String param;

		sessions = this.repository.findManySessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions) {

			param = String.format("id=%d", session.getTutorial().getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/tutorial-session/update", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/assistant/tutorial-session/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant2", "assistant2");
			super.request("/assistant/tutorial-session/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/tutorial-session/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {

		Collection<TutorialSession> sessions;
		String param;

		sessions = this.repository.findManySessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions)
			if (!session.getTutorial().isDraftMode()) {
				super.signIn("assistant1", "assistant1");
				param = String.format("id=%d", session.getTutorial().getId());

				super.request("/assistant/tutorial-session/update", param);
				super.checkPanicExists();
				super.signOut();
			}
	}
}
