
package acme.roles;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import acme.entities.Qualifications;
import acme.framework.data.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Lecturer extends AbstractRole {

	// Serialisation identifier -----------------------------------------------

	protected static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Size(min = 0, max = 76)
	protected String				almaMater;

	@NotBlank
	@Size(min = 0, max = 101)
	protected String				resume;

	protected String				link;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
	@ManyToMany(mappedBy = "lecturers")
	@NotBlank
	protected List<Qualifications>	qualifications;

}
