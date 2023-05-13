<%--
- list.jsp
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

<acme:list>
	<acme:list-column code="lecturer.courseLecture.label.title" path="lecture.title" width="70%"/>
	<acme:list-column code="lecturer.courseLecture.label.estimatedLearningTime" path="lecture.estimatedLearningTime" width="20%"/>
	<acme:list-column code="lecturer.courseLecture.label.lectureType" path="lecture.lectureType" width="10%"/>
</acme:list>


<jstl:if test="${_command == 'list' && showCreate == true}">
	<acme:button code="lecturer.courseLecture.form.button.add" action="/lecturer/course-lecture/add?courseId=${courseId}"/>
</jstl:if>	