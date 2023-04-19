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
	<acme:input-textbox code="auditor.dashboard.form.label.totalNumberOfAudits" path="totalNumberOfAudits" readonly="true"/>	
	<acme:input-textbox code="auditor.dashboard.form.label.averageNumberOfAuditingRecordsAudit" path="averageNumberOfAuditingRecordsAudit" readonly="true"/>	
	<acme:input-textbox code="auditor.dashboard.form.label.deviationNumberOfAuditingRecordsAudit" path="deviationNumberOfAuditingRecordsAudit" readonly="true"/>	
	<acme:input-textbox code="auditor.dashboard.form.label.minimunNumberOfAuditingRecordsAudit" path="minimunNumberOfAuditingRecordsAudit" readonly="true"/>	
	<acme:input-textbox code="auditor.dashboard.form.label.maximunNumberOfAuditingRecordsAudit" path="maximunNumberOfAuditingRecordsAudit" readonly="true"/>
	<acme:input-textbox code="auditor.dashboard.form.label.averageTimeOfPeriodLengthsAuditingRecords" path="averageTimeOfPeriodLengthsAuditingRecords" readonly="true"/>	
	<acme:input-textbox code="auditor.dashboard.form.label.deviationTimeOfPeriodLengthsAuditingRecords" path="deviationTimeOfPeriodLengthsAuditingRecords" readonly="true"/>	
	<acme:input-textbox code="auditor.dashboard.form.label.minimunTimeOfPeriodLengthsAuditingRecords" path="minimunTimeOfPeriodLengthsAuditingRecords" readonly="true"/>	
	<acme:input-textbox code="auditor.dashboard.form.label.maximunTimeOfPeriodLengthsAuditingRecords" path="maximunTimeOfPeriodLengthsAuditingRecords" readonly="true"/>	

</acme:form>