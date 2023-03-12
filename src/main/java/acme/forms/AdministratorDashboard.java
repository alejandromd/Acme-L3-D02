
package acme.forms;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Integer						numberOfAssistants;
	Integer						numberOfAuditors;
	Integer						numberOfCompanies;
	Integer						numberOfConsumers;
	Integer						numberOfProviders;
	Integer						numberOfLecturers;

	Double						ratioOfPeepsWithEmailAndLink;
	Double						ratioOfCriticalBulletins;
	Double						ratioOfNonCriticalBulletins;

	Double						averageCurrentOfferBudget;
	Double						minimumCurrentOfferBudget;
	Double						maximumCurrentOfferBudget;
	Double						deviationCurrentOfferBudget;

	Double						averageNumberOfNotesLastTenWeeks;
	Double						minimumNumberOfNotesLastTenWeeks;
	Double						maximumNumberOfNotesLastTenWeeks;
	Double						deviationNumberOfNotesLastTenWeeks;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
