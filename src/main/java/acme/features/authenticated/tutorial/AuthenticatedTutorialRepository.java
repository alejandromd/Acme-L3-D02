
package acme.features.authenticated.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Tutorial;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedTutorialRepository extends AbstractRepository {

	@Query("SELECT t FROM Tutorial t WHERE t.course.id = :courseId")
	Collection<Tutorial> findTutorialsByCourseId(int courseId);

	@Query("SELECT t FROM Tutorial t WHERE t.id = :tutorialId")
	Tutorial findTutorialById(int tutorialId);

}
