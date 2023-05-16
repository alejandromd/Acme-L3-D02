
package acme.testing.lecturer.course;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.Course;
import acme.framework.repositories.AbstractRepository;

public interface LecturerCourseTestRepository extends AbstractRepository {

	@Query("select c from Course c where c.lecturer.userAccount.username = :username")
	Collection<Course> findCoursesByUsername(String username);

	@Query("select c from Course c where c.code = :code")
	Course findCourseByCode(String code);

	@Query("select c from Course c where c.id = :id")
	Course findCourseById(int id);

}
