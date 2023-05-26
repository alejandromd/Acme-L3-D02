
package acme.features.student.activity;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Activity;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

@Controller
public class ActivityController extends AbstractController<Student, Activity> {

	@Autowired
	protected ActivityFindAllService	findAll;

	@Autowired
	protected ActivityService			showDetails;

	@Autowired
	protected ActivityCreateService		createActivity;

	@Autowired
	protected ActivityUpdateService		updateActivity;

	@Autowired
	protected ActivityDeleteService		deleteActivity;

	@Autowired
	protected ActivityPublishService	publishActivity;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.findAll);
		super.addBasicCommand("show", this.showDetails);
		super.addBasicCommand("create", this.createActivity);
		super.addBasicCommand("update", this.updateActivity);
		super.addBasicCommand("delete", this.deleteActivity);
		super.addCustomCommand("publish", "update", this.publishActivity);

	}

}
