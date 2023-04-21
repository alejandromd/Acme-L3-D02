
package acme.features.company.practicumSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Practicum;
import acme.entities.PracticumSession;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface CompanySessionRepository extends AbstractRepository {

	@Query("select ts from PracticumSession ts where ts.practicum.id = :id")
	Collection<PracticumSession> findManySessionsByPracticumId(int id);

	@Query("select t from Practicum t where t.id = :id")
	Practicum findOnePracticumById(int id);

	@Query("select ts from PracticumSession ts where ts.id = :id")
	PracticumSession findOneSessionById(int id);

	@Query("Select s From PracticumSession s Where s.practicum.id = :id And s.addendum = true")
	Collection<PracticumSession> findAddendumSessionsByPracticumId(int id);

	@Query("select ts.practicum from PracticumSession ts where ts.id = :id")
	Practicum findOneSessionByPracticumId(int id);

	@Query("Select p From Practicum p Where p.company.id = :id And p.draftMode = false")
	Collection<Practicum> findManyPublishedPracticaByCompanyId(int id);

}
