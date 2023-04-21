<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="company.practicumSession.form.label.title" path="title"/>
	<acme:input-textarea code="company.practicumSession.form.label.recap" path="summary"/>
	<acme:input-moment code="company.practicumSession.form.label.startTime" path="initialPeriod"/>
	<acme:input-moment code="company.practicumSession.form.label.endTime" path="finalPeriod"/>
	<acme:input-url code="company.practicumSession.form.label.link" path="link"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="company.practicumSession.form.button.update" action="/company/practicum-session/update"/>
			<acme:submit code="company.practicumSession.form.button.delete" action="/company/practicum-session/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="company.practicumSession.form.button.create" action="/company/practicum-session/create?masterId=${masterId}"/>
		</jstl:when>
	</jstl:choose>

</acme:form>