
package acme.testing.auditor.auditingRecord;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditingRecord.AuditingRecord;
import acme.testing.TestHarness;

public class AuditorAuditingRecordDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditRecordIndex, final int auditingRecordRecordIndex, final String subject, final String assessment, final String periodStartDate, final String periodEndDate, final String mark, final String link) {
		// HINT: this test authenticates as an auditor, list his or her Audits, navigates
		// HINT+ to their auditing records, and checks that they have the expected data.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(auditRecordIndex);
		super.clickOnButton("Auditing records");

		super.clickOnButton("Create");
		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("periodStartDate", periodStartDate);
		super.fillInputBoxIn("periodEndDate", periodEndDate);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create");

		super.checkListingExists();
		super.clickOnListingRecord(auditingRecordRecordIndex);
		super.clickOnSubmit("Delete");
		super.checkNotPanicExists();

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
			super.request("/auditor/auditing-record/delete", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/auditing-record/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/auditing-record/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/auditor/auditing-record/delete", param);
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

				super.request("/auditor/auditing-record/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
	}
}
