
package acme.features.authenticated.auditor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuditorDashboardRepository extends AbstractRepository {

	@Query("select sum(sqrt(power((select count(ar) from AuditingRecord ar where ar.au.id = au.id) - (avg(select count(ar) from AuditingRecord ar where ar.au.id = au.id) from Audit au where au.auditor.id = auditor.id)), 2)) / (select count(ar) from AuditingRecord ar where ar.audit.auditor.id = auditor.id) from Audit au where au.auditor.id = auditor.id")
	Double deviationNumberOfAuditingRecordsAudit();

	@Query("select avg(select count(ar) from AuditingRecord ar where ar.au.id = au.id) from Audit au where au.auditor.id = auditor.id")
	Double averageNumberOfAuditingRecordsAudit();

	@Query("select max(select count(ar) from AuditingRecord ar where ar.au.id = au.id) from Audit au where au.auditor.id = auditor.id")
	Double maximunNumberOfAuditingRecordsAudit();

	@Query("select min(select count(ar) from AuditingRecord ar where ar.au.id = au.id) from Audit au where au.auditor.id = auditor.id")
	Double minimunNumberOfAuditingRecordsAudit();

}
