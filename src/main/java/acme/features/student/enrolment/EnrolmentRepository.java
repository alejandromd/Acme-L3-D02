
package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Enrolment;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface EnrolmentRepository extends AbstractRepository {

	@Query("select e from Enrolment e where e.id = :id")
	Enrolment findEnrolmentById(int id);

	@Query("select e from Enrolment e where e.student.userAccount.id = :id")
	Collection<Enrolment> findEnrolmentsByStudentId(int id);

}
