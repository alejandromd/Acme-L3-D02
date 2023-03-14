
package acme.entities.auditingRecord;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AuditingRecord extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Length(max = 75)
	protected String			subject;

	@NotBlank
	@Length(max = 100)
	protected String			assessment;

	@Temporal(TemporalType.TIMESTAMP)
	@NotBlank
	@Past
	protected Date				periodStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@NotBlank
	@Past
	protected Date				periodEndDate;

	@NotNull
	protected Mark				mark;

	@URL
	protected String			link;


	// Derived attributes -----------------------------------------------------
	public Double duration() {

		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime(this.periodEndDate);
		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime(this.periodStartDate);

		//calculate difference in milliseconds and then we transform it to hours.
		final long differenceInMilliseconds = cal1.getTimeInMillis() - cal2.getTimeInMillis();
		final double differenceHours = differenceInMilliseconds / (1000.0 * 60 * 60);

		return differenceHours;
	}


	// Relationships ----------------------------------------------------------
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	protected Audit audit;
}
