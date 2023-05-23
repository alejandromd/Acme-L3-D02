
package acme.testing.student.enrolment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.Activity;
import acme.entities.Enrolment;
import acme.framework.repositories.AbstractRepository;

public interface StudentEnrolmentTestRepository extends AbstractRepository {

	@Query("select e from Enrolment e where e.student.userAccount.username = :username")
	Collection<Enrolment> findManyEnrolemntsByStudentUsername(String username);

	@Query("select a from Activity a where a.enrolment.student.userAccount.username = :username")
	Collection<Activity> findManyActivitiesByStudentUsername(String username);

	@Query("select e from Enrolment e where e.code = :code")
	Enrolment findEnrolmentByCode(String code);

}
