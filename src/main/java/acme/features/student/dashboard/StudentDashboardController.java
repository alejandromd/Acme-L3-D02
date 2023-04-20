
package acme.features.student.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.forms.StudentDashboard;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

@Controller
public class StudentDashboardController extends AbstractController<Student, StudentDashboard> {

	@Autowired
	protected StudentDashboardService showDashboard;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showDashboard);
	}

}
