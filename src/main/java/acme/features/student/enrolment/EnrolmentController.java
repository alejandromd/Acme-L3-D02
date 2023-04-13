
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
	EnrolmentDeleteService				deleteEnrolment;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.findAll);
		super.addBasicCommand("show", this.showDetails);
		super.addBasicCommand("create", this.createEnrolment);
		super.addBasicCommand("update", this.createEnrolment);
		super.addBasicCommand("delete", this.createEnrolment);

	}
}
