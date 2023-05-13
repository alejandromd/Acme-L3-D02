
package acme.features.lecturer.lecture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Lecture;
import acme.framework.controllers.AbstractController;
import acme.roles.Lecturer;

@Controller
public class LecturerLectureController extends AbstractController<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureShowService	showService;

	@Autowired
	protected LecturerLectureFindAllService	listAllService;

	@Autowired
	protected LecturerLectureCreateService	createService;

	@Autowired
	protected LecturerLectureUpdateService	updateService;

	@Autowired
	protected LecturerLecturePostService	publishService;

	@Autowired
	protected LecturerLectureDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addCustomCommand("list-all", "list", this.listAllService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);

	}
}
