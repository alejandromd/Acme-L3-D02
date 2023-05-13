
package acme.features.lecturer.course_lecture;

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

	@Query("select cl from CourseLecture cl where cl.course.id = :courseId and cl.lecture.id = :lectureId")
	CourseLecture findOneCourseLectureByIds(int courseId, int lectureId);

	@Query("select cl from CourseLecture cl where cl.course.id = :courseId")
	Collection<CourseLecture> findCourseLectureByCourseId(int courseId);

	@Query("select cl from CourseLecture cl where cl.id = :courseLectureId")
	CourseLecture findOneLectureCourseById(int courseLectureId);

	@Query("select l from Lecture l where l.lecturer.id = :lecturerId")
	Collection<Lecture> findLecturesByLecturerId(int lecturerId);

}
