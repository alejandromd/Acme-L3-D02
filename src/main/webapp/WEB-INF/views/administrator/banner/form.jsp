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
	<acme:input-textbox code="administrator.bulletin.title" path="instantiationMoment" />
	<acme:input-textbox code="administrator.bulletin.message" path="slogan" />
	<acme:input-textbox code="administrator.bulletin.critical" path="displayPeriodBegin" />
	<acme:input-textbox code="administrator.bulletin.critical" path="displayPeriodFinish" />
	<acme:input-textbox code="administrator.bulletin.critical" path="picture" />
	<acme:input-url code="administrator.bulletin.link" path="linkWeb" />
	
</acme:form>