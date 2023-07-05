
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "draftMode")
})
public class PracticumSession extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			summary;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				initialPeriod;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				finalPeriod;

	@URL
	protected String			link;

	protected boolean			addendum;

	protected boolean			draftMode;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Practicum			practicum;

}
