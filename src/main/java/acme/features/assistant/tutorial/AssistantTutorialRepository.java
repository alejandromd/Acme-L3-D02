
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Course;
import acme.entities.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantTutorialRepository extends AbstractRepository {

	@Query("SELECT t FROM Tutorial t WHERE t.assistant.id = :assistantId")
	Collection<Tutorial> findManyTutorialsByAssistantId(int assistantId);

	@Query("SELECT t FROM Tutorial t WHERE t.id = :tutorialId")
	Tutorial findOneTutorialById(int tutorialId);

	@Query("SELECT t FROM Tutorial t WHERE t.code = :code")
	Tutorial findOneTutorialByCode(String code);

	@Query("SELECT c FROM Course c WHERE c.draftMode = 0")
	Collection<Course> findAllPublishedCourses();

	@Query("SELECT c FROM Course c WHERE c.id = :courseId")
	Course findOneCourseById(int courseId);

	@Query("SELECT a FROM Assistant a WHERE a.id = :assistantId")
	Assistant findOneAssistantById(int assistantId);

	@Query("SELECT s FROM TutorialSession s WHERE s.tutorial.id = :tutorialId")
	Collection<TutorialSession> findManySessionsByTutorialId(int tutorialId);
}
