
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorialSession.TutorialSession;
import acme.testing.TestHarness;

public class AssistantSessionShowTest extends TestHarness {

	@Autowired
	protected AssistantSessionTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String informativeAbstract, final String type, final String startTimestamp, final String endTimestamp, final String furtherInfo) {
		// HINT: this test signs in as an assistant, lists his or her sessions, selects
		// HINT: one of them and checks that it's as expected.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("Show sessions");
		super.checkListingExists();
		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("informativeAbstract", informativeAbstract);
		super.checkInputBoxHasValue("type", type);
		super.checkInputBoxHasValue("startTimestamp", startTimestamp);
		super.checkInputBoxHasValue("endTimestamp", endTimestamp);
		super.checkInputBoxHasValue("furtherInfo", furtherInfo);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT: involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show a session of an assistant that wasn't published by the principal;

		Collection<TutorialSession> sessions;
		String param;

		sessions = this.repository.findManySessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions) {

			param = String.format("id=%d", session.getTutorial().getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/tutorial-session/show", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/assistant/tutorial-session/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/assistant/tutorial-session/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/tutorial-session/show", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
