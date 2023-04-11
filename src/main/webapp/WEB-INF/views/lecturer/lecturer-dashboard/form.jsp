<%--
- form.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>
<acme:form>
	<acme:input-textbox code="lecturer.course.form.label.totalLectures" path="totalLectures" readonly="true"/>	
	<acme:input-textbox code="lecturer.course.form.label.averageTimeOfLectures" path="averageTimeOfLectures" readonly="true"/>	
	<acme:input-textbox code="lecturer.course.form.label.deviationTimeOfLectures" path="deviationTimeOfLectures" readonly="true"/>	
	<acme:input-textbox code="lecturer.course.form.label.minimumTimeOfLectures" path="minimumTimeOfLectures" readonly="true"/>	
	<acme:input-textbox code="lecturer.course.form.label.maximumTimeOfLectures" path="maximumTimeOfLectures" readonly="true"/>
	<acme:input-textbox code="lecturer.course.form.label.averageTimeOfCourses" path="averageTimeOfCourses" readonly="true"/>	
	<acme:input-textbox code="lecturer.course.form.label.deviationTimeOfCourses" path="deviationTimeOfCourses" readonly="true"/>	
	<acme:input-textbox code="lecturer.course.form.label.minimumTimeOfCourses" path="minimumTimeOfCourses" readonly="true"/>	
	<acme:input-textbox code="lecturer.course.form.label.maximumTimeOfCourses" path="maximumTimeOfCourses" readonly="true"/>	

</acme:form>