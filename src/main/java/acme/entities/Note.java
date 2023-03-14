
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

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
	@NotNull
	protected Date				instantiationMoment;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			message;

	@Email
	protected String			email;

	/*
	 * Se computa con la siguiente estructura "<username>-<surname,name>" siendo
	 * username el nombre de usuario de la persona que escribe la nota y surname
	 * name su respectivos nombre y apellidos este campo no se puede modificar
	 * una vez computado
	 */
	@NotBlank
	@Length(max = 75)
	protected String			author;

	@URL
	protected String			link;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
