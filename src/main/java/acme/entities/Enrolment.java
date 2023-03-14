
package acme.entities;

import java.time.Duration;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.framework.data.AbstractEntity;
import acme.framework.helpers.MomentHelper;
import acme.roles.Student;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Enrolment extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Pattern(regexp = "[A-Z]{1,3}[0-9][0-9]{3}")
	@NotBlank
	@Column(unique = true)
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			motivation;

	@NotBlank
	@Length(max = 100)
	protected String			goals;

	@NotNull
	protected List<Activity>	workbook;


	// Derived attributes -----------------------------------------------------
	@Transient
	public Double workTime() {
		final List<Activity> ls = this.workbook;
		double res = 0.;
		for (final Activity a : ls) {
			final Duration d = MomentHelper.computeDuration(a.getStartPeriod(), a.getEndPeriod());
			final double horas = (double) d.toMinutes() / 60;
			res += horas;
		}
		return res;
	}


	// Relationships ----------------------------------------------------------
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Course	course;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Student	student;
}
