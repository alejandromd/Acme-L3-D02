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
	<acme:input-textbox code="assistant.tutorialSession.form.label.title" path="title"/>	
	<acme:input-textarea code="assistant.tutorialSession.form.label.informativeAbstract" path="informativeAbstract"/>	
	<acme:input-select code="assistant.tutorialSession.form.label.type" path="type" choices="${types}"/>	
	<acme:input-moment code="assistant.tutorialSession.form.label.startTimestamp" path="startTimestamp"/>
	<acme:input-moment code="assistant.tutorialSession.form.label.endTimestamp" path="endTimestamp"/>
	<acme:input-url code="assistant.tutorialSession.form.label.furtherInfo" path="furtherInfo"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="assistant.tutorialSession.form.button.update" action="/assistant/tutorial-session/update"/>
			<acme:submit code="assistant.tutorialSession.form.button.delete" action="/assistant/tutorial-session/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistant.tutorialSession.form.button.create" action="/assistant/tutorial-session/create?masterId=${masterId}"/>
		</jstl:when>
	</jstl:choose>	
	
</acme:form>