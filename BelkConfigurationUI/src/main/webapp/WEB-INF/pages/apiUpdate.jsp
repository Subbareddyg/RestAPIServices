<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<c:url value='/resources/css/configui.css' />"
	rel="stylesheet">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script src="<c:url value="/resources/js/configui.js" />"></script>
<title>Update APIs</title>
</head>

<body>
	<div><%@ include file="header.jsp"%></div>
	<div class="body">
		<div class="headingfontstyle heading">Update API</div>
		<form action="" method="POST" id="updateApi">
		<div class="updateBtn">
		<input type="hidden" name="encryptedAppId" id="encryptedAppId" value="${encryptedAppId}">
		<input type="hidden" name="encryptedKeyParams" id="encryptedKeyParams" value="${encryptedKeyParams}">
		<input type="hidden" name="fileName" id="fileName" value="${fileName}" /> 
		<input type="hidden" name="propList" id="propList" value="${propList}" /> 
		<input type="hidden" name="logLevel" id="logLevel" value="${logLevel}" />
				<input class="mouseStyle" type="button" id="update" name="update"
					onclick="updateApi()" value="Update API"
					title="Click here to Update API">
			</div>
			<p class="selctMsg" id="selectMsg"></p>

			<br>
			<table border="1" width="95%">
				<tr>
					<!-- <th><input type="checkbox" id="selectall" /></th> -->
					<th>API Name</th>
					<th>Environment URLs</th>
					
				</tr>
				<tr>
				<td></td>
				<td>
				<c:choose>
					<c:when test="${fileName != ''}">
					<c:choose>
						<c:when test="${propList != ''}">
						Updated properties: ${fn:replace(propList,'|',',')}
						</c:when>
						<c:otherwise>
						Property file to be reloaded: ${fileName}
						</c:otherwise>
					</c:choose>
					</c:when>
					<c:otherwise>
					Updated log level: ${logLevel}
					</c:otherwise>
				</c:choose>
				</td>
				</tr>
				<c:forEach items="${apiNames}" var="element">
					<tr>
						<%-- <td align="center" class="tdChkboxstyle" id="ChkBoxDiv">
						<input type="checkbox" class="case" name="chkBox" id="chkBox" value='${element.key}' />
						</td> --%>
						<td align="center" class="tdstyle"><input type="hidden" name="api" value="${element.key}" />${element.key}</td>
						<td>
							<table width="98%" >
								<c:forEach items="${element.value}" var="url">
									<tr>
										<td align="left" width="2%" id="ChkBoxDiv">
										<input type="checkbox" name="urlChkBox_${element.key}" id="urlChkBox_${element.key}" />
										</td>
										<c:set var="splitString" value="${fn:split(url,'~')}" />
										<td align="left"  width="30%">${splitString[0]}</td>
										<td align="left"  width="60%">
										<input type="hidden" name="apiURL" value="${splitString[1]}" />${fn:split(fn:split(splitString[1],'/')[1],':')[0]}
										</td>
										<td align="left" width="10%">
										<div id="result"></div>
										</td>
									</tr>
								</c:forEach>
							</table>
						</td>
						
					</tr>

				</c:forEach>
			</table>

			<br>
			<div class="updateBtn">
				<input class="mouseStyle" type="button" id="update" name="update"
					onclick="updateApi()" value="Update API"
					title="Click here to Update API">
			</div>
		</form>

		<form action="getFileList" method="get">
			<div class="BackBtnStl">
				<input class="mouseStyle" type="submit" value="Back To Home Page"
					title="Click here to go back to Home Page">
			</div>
		</form>
	</div>
	<div><%@ include file="footer.jsp"%></div>
</body>
</html>