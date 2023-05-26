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
	<acme:input-textbox code="student.enrolment.form.label.code" path="code"/>	
	<acme:input-textbox code="student.enrolment.form.label.motivation" path="motivation"/>	
	<acme:input-textbox code="student.enrolment.form.label.goals" path="goals"/>		
	<acme:input-select code="student.enrolment.form.label.course" path="course" choices="${courses}"/>
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|finalise')}">
		<acme:input-textbox code="student.enrolment.form.label.draftMode" path="draftMode" readonly="true"/>
		<acme:input-integer code="student.enrolment.form.label.workTime" path="workTime" readonly="true"/>
	</jstl:if>
		<br>
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|finalise') && draftMode == true}">
		<h3><acme:message code="student.enrolment.form.message.creditCard"/></h3>
		<acme:input-textbox code="student.enrolment.form.label.holderName" path="holderName"/>
		<acme:input-textbox code="student.enrolment.form.label.cardNumber" path="cardNumber"/>
		<acme:input-textbox code="student.enrolment.form.label.expiryDate" path="expiryDate"/>
		<acme:input-textbox code="student.enrolment.form.label.cvc" path="cvc"/>
	</jstl:if>
		
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="student.enrolment.form.button.activities" action="/student/activity/list?enrolmentId=${id}"/>		
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|finalise') && draftMode == true}">
			<acme:submit code="student.enrolment.form.button.update" action="/student/enrolment/update"/>
			<acme:submit code="student.enrolment.form.button.delete" action="/student/enrolment/delete"/>
			<acme:submit code="student.enrolment.form.button.finalise" action="/student/enrolment/finalise"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="student.enrolment.form.button.create" action="/student/enrolment/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>