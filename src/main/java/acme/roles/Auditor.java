
package acme.roles;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
public class Auditor extends AbstractRole {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------


	@Length(max = 75)
	protected String			firm;

	@NotBlank
	@Length(max = 25)
	protected String			professionalId;

	@NotBlank
	@Length(max = 100)
	protected String			certifications;

	@URL
	protected String			link;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
  @NotBlank
	@Size(min = 0, max = 101)
	@ManyToMany(mappedBy = "auditors")
	protected List<String>		certifications;


}
