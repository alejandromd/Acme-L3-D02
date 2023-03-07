
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;

import acme.framework.components.accounts.Authenticated;
import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Note extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Past
	protected Date				instantiationMoment;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			message;

	@Email
	protected String			email;


	// Derived attributes -----------------------------------------------------
	@NotBlank
	@Length(max = 75)
	public String author() {
		final String res = "<" + +"> - <" + +"," + +">";
		return res;
	}


	// Relationships ----------------------------------------------------------
	@ManyToOne
	protected Authenticated authenticated;
}
