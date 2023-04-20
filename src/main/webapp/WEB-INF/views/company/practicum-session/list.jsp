<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="company.practicumSession.list.label.title" path="title" width="10%"/>
	<acme:list-column code="company.practicumSession.list.label.recap" path="recap" width="40%"/>
	<acme:list-column code="company.practicumSession.list.label.duration" path="duration" width="40%"/>
	<acme:list-column code="company.practicumSession.list.label.link" path="link" width="40%"/>
</acme:list>

<acme:button test="${showCreate}" code="company.practicumSession.list.button.create" action="/company/practicumSession/create?masterId=${masterId}"/>