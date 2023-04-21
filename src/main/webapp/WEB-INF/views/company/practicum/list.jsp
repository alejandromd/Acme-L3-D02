<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="company.practicum.list.label.code" path="code" width="10%"/>
	<acme:list-column code="company.practicum.list.label.title" path="title" width="40%"/>
	<acme:list-column code="company.practicum.list.label.course.code" path="courseCode" width="10%"/>
	<acme:list-column code="company.practicum.list.label.course.title" path="courseTitle" width="40%"/>
</acme:list>
<acme:button code="company.practicum.form.button.create" action="/company/practicum/create"/>