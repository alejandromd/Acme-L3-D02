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

<h2>
	<acme:message code="assistant.dashboard.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="assistant.tutorial.dashboard.label.tutorialNumber"/>
		</th>
		<td>
			<acme:print value="${numberOfTutorials}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="assistant.tutorial.dashboard.label.averageTutorialTime"/>
		</th>
		<td>
			<acme:print value="${averageTimeOfTutorials}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="assistant.tutorial.dashboard.label.deviationTutorialTime"/>
		</th>
		<td>
			<acme:print value="${deviationTimeOfTutorials}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="assistant.tutorial.dashboard.label.maximumTutorialTime"/>
		</th>
		<td>
			<acme:print value="${maximumTimeOfTutorials}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="assistant.tutorial.dashboard.label.minimumTutorialTime"/>
		</th>
		<td>
			<acme:print value="${minimumTimeOfTutorials}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="assistant.tutorialSession.dashboard.label.averageSessionTime"/>
		</th>
		<td>
			<acme:print value="${averageTimeOfSessions}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="assistant.tutorialSession.dashboard.label.deviationSessionTime"/>
		</th>
		<td>
			<acme:print value="${deviationTimeOfSessions}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="assistant.tutotialSession.dashboard.label.maximumSessionTime"/>
		</th>
		<td>
			<acme:print value="${maximumTimeOfSessions}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="assistant.tutorialSession.dashboard.label.minimumSessionTime"/>
		</th>
		<td>
			<acme:print value="${minimumTimeOfSessions}"/>
		</td>
	</tr>	
</table>

<h2>
	<acme:message code="assistant.dashboard.title.visual-representation"/>
</h2>

<div style="height: 40vh; width: 100%; margin:auto">
	<canvas id="canvas"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [
					"AVERAGE", "DEVIATION", "MAXIMUM", "MINIMUM"
			],
			datasets : [
				{
					 label: "Tutorials",
					 data: [
						 ${averageTimeOfTutorials},
						 ${deviationTimeOfTutorials},
						 ${maximumTimeOfTutorials},
						 ${minimumTimeOfTutorials}
					 ]
				},
				{
					label: "Sessions",
					data: [
						${averageTimeOfSessions},
						${deviationTimeOfSessions},
						${maximumTimeOfSessions},
						${minimumTimeOfSessions}
					]
				}
			]
		};
		var options = {
			scales : {
				yAxes : [
					{
						ticks : {
							suggestedMin : 0.0,
							suggestedMax : 1.0
						}
					}
				]
			},
			maintainAspectRatio: false
		};
	
		var canvas, context;
	
		canvas = document.getElementById("canvas");
		context = canvas.getContext("2d");
		new Chart(context, {
			type : "radar",
			data : data,
			options : options
		});
	});
</script>

<acme:return/>