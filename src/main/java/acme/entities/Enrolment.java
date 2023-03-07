
package acme.entities;

import java.time.Duration;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Enrolment extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	protected static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Pattern(regexp = "[A-Z]{1,3}[0-9][0-9]{3}")
	@NotBlank
	@Column(unique = true)
	protected String				code;

	@NotBlank
	@Length(max = 75)
	protected String				motivation;

	@NotBlank
	@Length(max = 100)
	protected String				goals;

	protected Duration				workTime;

	// Derived attributes -----------------------------------------------------
	//	public void setWorkTime() {
	//		Collection<Activity> a = Activity.
	//		
	//	}

	// Relationships ----------------------------------------------------------
	@OneToMany(mappedBy = "workbook")
	protected Collection<Activity>	workbook;

}
