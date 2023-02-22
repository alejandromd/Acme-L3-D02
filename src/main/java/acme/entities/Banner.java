
package acme.entities;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Banner extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Past
	protected Date				moment;

	@Past
	@Temporal(value = TemporalType.TIMESTAMP)
	protected Date				displayPeriod;

	@URL
	protected String			picture;

	@Length(max = 75)
	@NotBlank
	protected String			slogan;

	@URL
	protected String			linkWeb;


	public void setDisplayPeriod(final Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		if (date.after(calendar.getTime()))
			this.displayPeriod = date;
	}
}
