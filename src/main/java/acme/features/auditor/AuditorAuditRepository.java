
package acme.features.auditor;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Audit;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuditorAuditRepository extends AbstractRepository {

	@Query("select a from Audit a where a.auditor.userAccount.id = :id")
	Collection<Audit> findAuditsByAuditorId(int id);

	@Query("select a from Audit a where a.id = :id")
	Audit findOneAuditById(int id);

}
