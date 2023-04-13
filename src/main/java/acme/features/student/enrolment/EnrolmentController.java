
package acme.features.student.enrolment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Enrolment;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

@Controller
public class EnrolmentController extends AbstractController<Student, Enrolment> {

	@Autowired
	protected EnrolmentServiceFindAll	findAll;

	@Autowired
	protected EnrolmentService			showDetails;

	@Autowired
	protected EnrolmentCreateService	createEnrolment;

	@Autowired
	protected EnrolmentUpdateService	updateEnrolment;

	@Autowired
	protected EnrolmentDeleteService	deleteEnrolment;

	@Autowired
	protected EnrolmentFinaliseService	finaliseEnrolment;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.findAll);
		super.addBasicCommand("show", this.showDetails);
		super.addBasicCommand("create", this.createEnrolment);
		super.addBasicCommand("update", this.updateEnrolment);
		super.addBasicCommand("delete", this.deleteEnrolment);
		super.addCustomCommand("finalise", "update", this.finaliseEnrolment);

	}
}
