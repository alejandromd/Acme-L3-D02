<%--
- menu.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java" import="acme.framework.helpers.PrincipalHelper,acme.roles.Provider,acme.roles.Consumer"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:menu-bar code="master.menu.home">
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link" action="http://www.twitch.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link2" action="http://www.instagram.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link3" action="http://www.whatsapp.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link4" action="http://www.youtube.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link5" action="http://www.facebook.com/"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.any.peep.list" action="/any/peep/list"/>
		</acme:menu-option>
		

		<acme:menu-option code="master.menu.list" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.course" action="/any/course/list"/>
			<acme:menu-suboption code="master.menu.authenticated.note.list" action="/authenticated/note/list"/>
			<acme:menu-suboption code="master.menu.authenticated.bulletin.list" action="/authenticated/bulletin/list"/>
			<acme:menu-suboption code="master.menu.authenticated.offers"  action="/authenticated/offer/list"/>
			<acme:menu-suboption code="master.menu.any.peep.list" action="/any/peep/list"/>
		</acme:menu-option>
		
			
		<acme:menu-option code="master.menu.administrator" access="hasRole('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-initial" action="/administrator/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-sample" action="/administrator/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-down" action="/administrator/shut-down"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.offers"  action="/administrator/offer/list"/>
			<acme:menu-suboption code="master.menu.authenticated.bulletin.create" action="/administrator/bulletin/create"/>
			<acme:menu-suboption code="master.menu.administrator.spam-config.list" action="/administrator/spam-config/list"/>
			<acme:menu-suboption code="master.menu.administrator.banner.list" action="/administrator/banner/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRole('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRole('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		

		<acme:menu-option code="master.menu.auditor" access="hasRole('Auditor')">
			<acme:menu-suboption code="master.menu.auditor.audit" action="/auditor/audit/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.lecturer" access="hasRole('Lecturer')">
			<acme:menu-suboption code="master.menu.lecturer.course" action="/lecturer/course/list"/>
			<acme:menu-suboption code="master.menu.lecturer.lecture" action="/lecturer/lecture/list-all"/>
			<acme:menu-suboption code="master.menu.lecturer.dashboard" action="/lecturer/lecturer-dashboard/show"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.assistant" access="hasRole('Assistant')">
			<acme:menu-suboption code="master.menu.assistant.list-my-tutorials" action="/assistant/tutorial/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.assistant.dashboard" action="/assistant/assistant-dashboard/show"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.student" access="hasRole('Student')">
			<acme:menu-suboption code="master.menu.student.enrolment" action="/student/enrolment/list"/>
			<acme:menu-suboption code="master.menu.student.dashboard" action="/student/student-dashboard/show"/>
			<acme:menu-suboption code="master.menu.student.course" action="/student/course/list"/>
      </acme:menu-option>

		<acme:menu-option code="master.menu.company" access="hasRole('Company')">
			<acme:menu-suboption code="master.menu.Company.practicum" action="/company/practicum/list"/>
		</acme:menu-option>

	</acme:menu-left>

	<acme:menu-right>
		<acme:menu-option code="master.menu.sign-up" action="/anonymous/user-account/create" access="isAnonymous()"/>
		<acme:menu-option code="master.menu.sign-in" action="/master/sign-in" access="isAnonymous()"/>

		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-data" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider" action="/authenticated/provider/update" access="hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRole('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer" action="/authenticated/consumer/update" access="hasRole('Consumer')"/>
			<acme:menu-suboption code="master.menu.authenticated.lecturer.create" action="/authenticated/lecturer/create" access="!hasRole('Lecturer')"/>
			<acme:menu-suboption code="master.menu.authenticated.lecturer.update" action="/authenticated/lecturer/update" access="hasRole('Lecturer')"/>
			<acme:menu-suboption code="master.menu.authenticated.student.create" action="/authenticated/student/create" access="!hasRole('Student')"/>
			<acme:menu-suboption code="master.menu.authenticated.student.update" action="/authenticated/student/update" access="hasRole('Studentz')"/>
			<acme:menu-suboption code="master.menu.authenticated.auditor.create" action="/authenticated/auditor/create" access="!hasRole('Auditor')"/>
			<acme:menu-suboption code="master.menu.authenticated.auditor.update" action="/authenticated/auditor/update" access="hasRole('Auditor')"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.sign-out" action="/master/sign-out" access="isAuthenticated()"/>
	</acme:menu-right>
</acme:menu-bar>

