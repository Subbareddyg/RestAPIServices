var ProductSearchUrl ="/productsearch/v1/products/search";
var ProductDetailsUrl ="/productdetails/v1/products";
var CategoryProductUrl="/categoryproduct/v1/categories";
var CategoryDetailsUrl="/categorydetails/v1/categories";
var PatternProductDetailsUrl ="/patternproductdetails/v1/childpattern";
var CatalogUrl="/catalog/v1/catalog";
var AdminUrl="/admin/v1/admin";
var numOfAPIs=0;
$(function(){
	$("#selectall").click(function () {
		$('.case').attr('checked', this.checked);
	});
	$(".case").click(function(){

		if($(".case").length == $(".case:checked").length) {
			$("#selectall").attr("checked", "checked");
		} else {
			$("#selectall").removeAttr("checked");
		}

	});
	$("#chkBox").click(function(){
		var apiName = $("#chkBox").val();
		$("[id^=urlChkBox_"+ apiName + "][type=checkbox]").each(function(){
			if($("#chkBox").is(":checked")){
				$(this).attr('disabled', false);
			} else {
				$(this).attr('disabled', true);
			}
		});

	});
});

function setThisLocation(index) {
	var formName = "fileListForm";
	var params = "";
	document.forms[0].location.value = document.getElementById("location_"+index).value;
	postUrl(formName, params);

}

function changeLogLevel() {
	var logLevel = document.getElementById('logLevelSelect');
	var selectedOption = logLevel.options[logLevel.selectedIndex].innerHTML;
	document.getElementById("logLevel").value = selectedOption;

}

function setLogLevel(){
	var finalUrl = createUrl("log");
	var updateUrl ="http://localhost:8080"+finalUrl;
	$.ajax({
		type: 'GET',
		url:  updateUrl,
		success: function (data) {

			document.getElementById('logLevelStatus').innerHTML = "<font color='"+color+"'>Data Saved successfully</font>";
		},
		error: function(data){
			document.getElementById('logLevelStatus').innerHTML = "<font color='"+color+"'>Data is NOT Saved successfully</font>";
		}

		//}
	});
}

function proceedToUpdate()
{
	var chkBox = document.getElementsByName("chkBox");
	var fileName = document.getElementById('filePath').value;
	var propertyList = document.getElementsByName('keys');
	var changedValueList = document.getElementsByName('values');

	var txt = "";
	var i;
	var formName = "proceedForm";

	if(validateForm())
	{

		for (i=0;i<chkBox.length;i++)
		{
			if (chkBox[i].checked)
			{

				txt = txt + propertyList[i].value + "=" + changedValueList[i].value + ",";
			}

		}

		document.getElementById("properties").value = txt;
		params ="fileName="+fileName+"&list="+txt;
		postUrl(formName,params);
	}
}

function proceedToUpdateValidation()
{

	var formName = "proceedValidationForm";
	var params = "";
	postUrl(formName,params);

}

function postUrl(formName, params) {
	document.getElementById(formName).submit();
}

function validateForm()
{
	var c=document.getElementsByTagName('input');
	for (var i = 0; i<c.length; i++){
		if (c[i].type=='checkbox')
		{
			if (c[i].checked){
				return true;
			}

		}
	}
	document.getElementById("selectMsg").innerHTML="Please select atleast ONE check box";
	return false;
}

$(function(){
	$("#selectall").click(function () {
		$('.case').attr('checked', this.checked);

	});
	$(".case").click(function(){

		if($(".case").length == $(".case:checked").length) {
			$("#selectall").attr("checked", "checked");
		} else {
			$("#selectall").removeAttr("checked");
		}

	});
});

//The following method was used before making the links server specific.
/*function updateApi()
{


	var chkBox = document.getElementsByName("chkBox");
	var apis = document.getElementsByName("api");

	if(validateForm())
	{
		document.getElementById("selectMsg").innerHTML="";
		for (var i=0;i<chkBox.length;i++)
		{

			if (chkBox[i].checked)
			{ 

				var apiName = apis[i].value;

				update(createUrl(apiName), i);
			}
		}
		document.getElementById("selectall").checked = false;
	}
}*/

function updateApi()
{
	var chkBox = $("[id^=urlChkBox_][type=checkbox]");
	var apiURLs = $("[name^=apiURL][type=hidden]");
	
	numOfAPIs = $(":checkbox:checked").length;

	if(validateForm())
	{
		toggleUpdateButton("disable");
		document.getElementById("selectMsg").innerHTML="";
		for (var i=0;i<chkBox.length;i++)
		{

			if (chkBox[i].checked)
			{ 
				update(apiURLs[i].value, i);
			}
		}
	}
}

function updateApiForValidation()
{
	var choices = document.forms[0].moduleChoice;
	for(var i=0;i<choices.length;i++) {
		if(choices[i].selected)
			return true;
	}
	document.getElementById("selectMsg").innerHTML="Please select atleast ONE radio";
	return false;
}

function proceedToAPISelection(){
	document.forms[0].action = "proceedToUpdate";
	document.forms[0].submit();
}

function createUrl(apiName)
{
	var fileName = "";
	var logLevel = "";
	var logUrl = "";
	var finalUrl="";
	var commonUrl="";

	if(document.getElementById('fileName').value != ''){
		fileName = document.getElementById('fileName').value;
		var propertiesList = document.getElementById('propList').value;

		commonUrl = "/configuration?filename="+fileName+"&propertieslist="+propertiesList;
	}
	else {
		logLevel = document.getElementById('logLevel').value;

		logUrl = "/configuration?loglevel=" + logLevel;
		commonUrl = logUrl;

	}

	if(apiName=="ProductSearch")
	{
		finalUrl = ProductSearchUrl+commonUrl;

	}
	else if(apiName=="ProductDetails")
	{
		finalUrl = ProductDetailsUrl+commonUrl;

	}
	else if(apiName=="CategoryProduct")
	{
		finalUrl = CategoryProductUrl+commonUrl;

	}
	else if(apiName=="CategoryDetails")
	{
		finalUrl = CategoryDetailsUrl+commonUrl;

	}
	else if(apiName=="PatternProductDetails")
	{
		finalUrl = PatternProductDetailsUrl+commonUrl;

	}
	else if(apiName=="Catalog") 
	{
		finalUrl = CatalogUrl+commonUrl;

	}
	else if(apiName=="Admin")  
	{
		finalUrl = AdminUrl+commonUrl;

	}

	console.log(finalUrl);
	return finalUrl;

}

function update(finalUrl, index)
{
	
	var updateUrl = finalUrl;
	var chkBox = $("[id^=urlChkBox_][type=checkbox]");
	var status = $('div[id^="result"]');
	var encryptedAppId = document.getElementById('encryptedAppId').value;
	var encryptedKeyParams = document.getElementById('encryptedKeyParams').value;
	
	updateUrl = updateUrl + "&encryptedAppId=" + encodeURIComponent(encryptedAppId) + "&encryptedKeyParams=" + encodeURIComponent(encryptedKeyParams);
	$.ajax({
		type: 'GET',
		url:  updateUrl,
		success: function (data) {

			if (chkBox[index].type=='checkbox')
			{
				if (chkBox[index].checked){
					var color = "#009900";
					status[index].innerHTML = "<font color='"+color+"'>Success</font>";
					chkBox[index].checked=false;

				}

			}
			ajaxServed();    
		},
		error: function(data){
			var errorMsg = "";
			if(data.responseText.indexOf('11525') > -1 ) {
				errorMsg = "Expired token.";
			} else if (data.responseText.indexOf('11526') > -1) {
				errorMsg = "Invalid token.";
			}
			if (chkBox[index].type=='checkbox')
			{
				if (chkBox[index].checked){
					var color = "#FF0000";
					status[index].innerHTML = "<font color='"+color+"'>Failure. " + errorMsg + "</font>";
					chkBox[index].checked=false;
				}

			}
			ajaxServed();    
		}


	});

}

function toggleUpdateButton(option){
	if(option=='enable'){
		$("[name^=update][type=button]").attr('disabled', false);
	} else {
		$("[name^=update][type=button]").attr('disabled', true);
	}
}

function toggleReloadButton(option){
	if(option=='enable'){
		$("#reloadDefaultValues").attr('disabled', false);
	} else {
		$("#reloadDefaultValues").attr('disabled', true);
	}
}

function ajaxServed()
{
	numOfAPIs = numOfAPIs - 1;
	if(numOfAPIs==0){
		toggleUpdateButton('enable');
	}
}