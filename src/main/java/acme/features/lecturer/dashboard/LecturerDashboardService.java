
package acme.features.lecturer.dashboard;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.Nature;
import acme.forms.LecturerDashboard;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerDashboardService extends AbstractService<Lecturer, LecturerDashboard> {

	@Autowired
	protected LecturerDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		final LecturerDashboard dashboard = new LecturerDashboard();

		Principal principal;
		int userAccountId;
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		final Lecturer lecturer = this.repository.findOneLecturerByUserAccountId(userAccountId);

		final double averageLectureLearningTime = this.repository.findAverageLectureLearningTime(lecturer).orElse(0.0);
		final double maxLectureLearningTime = this.repository.findMaxLectureLearningTime(lecturer).orElse(0.0);
		final double minLectureLearningTime = this.repository.findMinLectureLearningTime(lecturer).orElse(0.0);
		final double devLectureLearningTime = this.repository.findLinearDevLectureLearningTime(lecturer).orElse(0.0);

		dashboard.setAverageTimeOfLectures(averageLectureLearningTime);
		dashboard.setMinimumTimeOfLectures(minLectureLearningTime);
		dashboard.setMaximumTimeOfLectures(maxLectureLearningTime);
		dashboard.setDeviationTimeOfLectures(devLectureLearningTime);

		final Collection<Double> courseEstimatedLearningTime = this.repository.findEstimatedLearningTimeByCourse(lecturer);
		dashboard.calculateCourseAverage(courseEstimatedLearningTime);
		dashboard.calculateCourseMax(courseEstimatedLearningTime);
		dashboard.calculateCourseMin(courseEstimatedLearningTime);
		dashboard.calculateCourseDev(courseEstimatedLearningTime);

		//total lectures
		final Integer handsOnLectures = this.repository.findNumOfLecturesByType(lecturer, Nature.HANDS_ON).orElse(0);
		final Integer theoreticalLectures = this.repository.findNumOfLecturesByType(lecturer, Nature.THEORETICAL).orElse(0);

		dashboard.setTotalLectures(handsOnLectures + theoreticalLectures);

		super.getBuffer().setData(dashboard);
	}

	@Override
	public void unbind(final LecturerDashboard object) {
		final Tuple tuple = super.unbind(object, "totalLectures", "averageTimeOfLectures", "deviationTimeOfLectures", "minimumTimeOfLectures", "maximumTimeOfLectures", "averageTimeOfCourses", "deviationTimeOfCourses", "minimumTimeOfCourses",
			"maximumTimeOfCourses");

		super.getResponse().setData(tuple);
	}

}
