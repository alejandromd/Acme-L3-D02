
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Audit;
import acme.entities.auditingRecord.AuditingRecord;
import acme.testing.TestHarness;

public class AuditorAuditPublishTest extends TestHarness {

	// Internal data ----------------------------------------------------------

	@Autowired
	protected AuditorAuditTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code) {
		// HINT: this test authenticates as an employer, lists his or her jobs,
		// HINT: then selects one of them, and publishes it.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Audits");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, code);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@Test
	public void test200Negative() {

	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to publish a job with a role other than "Employer".

		Collection<Audit> audits;
		String params;

		audits = this.repository.findManyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits)
			if (audit.isDraftMode()) {
				params = String.format("id=%d", audit.getId());

				super.checkLinkExists("Sign in");
				super.request("/auditor/audit/publish", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/auditor/audit/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/auditor/audit/publish", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a published audit that was registered by the principal.

		Collection<Audit> audits;
		String params;

		super.signIn("auditor1", "auditor1");
		audits = this.repository.findManyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits)
			if (!audit.isDraftMode()) {
				params = String.format("id=%d", audit.getId());
				super.request("/auditor/audit/publish", params);
				super.checkPanicExists();
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish an audit that wasn't registered by the principal,
		// HINT+ be it published or unpublished.

		Collection<Audit> audits;
		String params;

		super.signIn("auditor2", "auditor2");
		audits = this.repository.findManyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits) {
			params = String.format("id=%d", audit.getId());
			super.request("/auditor/audit/publish", params);
			super.checkPanicExists();
		}
		super.signOut();
	}
	@Test
	public void test303Hacking() {
		// HINT: this test tries to publish an audit that wasn't registered by the principal,
		// HINT+ be it published or unpublished.

		Collection<Audit> audits;
		String params;

		super.signIn("auditor1", "auditor1");
		audits = this.repository.findManyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits) {
			final Collection<AuditingRecord> auditingRecords = this.repository.findManyAuditingRecordsByAuditId(audit.getId());
			if (auditingRecords.isEmpty()) {
				params = String.format("id=%d", audit.getId());
				super.request("/auditor/audit/publish", params);
				super.checkPanicExists();
			}
		}
		super.signOut();
	}

}
