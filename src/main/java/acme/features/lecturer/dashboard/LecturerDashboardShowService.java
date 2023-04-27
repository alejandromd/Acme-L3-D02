
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
public class LecturerDashboardShowService extends AbstractService<Lecturer, LecturerDashboard> {

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
		LecturerDashboard dashboard;
		Lecturer lecturer;

		dashboard = new LecturerDashboard();

		Principal principal;
		int userAccountId;
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		lecturer = this.repository.findOneLecturerByUserAccountId(userAccountId);

		double averageLectureLearningTime;
		double maxLectureLearningTime;
		double minLectureLearningTime;
		double devLectureLearningTime;

		averageLectureLearningTime = this.repository.findAverageLectureLearningTime(lecturer).orElse(0.0);
		maxLectureLearningTime = this.repository.findMaxLectureLearningTime(lecturer).orElse(0.0);
		minLectureLearningTime = this.repository.findMinLectureLearningTime(lecturer).orElse(0.0);
		devLectureLearningTime = this.repository.findLinearDevLectureLearningTime(lecturer).orElse(0.0);

		dashboard.setAverageTimeOfLectures(averageLectureLearningTime);
		dashboard.setMinimumTimeOfLectures(minLectureLearningTime);
		dashboard.setMaximumTimeOfLectures(maxLectureLearningTime);
		dashboard.setDeviationTimeOfLectures(devLectureLearningTime);

		Collection<Double> courseEstimatedLearningTime;
		courseEstimatedLearningTime = this.repository.findEstimatedLearningTimeByCourse(lecturer);
		dashboard.calculateCourseAverage(courseEstimatedLearningTime);
		dashboard.calculateCourseMax(courseEstimatedLearningTime);
		dashboard.calculateCourseMin(courseEstimatedLearningTime);
		dashboard.calculateCourseDev(courseEstimatedLearningTime);

		//total lectures
		Integer handsOnLectures;
		Integer theoreticalLectures;
		handsOnLectures = this.repository.findNumOfLecturesByType(lecturer, Nature.HANDS_ON).orElse(0);
		theoreticalLectures = this.repository.findNumOfLecturesByType(lecturer, Nature.THEORETICAL).orElse(0);

		dashboard.setTotalLectures(handsOnLectures + theoreticalLectures);

		super.getBuffer().setData(dashboard);
	}

	@Override
	public void unbind(final LecturerDashboard object) {
		Principal principal;
		int userAccountId;
		Lecturer lecturer;
		Integer handsOnLectures;
		Integer theoreticalLectures;
		Tuple tuple;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		lecturer = this.repository.findOneLecturerByUserAccountId(userAccountId);
		handsOnLectures = this.repository.findNumOfLecturesByType(lecturer, Nature.HANDS_ON).orElse(0);
		theoreticalLectures = this.repository.findNumOfLecturesByType(lecturer, Nature.THEORETICAL).orElse(0);

		tuple = super.unbind(object, "totalLectures", "averageTimeOfLectures", "deviationTimeOfLectures", "minimumTimeOfLectures", "maximumTimeOfLectures", "averageTimeOfCourses", "deviationTimeOfCourses", "minimumTimeOfCourses", "maximumTimeOfCourses");
		tuple.put("handsOnLectures", handsOnLectures);
		tuple.put("theoreticalLectures", theoreticalLectures);

		super.getResponse().setData(tuple);
	}

}
