
package acme.forms;

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

}
