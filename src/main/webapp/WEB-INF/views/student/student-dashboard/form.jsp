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
	<acme:input-textbox code="student.dashboard.form.label.totalActivities" path="totalActivities" readonly="true"/>	
	<acme:input-textbox code="student.dashboard.form.label.averageActivities" path="averagePeriodOfActivities" readonly="true"/>	
	<acme:input-textbox code="student.dashboard.form.label.minActivities" path="minimumPeriodOfActivities" readonly="true"/>	
	<acme:input-textbox code="student.dashboard.form.label.maxActivities" path="maximumPeriodOfActivities" readonly="true"/>	
	<acme:input-textbox code="student.dashboard.form.label.devActivitites" path="deviationPeriodOfActivities" readonly="true"/>
	<acme:input-textbox code="student.dashboard.form.label.averageLearningTime" path="averageLearningTime" readonly="true"/>	
	<acme:input-textbox code="student.dashboard.form.label.minLearningTime" path="minimumLearningTime" readonly="true"/>	
	<acme:input-textbox code="student.dashboard.form.label.maxLearningTime" path="maximunLearningTime" readonly="true"/>	
	<acme:input-textbox code="student.dashboard.form.label.devLearningTime" path="devitationLearningTime" readonly="true"/>	

</acme:form>