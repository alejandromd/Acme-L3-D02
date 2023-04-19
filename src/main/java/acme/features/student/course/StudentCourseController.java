
package acme.features.student.course;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Course;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

@Controller
public class StudentCourseController extends AbstractController<Student, Course> {

	@Autowired
	protected StudentCourseFindAllService	findAllService;

	@Autowired
	protected StudentCourseService			showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.findAllService);
		super.addBasicCommand("show", this.showService);
	}

}
