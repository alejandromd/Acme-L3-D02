
package acme.entities;

import java.time.Duration;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Transient;

import acme.datatypes.Nature;
import acme.framework.data.AbstractEntity;
import acme.framework.helpers.MomentHelper;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Activity extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			summary;

	@NotNull
	protected Nature			activityType;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				startPeriod;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				endPeriod;

	@URL
	protected String			link;

	protected Boolean			draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Integer period() {
		final Duration d = MomentHelper.computeDuration(this.startPeriod, this.endPeriod);
		return (int) d.toHours();

	}


	// Relationships ----------------------------------------------------------
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Enrolment enrolment;
}
