
package acme.testing.auditor.audit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.Audit;
import acme.entities.Course;

public interface AuditorAuditTestRepository {

	@Query("select au from Audit au where au.auditor.userAccount.username = :username")
	Collection<Audit> findManyAuditsByAuditorUsername(String username);

	@Query("select c from Course c where c.code = :code")
	Course findOneCourseByCourseCode(String code);

}
