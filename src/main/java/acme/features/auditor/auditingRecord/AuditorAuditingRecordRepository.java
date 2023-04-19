
package acme.features.auditor.auditingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Audit;
import acme.entities.auditingRecord.AuditingRecord;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuditorAuditingRecordRepository extends AbstractRepository {

	@Query("select a from Audit a where a.id = :id")
	Audit findOneAuditById(int id);

	@Query("select ar from AuditingRecord ar where ar.audit.id = :id")
	Collection<AuditingRecord> findManyAuditingRecordsByMasterId(int id);

	@Query("select ar.audit from AuditingRecord ar where ar.id = :id")
	Audit findOneAuditByAuditingRecordId(int id);

	@Query("select ar from AuditingRecord ar where ar.id = :id")
	AuditingRecord findOneAuditingRecordById(int id);

	@Query("select sc.threshold from SpamConfig sc")
	Double findThreshold();

}
