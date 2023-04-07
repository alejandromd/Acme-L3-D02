
package acme.features.auditor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Audit;
import acme.framework.controllers.AbstractController;
import acme.roles.Auditor;

@Controller
public class AuditorAuditController extends AbstractController<Auditor, Audit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditListService	listService;

	@Autowired
	protected AuditorAuditShowService	showService;

	@Autowired
	protected AuditorAuditCreateService	createService;

	@Autowired
	protected AuditorAuditDeleteService	deleteService;

	@Autowired
	protected AuditorAuditUpdateService	updateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.showService);
		super.addBasicCommand("delete", this.showService);
		super.addBasicCommand("update", this.showService);

	}

}
