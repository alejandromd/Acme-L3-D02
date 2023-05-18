
package acme.testing.auditor.auditingRecord;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditingRecord.AuditingRecord;
import acme.testing.TestHarness;

public class AuditorAuditingRecordUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditRecordIndex, final int auditingRecordRecordIndex, final String subject, final String assessment, final String periodStartDate, final String periodEndDate, final String mark, final String link) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(auditRecordIndex);
		super.clickOnButton("Auditing records");
		super.checkListingExists();
		super.clickOnListingRecord(auditingRecordRecordIndex);
		super.checkFormExists();

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("periodStartDate", periodStartDate);
		super.fillInputBoxIn("periodEndDate", periodEndDate);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.checkListingExists();
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
	@CsvFileSource(resources = "/auditor/auditingRecord/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditRecordIndex, final int auditingRecordRecordIndex, final String subject, final String assessment, final String periodStartDate, final String periodEndDate, final String mark, final String link) {
		// HINT: this test attempts to update a job with wrong data.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(auditRecordIndex);
		super.clickOnButton("Auditing records");
		super.checkListingExists();
		super.clickOnListingRecord(auditingRecordRecordIndex);
		super.checkFormExists();

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("periodStartDate", periodStartDate);
		super.fillInputBoxIn("periodEndDate", periodEndDate);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<AuditingRecord> auditingRecords;
		String param;

		auditingRecords = this.repository.findManyAuditingRecordsByAuditorUsername("auditor1");
		for (final AuditingRecord auditingRecord : auditingRecords) {

			param = String.format("id=%d", auditingRecord.getAudit().getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/auditing-record/update", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/auditing-record/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/auditing-record/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/auditor/auditing-record/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {

		Collection<AuditingRecord> auditingRecords;
		String param;

		auditingRecords = this.repository.findManyAuditingRecordsByAuditorUsername("auditor1");
		for (final AuditingRecord auditingRecord : auditingRecords)
			if (!auditingRecord.getAudit().isDraftMode()) {
				super.signIn("auditor1", "auditor1");
				param = String.format("id=%d", auditingRecord.getAudit().getId());

				super.request("/auditor/auditing-record/update", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
