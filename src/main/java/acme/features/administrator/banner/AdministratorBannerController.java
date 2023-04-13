
package acme.features.administrator.banner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Banner;
import acme.framework.components.accounts.Administrator;
import acme.framework.controllers.AbstractController;

@Controller
public class AdministratorBannerController extends AbstractController<Administrator, Banner> {

	@Autowired
	protected AdministratorBannerServiceFindAll	findAll;

	@Autowired
	protected AdministratorBannerService		showDetails;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.findAll);
		super.addBasicCommand("show", this.showDetails);
	}

}
