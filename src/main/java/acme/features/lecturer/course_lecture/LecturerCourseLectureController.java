
package acme.features.lecturer.course_lecture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.CourseLecture;
import acme.framework.controllers.AbstractController;
import acme.roles.Lecturer;

@Controller
public class LecturerCourseLectureController extends AbstractController<Lecturer, CourseLecture> {

	@Autowired
	protected LecturerCourseLectureCreateService	createService;

	@Autowired
	protected LecturerCourseLectureDeleteService	deleteService;

	@Autowired
	protected LecturerCourseLectureListService		listService;

	@Autowired
	LecturerCourseLectureShowService				showService;


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("add", "create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
