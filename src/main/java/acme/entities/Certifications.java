
package acme.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import acme.framework.data.AbstractEntity;
import acme.roles.Auditor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Certifications extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	protected static final long serialVersionUID = 1L;

	// Attributes -------------------------------------------------------------
	@NotBlank
	@Size(min = 0, max = 101)
	protected String certification;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
	@ManyToMany
	protected List<Auditor> auditors;

}
