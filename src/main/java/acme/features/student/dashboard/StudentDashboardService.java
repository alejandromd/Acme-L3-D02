
package acme.features.student.dashboard;

import org.springframework.stereotype.Service;

import acme.forms.StudentDashboard;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentDashboardService extends AbstractService<Student, StudentDashboard> {

}
