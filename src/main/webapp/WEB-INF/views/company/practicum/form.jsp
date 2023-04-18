<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="company.practicum.form.label.code" path="code"/>
	<acme:input-textbox code="company.practicum.form.label.title" path="title"/>
	<acme:input-textarea code="company.practicum.form.label.recap" path="summary"/>
	<acme:input-textbox code="company.practicum.form.label.goals" path="goals"/>
	<acme:input-select code = "company.practicum.form.label.course" path="course" choices="${courses}"/>
	<acme:input-integer readonly = "true" code="company.practicum.form.label.totalTime" path="totalTime"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="company.practicum.form.buttton.sessions" action="/company/session/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="company.practicum.form.buttton.sessions" action="/company/practicumSession/list?masterId=${id}"/>
			<acme:button code="company.practicum.form.button.create.session" action="/company/practicumSession/create?masterId=${id}"/>
			<acme:submit code="company.practicum.form.button.update" action="/company/practicum/update"/>
			<acme:submit code="company.practicum.form.button.delete" action="/company/practicum/delete"/>
			<acme:submit code="company.practicum.form.button.publish" action="/company/practicum/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="company.practicum.form.button.create" action="/company/practicum/create?courseId=${courseId}"/>
		</jstl:when>
	</jstl:choose>

</acme:form>