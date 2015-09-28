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

<title>Property Details Page</title>
</head>

<BODY>
<div><%@ include file="header.jsp" %></div>
<div class="body">
  	<div class="headingfontstyle heading">List of Properties</div> 
 	<form action="proceedToUpdate" method = "POST" id="proceedForm">
 	<p class= "selctMsg" id="selectMsg"></p>
 		<div class="updateBtn">
		<input class="mouseStyle" type="button" id= "proceed" value="Proceed to Update" onclick="proceedToUpdate(this)" title="Click to Proceed to Update">
		</div>
  		<table border="1">
		<tr>
    		<th><input type="checkbox" id="selectall"/></th>
    		<th>Property Name</th>
    		<th>Property Value</th>
    	</tr>
		<c:forEach items="${propList}" var = "element" >
		<tr>
    		<td align="center" class="tdChkboxstyle"><input type="checkbox" class="case" name='chkBox' value='${element}'/></td>
    		<td align="center" class="tdstyle"><input class="tdstyle" readonly id= "keys" name="keys" type="text" value='${element.key}'/></td>
    		<td align="center" class="tdstyle"><input class="tdstyle" id= "values" name="values" type="text" value=''/></td>
   		</tr>
		</c:forEach>
		</table>
		<br>
		<br>
		
      <div class="heading headingfontstyle">Reload Default Values</div>
            <div>
                  <table>
                        <tr>
                              <td><input type="radio" name="reloadChoice" id="reloadChoice" value="True" onclick="toggleReloadButton('enable');" /></td><td>Yes</td>
                              <td><input type="radio" name="reloadChoice" id="reloadChoice" value="False" onclick="toggleReloadButton('disable');" checked="checked" /></td><td>No</td>
                        </tr>
                  </table>
                  <br /> <input type="button" id="reloadDefaultValues"
                              name="reloadDefaultValues" value="Reload"
                              onclick="proceedToAPISelection();" disabled="disabled">
            </div>
		<input type="hidden" name="filePath" id="filePath" value='<c:out value="${filePath}"/>' />
	
		<input type="hidden" id="properties" name=properties size="50">

		
 	</form>
 	<form action="getFileList" method="get">
 	<div class="BackBtnStl">
 	<input class="mouseStyle" type="submit" value="Back To Home Page" title="Click here to go back to Home Page">
	</div>
 	</form>
 	</div>
	<div><%@ include file="footer.jsp" %></div>
</html>