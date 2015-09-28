<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
<link href="<c:url value='/resources/css/configui.css' />" rel="stylesheet">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script src="<c:url value="/resources/js/configui.js" />"></script>
<TITLE>Update APIs</TITLE>
</HEAD>

<BODY>
<div><%@ include file="header.jsp" %></div>
<div class="body">
  	<div class="headingfontstyle heading">Update API</div> 
 	<form action="" method = "POST" id="updateApi">
 	<p class= "selctMsg" id="selectMsg"></p>
 	
 	<br>
  		<table border="1">
		<tr>
    		<th></th>
    		<th>API Name</th>
    		<th>Status</th>
    	</tr>
		<c:forEach items="${apiNames}" var = "element" >
		<tr>
    		<td align="center" class="tdChkboxstyle" id="ChkBoxDiv">
    		<input type="radio" class="case" name="moduleChoice" id="moduleChoice" value='${element}'/></td>
    		<td align="center" class="tdstyle"><input class="inputBackgrnd" readonly type="text" name="api" value="${element}" /></td>
    		<td class= "resultFontStl" id="${element}"></td>
   		</tr>
   		
		</c:forEach>
		</table>

		<br>
	
 	
 	<input type="hidden" name="fileName" id="fileName" value="${fileName}" />
 	
 	<div class="updateBtn">
 	<input class="mouseStyle" type="button" id= "update" name="update" 
 	onclick="updateApiForValidation(this)" value="Update API" title="Click here to Update API" >
 		</div>
 	</form>
 	
 	<FORM action="getModuleList" method="get">
 	<div class="BackBtnStl">
 	<INPUT class="mouseStyle" Type="submit" VALUE="Back To Home Page" title="Click here to go back to Home Page">
	</div>
 	</FORM>
 </div>
 <div><%@ include file="footer.jsp" %></div>
</BODY>
</HTML>