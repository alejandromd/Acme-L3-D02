
package acme.features.any.course;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Course;
import acme.framework.components.accounts.Any;
import acme.framework.controllers.AbstractController;

@Controller
public class AnyCourseController extends AbstractController<Any, Course> {

	@Autowired
	protected AnyCourseFindAllService	findAllService;

	@Autowired
	protected AnyCourseService			courseService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.courseService);
		super.addBasicCommand("list", this.findAllService);
	}
}
