
package acme.features.administrator.spamConfig;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.SpamConfig;
import acme.framework.components.accounts.Administrator;
import acme.framework.controllers.AbstractController;

@Controller
public class SpamConfigController extends AbstractController<Administrator, SpamConfig> {

	@Autowired
	protected AdministratorSpamConfigUpdateService	updateService;

	@Autowired
	protected AdministratorSpamConfigListService	listService;

	@Autowired
	protected AdministratorSpamConfigShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
