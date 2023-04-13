
package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Activity;
import acme.entities.Course;
import acme.entities.Enrolment;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;

@Repository
public interface EnrolmentRepository extends AbstractRepository {

	@Query("select e from Enrolment e where e.id = :id")
	Enrolment findEnrolmentById(int id);

	@Query("select e from Enrolment e where e.student.userAccount.id = :id")
	Collection<Enrolment> findEnrolmentsByStudentId(int id);

	@Query("select s from Student s where s.id = :id")
	Student findStudentById(int id);

	@Query("select c from Course c where c.id = :id")
	Course findCourseById(int id);

	@Query("select c from Course c")
	Collection<Course> findAllCourses();

	@Query("select a from Activity a where a.enrolment = :e")
	Collection<Activity> findActivitiesByEnrolment(Enrolment e);

	@Query("select e from Enrolment e where e.code = :code")
	Enrolment findEnrolmentByCode(String code);

}
