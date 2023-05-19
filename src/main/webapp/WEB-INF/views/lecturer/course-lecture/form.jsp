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
			<h3><acme:message code="lecturer.courseLecture.form.lecture.info"/></h3>
			<table class="table table-sm">
			<caption></caption>
			<tr>
			<th scope="row">
				<acme:message code="lecturer.courseLecture.form.lecture.title"/>
			</th>
			<td>
				<acme:print value="${lecture.getTitle()}"/>
			</td>
			</tr>
			<tr>
			<th scope="row">
				<acme:message code="lecturer.courseLecture.form.lecture.summary"/>
			</th>
			<td>
				<acme:print value="${lecture.getSummary()}"/>
			</td>
			</tr>
			<tr>
			<th scope="row">
				<acme:message code="lecturer.courseLecture.form.lecture.lectureType"/>
			</th>
			<td>
				<acme:print value="${lecture.getLectureType()}"/>
			</td>
			</tr>
						<tr>
			<th scope="row">
				<acme:message code="lecturer.courseLecture.label.draftMode"/>
			</th>
			<td>
				<acme:print value="${lecture.isDraftMode()}"/>
			</td>
			</tr>
			</table>
			<acme:submit test="${showCreate == true}" code="lecturer.courseLecture.form.button.delete" action="/lecturer/course-lecture/delete?courseId=${courseId}"/>		
			<acme:button test="${_command == 'show'}" code="lecturer.courseLecture.form.button.details" action="/lecturer/lecture/show?id=${lectureId}"/>
		</jstl:when>	
		<jstl:when test="${_command == 'add'}">
			<h3><acme:message code="lecturer.courseLecture.form.course.info"/></h3>
			<table class="table table-sm">
			<caption></caption>
			<tr>
			<th scope="row">
				<acme:message code="lecturer.courseLecture.form.label.course"/>
			</th>
			<td>
				<acme:print value="${course.getCode()}"/>
			</td>
			</tr>
			<tr>
			<th scope="row">
				<acme:message code="lecturer.courseLecture.label.title"/>
			</th>
			<td>
				<acme:print value="${course.getTitle()}"/>
			</td>
			</tr>
			<tr>
			<th scope="row">
				<acme:message code="lecturer.courseLecture.label.summary"/>
			</th>
			<td>
				<acme:print value="${course.getSummary()}"/>
			</td>
			</tr>
			</table>
		<acme:input-select code="lecturer.courseLecture.form.lecture.addLecture" path="lecture" choices="${lectures}"/>
		<acme:submit code="lecturer.courseLecture.form.button.confirm" action="/lecturer/course-lecture/add?courseId=${courseId}"/>		
		<acme:button code="lecturer.courseLecture.form.button.lectures" action="/lecturer/lecture/list-all"/>
		</jstl:when>
	</jstl:choose>
</acme:form>