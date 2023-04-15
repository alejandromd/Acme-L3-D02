
package acme.features.student.dashboard;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.datatypes.Nature;
import acme.entities.Enrolment;
import acme.framework.components.accounts.UserAccount;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;

@Repository
public interface StudentDashboardRepository extends AbstractRepository {

	@Query("select s from Student s where s.userAccount.id = :id")
	Student findOneStudentByUserAccountId(int id);

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select e from Enrolment e where e.student = :student")
	Enrolment findOneEnrolmentByStudent(Student student);

	@Query("select count(a) from Activity a where a.enrolment = :enrolment and a.activtyType = :activityType")
	Optional<Integer> findNumOfActivitiesByType(Enrolment enrolment, Nature activityType);

	@Query("select max(a.period) from Activity a where a.enrolment = :enrolment")
	Optional<Double> findMaxActivityPeriod(Enrolment enrolemnt);

	@Query("select min(a.period) from Activity a where a.enrolment = :enrolment")
	Optional<Double> findMinActivityPeriod(Enrolment enrolment);

	@Query("select stddev(a.period) from Activity a where a.enrolment = :enrolment")
	Optional<Double> findLinearDevActivityPeriod(Enrolment enrolemnt);

	@Query("select avg(a.period) from Activity a where a.enrolment = :enrolment")
	Optional<Double> findAverageActivityPeriod(Enrolment enrolment);

}
