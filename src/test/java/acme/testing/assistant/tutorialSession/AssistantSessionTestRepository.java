
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.repositories.AbstractRepository;

public interface AssistantSessionTestRepository extends AbstractRepository {

	@Query("SELECT t FROM Tutorial t WHERE t.assistant.userAccount.username = :username")
	Collection<Tutorial> findManyTutorialsByAssistantUsername(String username);

	@Query("SELECT s FROM TutorialSession s WHERE s.tutorial.assistant.useAccount.username = :username")
	Collection<TutorialSession> findManySessionsByAssistantUsername(String username);

}
