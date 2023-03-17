
package acme.forms;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDashboard extends AbstractForm {

	protected static final long	serialVersionUID	= 1L;

	protected Integer			totalNumberOfPractica;
	protected Double			averageLengthOfSessionsPractica;
	protected Double			deviationLengthOfSessionsPractica;
	protected Double			minimumLengthOfSessionsPractica;
	protected Double			maximunLengthOfSessionsPractica;
	protected Double			averageLengthOfPractica;
	protected Double			deviationLengthOfPractica;
	protected Double			minimumLengthOfPractica;
	protected Double			maximunLengthOfPractica;

}
