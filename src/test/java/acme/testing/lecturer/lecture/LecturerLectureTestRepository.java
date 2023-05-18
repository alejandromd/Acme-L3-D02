
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.Course;
import acme.entities.Lecture;
import acme.framework.repositories.AbstractRepository;

public interface LecturerLectureTestRepository extends AbstractRepository {

	@Query("select l from Lecture l where l.lecturer.userAccount.username = :username")
	Collection<Lecture> findLecturesByUsername(String username);

	@Query("select l from Lecture l where l.id = :id")
	Lecture findLectureById(int id);

	@Query("select c from Course c where c.code = :code")
	Course findCourseByCode(String code);

	@Query("select l from Lecture l where l.lecturer.userAccount.username = :username and l.draftMode = true")
	Collection<Lecture> findLecturesByUsernameInDraftMode(String username);
}
