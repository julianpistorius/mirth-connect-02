<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- param pageTitle: default title if none is chosen --%>
<jsp:include page="/_jsp/header.jsp">
	<jsp:param name="pageTitle" value="Channels" />
	<jsp:param name="selected" value="channel" />
</jsp:include>

<div id="primaryContent">
	<!-- Main content -->
	<form action="/channel/?state=search" id="listingForm" method="post">
		<div>
			<label>Search: <input name="term" size="20" /><input type="submit" value="GO" class="button" /></label>
		</div>
	</form>
	<h1>Search Results</h1>

	<div class="breadcrumb">
		<a href="/channel/">Channels</a> &gt; Search Results
	</div>

	<div id="innerContent">

		<table cellspacing="0" id="channelsTable">    
			<tr><th class="firstCol">Deployed</th><th>Name</th><th class="actionCol">Edit</th><th class="actionCol">Delete</th><th class="actionCol">Clone</th><th class="actionCol">Logs</th></tr>

			<!-- data -->
			<c:forEach var="channel" items="${channels}">
				<tr>
					<c:choose>
						<c:when test="${channel.enabled}">
							<td><span class="statusDeploy" title="Deployed"><span>Deployed</span></span></td>
						</c:when>
						<c:otherwise>
							<td><span class="statusNotDeploy" title="Not Deployed"><span>Not Deployed</span></span></td>
						</c:otherwise>
					</c:choose>
					<td><a href="/channel/?state=edit&amp;id=${channel.id}"><c:out value="${fn:replace(fn:escapeXml(channel.name),'  ', '&nbsp;&nbsp;')}" escapeXml="false" /></a></td>
					<td class="actionCol"><a href="/channel/?state=edit&amp;id=${channel.id}" class="editButton" title="Edit"><span>Edit</span></a></td>
					<td class="actionCol"><a href="/channel/?op=delete&amp;id=${channel.id}" class="deleteButton" title="Delete"><span>Delete</span></a></td>
					<td class="actionCol"><a href="/channel/?state=clone&amp;id=${channel.id}" class="cloneButton" title="Clone"><span>Clone</span></a></td>
					<td class="actionCol"><a href="#" class="logsButton" title="View Logs"><span>View Logs</span></a></td>
				</tr>
			</c:forEach>
			<c:if test="${fn:length(channels) == 0}">
				<tr><td colspan="6"> There are no channels. </td></tr>
			</c:if>			
		</table>
	</div>
</div><!-- primaryContent -->

<jsp:include page="/_jsp/footer.jsp" />