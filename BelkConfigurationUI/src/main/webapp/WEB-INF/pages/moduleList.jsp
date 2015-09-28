<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
<link href="<c:url value='/resources/css/configui.css' />"
	rel="stylesheet">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script src="<c:url value="/resources/js/configui.js" />"></script>
<title>Configuration Modules List</title>
</head>

<body>
	<div><%@ include file="header.jsp"%></div>
	<div class="body">


		<form method="post" action="getFileList">
			<div class="heading headingfontstyle">Configuration Module List</div>
			<div>
				<c:forEach items="${moduleList}" var="element">
					<input type="submit" name="moduleName" size="100"
						value="${element}" class="inputBox heading"
						title="Click to see file list in this Module" />
					<br>
					<br>
				</c:forEach>
			</div>

			<div class="heading headingfontstyle">Log level</div>
			<div>
				<input type="hidden" id="logLevel" name="logLevel" value="">
				<table>
					<tr>
						<td>Select a log level:</td>
						<td><select id="logLevelSelect" name="logLevelSelect"
							onchange="changeLogLevel();">
								<c:forEach items="${logLevelList}" var="element">
									<option value="${element}">${element}</option>
								</c:forEach>
						</select></td>
						<td id="logLevelStatus"></td>
					</tr>
				</table>

				<br /> <input type="button" id="logLevelSubmit"
					name="logLevelSubmit" value="Update Log Level"
					onclick="proceedToAPISelection();">
			</div>
		</form>
	</div>
	<div><%@ include file="footer.jsp"%></div>
</body>
</html>