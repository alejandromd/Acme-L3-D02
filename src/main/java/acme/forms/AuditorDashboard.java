
package acme.forms;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Double						averageNumberOfAuditingRecordsAudit;
	Double						deviationNumberOfAuditingRecordsAudit;
	Double						minimunNumberOfAuditingRecordsAudit;
	Double						maximunNumberOfAuditingRecordsAudit;
	Double						averageTimeOfPeriodLengthsAuditingRecords;
	Double						deviationTimeOfPeriodLengthsAuditingRecords;
	Double						minimunTimeOfPeriodLengthsAuditingRecords;
	Double						maximunTimeOfPeriodLengthsAuditingRecords;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
