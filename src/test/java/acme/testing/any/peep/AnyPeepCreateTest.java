
package acme.testing.any.peep;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AnyPeepCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-positive1.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String nick, final String message, final String email, final String link) {
		// HINT: Testea que si se crea un peep pasándole un nick se guarda con ese nick

		super.signIn("student1", "student1");

		super.clickOnMenu("Anonymous", "See peeps");
		super.checkListingExists();
		super.clickOnButton("Publish peep");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish peep");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, nick);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("nick", nick);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-positive2.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Positive(final int recordIndex, final String title, final String message, final String email, final String link) {
		// HINT: Testea que si se crea un peep siendo anónimo se pone como nick anonymous al dejar el campo en blanco

		super.clickOnMenu("Anonymous", "See peeps");
		super.checkListingExists();
		super.clickOnButton("Publish peep");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnButton("Publish peep");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, "anonymous");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("nick", "anonymous");
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@CsvFileSource(resources = "/any/peep/create-positive3.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test300Positive(final int recordIndex, final String title, final String message, final String email, final String link) {
		// HINT: Testea que si se crea un peep siendo un usuario autenticado se pone como nick el nombre de usuario al dejar el campo en blanco

		super.signIn("company1", "company1");

		super.clickOnMenu("Anonymous", "See peeps");
		super.checkListingExists();
		super.clickOnButton("Publish peep");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnButton("Publish peep");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, "company1");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("nick", "company1");
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String nick, final String message, final String email, final String link) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Anonymous", "See peeps");
		super.clickOnButton("Publish peep");
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish peep");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: there aren't any negative tests for this feature since it's is accessible to anyone
	}

}
