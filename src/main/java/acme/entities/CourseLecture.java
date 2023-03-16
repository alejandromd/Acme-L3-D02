
package acme.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CourseLecture extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@ManyToOne(optional = false)
	@NotNull
	@Valid
	protected Course			course;

	@ManyToOne(optional = false)
	@NotNull
	@Valid
	protected Lecture			lecture;

}
