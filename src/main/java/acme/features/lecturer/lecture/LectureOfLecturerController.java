
package acme.features.lecturer.lecture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Lecture;
import acme.framework.controllers.AbstractController;
import acme.roles.Lecturer;

@Controller
public class LectureOfLecturerController extends AbstractController<Lecturer, Lecture> {

	@Autowired
	protected LectureOfLecturerListService		listService;

	@Autowired
	protected LectureOfLecturerShowService		showService;

	@Autowired
	protected LectureOfLecturerFindAllService	listAllService;

	@Autowired
	protected LectureOfLecturerCreateService	createService;

	@Autowired
	protected LectureOfLecturerUpdateService	updateService;

	@Autowired
	protected LectureOfLecturerPostService		publishService;

	@Autowired
	protected LectureOfLecturerDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addCustomCommand("list-all", "list", this.listAllService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);

	}
}
