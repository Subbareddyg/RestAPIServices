<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page session="true" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Header</title>
<link href="<c:url value='/resources/css/configui.css' />" rel="stylesheet">
</head>
<body>
<div class="usrNameStl">
Welcome ${sessionScope.loggedInUserName}
<input type="hidden" id="loggedInUserName" name="loggedInUserName" value="${sessionScope.loggedInUserName}">
<a class="logoutLinkStl" href="<c:url value="/j_spring_security_logout" />" >[Logout]</a>
</div>
</body>
</html>