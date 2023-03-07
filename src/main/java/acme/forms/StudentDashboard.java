
package acme.forms;

import java.util.Map;

import acme.datatypes.ActivityType;
import acme.datatypes.Statistic;
import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDashboard extends AbstractForm {

	protected static final long	serialVersionUID	= 1L;

	Map<ActivityType, Integer>	numActivityType;
	Statistic					statisticActivities;
	Statistic					statisticEnrolment;

}
