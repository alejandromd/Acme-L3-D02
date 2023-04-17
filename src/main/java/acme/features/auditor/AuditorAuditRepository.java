
package acme.features.auditor;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Audit;
import acme.entities.Course;
import acme.entities.auditingRecord.AuditingRecord;
import acme.entities.auditingRecord.Mark;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Auditor;

@Repository
public interface AuditorAuditRepository extends AbstractRepository {

	@Query("select au from Audit au where au.auditor.userAccount.id = :id")
	Collection<Audit> findAuditsByAuditorId(int id);

	@Query("select au from Audit au where au.id = :id")
	Audit findOneAuditById(int id);

	@Query("select c from Course c where c.id = :courseId")
	Course findOneCourseById(int courseId);

	@Query("select a from Auditor a where a.id = :id")
	Auditor findOneAuditorById(int id);

	@Query("select au from Audit au where au.code = :code")
	Audit findAuditByCode(String code);

	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findCoursesWithoutAudit();

	@Query("select ar from AuditingRecord ar where ar.audit = :audit")
	Collection<AuditingRecord> findAuditingRecordsByAudit(Audit audit);

	@Query("select ar.mark from AuditingRecord ar where ar.audit.id = :id")
	Collection<Mark> findMarkByAuditId(int id);

}
