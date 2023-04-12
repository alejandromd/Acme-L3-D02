
package acme.features.student.enrolment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Enrolment;
import acme.framework.components.accounts.Authenticated;
import acme.framework.controllers.AbstractController;

@Controller
public class EnrolmentController extends AbstractController<Authenticated, Enrolment> {

	@Autowired
	protected EnrolmentServiceFindAll	findAll;

	@Autowired
	protected EnrolmentService			showDetails;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.findAll);
		super.addBasicCommand("show", this.showDetails);
	}
}
