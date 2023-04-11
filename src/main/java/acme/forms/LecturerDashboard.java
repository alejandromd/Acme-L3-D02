
package acme.forms;

import java.util.Collection;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LecturerDashboard extends AbstractForm {

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Integer						totalLectures;

	Double						averageTimeOfLectures;
	Double						deviationTimeOfLectures;
	Double						minimumTimeOfLectures;
	Double						maximumTimeOfLectures;

	Double						averageTimeOfCourses;
	Double						deviationTimeOfCourses;
	Double						minimumTimeOfCourses;
	Double						maximumTimeOfCourses;


	public void calcAverage(final Collection<Double> values) {
		Double res;
		res = 0.0;
		if (!values.isEmpty()) {
			final Double total = values.stream().mapToDouble(Double::doubleValue).sum();
			res = total / values.size();
		}
		this.averageTimeOfCourses = res;
	}

	public void calcMax(final Collection<Double> values) {
		final Double max = values.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
		this.maximumTimeOfCourses = max;
	}

	public void calcMin(final Collection<Double> values) {
		final Double min = values.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
		this.minimumTimeOfCourses = min;
	}
	public void calcLinDev(final Collection<Double> values) {
		Double res;
		Double aux;
		res = 0.0;
		if (!values.isEmpty()) {
			aux = 0.0;
			for (final Double value : values)
				aux += Math.pow(value + this.averageTimeOfCourses, 2);
			res = Math.sqrt(aux / values.size());
		}
		this.deviationTimeOfCourses = res;

	}

}
