
package acme.roles;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.data.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Student extends AbstractRole {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Size(min = 0, max = 76)
	protected String			statement;

	@URL
	protected String			link;

	@Length(max = 101)
	@NotBlank
	protected String			strongFeatures;

	@Length(max = 101)
	@NotBlank
	protected String			weakFeatures;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
