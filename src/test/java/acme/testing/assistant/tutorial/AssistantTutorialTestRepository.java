
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.Course;
import acme.entities.Tutorial;
import acme.framework.repositories.AbstractRepository;

public interface AssistantTutorialTestRepository extends AbstractRepository {

	@Query("SELECT t FROM Tutorial t WHERE t.assistant.userAccount.username = :username")
	Collection<Tutorial> findManyTutorialsByAssistantUsername(String username);

	@Query("SELECT t FROM Tutorial t WHERE t.code = :code")
	Tutorial findOneTutorialByCode(String code);

	@Query("SELECT t FROM Tutorial t WHERE t.id = :id")
	Tutorial findOneTutorialById(int id);

	@Query("select c from Course c where c.code = :code")
	Course findOneCourseByCourseCode(String code);
}
