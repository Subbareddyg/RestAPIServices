<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
<link href="<c:url value='/resources/css/configui.css' />" rel="stylesheet">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script src="<c:url value="/resources/js/configui.js" />"></script>

<title>Validation Property Details Page</title>
</head>

<BODY>
<div><%@ include file="header.jsp" %></div>
<div class="body">
  	<div class="headingfontstyle heading">Content</div> 
 	<form action="proceedToUpdateValidation" method = "POST" id="proceedValidationForm">
 	<p class= "selctMsg" id="selectMsg"></p>
 		<div class="updateBtn">
		<input class="mouseStyle" type="button" id= "proceedValidation" value="Proceed to Update" onclick="proceedToUpdateValidation(this)" title="Click to Proceed to Update">
		</div>
  		<table border="1">
  		<tr>
  		<td>
  		<textarea name="content" id="content" name="content" >${content}</textarea>
  		</td>
  		</tr>
		
		</table>
		<br>
		<br>
		<input type="hidden" name="filePath" id="filePath" value='<c:out value="${filePath}"/>' />
	
		<input type="hidden" id="properties" name=properties size="50">

		
 	</form>
 	</div>
	<div><%@ include file="footer.jsp" %></div>
</html>