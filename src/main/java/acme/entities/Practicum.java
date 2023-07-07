
package acme.entities;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.framework.data.AbstractEntity;
import acme.roles.Company;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "code")
})
public class Practicum extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@NotBlank
	@Pattern(regexp = "[A-Z]{1,3}\\d{3}")
	@Column(unique = true)
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			summary;

	@NotBlank
	@Length(max = 100)
	protected String			goals;

	protected Boolean			draftMode;

	// Derived attributes -----------------------------------------------------


	public Double estimatedTime(final Collection<PracticumSession> sessions) {
		double estimatedTime = 0;
		if (!sessions.isEmpty())
			for (final PracticumSession session : sessions) {
				final long diffM = session.getFinalPeriod().getTime() - session.getInitialPeriod().getTime();
				final double diffH = diffM / (1000.0 * 60 * 60);
				estimatedTime = estimatedTime + diffH;
			}
		return estimatedTime;
	}
	// Relationships ----------------------------------------------------------


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	protected Company	company;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Course	course;
}
