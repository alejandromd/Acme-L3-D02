
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

	Integer						totalNumberOfAudits;
	Double						averageNumberOfAuditingRecordsAudit;
	Double						deviationNumberOfAuditingRecordsAudit;
	Integer						minimunNumberOfAuditingRecordsAudit;
	Integer						maximunNumberOfAuditingRecordsAudit;
	Double						averageTimeOfPeriodLengthsAuditingRecords;
	Double						deviationTimeOfPeriodLengthsAuditingRecords;
	Double						minimunTimeOfPeriodLengthsAuditingRecords;
	Double						maximunTimeOfPeriodLengthsAuditingRecords;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
