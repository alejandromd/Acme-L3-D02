
package acme.features.lecturer.course;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Course;
import acme.framework.controllers.AbstractController;
import acme.roles.Lecturer;

@Controller
public class CourseOfLectureController extends AbstractController<Lecturer, Course> {

	@Autowired
	protected CourseOfLectureService		showService;

	@Autowired
	protected CourseOfLectureFindAllService	listAllService;

	@Autowired
	protected CourseOfLectureCreateService	createService;

	@Autowired
	protected CourseOfLectureDeleteService	deleteService;

	@Autowired
	protected CourseOfLectureUpdateService	updateService;

	@Autowired
	protected CourseOfLecturePostService	postService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listAllService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.postService);
	}

}
