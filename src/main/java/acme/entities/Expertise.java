
package acme.entities;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Expertise extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Size(max = 100)
	protected String			expertise;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
