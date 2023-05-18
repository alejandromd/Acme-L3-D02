
package acme.testing.auditor.auditingRecord;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Audit;
import acme.testing.TestHarness;

public class AuditorAuditingRecordCreateCorrectionTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/create-correction-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditRecordIndex, final int auditingRecordRecordIndex, final String subject, final String assessment, final String periodStartDate, final String periodEndDate, final String mark, final String link) {
		// HINT: this test authenticates as an auditor, list his or her Audits, navigates
		// HINT+ to their auditing records, and checks that they have the expected data.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(auditRecordIndex);
		super.clickOnButton("Auditing records");

		super.clickOnButton("Create a correctional auditing record");
		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("periodStartDate", periodStartDate);
		super.fillInputBoxIn("periodEndDate", periodEndDate);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("confirmation", "true");
		super.clickOnSubmit("Create a correctional auditing record");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(auditingRecordRecordIndex, 0, subject);
		super.checkColumnHasValue(auditingRecordRecordIndex, 1, mark);

		super.clickOnListingRecord(auditingRecordRecordIndex);
		super.checkInputBoxHasValue("subject", subject);
		super.checkInputBoxHasValue("assessment", assessment);
		super.checkInputBoxHasValue("periodStartDate", periodStartDate);
		super.checkInputBoxHasValue("periodEndDate", periodEndDate);
		super.checkInputBoxHasValue("mark", mark);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/create-correction-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditRecordIndex, final int auditingRecordRecordIndex, final String subject, final String assessment, final String periodStartDate, final String periodEndDate, final String mark, final String link) {
		// HINT: this test attempts to create auditing records using wrong data.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(auditRecordIndex);
		super.clickOnButton("Auditing records");

		super.clickOnButton("Create a correctional auditing record");
		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("periodStartDate", periodStartDate);
		super.fillInputBoxIn("periodEndDate", periodEndDate);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("confirmation", "true");
		super.clickOnSubmit("Create a correctional auditing record");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create an auditing record for an audit as a different auditor and other roles

		Collection<Audit> audits;
		String param;

		audits = this.repository.findManyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits) {
			param = String.format("masterId=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/auditing-record/create-correction", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/auditing-record/create-correction", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/auditing-record/create-correction", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/auditor/auditing-record/create-correction", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	public void test301Hacking() {

		Collection<Audit> audits;
		String param;

		super.checkLinkExists("Sign in");
		super.signIn("auditor1", "auditor1");
		audits = this.repository.findManyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits)
			if (audit.isDraftMode()) {
				param = String.format("masterId=%d", audit.getId());
				super.request("/auditor/auditing-record/create-correction", param);
				super.checkPanicExists();
			}
	}
}
