
package acme.features.student.dashboard;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Activity;
import acme.entities.Course;
import acme.entities.Lecture;
import acme.forms.StudentDashboard;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentDashboardService extends AbstractService<Student, StudentDashboard> {

	@Autowired
	protected StudentDashboardRepository repository;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(super.getRequest().getPrincipal().hasRole(Student.class));
	}

	@Override
	public void load() {
		StudentDashboard object;
		List<Double> periods;
		List<Double> learningTimes;
		int totalActivities;
		double averagePeriodOfActivities;
		double minimumPeriodOfActivities;
		double maximumPeriodOfActivities;
		double deviationOfThePeriodActivities;
		double averageLearningTime;
		double minimumLearningTime;
		double maximumLearningTime;
		double deviationLearningTime;

		final int id = super.getRequest().getPrincipal().getAccountId();
		final Student student = this.repository.findStudentByUserAccountId(id);

		totalActivities = this.repository.findActivitiesByStudent(student).size();

		periods = this.getPeriodOfActivity(student);
		learningTimes = this.getLearningTimesOfCourses(student);

		averagePeriodOfActivities = periods.stream().mapToDouble(x -> x.doubleValue()).average().orElse(0);
		minimumPeriodOfActivities = periods.stream().mapToDouble(x -> x.doubleValue()).min().orElse(0);
		maximumPeriodOfActivities = periods.stream().mapToDouble(x -> x.doubleValue()).max().orElse(0);
		deviationOfThePeriodActivities = this.standardDeviation(periods);

		averageLearningTime = learningTimes.stream().mapToDouble(x -> x.doubleValue()).average().orElse(0);
		minimumLearningTime = learningTimes.stream().mapToDouble(x -> x.doubleValue()).min().orElse(0);
		maximumLearningTime = learningTimes.stream().mapToDouble(x -> x.doubleValue()).max().orElse(0);
		deviationLearningTime = this.standardDeviation(learningTimes);

		object = new StudentDashboard();
		object.setTotalActivities(totalActivities);
		object.setAveragePeriodOfActivities(averagePeriodOfActivities == 0 ? null : averagePeriodOfActivities);
		object.setMinimumPeriodOfActivities(minimumPeriodOfActivities == 0 ? null : minimumPeriodOfActivities);
		object.setMaximumPeriodOfActivities(maximumPeriodOfActivities == 0 ? null : maximumPeriodOfActivities);
		object.setDeviationPeriodOfActivities(deviationOfThePeriodActivities == 0 ? null : deviationOfThePeriodActivities);

		object.setAverageLearningTime(averageLearningTime == 0 ? null : averageLearningTime);
		object.setMinimumLearningTime(minimumLearningTime == 0 ? null : minimumLearningTime);
		object.setMaximunLearningTime(maximumLearningTime == 0 ? null : maximumLearningTime);
		object.setDevitationLearningTime(deviationLearningTime == 0 ? null : deviationLearningTime);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final StudentDashboard object) {
		Tuple tuple;

		tuple = super.unbind(object, "totalActivities", "averagePeriodOfActivities", "minimumPeriodOfActivities", "maximumPeriodOfActivities", "deviationPeriodOfActivities", "averageLearningTime", "minimumLearningTime", "maximunLearningTime",
			"devitationLearningTime");

		super.getResponse().setData(tuple);
	}

	private List<Double> getPeriodOfActivity(final Student student) {
		final List<Double> result = new ArrayList<>();
		final Collection<Activity> activities = this.repository.findActivitiesByStudent(student);
		for (final Activity a : activities) {
			final Duration d = MomentHelper.computeDuration(a.getStartPeriod(), a.getEndPeriod());
			final double hours = d.toHours();
			result.add(hours);
		}
		return result;
	}

	private List<Double> getLearningTimesOfCourses(final Student student) {
		final List<Double> result = new ArrayList<>();
		final Collection<Course> courses = this.repository.findEnrolledCoursesByStudent(student);
		for (final Course course : courses) {
			final Collection<Lecture> lectures = this.repository.findLecturesByCourse(course.getId());
			double res = 0;
			for (final Lecture lecture : lectures)
				res += lecture.getEstimatedLearningTime();
			result.add(res);
		}
		return result;
	}

	private double standardDeviation(final List<Double> list) {
		if (list.isEmpty() || list.size() == 1)
			return 0;
		final double avg = list.stream().mapToDouble(x -> x.doubleValue()).average().orElse(0);
		final double sum = list.stream().mapToDouble(x -> Math.pow(x - avg, 2)).sum();
		return Math.sqrt(sum / (list.size() - 1));
	}

}
