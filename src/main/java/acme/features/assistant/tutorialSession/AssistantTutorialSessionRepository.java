
package acme.features.assistant.tutorialSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AssistantTutorialSessionRepository extends AbstractRepository {

	@Query("SELECT t FROM Tutorial t WHERE t.id = :tutorialId")
	Tutorial findOneTutorialById(int tutorialId);

	@Query("SELECT s FROM TutorialSession s WHERE s.tutorial.id = :tutorialId")
	Collection<TutorialSession> findManySessionsByTutorialId(int tutorialId);

	@Query("SELECT s.tutorial FROM TutorialSession s WHERE s.id = :sessionId")
	Tutorial findOneTutorialBySessionId(int sessionId);

	@Query("SELECT s FROM TutorialSession s WHERE s.id = :sessionId")
	TutorialSession findOneSessionById(int sessionId);

	@Query("SELECT sc.threshold FROM SpamConfig sc")
	Double findThreshold();

}
