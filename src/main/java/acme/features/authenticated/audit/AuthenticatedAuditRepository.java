
package acme.features.authenticated.audit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Audit;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedAuditRepository extends AbstractRepository {

	@Query("select a from Audit a where a.course.id = :courseId")
	Collection<Audit> findAuditsByCourse(int courseId);

	@Query("select a from Audit a where a.id = :id")
	Audit findOneAuditById(int id);

}
