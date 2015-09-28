<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
<link href="<c:url value='/resources/css/configui.css' />" rel="stylesheet">
<title>Validation Files</title>
</head>

<body>
<div><%@ include file="header.jsp" %></div>
<div class="body">
	<div class="heading headingfontstyle">Validation Files </div>

	<form method="post" action="getValidationProperty">
	<c:forEach items="${fileList}" var = "element" >
	<input type = "submit" name="moduleName" size="100" value = "${element}" class="inputBox heading" title="Click to edit this File"/>
	<br>
	<input type="hidden" name="location" id="location" value='<c:out value="${location}/${element}"/>' />
	<br>
	</c:forEach>
	</form>
	</div>
<div><%@ include file="footer.jsp" %></div>
</body>

</html>