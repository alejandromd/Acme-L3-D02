
package acme.features.authenticated.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedTutorialRepository extends AbstractRepository {

	@Query("SELECT t FROM Tutorial t WHERE t.course.id = :courseId and t.draftMode = false")
	Collection<Tutorial> findTutorialsByCourseId(int courseId);

	@Query("SELECT t FROM Tutorial t WHERE t.id = :tutorialId")
	Tutorial findTutorialById(int tutorialId);

	@Query("SELECT s FROM TutorialSession s WHERE s.tutorial.id = :tutorialId")
	Collection<TutorialSession> findManySessionsByTutorialId(int tutorialId);
}
