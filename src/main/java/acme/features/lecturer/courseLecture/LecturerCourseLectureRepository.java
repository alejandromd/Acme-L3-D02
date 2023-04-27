
package acme.features.lecturer.courseLecture;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Course;
import acme.entities.CourseLecture;
import acme.entities.Lecture;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

@Repository
public interface LecturerCourseLectureRepository extends AbstractRepository {

	@Query("select l from Lecturer l where l.id = :id")
	Lecturer findLecturerById(int id);

	@Query("select l from Lecture l where l.id = :id")
	Lecture findLectureById(int id);

	@Query("select c from Course c where c.id = :id")
	Course findCourseById(int id);

	@Query("select c from Course c inner join CourseLecture cl on c = cl.course inner join Lecture l on cl.lecture = l where l = :lecture and c.draftMode = true")
	Collection<Course> findCourseByLecture(Lecture lecture);

	@Query("select c from Course c where c.draftMode = true and c.lecturer = :lecturer")
	Collection<Course> findCoursesByLecturer(Lecturer lecturer);

	@Query("select l from Lecture l inner join CourseLecture cl on l = cl.lecture inner join Course c on cl.course = c where c.lecturer = :lecturer")
	Collection<Lecture> findLecturesByLecturer(Lecturer lecturer);

	@Query("select l from Lecture l inner join CourseLecture cl on l = cl.lecture inner join Course c on cl.course = c where c.id = :id")
	Collection<Lecture> findLecturesByCourse(int id);

	@Query("select cl from CourseLecture cl where cl.lecture = :lecture and cl.course = :course ")
	CourseLecture findCourseLectureByLectureAndCourse(Course course, Lecture lecture);

}
