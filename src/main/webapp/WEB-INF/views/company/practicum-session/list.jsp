<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="company.practicumSession.list.label.title" path="title" width="10%"/>
	<acme:list-column code="company.practicumSession.list.label.recap" path="summary" width="40%"/>
	<acme:list-column code="company.practicumSession.list.label.duration" path="duration" width="40%"/>
</acme:list>

<acme:button test="${showCreate}" code="company.practicumSession.list.button.create" action="/company/practicum-session/create?masterId=${masterId}"/>
<acme:button code="company.practicum-session.list.button.create.addendum" action="/company/practicum-session/create-addendum"/>