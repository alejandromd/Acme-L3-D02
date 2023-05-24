
package acme.testing.any.peep;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AnyPeepCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-positive1.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String nick, final String message, final String email, final String link) {
		// HINT: Testea que si se crea un peep pasándole un nick se guarda con ese nick, aunque estemos autenticados en el sistema

		super.signIn("student1", "student1");

		super.clickOnMenu("Authenticated", "See peeps");
		super.checkListingExists();
		super.clickOnButton("Publish peep");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish peep");

		super.checkNotPanicExists();
		super.checkListingExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-positive2.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test101Positive(final int recordIndex, final String title, final String nick, final String message, final String email, final String link) {
		// HINT: Testea que si se crea un peep siendo anónimo se pone como nick el que le pasemos nosotros

		super.clickOnMenu("Anonymous", "See peeps");
		super.checkListingExists();
		super.clickOnButton("Publish peep");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish peep");

		super.checkNotPanicExists();
		super.checkListingExists();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-positive3.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test102Positive(final int recordIndex, final String title, final String message, final String email, final String link) {
		// HINT: Testea que si se crea un peep siendo un usuario autenticado se pone como nick el nombre de usuario al dejar el campo en blanco

		super.signIn("company1", "company1");

		super.clickOnMenu("Authenticated", "See peeps");
		super.checkListingExists();
		super.clickOnButton("Publish peep");

		super.fillInputBoxIn("title", title);
		super.checkInputBoxHasValue("nick", "Company, Company");
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish peep");

		super.checkNotPanicExists();
		super.checkListingExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String nick, final String message, final String email, final String link) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Authenticated", "See peeps");
		super.clickOnButton("Publish peep");
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("nick", nick);
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
