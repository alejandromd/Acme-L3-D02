
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Audit;
import acme.testing.TestHarness;

public class AuditorAuditDeleteTest extends TestHarness {

	@Autowired
	protected AuditorAuditTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String course, final String code, final String strongPoints, final String weakPoints, final String conclusion) {
		// HINT: this test authenticates as an auditor and then lists his or her
		// HINT: audits, creates a new one, and check that it's been created properly.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Audits");
		super.checkListingExists();

		super.clickOnButton("Create");
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("strongPoints", strongPoints);
		super.fillInputBoxIn("weakPoints", weakPoints);
		super.fillInputBoxIn("conclusion", conclusion);
		super.clickOnSubmit("Create");

		super.checkListingExists();

		Audit audit;
		audit = this.repository.findOneAuditByCode(code);

		String param;
		param = String.format("id=%d", audit.getId());

		super.request("/auditor/audit/show", param);
		super.clickOnSubmit("Delete");
		super.checkNotPanicExists();

		super.signOut();

	}

	@Test
	public void test300Hacking() {
		Collection<Audit> audits;
		String param;

		audits = this.repository.findManyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits) {
			param = String.format("id=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {

		Collection<Audit> audits;
		String param;

		audits = this.repository.findManyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits)
			if (!audit.isDraftMode()) {
				super.signIn("auditor1", "auditor1");

				param = String.format("id=%d", audit.getId());

				super.request("/auditor/audit/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
	}
}
