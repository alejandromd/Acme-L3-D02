
package acme.features.student.dashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Activity;
import acme.entities.Course;
import acme.entities.Lecture;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;

@Repository
public interface StudentDashboardRepository extends AbstractRepository {

	@Query("select s from Student s where s.userAccount.id = :id")
	Student findStudentByUserAccountId(int id);

	@Query("select a from Activity a where a.enrolment.student = :student")
	Collection<Activity> findActivitiesByStudent(Student student);

	@Query("select e.course from Enrolment e where e.student = :student")
	Collection<Course> findEnrolledCoursesByStudent(Student student);

	@Query("select l from Lecture l inner join CourseLecture cl on l = cl.lecture inner join Course c on cl.course = c where c.id = :id")
	Collection<Lecture> findLecturesByCourse(int id);

}
