
package acme.features.authenticated.student;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.framework.components.accounts.Authenticated;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

@Controller
public class StudentController extends AbstractController<Authenticated, Student> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CreateStudentService	createService;

	@Autowired
	protected UpdateStudentService	updateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}

}
