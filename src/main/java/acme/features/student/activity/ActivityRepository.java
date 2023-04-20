
package acme.features.student.activity;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Activity;
import acme.entities.Enrolment;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface ActivityRepository extends AbstractRepository {

	@Query("select a from Activity a where a.enrolment.id = :id")
	Collection<Activity> findActivitiesByEnrolmentId(int id);

	@Query("select a from Activity a where a.id = :id")
	Activity findActivityById(int id);

	@Query("select e from Enrolment e where e.id = :id")
	Enrolment findEnrolmentById(int id);

	@Query("select a.enrolment from Activity a where a.id = :id")
	Enrolment findEnrolmentByActivityId(int id);
}
