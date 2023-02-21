
package acme.roles;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import acme.framework.data.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Auditor extends AbstractRole {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Size(min = 0, max = 76)
	protected String			firm;

	@NotBlank
	@Size(min = 0, max = 26)
	protected String			professionalId;

	protected String			link;

	// Relationships ----------------------------------------------------------

	@NotBlank
	@Size(min = 0, max = 101)
	@ManyToMany(mappedBy = "auditors")
	protected List<String>		certifications;

}
