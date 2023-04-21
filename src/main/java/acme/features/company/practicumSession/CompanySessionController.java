
package acme.features.company.practicumSession;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.PracticumSession;
import acme.framework.controllers.AbstractController;
import acme.roles.Company;

@Controller
public class CompanySessionController extends AbstractController<Company, PracticumSession> {

	@Autowired
	protected CompanySessionListService		listService;

	@Autowired
	protected CompanySessionShowService		showService;

	@Autowired
	protected CompanySessionCreateService	createService;

	@Autowired
	protected CompanySessionUpdateService	updateService;

	@Autowired
	protected CompanySessionDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

	}
}
