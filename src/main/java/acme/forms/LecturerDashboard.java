
package acme.forms;

import java.util.Map;

import acme.datatypes.LectureType;
import acme.datatypes.Statistic;
import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LecturerDashboard extends AbstractForm {

	protected static final long	serialVersionUID	= 1L;

	Map<LectureType, Integer>	numLecturesType;
	Statistic					statisticLectures;
	Statistic					statisticCourses;

}
