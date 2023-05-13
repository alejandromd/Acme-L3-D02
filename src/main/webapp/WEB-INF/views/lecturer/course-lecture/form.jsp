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
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete')}">
			<acme:input-textbox code="lecturer.courseLecture.form.lecture.title" path="lecture.title" readonly="true"/>
			<acme:input-textbox code="lecturer.courseLecture.form.label.course" path="course.code" readonly="true"/>
			<acme:submit test="${showCreate == true}" code="lecturer.courseLecture.form.button.delete" action="/lecturer/course-lecture/delete?courseId=${courseId}"/>		
		</jstl:when>	
		<jstl:when test="${_command == 'add'}">
			<acme:input-select code="lecturer.courseLecture.form.lecture.addLecture" path="lecture" choices="${lectures}"/>
			<acme:submit code="lecturer.courseLecture.form.button.confirm" action="/lecturer/course-lecture/add?courseId=${courseId}"/>		
		</jstl:when>
	</jstl:choose>
</acme:form>