
package acme.forms;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistantDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Integer						numberOfTutorials;

	Double						averageTimeOfSessions;
	Double						deviationTimeOfSessions;
	Double						minimumTimeOfSessions;
	Double						maximumTimeOfSessions;

	Double						averageTimeOfTutorials;
	Double						deviationTimeOfTutorials;
	Double						minimumTimeOfTutorials;
	Double						maximumTimeOfTutorials;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
