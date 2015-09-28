//window.localStorage.clear();
window.localStorage.home = "<a href='index.html'>Home</a>"
var baseURL="";

$(document).ready(function () {
 window.onload = $.getScript("media/javascript/jquery.i18n.properties-min-1.0.9.js", function(){
		   $.i18n.properties( {
			    name: 'hellomessage',
			    path: 'message_properties/',
			    mode: 'both',
			    callback: function(){
					baseURL = Base_url;
				}
			} );
			baseURL = Base_url;
			play();
		});
		
});

var localurl = 'http://10.60.202.143:8080/Belk/media/javascript/';

    productSearch = "v1/products/search?format=json&limit=20&",
    productDetail = "v1/products?format=json&",
    categoryDetails = "v1/categories/",
    subCategoryDetails = "v1/categories/";
	maincategory = "v1/categories/4294859844/subcategories?format=json";
	patternProduct = "v1/childpattern?productcode=";
	childProductDetail = "v1/products?styleupc=";
	storeLocator = "v1/stores?zipcode=";
	storeLocatorWithState = "v1/stores?state=";
	productFinderWithZip = "v1/productfinder/products?zipcode=";
	productFinderWithCity = "v1/productfinder/products?city=";
var MainBreadCrum = "";
	//resize div 
	function resize(){ 
		var maxHeight = 0,tmp; 
		$('.productBox').each(function(){ 
		tmp=$(this).height(); 
		if (maxHeight < tmp) {  

		maxHeight = tmp; 
		} 
		}); 
		$('.productBox').css('height', maxHeight+"px"); 
		
		
		 $('.tabs_nav li:first').addClass('active');
			$('.tab_content:first').addClass('active');
			
			$('.tab_content').hide();
			$('.tabs_nav li a').on('click', function (e) {
				e.preventDefault();
				var selected = $(this).attr('href');
				$('.tabs_nav li').removeClass('active');
				$(this).parent().addClass('active');


				$('.tab_content').removeClass('active');
				$(selected).addClass('active');


			});
		
		$("#mainNav li a").on("click", function () {
			window.localStorage.clear();
			window.localStorage.setItem("mainCate", "<a href='"+$(this).attr('href')+"'>"+$(this).text()+"</a>");
	}); 
	$('#cateList li a').on('click', function(){
		var temp = window.localStorage.getItem('mainCate');
		var temp2 = window.localStorage.getItem('subCate');
		if(temp != null && temp2!= null){
			window.localStorage.setItem("subsubCate","<a href='"+$(this).attr('href')+"'>"+$(this).text()+"</a>");
		}
	}); 
	$('#brand li a').on('click', function(){
		var temp = window.localStorage.getItem('mainCate');
		if(temp != null){
			window.localStorage.setItem("subCate","<a href='"+$(this).attr('href')+"'>"+$(this).text()+"</a>");
		}else{
		}
	}); 
	$('#btnsearch').on('click', function(){
		window.localStorage.clear();
	}); 	
	
	
	}
	
/*------------javascript--------------*/
var prodname =[];
$(document).ready(function () {
	//resize product wrapper
	var imgheights = $(".qv_image").map(function () {
        return $(this).height();
    }).get();

    var divheights = $(".info").map(function () {
        return $(this).height();
    }).get();

    imgmaxHeight = Math.max.apply(null, imgheights);
    divmaxHeight = Math.max.apply(null, divheights);

    $('.productBox').css('height', divmaxHeight + imgmaxHeight + 40 + "px");
	
	
	//Append query string dynamically
    $(".nav li a").on("click", function (e) {
        e.preventDefault();
        window.location = this.href + "?q=" + $.trim($(this).text());
    });

    //Show hide response data div
	$(document.body).append("<div class='toggle'>Toggle</div>");
    $('.toggle').on('click', function (e) {
        e.stopPropagation();
        if ($('#response').is(":visible")) {
            $('#response').hide();

        } else {
            $('#response').show();
        }
    });
	
	 $('.basic_close1').on('click', function (e) {
		
        e.stopPropagation();
        if ($('#response').is(":visible")) {
            $('#response').hide();

        } else {
            $('#response').show();
        }
    });

	//Limit the product name to limited character
    $(".info h3 a").each(function (i) {
        len = $(this).text().length;
        var txt = $(this).text().substr(0, 50);
        $(this).html(txt + '...');
    });

    //Toggle menu for iphone and ipad devices
    $('.togglemenu').on('click', function (e) {
        e.stopPropagation();
        if ($(this).next('.nav').is(":visible")) {
            $(this).next('.nav').hide();

        } else {
            $(this).next('.nav').show();
        }
    });

	//Dropdown menu for iphone and smartphones
    $('.sidemenu h3').on('click', function (e) {
        e.stopPropagation();
        if ($(this).next('ul').is(":visible")) {
            $(this).next('ul').hide();

        } else {
            $(this).next('ul').show();
        }
    });

	
	
	
});



	
	//Get query string
	function GetQueryStringParams(){
		var oResult = {};
		var oResult1 = {};
		var temp1 = [];
		var c = 0;
		var aQueryString = (window.location.search.substr(1)).split("&");
			for(var i = 0; i < aQueryString.length; i++){
			
				if(aQueryString[i].indexOf("=") != -1){
				
				
					temp1[c] = aQueryString[i];
					
					c++;
					
				}else{
			
					var temptemp = aQueryString[i-1]+"&"+aQueryString[i];
					temp1[c-1] = temptemp;
				
				}
			}
		
			
			for (var i = 0;i < temp1.length; i++){
			
				var aTemp1 = temp1[i].split("=");
				
				if(aTemp1 != ""){
					
					if (aTemp1[1].length > 0) {
						oResult1[aTemp1[0]] = decodeURI(aTemp1[1]);
						
						var spliting = oResult1[aTemp1[0]].split(" ");
						
					}
				}
			}
			return oResult1;
	}
	var projid = GetQueryStringParams();

function play(){	
	
	//dynamic product page
			$.ajax({
			type: 'GET',
			url :  "/"+categoryDetails+'4294859844/subcategories?format=JSON',
			dataType: "json",
			
			success: function (data) {
				var category = data.categories;
				var insertData = "";
				
				$.each(category, function(key, val){
					var catarray = val.subCategories;
					var n = catarray.length-1;
					for(var j=n;j>=0;j--){
						insertData += "<li><a href='product.html?categoryId="+catarray[j].categoryId+"&name="+catarray[j].name.replace('&', "")+"'>"+catarray[j].name+"</a></li>";
					}
					$('#mainNav ul').html(insertData);
				});
				resize();
			},
			error: function (e) {
				console.log("faliur");
			}
			
		});


	
	
//product listing data
if (window.location.href.indexOf("product-list") > -1) {
    $.ajax({
        type: 'GET',
        url:  "/" + productSearch +"refinementid="+ projid.categoryId,
        dataType: 'json',
        success: function (data) {
            var jsonData = data;
            var flag = false;
			var prodCode = [];
			function resize(){ 
                var maxHeight = 0,tmp; 
                $('.productBox').each(function(){ 
                tmp=$(this).height(); 
                if (maxHeight < tmp) {  

                maxHeight = tmp; 
                } 
                }); 
                $('.productBox').css('height', maxHeight+"px"); 
			}


		    $.each(data.category, function (key, val) {
				if(val.categoryId == projid.categoryId){
					
					for(var j in val.products){
					prodCode.push(val.products[j].productCode);
					}
				}
			});
			
			
			var productArray= {};
			$.getJSON("/" + productSearch +"q="+ projid.search, function(jsonData){
				$.each(jsonData.products, function (key, val) {
					if($.inArray(val.productCode, prodCode )!= -1){
						var insertData = "<div class='col span_3_of_12 productBox'>";
						insertData += "<div class='image qv_image'><img src='" + val.productAttributes[0].value + "' />";
						insertData += "<div class='info'><h3><a href='product-details.html?vendorId=" + val.productCode + "'>" + val.name + "</a></h3>";
						insertData += "<p class='price'>$" + val.productPrice[0].value + "</p></div>";
						insertData += "</div>";
						$('#productlist').append(insertData);
						flag = true;
						$('#response').append("<li class='lists'>" + val.name + "</li>");
					}

				});	
					$('#plcount').append($('.qv_image').length);
					$('#plname').append(projid.name);
					
					 if (flag == false) {
					$('.noofitems').html("No Record found for " + projid.name);
					}
					resize();
			});
            var divs = $(".productBox");
            for (var i = 0; i < divs.length; i += 4) {
                divs.slice(i, i + 4).wrapAll("<div class='prodSec clearfix'></div>");
            }
			resize();
        },
        error: function (e) {
		

        }
    });
}


//Search Listing
if (window.location.href.indexOf("search-list") > -1) {

    $.ajax({
        type: 'GET',
        url:  "/" + productSearch +"q="+ projid.search,
        dataType: 'json',
        success: function (data) {
            var product = data;
			var searchCount = product.searchReport.totalProducts;
            var flag = false;
				
			var getData = projid.search.split('+');
			for (var i =0; i<getData.length; i++){
				var regEx = new RegExp(getData[i], "gi");
			}
			$(document.body).append('<div id="response"></div>');
			$('#response').html("<button class='basic_close1' style='margin: -20px -22px 10 0;' onclick='closetoggle();'>&nbsp;</button>");
			$('#response').append("<h2>Search search Listing for :" + projid.search + " </h2>");	
		   if(product.products.length == 0){
				 flag = false;
		   }else{
			 flag = true;
		   }
            $.each(product.products, function (key, val) {
					var price = "";
					var listprice = "";
                    var insertData = "<div class='col span_3_of_12 productBox'>";
					if(val.productType =="Outfit"){
						if(val.productAttributes[0].value == " "){

							insertData += "<div class='image qv_image'><a href='product-details.html?productCode=" + val.productCode + "'><img src='http://www.whiskersproject.org/wp-content/uploads/2011/03/BelkSp1.jpg' /></a><div class='collection'></div></div>";
						}else{
							
							insertData += "<div class='image qv_image'><a href='product-details.html?productCode=" + val.productCode + "'><img style='border-bottom:none;' src='" + val.productAttributes[0].value + "' /></a><div class='collection'></div></div>";
						}
					}else{
						if(val.productAttributes[0].value == " "){
							
							insertData += "<div class='image qv_image'><a href='product-details.html?productCode=" + val.productCode + "'><img src='http://www.whiskersproject.org/wp-content/uploads/2011/03/BelkSp1.jpg' /></a></div>";
						}else{
							
							insertData += "<div class='image qv_image'><a href='product-details.html?productCode=" + val.productCode + "'><img src='" + val.productAttributes[0].value + "' /></a></div>";
						}
					}
					
					
                    insertData += "<div class='info'><h3><a href='product-details.html?productCode=" + val.productCode + "'>" + val.name + "</a></h3>";
					for(var j=0;j<val.productPrice.length;j++){
						if(val.productPrice[j].key == 'salePrice' || val.productPrice[j].key == 'salePriceRange'){
							price =  val.productPrice[j].value;
						}
					}
					if(price.indexOf('$') === -1){
						insertData += "<p class='price'>$" +price + "</p></div>";
					}else{
						insertData += "<p class='price'>" +price + "</p></div>";
					}
					
                    insertData += "</div>";
                    $('#myjson').append(insertData);
                    $('#response').append("<li class='lists'>" + val.name + "</li>");
                
            });
			var searchVal = "";
            $('#pcount').append(searchCount);
			if(projid.search.indexOf('+') === -1){
				searchVal = projid.search;
			}else{
				searchVal = projid.search.replace('+',' ');
			}
            $('#pname').append(searchVal);
            $('#pname_breadcrum').append(searchVal);

            if (flag == false) {
                $('.noofitems').html("No Record found for " + searchVal);
            }
            var divs = $(".productBox");
            for (var i = 0; i < divs.length; i += 4) {
                divs.slice(i, i + 4).wrapAll("<div class='prodSec clearfix'></div>");
            }	
        },
        error: function (e) {
        }
		
    });
	

}

//product page

				$.ajax({
				type: 'GET',
				url:  "/" + subCategoryDetails + projid.categoryId + "/subcategories?format=JSON",
				dataType: 'json',
				success: function (data) {
				var jsonData = data;
				var categ = jsonData.categories;
				$(document.body).append('<div id="response"></div>');
					
					if (window.location.href.indexOf("product") > -1) {
					$.each(categ, function (key, val) {
						
						if (val.parentCategoryId == projid.categoryId) {
						$('#cateTitle').html(projid.name);
						$('#response').html("<button class='basic_close1' style='margin: -20px -22px 10 0;' onclick='closetoggle();'>&nbsp;</button>");
						$('#response').append("<h2>Sub Category :</h2>");
						$('#response').append("<li class='lists'>" + val.name + "</li>");
						$('.active').html(projid.name);
						$('#cateList').append("<li><a href='product-list.html?categoryId="+val.categoryId+"&name="+ val.name+"'>"+val.name+ "</a></li>");
						
						$('#verticalhero').attr("src", "http://s7d4.scene7.com/is/image/Belk/mens_Hero_wk43_112413?&$P_CONTENT$");
						$('#verticalBlockFirst').attr("src", "http://s7d4.scene7.com/is/image/Belk/mens_P1_wk42_112313?&$P_CONTENT$");
						$('#verticalBlockSec').attr("src", "http://s7d4.scene7.com/is/image/Belk/mens_P2_wk42_111713?&$P_CONTENT$");
						$('#feature1').attr("src", "http://s7d4.scene7.com/is/image/Belk/mens_F1_wk42_111713?&$P_CONTENT$");
						$('#feature2').attr("src", "http://s7d4.scene7.com/is/image/Belk/mens_F2_wk42_111713?&$P_CONTENT$");
						$('#feature3').attr("src", "http://s7d4.scene7.com/is/image/Belk/mens_F3_wk42_111713?&$P_CONTENT$");
						$('#recommend1').attr("src", "http://s7d4.scene7.com/is/image/Belk?layer=0&src=3202380_T21797_A_803_T10L00&layer=comp&$P_PROD$");
						$('#recommend2').attr("src", "http://s7d4.scene7.com/is/image/Belk?layer=0&src=3201602_4386427_A_440_T10L00&layer=comp&$P_PROD$");
						$('#recommend3').attr("src", "http://s7d4.scene7.com/is/image/Belk?layer=0&src=3203216_110235_A_001_T10L00&layer=comp&$P_PROD$");
						$('#recommend4').attr("src", "http://s7d4.scene7.com/is/image/Belk?layer=0&src=3202670_LELA12CX0206_A_600_T10L00&layer=comp&$P_PROD$");

						}else{
							if(categ[0].parentCategoryId == "4294859844"){
								MainBreadCrum=projid.name;
							}else{
								MainBreadCrum+=projid.name;
							}
							$('#response').html("<button class='basic_close1' style='margin: -20px -22px 10 0;' onclick='closetoggle();'>&nbsp;</button>");
							$('#response').append("<h2>Sub Category :</h2>"+ val.name+"");
							$('#cateTitle').html(projid.name);
							$('#cateList').html("");
						}
						$('#bread_crumb').html(window.localStorage.home + " > " + window.localStorage.getItem('mainCate')); 
					});
					}
				if (window.location.href.indexOf("product-list") > -1|| (window.location.href.indexOf("search") > -1)) {
					MainBreadCrum+=projid.name;
					var mainCat = localStorage.getItem('mainCatName');
					$('#cateTitle').html(projid.name);
					$('#response').append("<h2>Sub Category :</h2>");
					$('#response').append("<h2>Category Product : </h2>");
					$('.active').html(projid.name);
					$('#bread_crumb').html(window.localStorage.home + " > " + window.localStorage.getItem('mainCate')+ " > " + window.localStorage.getItem('subCate')); 
					var subcate = categ;
					var updatecat = "";
					var getData = projid.categoryId;
						
					
					$.each(subcate, function (key, val) {
						$.each(val.subCategories, function(kays, vals){
							if (vals.parentCategoryId == getData) {
								updatecat += "<li><a href='product-list-cat.html?categoryId="+vals.categoryId+"&name="+ vals.name+"'>" + vals.name + "</a></li>";
								$('#response').append("<li class='lists'>" + vals.name + "</li>");
								}
						});
				
					});
					$('#cateList').html("");
					$('#cateList').append(updatecat);
				
				}
				resize();
				
			},
			error: function (e) {
				console.log("failure");
			}
		});
	
		


//product detail page data
if (window.location.href.indexOf("product-details") > -1) {
	var productType = "";
	var childProductId = new Array();
	var count = 0;
    $.ajax({
        type: 'GET',
		url:  "/" + productDetail +"productcode="+ projid.productCode,
        dataType: 'json',
        success: function (data) {
            var productDetail = data;
			$(document.body).append('<div id="response"></div>');
			if(productDetail.products[0].productType == 'Outfit' || productDetail.products[0].productType == 'Pattern' ){
				 $.each(productDetail.products, function (key, val) {
                var insertData = "";
				$('#response').html("<button class='basic_close1' style='margin: -20px -22px 10 0;' onclick='closetoggle();'>&nbsp;</button>");
				$('#response').append("<h2>Product Name :</h2>"+val.name+"");
                insertData += "<h1>" + val.name + "</h1>";
                $('#productName').append(insertData);
				$('#regular').append(val.longDescription);
				if(productDetail.products[0].productType == 'Pattern'){
				var hiddenOutfitVal = projid.productCode+","+val.brand+","+val.name;
					var insertDataGoToBelk = "";
					insertDataGoToBelk +="<div style='margin: 0px 90px 130px 20px;'>";
					insertDataGoToBelk +="<div class='go_to_belk'>";
					insertDataGoToBelk +="<form name='cartid2' onsubmit='cart(this)' target='_blank' id='cartid2'";
					insertDataGoToBelk +="action='http://www.belk.com/AST/Main/Belk_Primary/PRD~"+projid.productCode+"/"+val.brand+"+"+val.name+".jsp'";
					insertDataGoToBelk +=">";
					insertDataGoToBelk +="<input type='submit' id='add_cart' name='add_cart' value=' ' title='Go To Belk Site'/>";
					insertDataGoToBelk +="<input type='hidden' value='"+hiddenOutfitVal+"' id='hid_val' name='hid_val' />";
					insertDataGoToBelk +="<input type='hidden' value='pattern' id='form_name' name='form_name' />";
					insertDataGoToBelk +="</form>";
					insertDataGoToBelk +="</div>";
					
					insertDataGoToBelk +="<form onsubmit='#basic'>";
					insertDataGoToBelk +="<div class='product_store'>";
					insertDataGoToBelk +="<input class='basic_open' type='button' value=' ' id='btnlink' title='Find In Store'/>";
					insertDataGoToBelk +="</div>";
					insertDataGoToBelk +="</form>";
					insertDataGoToBelk +="</div>";
					$('#patternGoToBelk').html(insertDataGoToBelk);
				}
				if(productDetail.products[0].productType == 'Outfit'){
					var hiddenOutfitVal = projid.productCode+","+val.brand+","+val.name;
					var insertDataGoToBelk = "";
					insertDataGoToBelk +="<div style='margin: 0px 90px 130px 20px;'>";
					insertDataGoToBelk +="<div class='go_to_belk'>";
					insertDataGoToBelk +="<form name='cartid1' onsubmit='cart(this)' target='_blank' id='cartid1'";
					insertDataGoToBelk +="action='http://www.belk.com/AST/Main/Belk_Primary/PRD~"+projid.productCode+"/"+val.brand+"+"+val.name+".jsp'";
					insertDataGoToBelk +=">";
					insertDataGoToBelk +="<input type='submit' id='add_cart' name='add_cart' value=' ' title='Go To Belk Site'/>";
					insertDataGoToBelk +="<input type='hidden' value='"+hiddenOutfitVal+"' id='hid_val' name='hid_val' />";
					insertDataGoToBelk +="<input type='hidden' value='outfit' id='form_name' name='form_name' />";
					insertDataGoToBelk +="</form>";
					insertDataGoToBelk +="</div>";
					
					insertDataGoToBelk +="<form onsubmit='#basic'>";
					insertDataGoToBelk +="<div class='product_store'>";
					insertDataGoToBelk +="<input class='basic_open' type='button' value=' ' id='btnlink' title='Find In Store'/>";
					insertDataGoToBelk +="</div>";
					insertDataGoToBelk +="</form>";
					insertDataGoToBelk +="</div>";
					$('#outfitGotToBelk').html(insertDataGoToBelk);
				}
				
				var backLinkValue = "";
				if(typeof projid.name === 'undefined'){
					backLinkValue="Search";
				}else{
					backLinkValue=projid.name;
				}
                $('#pname_detail').append(backLinkValue);
                var child = productDetail.products;
				
				productType = child[0].productType;
				if(child[0].childProducts.childProduct.length > 0)
				{
				
				for(var i=0;i<child[0].childProducts.childProduct.length;i++){
					childProductId[i] = child[0].childProducts.childProduct[i].value;
				}
				}
                var skuarray = [];
                var colors = [];
                var uniqueNames = [];
                for (var i = 0; i < child.length; i++) {
                    var skus = child[i].skus;
					if(skus[0].skuImages.productSKUImages[0].skuImageAttribute[0].value == " "){
						$('#zoom_image_main').append("<img src='http://www.whiskersproject.org/wp-content/uploads/2011/03/BelkSp1.jpg' />");
					}else{
						$('#zoom_image_main').append("<img src='" + skus[0].skuImages.productSKUImages[0].skuImageAttribute[0].value + "'/>");
					}
                    for (var j in skus) {
                        var skuattr = skus[j].skuMainAttributes;
                        for (var sma in skuattr) {
                            skuarray[skuattr[sma].key] = skuattr[sma].value;

                        }
                        colors.push(skuarray.Color);
                        $.each(colors, function (i, el) {
                            if ($.inArray(el, uniqueNames) === -1)
                                uniqueNames.push(el);
                        });  
                    }
                   
                }
				
				resize();
            });
			}else if(productDetail.products[0].productType == 'Regular'){
            $.each(productDetail.products, function (key, val) {
			$('#response').append("<h2>Product Name :</h2>"+val.name+"");
                var insertData = "";
				var insertDatadiv = "";
			insertDatadiv +="<div class='sel_options clearfix' id='details_div' style='clear:both;width:400px'>";
			insertDatadiv +="<div class='sel_size ' id='div_size_1' style='width:350px'>";
			insertDatadiv +="<label for='size'>Select Size</label>";
			insertDatadiv +="<select id='size' name='ADD_CART_ITEM&lt;&gt;sku_size'>";
			insertDatadiv +="</select>";
            insertDatadiv +=" </div>";
			insertDatadiv +=" <div id='div_color_1' class='sel_color' style='width:350px'>";
			insertDatadiv +="<label for='size'>Select Color</label>";
			insertDatadiv +="<span id='selectColors' class='current'></span> ";
			insertDatadiv +="<input type='hidden' name='bmSingle' value='ADD_CART_ITEM&lt;&gt;sku_color'>";
			insertDatadiv +="<select id='sel_color' class='sel_color_dropdown clearfix' name='ADD_CART_ITEM&lt;&gt;sku_color' style='display:none;'>";
			insertDatadiv +="<option value='-1'>Please Select</option>";
            insertDatadiv +="<option value='961' selected=''>White/Blue</option>";
            insertDatadiv +="<option value='960' selected=''>White/Pink</option>";
            insertDatadiv +="<option value='962'>White/Purple</option>";
            insertDatadiv +="</select>";
			insertDatadiv +="</div>";
			insertDatadiv +="<!-- end color -->	";
			insertDatadiv +="<div class='quantity clearfix' style='width:350px'>";
			insertDatadiv +="<label for='quantity'>Select Quantity</label>";
			insertDatadiv +="<div class='control'>";
			insertDatadiv +="<select id='quantity' name='ADD_CART_ITEM&lt;&gt;quantity' style='opacity: 1;'>";
			insertDatadiv +="<option value='1'>1</option>";
			insertDatadiv +="<option value='2'>2</option>";
			insertDatadiv +="<option value='3'>3</option>";
			insertDatadiv +="<option value='4'>4</option>";
            insertDatadiv +="<option value='5'>5</option>";
            insertDatadiv +="<option value='6'>6</option>";
            insertDatadiv +="<option value='7'>7</option>";
            insertDatadiv +="<option value='8'>8</option>";
			insertDatadiv +="<option value='9'>9</option>";
			insertDatadiv +="</select>";
			insertDatadiv +="</div>";
			insertDatadiv +="<p id='inv_level' class='inv_level' style='display: none;'></p>";
			insertDatadiv +="<p id='online_msg' class='online_msg' style='display: none;'></p>";
			
			var hidVal = val.productCode+","+val.name+","+val.brand;
			insertDatadiv +="<div class='go_to_belk'>";
			insertDatadiv +="<form name='cartid' onsubmit='cart(this)' target='_blank' id='cartid'";
			insertDatadiv +="action='http://www.belk.com/AST/Main/Belk_Primary/PRD~"+val.productCode+"/"+val.brand+"+"+val.name+".jsp'";
			insertDatadiv +=">";
			
			//insertDatadiv +="<form name='cartid' id='cartid' onsubmit='cart(this)' target='_blank'>";
			insertDatadiv +="<input type='submit' id='add_cart' name='add_cart' value=' ' title='Go To Belk Site'/>";
			insertDatadiv +="<input type='hidden' value='"+hidVal+"' id='hid_val' name='hid_val' />";
			insertDatadiv +="</form>";
			insertDatadiv +="</div>";
			insertDatadiv +="<form onsubmit='#basic'>";
			insertDatadiv +="<div class='product_store'>";
			insertDatadiv +="<input class='basic_open' type='button' value=' ' id='btnlink' title='Find In Store'/>";
			insertDatadiv +="</div>";
			insertDatadiv +="</form>";
			
			var hidden_skus = "";
			
			hidden_skus = val.vendorId+"."+val.vendorPartNumber;
			var insert_product_finder = "";

			insert_product_finder += "<div class='findStore'>"; 
            insert_product_finder +="<form name='form_store_locator_new' id='formsearchNew'>";
			insert_product_finder += "<input name='hidden_sku' type='hidden' id='hidden_sku' value='"+hidden_skus+"'/>";
				insert_product_finder +="<div id='findByZip'> ";
            insert_product_finder +="<div class='inputWrap'>";
			insert_product_finder +="<input type='radio' name='findByZipOrStateRadio' id='findByZipOrStateRadio_0' value='radio' checked='checked' onchange='storeradioOfPopup();'>Find by ZipCode</input>";
			insert_product_finder +="<lable style='margin: 0px 0px 0px 50px;' >Enter ZIP</lable><input style='height: 5px;width: 12%;' id='findStoreInput' type='text' name='STORE_SEARCHzipcode1' value='' />";  
			insert_product_finder +="<lable style='margin: 0px 0px 0px 10px;' >Enter Miles</lable><input style='margin: 0px 0px 0px 10px;height: 5px;width: 15%;'  id='findStoreInputMiles' type='text' name='findStoreInputMiles' value='' />";  
            insert_product_finder +="</div>";
			insert_product_finder +="<br>";
			insert_product_finder +="</div>";
			insert_product_finder +="<div class='findByCity'>";				
			insert_product_finder +="<div class='inputWrap'>";
            insert_product_finder +="<input type='radio' name='findByZipOrStateRadio' id='findByZipOrStateRadio_1' value='radio' onchange='storeradioOfPopup();'>Find by State and City</input>";
			insert_product_finder +="<lable style='margin: 0px 0px 0px 20px;' >Enter City</lable><input style='margin: 0px 0px 0px 10px;height: 5px;width: 15%;'  id='findStoreCityInput' type='text' name='findStoreCityInput' value='' disabled='disabled'/>";
			
			
			
			insert_product_finder +="<select name='findStoreStateInput' disabled='disabled' style='margin: 0px 0px 0px 20px;float: right;height: 25px;'>";
			insert_product_finder +="<option value=''>--SELECT STATE--</option>";
            insert_product_finder +="<option value='AL'>Alabama</option>";
            insert_product_finder +="<option value='AR'>Arkansas</option>";
            insert_product_finder +="<option value='FL'>Florida</option>"
            insert_product_finder +="<option value='GA'>Georgia</option>";
			insert_product_finder +="<option value='KY'>Kentucky</option>";
			insert_product_finder +="<option value='LA'>Louisiana</option>";
			insert_product_finder +="<option value='MD'>Maryland</option>";
			insert_product_finder +="<option value='MO'>Missouri</option>";
			insert_product_finder +="<option value='MS'>Mississippi</option>";
			insert_product_finder +="<option value='NC'>North Carolina</option>";
			insert_product_finder +="<option value='OK'>Oklahoma</option>";
			insert_product_finder +="<option value='SC'>South Carolina</option>";
			insert_product_finder +="<option value='TN'>Tennessee</option>";
			insert_product_finder +="<option value='TX'>Texas</option>";
			insert_product_finder +="<option value='VA'>Virginia</option>";
			insert_product_finder +="<option value='WV'>West Virginia</option>";
            insert_product_finder +="</select>";
			
			
			
            insert_product_finder +="</div>";
            insert_product_finder +="<div id='storesearchmiles'>";
			
			
			
			insert_product_finder +="</div>";
			insert_product_finder +="</div>";
			insert_product_finder +="<br>";
            insert_product_finder +="<div class='inputWrap1'>";
			insert_product_finder +="<input type='button' class='btn' id='findStoreLink' onclick='regularpopupfun(this)' value=' '/>";
			insert_product_finder +="</div>";
            insert_product_finder +="</form>";
            insert_product_finder +="</div>";
			insert_product_finder +="<div id='jsregulardetails'>";
			insert_product_finder += "</div>";
			
			
			$('#regular_product_finder').html(insert_product_finder);
			insertDatadiv +="</div>";
			insertDatadiv +="</div>";
			$('#regular').append(insertDatadiv);
			insertData += "<h1>" + val.name + "</h1>";
			if(val.productPrice[3].value == val.productPrice[0].value){
				insertData += "<p class='price standardProduct' id='p_price'><span class='price' style='color:red;'>Price $" + val.productPrice[3].value +"</span></p>";
			}else{
				insertData += "<p class='price standardProduct' id='p_price'><span class='price' style='color:red;'>Price $" + val.productPrice[3].value + " <span style='color:black;'>Original Price: $" + val.productPrice[0].value + "</span>" + "</span></p>";
			}
            
			
			var desc = "";
			
			
			desc +=	 "<div id='prod_detail_tabs' class='tabs clearfix' style='clear:both;'>";
			desc +=  "<ul class='tabs_nav clearfix'>";
			desc +=    "<li class='active'><a href='#description_panel'>Description</a></li>";
			desc +="<li class=''><a href='#shipping_and_returns_tab'>Shipping &amp; Returns</a></li>";
			desc +="</ul>";
			desc +="<div id='description_panel' class='tab_content'></div>";
			desc +="<div id='shipping_and_returns_tab' class='tab_content '>";
			desc +="Shipping &amp; Returns<br>	";
			desc +="<p>Shipping your purchase is easy on belk.com. Depending on the destination of your package, you can expect it to arrive within 4-7 business days.  <a href='#'><b>Shipping Information</b></a> </p>";
			desc +="<p>Returns are easy on belk.com. <a href='#'><b>Easy returns</b></a></p>";
			desc +="</div>";
			desc +="</div>";
			 $('#descRegular').append(desc);
			 $('#description_panel').append(val.longDescription);
                $('#productName').append(insertData);
				var backLinkValue = "";
				if(typeof projid.name === 'undefined'){
					backLinkValue="Search";
				}else{
					backLinkValue=projid.name;
				}
                $('#pname_detail').append(backLinkValue);
                var child = productDetail.products;
				productType = child[0].productType;
				if(child[0].childProducts.childProduct.length > 0)
				{
				for(var i=0;i<child[0].childProducts.childProduct.length;i++){
					childProductId[i] = child[0].childProducts.childProduct[i].value;
				}
				}
                var skuarray = [];
                var colors = [];
                var uniqueNames = [];
                for (var i = 0; i < child.length; i++) {
                    var skus = child[i].skus;
					if(skus[0].skuImages.productSKUImages[0].skuImageAttribute[0].value == " "){
						$('#zoom_image_main').append("<img src='http://www.whiskersproject.org/wp-content/uploads/2011/03/BelkSp1.jpg' />");
					}else{
						$('#zoom_image_main').append("<img src='" + skus[0].skuImages.productSKUImages[0].skuImageAttribute[0].value + "'/>");
					}
                    for (var j in skus) {
                        var skuattr = skus[j].skuMainAttributes;
                        for (var sma in skuattr) {
                            skuarray[skuattr[sma].key] = skuattr[sma].value;

                        }
                        colors.push(skuarray.Color);
                        $.each(colors, function (i, el) {
                            if ($.inArray(el, uniqueNames) === -1)
                                uniqueNames.push(el);
                        });
                        if(skuarray.size){
                        $('#size').append("<option value='30041' selected=''>" + skuarray.size + "</option>");
                        }
                        else {
                        $('#size').append("<option value='30041' selected=''>Not Available</option>");
                        }
                        
                    }
					var col = "";
					col += "<select onchange='handleProductDetailsColor(this);'>";
					for(var r=0;r<uniqueNames.length;r++){
						for(var p=0;p<data.products[0].skus.length;p++){
							for(var q=0;q<data.products[0].skus[p].skuMainAttributes.length;q++){
								if(data.products[0].skus[p].skuMainAttributes[q].key == 'Color'){
									if(data.products[0].skus[p].skuMainAttributes[q].value == uniqueNames[r]){
										img = data.products[0].skus[p].skuImages.productSKUImages[0].skuImageAttribute[0].value;
									}
						
								}
			
							}
						}
			
						col +="<option value='"+img+"'>"+uniqueNames[r]+"</option>";
					}
					col += "</select>";
                    if(uniqueNames==''){
						$('#color').append("<label for='size'>Select Color</label>");
						$('#color').append("<span id='selectColors' class='current'></span>");
                    	$('#selectColors').append("Not Available");
                    }else {
					$('#color').append("<label for='size'>Select Color</label>");
					$('#color').append("<span id='selectColors' class='current'></span>");
                    $('#selectColors').append(col);
                    }
                }
				resize();
            });
			}
			resize();
            var divs = $(".productBox");
            for (var i = 0; i < divs.length; i += 4) {
                divs.slice(i, i + 4).wrapAll("<div class='prodSec clearfix'></div>");
            }
			if(childProductId.length > 0){
				$('#response').append("<h2> Child Product Name :</h2>");
			}
	for(var i=0;i<childProductId.length;i++){
		if(productType == 'Pattern'){
		$.ajax({
        type: 'GET',
		url:  "/" + patternProduct +childProductId[i]+"&producttype="+productType+"&format=JSON",
        dataType: "json",
        success: function (data) {
		resize();
		var childProduct = data.products[0];
		var insertData="";
		$('#response').append("<li class='lists'>" + childProduct.name + "</li>");
		insertData +="<div class='sel_options zoomable secondary clearfix'> ";
		insertData +="<div class='image_wrap col span_2_of_12'>";
		insertData +="<a rel='secondary8455244420519843' class='zoom_image' href='javascript:void(0);'><img style='width:99px;' alt='"+childProduct.name+"' src='"+childProduct.skus[0].skuImages.productSKUImages[0].skuImageAttribute[0].value+"'/></a>";
		insertData +="</div>";
		insertData +="<div class='col span_9_of_12'>";
		insertData +="<div class='title'><h2>"+childProduct.name+"</h2></div>";

		insertData +="<div class='priceWrapper' id='div_price_1' style='width:300px'>";
		var sale="";
		var listPrice="";
		for(var z=0;z<childProduct.productPrice.length;z++){
			if(childProduct.productPrice[z].key == 'salePrice'){
				sale = childProduct.productPrice[z].value;
			}
			if(childProduct.productPrice[z].key == 'listPrice'){
				listPrice = childProduct.productPrice[z].value;
			}
		}
		if(sale == listPrice){
			insertData +="<p class='patern_product_price' style='margin-top:10px'><span class='sale_price' id='p_price3' style='font-size:125%;color:red;'>Price $"+sale+"</span>";
		}else{
			insertData +="<p class='patern_product_price' style='margin-top:10px'><span class='sale_price' id='p_price3' style='font-size:125%;color:red;'>Sale Price $"+sale+"</span>";
			insertData +="<span class='original_price' style='margin-top:10px'> Original Price $"+listPrice+"</span></p> ";
		}
		insertData +="<p class='price_valid_until assortmentPatten'></p>";
		insertData +="</div><div class='details'></div>";
		insertData +="<p class='patern_product_price' style='margin-top:10px'><span class='sale_price' id='p_price3' style='font-size:100%;'>Select Quantity:</span>";
		insertData +="<select>";
		insertData +="<option>--SELECT--</option>";
		insertData +="<option>1</option>";
		insertData +="<option>2</option>";
		insertData +="<option>3</option>";
		insertData +="<option>4</option>";
		insertData +="<option>5</option>";
		insertData +="<option>6</option>";
		insertData +="</select>";
		insertData +="</p>";
		
		insertData +="<form onsubmit='#basic'>";
			insertData +="<div class='product_store'>";
			insertData +="<input class='basic_open' type='button' value=' ' id='btnlink' title='Find In Store'/>";
			insertData +="</div>";
			insertData +="</form>";
			
		/*insertData += "<a class='basic_open' style='margin:0px 80px 0px 0px;float: right;padding: 10px 10px 10px 0px;font-weight:bold' href='#basic' title='Find In Store'>Find in store</a>";*/
		insertData +="<div class='options'>";
		insertData +="<div id='store_link_3' class='control find_in_store' style='display: block;'>";
		insertData +="<div class='wht_bl'></div><div class='wht_tl'></div><div class='wht_br'></div><div class='wht_tr'></div></div> </div>";
		insertData +="</div></div>";
		$('#cdiv').append(insertData);
		
		
		var hidden_skus = "";
			hidden_skus = childProduct.vendorId+"."+childProduct.vendorPartNumber;
	
		
		var insert_product_finder = "";

			insert_product_finder += "<div class='findStore'>"; 
            insert_product_finder +="<form name='form_store_locator_new' id='formsearchNew'>";
			insert_product_finder += "<input name='hidden_sku' type='hidden' id='hidden_sku' value='"+hidden_skus+"'/>";
				insert_product_finder +="<div id='findByZip'> ";
            insert_product_finder +="<div class='inputWrap'>";
			insert_product_finder +="<input type='radio' name='findByZipOrStateRadio' id='findByZipOrStateRadio_0' value='radio' checked='checked' onchange='storeradioOfPopup();'>Find by ZipCode</input>";
			insert_product_finder +="<lable style='margin: 0px 0px 0px 50px;' >Enter ZIP</lable><input style='height: 5px;width: 12%;' id='findStoreInput' type='text' name='STORE_SEARCHzipcode1' value='' />";  
			insert_product_finder +="<lable style='margin: 0px 0px 0px 10px;' >Enter Miles</lable><input style='margin: 0px 0px 0px 10px;height: 5px;width: 15%;'  id='findStoreInputMiles' type='text' name='findStoreInputMiles' value='' />";  
            insert_product_finder +="</div>";
			insert_product_finder +="<br>";
			insert_product_finder +="</div>";
			insert_product_finder +="<div class='findByCity'>";				
			insert_product_finder +="<div class='inputWrap'>";
            insert_product_finder +="<input type='radio' name='findByZipOrStateRadio' id='findByZipOrStateRadio_1' value='radio' onchange='storeradioOfPopup();'>Find by State and City</input>";
			insert_product_finder +="<lable style='margin: 0px 0px 0px 20px;' >Enter City</lable><input style='margin: 0px 0px 0px 10px;height: 5px;width: 15%;'  id='findStoreCityInput' type='text' name='findStoreCityInput' value='' disabled='disabled'/>";
			
			
			insert_product_finder +="<select name='findStoreStateInput' disabled='disabled' style='margin: 0px 0px 0px 20px;float: right;height: 25px;'>";
			insert_product_finder +="<option value=''>--SELECT STATE--</option>";
            insert_product_finder +="<option value='AL'>Alabama</option>";
            insert_product_finder +="<option value='AR'>Arkansas</option>";
            insert_product_finder +="<option value='FL'>Florida</option>"
            insert_product_finder +="<option value='GA'>Georgia</option>";
			insert_product_finder +="<option value='KY'>Kentucky</option>";
			insert_product_finder +="<option value='LA'>Louisiana</option>";
			insert_product_finder +="<option value='MD'>Maryland</option>";
			insert_product_finder +="<option value='MO'>Missouri</option>";
			insert_product_finder +="<option value='MS'>Mississippi</option>";
			insert_product_finder +="<option value='NC'>North Carolina</option>";
			insert_product_finder +="<option value='OK'>Oklahoma</option>";
			insert_product_finder +="<option value='SC'>South Carolina</option>";
			insert_product_finder +="<option value='TN'>Tennessee</option>";
			insert_product_finder +="<option value='TX'>Texas</option>";
			insert_product_finder +="<option value='VA'>Virginia</option>";
			insert_product_finder +="<option value='WV'>West Virginia</option>";
            insert_product_finder +="</select>";
			
			
			
            insert_product_finder +="</div>";
            insert_product_finder +="<div id='storesearchmiles'>";
			
			
			
			insert_product_finder +="</div>";
			insert_product_finder +="</div>";
			insert_product_finder +="<br>";
            insert_product_finder +="<div class='inputWrap1'>";
			insert_product_finder +="<input type='button' class='btn' id='findStoreLink' onclick='regularpopupfun(this)' value=' '/>";
			insert_product_finder +="</div>";
            insert_product_finder +="</form>";
            insert_product_finder +="</div>";
			insert_product_finder +="<div id='jsregulardetails'>";
			insert_product_finder += "</div>";
		
		
		
		$('#regular_product_finder').html(insert_product_finder);
		resize();
		},
        error: function (e) {
			console.log("errors in child")
            $('.noofitems').append("No Product found");
        }
    });
	}
	var cc=1;
	if(productType == 'Outfit'){
		$.ajax({
        type: 'GET',
		url:  "/"+childProductDetail+childProductId[i]+"&format=JSON",
        dataType: "json",
        success: function (data) {
		resize();
		var color = new Array();
		var colorCount = 0;
		var size = new Array();
		var sizeCount = 0;
		var insertData1="";
		for(var p=0;p<data.products[0].skus.length;p++){
			for(var q=0;q<data.products[0].skus[p].skuMainAttributes.length;q++){
				if(data.products[0].skus[p].skuMainAttributes[q].key == 'Color'){
					color[colorCount]=data.products[0].skus[p].skuMainAttributes[q].value
					colorCount = colorCount + 1;
				}
				if(data.products[0].skus[p].skuMainAttributes[q].key == 'size'){
					size[sizeCount]=data.products[0].skus[p].skuMainAttributes[q].value
					sizeCount = sizeCount + 1;
				}
			
			}
		}
		
		Array.prototype.contains = function(v) {
			for(var i = 0; i < this.length; i++) {
				if(this[i] === v) return true;
			}
			return false;
		};
		Array.prototype.unique = function() {
			var arr = [];
			for(var i = 0; i < this.length; i++) {
				if(!arr.contains(this[i])) {
					arr.push(this[i]);
				}
			}
		return arr; 
		}
		var uniques = color.unique();
		var uniquSize = size.unique();
		insertData1="";
		
		$('#response').append("<li class='lists'>" + data.products[0].name+ "</li>");
		insertData1 +="<div class='sel_options zoomable secondary clearfix' > ";
		insertData1 +="<div class='image_wrap col span_2_of_12' id='pimg"+data.products[0].productCode+"'>";
		insertData1 +="<a rel='secondary8455244420519843' class='zoom_image' href='javascript:void(0);'><img style='width:99px;' alt='"+data.products[0].name+"' src='"+data.products[0].skus[0].skuImages.productSKUImages[0].skuImageAttribute[0].value+"'/></a>";
		insertData1 +="</div>";
		insertData1 +="<div class='col span_9_of_12'>";
		insertData1 +="<div class='title'><h2>"+data.products[0].name+"</h2></div>";
		
		insertData1 +="<div class='priceWrapper' id='div_price_1'>";

		insertData1 +="<p class='patern_product_price'><span class='sale_price' id='p_price3'></span>";
		if(data.products[0].productPrice[3].value == data.products[0].productPrice[2].value){
			insertData1 +="<span class='original_price'><lable style='margin: 0px 0px;'>PRICE:</lable>  "+data.products[0].productPrice[3].value+" </span></p> ";
		}else{
			insertData1 +="<span class='original_price'><lable style='margin: 0px 0px;'>PRICE:</lable>  "+data.products[0].productPrice[3].value+" </span></p> ";
		}
		

		insertData1 +="<p class='price_valid_until assortmentPatten'></p>";
		insertData1 +="</div><div class='details'></div>";

		insertData1 +="<div class='options selSize'>";
		
		insertData1 +="<lable>SELECT SIZE</lable><select id='sel'>";
		insertData1 +="<option>--SELECT--</option>";
		for(var t=0;t<uniquSize.length;t++){
			insertData1 +="<option>"+uniquSize[t]+"</option>";
		}
		insertData1 +="</select></div>";
		insertData1 +="<div id='store_link_3' class='selColor control find_in_store' style='display: block;'>";
		insertData1 +="<lable>SELECT COLOR: </lable>";
		var img = "";
		insertData1 +="<select onchange='handleColor(this);'>";
		for(var r=0;r<uniques.length;r++){
			for(var p=0;p<data.products[0].skus.length;p++){
				for(var q=0;q<data.products[0].skus[p].skuMainAttributes.length;q++){
					if(data.products[0].skus[p].skuMainAttributes[q].key == 'Color'){
						if(data.products[0].skus[p].skuMainAttributes[q].value == uniques[r]){
							img = data.products[0].skus[p].skuImages.productSKUImages[0].skuImageAttribute[0].value;
						}
						
					}
			
				}
			}
			
			insertData1 +="<option value='"+img+","+data.products[0].productCode+"'>"+uniques[r]+"</option>";
		}
		insertData1 +="</select>";
		insertData1 +="</div>";
		insertData1 +=	"<div id='proddetail_tabs' class='tabs' style='width: 96%;'>";
		insertData1 +="<p class='childProdDetail'>";
		insertData1 +="<button type='button' value='"+data.products[0].productCode+"' onclick='toggleDesc(this);'>Product Description</button>";
		insertData1 +="</p>";
		insertData1 +="<div id='description_tab"+data.products[0].productCode+"' class='sel_options secondary_desc' style='margin-left: 0px; display:none;'>";
		insertData1 +=data.products[0].longDescription+"<br>";
		insertData1 +="<br>";
		
		
			
		
		
		insertData1 +="<a href='product-details.html?productCode="+data.products[0].productCode+"' class='prodDesc'><lable style='color:blue'>View Complete Product Details</lable></a></div>";
		insertData1 +="</div>";
		
		insertData1 +="";
		var hidVal1 = data.products[0].productCode+","+data.products[0].name+","+data.products[0].brand;
			insertData1 +="<div class='go_to_belk'>";
			insertData1 +="<form name='cartid1' target='_blank' id='cartid1'";
insertData1 +="action='http://www.belk.com/AST/Main/Belk_Primary/PRD~"+data.products[0].productCode+"/"+data.products[0].brand+"+"+data.products[0].name+".jsp'";
			insertData1 +=">";
			insertData1 +="<input type='submit' id='add_cart1' name='add_cart1' value=' ' title='Go To Belk Site'/>";
			insertData1 +="<input type='hidden' value='"+hidVal1+"' id='hid_val1' name='hid_val1' />";
			insertData1 +="</form>";
			insertData1 +="</div>";
			
			
			insertData1 +="<form onsubmit='#basic'>";
			insertData1 +="<div class='product_store'>";
			insertData1 +="<input class='basic_open' type='button' value=' ' id='btnlink' title='Find In Store'/>";
			insertData1 +="</div>";
			insertData1 +="</form>";
			
			
		/*	insertData1 +="<a class='basic_open' style='margin:0px 80px 0px 0px;float: right;padding: 10px 10px 10px 0px;font-weight:bold' href='#basic' title='Find In Store'>Find in store</a>";*/
		insertData1 +="<div class='wht_bl'></div><div class='wht_tl'></div><div class='wht_br'></div><div class='wht_tr'></div></div> </div>";
		insertData1 +="</div></div>";
		

		$('#cdiv').append(insertData1);
		var hidden_skus = "";
			
			hidden_skus = data.products[0].vendorId+"."+data.products[0].vendorPartNumber;
			var insert_product_finder = "";

			insert_product_finder += "<div class='findStore'>"; 
            insert_product_finder +="<form name='form_store_locator_new' id='formsearchNew'>";
			insert_product_finder += "<input name='hidden_sku' type='hidden' id='hidden_sku' value='"+hidden_skus+"'/>";
			insert_product_finder +="<div id='findByZip'> ";
            insert_product_finder +="<div class='inputWrap'>";
			insert_product_finder +="<input type='radio' name='findByZipOrStateRadio' id='findByZipOrStateRadio_0' value='radio' checked='checked' onchange='storeradioOfPopup();'>Find by ZipCode</input>";
			insert_product_finder +="<lable style='margin: 0px 0px 0px 50px;' >Enter ZIP</lable><input style='height: 5px;width: 12%;' id='findStoreInput' type='text' name='STORE_SEARCHzipcode1' value='' />";  
			insert_product_finder +="<lable style='margin: 0px 0px 0px 10px;' >Enter Miles</lable><input style='margin: 0px 0px 0px 10px;height: 5px;width: 15%;'  id='findStoreInputMiles' type='text' name='findStoreInputMiles' value='' />";  
            insert_product_finder +="</div>";
			insert_product_finder +="<br>";
			insert_product_finder +="</div>";
			insert_product_finder +="<div class='findByCity'>";				
			insert_product_finder +="<div class='inputWrap'>";
            insert_product_finder +="<input type='radio' name='findByZipOrStateRadio' id='findByZipOrStateRadio_1' value='radio' onchange='storeradioOfPopup();'>Find by State and City</input>";
			insert_product_finder +="<lable style='margin: 0px 0px 0px 20px;' >Enter City</lable><input style='margin: 0px 0px 0px 10px;height: 5px;width: 15%;'  id='findStoreCityInput' type='text' name='findStoreCityInput' value='' disabled='disabled'/>";
			
			
			
			insert_product_finder +="<select name='findStoreStateInput' disabled='disabled' style='margin: 0px 0px 0px 20px;float: right;height: 25px;'>";
			insert_product_finder +="<option value=''>--SELECT STATE--</option>";
            insert_product_finder +="<option value='AL'>Alabama</option>";
            insert_product_finder +="<option value='AR'>Arkansas</option>";
            insert_product_finder +="<option value='FL'>Florida</option>"
            insert_product_finder +="<option value='GA'>Georgia</option>";
			insert_product_finder +="<option value='KY'>Kentucky</option>";
			insert_product_finder +="<option value='LA'>Louisiana</option>";
			insert_product_finder +="<option value='MD'>Maryland</option>";
			insert_product_finder +="<option value='MO'>Missouri</option>";
			insert_product_finder +="<option value='MS'>Mississippi</option>";
			insert_product_finder +="<option value='NC'>North Carolina</option>";
			insert_product_finder +="<option value='OK'>Oklahoma</option>";
			insert_product_finder +="<option value='SC'>South Carolina</option>";
			insert_product_finder +="<option value='TN'>Tennessee</option>";
			insert_product_finder +="<option value='TX'>Texas</option>";
			insert_product_finder +="<option value='VA'>Virginia</option>";
			insert_product_finder +="<option value='WV'>West Virginia</option>";
            insert_product_finder +="</select>";
			
			
			
            insert_product_finder +="</div>";
            insert_product_finder +="<div id='storesearchmiles'>";
			
			
			
			insert_product_finder +="</div>";
			insert_product_finder +="</div>";
			insert_product_finder +="<br>";
            insert_product_finder +="<div class='inputWrap1'>";
			insert_product_finder +="<input type='button' class='btn' id='findStoreLink' onclick='regularpopupfun(this)' value=' '/>";
			insert_product_finder +="</div>";
            insert_product_finder +="</form>";
            insert_product_finder +="</div>";
			insert_product_finder +="<div id='jsregulardetails'>";
			insert_product_finder += "</div>";
	
	
	
		$('#regular_product_finder').html(insert_product_finder);
		
		resize();
		},
        error: function (e) {
			console.log("errors in child")
            $('.noofitems').append("No Product found");
        }
		});
		
	}
	
	}
        },
        error: function (e) {
            $('.noofitems').append("No Product found");
        }
    });
	
}



//Test
			$.ajax({
			type: 'GET',
			url: "/"+subCategoryDetails+projid.categoryId+'/subcategories?format=JSON',
			dataType: "json",
			success: function (data) {
				resize();
				var category = data.categories;
				var insertData = "";
				$.each(category, function(key, val){
					var catarray = val.subCategories;
					for(var j in catarray){
						insertData += "<li id="+catarray[j].categoryId+"><a href='product-list.html?categoryId="+catarray[j].categoryId+"&name="+catarray[j].name.replace('&', " ")+"'>"+catarray[j].name+ "</a> </li>";
					}
					resize();
					$('#brand').html(insertData);
					$('#cname').append(projid.name);
				});
				resize();
			},
			error: function (e) {
				console.log("faliur");
			}
			
		});
		
		
		
		
		
//Search Listing
if (window.location.href.indexOf("product-list-cat") > -1) {

    $.ajax({
        type: 'GET',
        url:  "/" + productSearch +"refinementid="+ projid.categoryId,
        dataType: 'json',
        success: function (data) {
		
            var product = data;
			var searchCount = product.searchReport.totalProducts;
            var flag = false;	
			$('#response').html("<button class='basic_close1' style='margin: -20px -22px 10 0;' onclick='closetoggle();'>&nbsp;</button>");
            $('#response').append("<h2>Sub Category:" + projid.name + " </h2>");
            $.each(product.products, function (key, val) {
                    var insertData = "<div class='col span_3_of_12 productBox'>";
						if(val.productType =="Outfit"){
							
							insertData += "<div class='image qv_image'><a href='product-details.html?productCode=" + val.productCode + "&name="+projid.name+"'><img style='border-bottom:none;' src='" + val.productAttributes[0].value + "' /><div class='collection'></div></a></div>";
						}else{
							insertData += "<div class='image qv_image'><a href='product-details.html?productCode=" + val.productCode + "&name="+projid.name+"'><img src='" + val.productAttributes[0].value + "' /></a></div>";
						}
                    insertData += "<div class='info'><h3><a href='product-details.html?productCode=" + val.productCode + "&name="+projid.name+"'>" + val.name + "</a></h3>";
					
					if(val.productPrice[0].value.indexOf('$') === -1){
						insertData += "<p class='price'>$" + val.productPrice[0].value + "</p></div>";
					}else{
						insertData += "<p class='price'>" + val.productPrice[0].value + "</p></div>";
					}
                    insertData += "</div>";
                    $('#myjson').append(insertData);
                    flag = true;

                    $('#response').append("<li class='lists'>" + val.name + "</li>");
            });
            $('#ccount').append(searchCount);
            $('#cname').append(projid.search);
            $('#cname_breadcrum').append(projid.search);
			$('#bread_crumb').html(window.localStorage.home + " > " + window.localStorage.getItem('mainCate')+ " > " + window.localStorage.getItem('subCate')); 
            if (flag == false) {
                $('.noofitems').html("No Record found for " + projid.search);
            }
            var divs = $(".productBox");
            for (var i = 0; i < divs.length; i += 4) {
                divs.slice(i, i + 4).wrapAll("<div class='prodSec clearfix'></div>");
            }
			
        },
        error: function (e) {
        }
		
    });
	

}

}
$(function(){
  $('#toggleDesc').click(function(){
     $('#description_tab').toggle();
  });
});
function toggleDesc(v){
			var id = "#description_tab"+v.value;
     $(id).toggle();
}
function closetoggle(){
		if ($('#response').is(":visible")) {
            $('#response').hide();

        } else {
            $('#response').show();
        }
}
function regularpopupfun(form){
var v = document.getElementById("formsearchNew");
var zip_code="";
var miles="";
var city="";
var state="";
var vendorstyles="";
var radio1=document.getElementById("findByZipOrStateRadio_0");
var radio2=document.getElementById("findByZipOrStateRadio_1");
for(var i=0; i<v.elements.length; i++) {
     var element = v.elements[i];
	 if(element.name == "hidden_sku"){
		vendorstyles = element.value;
	 }
	 if(element.name == "STORE_SEARCHzipcode1"){
		zip_code = element.value;
	 }
	 if(element.name == "findStoreInputMiles"){
		miles = element.value;
	 }
	 if(element.name == "findStoreCityInput"){
		city = element.value;
	 }
	 if(element.name == "findStoreStateInput"){
		state = element.value;
	 }
	
}

  if ( radio1.checked == true){
		var numbers = /^[0-9]+$/;  
		if((zip_code.match(numbers) && miles.match(numbers)) && (zip_code != "" && miles != "")){
				$.ajax({
			type: 'GET',
			//url:'/BelkAPI/storefinder.json',
			url:"/"+productFinderWithZip+zip_code+"&result=10&format=json&vendorstyles="+vendorstyles+"&radius="+miles,
			dataType: "json",
			success: function (data) {
				var insertData="<div id='stores'>";
				
			var count =1;
			if(data.storeInfo == null){
				insertData +="<h3 style='color:red;'>No Stores Found</h3>";
			}else{
				insertData += "<ul>";
			 $.each(data.storeInfo, function (key, val) {
				insertData += "<li>";
				insertData += "<ul>";
				
				insertData += "<li class='l1'>";
				insertData += count;
				insertData += "</li>";
				
				insertData += "<li class='l3'>";
				insertData += "<a href='#'>";
				insertData += val.name;
				insertData += "</a><br>";
				insertData += "<address>";
				insertData += val.address.line1;
				insertData += "<br>";
				insertData += val.address.city+" , "+val.address.state+" "+val.address.zip;
				insertData += "</address>";
				if(val.phone == null){
					insertData += "Ph: "+"";
				}else{
					insertData += "Ph: "+val.phone;
				}
				
				insertData += "</li>";
				
				insertData += "<li class='distance'>";
				if(val.distance == null){
					insertData += " ";
				}else{
					insertData += val.distance;
				}
				
				insertData += "</li>";
				
				insertData += "<li class='l6'>";
				
				insertData += "</li>";
				
				insertData += "</ul>";
				insertData += "</li>";
				count = count+1;
			 });
			 insertData += "</ul>";
			}
			insertData += "</div>";
				
				$('#jsregulardetails').html(insertData);
			},
			error: function (e) {
				console.log("faliur inside js");
				console.log(e);
				var insertData="<div id='stores'>";
				insertData +="<h3 style='color:red;'>no stores found</h3>";
				insertData += "</div>";
				$('#jsregulardetails').html(insertData);
				
			}
			
		});
	
	  }
	  else{
		if(!(zip_code.match(numbers) && miles.match(numbers))){
			
			$('#jsregulardetails').html("<h3 style='color:red;margin: 120px 0px 0px 0px;'>Only numbers are allowed</h3>");	
		}
		if(miles == ""){
			
			$('#jsregulardetails').html("<h3 style='color:red;margin: 120px 0px 0px 0px;'>Please enter the miles</h3>");
		}
		if(zip_code == ""){
		
			$('#jsregulardetails').html("<h3 style='color:red;margin: 120px 0px 0px 0px;'>Please enter the zip code</h3>");	
		}
	  }
	}
else if(radio2.checked == true){
	var alph = /^([a-zA-Z]+\s)*[a-zA-Z]+$/;  
      if((city.match(alph) && state != "") && (city != "" && state != "")){
		$.ajax({
        type: 'GET',
       url:"/"+productFinderWithCity+city+"&result=10&format=json&vendorstyles="+vendorstyles+"&state="+state,
	  // url:'/BelkAPI/storeFinder_city.json',
        dataType: 'json',
        success: function (data) {
			var insertData="";
			var count =1;
			insertData += "<div id='stores'>";
			if(data.storeInfo == null){
				insertData += "<h3 style='color:red;'>No Stores Found</h3>";
			}else{
				insertData += "<ul>";
			 $.each(data.storeInfo, function (key, val) {
				insertData += "<li>";
				insertData += "<ul>";
				
				insertData += "<li class='l1'>";
				insertData += count;
				insertData += "</li>";
				
				insertData += "<li class='l3'>";
				insertData += "<a href='#'>";
				insertData += val.name;
				insertData += "</a><br>";
				insertData += "<address>";
				insertData += val.address.line1;
				insertData += "<br>"
				insertData += val.address.city+" , "+val.address.state+" "+val.address.zip;
				insertData += "</address>";
				if(val.phone == null){
					insertData += "Ph: "+"";
				}else{
					insertData += "Ph: "+val.phone;
				}
				
				insertData += "</li>";
				
				insertData += "<li class='distance'>";
				if(val.distance == null){
					insertData += " ";
				}else{
					insertData += val.distance;
				}
				
				insertData += "</li>";
				
				insertData += "<li class='l6'>";
				
				insertData += "</li>";
				
				insertData += "</ul>";
				insertData += "</li>";
				count = count+1;
				
			 });
			 insertData += "</ul>";
			}
			 insertData += "</div>";
			$('#jsregulardetails').html(insertData);
		},
		error: function (e) {
				console.log("faliur inside js");
				console.log(e);
				var insertData="<div id='stores'>";
				insertData +="<h3 style='color:red;'>no stores found</h3>";
				insertData += "</div>";
				$('#jsregulardetails').html(insertData);
				
			}
		});
	
	  
	  }else{
		if(!(city.match(alph) && state.match(alph))){
			$('#jsregulardetails').html("<h3 style='color:red;margin: 120px 0px 0px 0px;'>Only alphabets are allowed</h3>");
			
		}
		if(state == ""){
			$('#jsregulardetails').html("<h3 style='color:red;margin: 120px 0px 0px 0px;'>Please select a state</h3>");	
		}
		if(city == ""){
			$('#jsregulardetails').html("<h3 style='color:red;margin: 120px 0px 0px 0px;'>Please enter the city name</h3>");	
		}
	  }
}


}
function cart(form){
var v = form;
var product_data="";
var form_name="";
for(var i=0; i<v.elements.length; i++) {
     var element = v.elements[i];
	 if(element.name == "hid_val"){
		product_data = element.value;
	 }
	 if(element.name == "form_name"){
		form_name = element.value;
	 }
}
var arr_product = product_data.split(",");
if(form_name == 'outfit'){
	
	document.cartid1.action = "http://www.belk.com/AST/Main/Belk_Primary/PRD~"+arr_product[0]+"/"+arr_product[2]+"+"+arr_product[1]+".jsp";
}else if(form_name == 'pattern'){
	document.cartid2.action = "http://www.belk.com/AST/Main/Belk_Primary/PRD~"+arr_product[0]+"/"+arr_product[2]+"+"+arr_product[1]+".jsp";
}else{
	document.cartid.action = "http://www.belk.com/AST/Main/Belk_Primary/PRD~"+arr_product[0]+"/"+arr_product[2]+"+"+arr_product[1]+".jsp";
}
}
function popitup() {
var url="index.html";
var p1 = "scrollbars=no,resizable=no,status=no,location=no,toolbar=no,menubar=no";
var p2 = "width=50,height=40,left=50,top=50";

	newwindow=window.open(url,'name',p1+p2);
}

function handleColor(img){
			var imgUrl = img.value;
			var arr = imgUrl.split(",");
			var val = "#pimg"+arr[1];
			var im ="<a rel='secondary8455244420519843' class='zoom_image' href='javascript:void(0);'><img style='width:99px;' alt='no image' src='"+arr[0]+"'/></a>";
			$(val).html(im);
}
function handleProductDetailsColor(img){
			var imgUrl = img.value;
			var val = "#zoom_image_main";
			var im ="<img src='"+imgUrl+ "'/>";
			$(val).html(im);
}		

//Resize product divs based on image and info height
$(window).on('load resize', function () {
    resize();
});

function callb(frm){
	var v = document.getElementById("formsearchNew");
  var radius = "";
  var zip = "";
  var city = "";
  var state = "";
  var radio1=document.getElementById("findByZipOrStateRadio_0");
var radio2=document.getElementById("findByZipOrStateRadio_1");
  for(var i=0; i<v.elements.length; i++) {
     var element = v.elements[i];
	 if(element.name == "STORE_SEARCHzipcode1"){
		zip = element.value;
	 }
	 if(element.name == "STORE_SEARCHradius1"){
		radius = element.value;
	 }
	 if(element.name == "findStoreCityInput"){
		city = element.value;
	 }
	 if(element.name == "findStoreStateInput"){
		state = element.value;
	 }
	}

	if(radio2.checked == true){
	var alph = /^([a-zA-Z]+\s)*[a-zA-Z]+$/;  
	if((city != "" && state != "" && (city.match(alph)))){
			$.ajax({
        type: 'GET',
        url:  "/"+storeLocatorWithState+state+"&city="+city+"&results=10&format=JSON",
	  // url:'/BelkAPI/store_city.json',
        dataType: 'json',
        success: function (data) {
			var insertData="";
			var count =1;
			if(data.stores == null){
				insertData += "<h3 style='color:red;'>No Stores Found</h3>";
			}else{
				insertData += "<ul>";
				$.each(data.stores, function (key, val) {
				insertData += "<li>";
				insertData += "<ul>";
				
				insertData += "<li class='l1'>";
				insertData += count;
				insertData += "</li>";
				
				insertData += "<li class='l3'>";
				insertData += "<a href='#'>";
				insertData += val.name;
				insertData += "</a><br>";
				insertData += "<address>";
				insertData += val.address.line1;
				insertData += "<br>"
				insertData += val.address.city+" , "+val.address.state+" "+val.address.zip;
				insertData += "</address>";
				if(val.phone == null){
					insertData += "Ph: "+"";
				}else{
					insertData += "Ph: "+val.phone;
				}
				insertData += "</li>";
				
				insertData += "<li class='distance'>";
				if(val.distance == null){
					insertData += " ";
				}else{
					insertData += val.distance;
				}
				
				insertData += "</li>";
				
				insertData += "<li class='l6'>";
				
				insertData += "</li>";
				
				insertData += "</ul>";
				insertData += "</li>";
				count = count+1;
				
			 });
			 insertData += "</ul>";
			}
			$('#stores').html(insertData);
		},
		error: function (e) {
				console.log("faliur inside js");
				console.log(e);
				var insertData="";
				insertData +="<h3 style='color:red;font-size: 210%;'>no stores found</h3>";
				$('#stores').html(insertData);
			}
});
	}else{
		if(city == ""){
			alert("city can not be empty");
			var insertData ="";
			$('#stores').html(insertData);
		}
		else if(state == ""){
			alert("Please select a state");
			var insertData ="";
			$('#stores').html(insertData);
		}
      else if(!(city.match(alph) && state != "")){
		alert("Only alphabets are allowed");
		var insertData ="";
		$('#stores').html(insertData);
	  }  
	}
}else if(radio1.checked == true){
	var numbers = /^[0-9]+$/;  
	if(!(!(zip.match(numbers) && radius.match(numbers))) && (zip != "" && radius != "")){
			$.ajax({
        type: 'GET',
        url:  "/"+storeLocator+zip+"&radus="+radius+"&results=10&format=JSON",
	  // url:'/BelkAPI/store.json',
        dataType: 'json',
        success: function (data) {
			var insertData="";
			var count =1;
			if(data.stores == null){
				insertData += "<h3 style='color:red;'>No Stores Found</h3>";
			}else{
						insertData += "<ul>";
			 $.each(data.stores, function (key, val) {
				insertData += "<li>";
				insertData += "<ul>";
				
				insertData += "<li class='l1'>";
				insertData += count;
				insertData += "</li>";
				
				insertData += "<li class='l3'>";
				insertData += "<a href='#'>";
				insertData += val.name;
				insertData += "</a><br>";
				insertData += "<address>";
				insertData += val.address.line1;
				insertData += "<br>"
				insertData += val.address.city+" , "+val.address.state+" "+val.address.zip;
				insertData += "</address>";
				if(val.phone == null){
					insertData += "Ph: "+"";
				}else{
					insertData += "Ph: "+val.phone;
				}
				insertData += "</li>";
				
				insertData += "<li class='distance'>";
				if(val.distance == null){
					insertData += " ";
				}else{
					insertData += val.distance;
				}
				insertData += "</li>";
				
				insertData += "<li class='l6'>";
				
				insertData += "</li>";
				
				insertData += "</ul>";
				insertData += "</li>";
				count = count+1;
				
			 });
			 insertData += "</ul>";
			}
			
			$('#stores').html(insertData);
		},
		error: function (e) {
				console.log("faliur inside js");
				console.log(e);
				var insertData="";
				insertData +="<h3 style='color:red;font-size: 210%;'>no stores found</h3>";
				$('#stores').html(insertData);
			}
	});
	
}else{
		if(zip == ""){
			alert("Zip code can not be empty");
			var insertData ="";
			$('#stores').html(insertData);
		}
		else if(radius == ""){
			alert("Miles can not be empty");
			var insertData ="";
			$('#stores').html(insertData);
		}
		else if(!(zip.match(numbers) && radius.match(numbers))){
			alert("Only numbers are allowed");
			var insertData ="";
			$('#stores').html(insertData);
		}	
	}
}

}
function watermark(inputId,text){
  var inputBox = document.getElementById(inputId);
    if (inputBox.value.length > 0){
      if (inputBox.value == text)
        inputBox.value = '';
    }
    else
      inputBox.value = text;
}
function storeradioOfPopup(){
var insertData ="";
	$('#jsregulardetails').html(insertData);
	if ( document.form_store_locator_new.findByZipOrStateRadio_0.checked == true){
		document.form_store_locator_new.findStoreInputMiles.disabled= false;
		document.form_store_locator_new.STORE_SEARCHzipcode1.disabled= false;
		document.form_store_locator_new.findStoreCityInput.disabled=true;
		document.form_store_locator_new.findStoreStateInput.disabled = true;
		document.form_store_locator_new.findStoreCityInput.value="";
		document.form_store_locator_new.findStoreStateInput.value="";
	}
	else if (form_store_locator_new.findByZipOrStateRadio_1.checked == true){
		document.form_store_locator_new.findStoreStateInput.disabled=false;
		document.form_store_locator_new.findStoreCityInput.disabled = false;
		document.form_store_locator_new.findStoreInputMiles.disabled = true;
		document.form_store_locator_new.STORE_SEARCHzipcode1.disabled = true;
		document.form_store_locator_new.findStoreInputMiles.value="";
		document.form_store_locator_new.STORE_SEARCHzipcode1.value="";
	}
}
function storeradio(){
var insertData ="";
	$('#stores').html(insertData);
	if ( document.form_store_locator_new.findByZipOrStateRadio_0.checked == true){
		document.form_store_locator_new.STORE_SEARCHradius1.disabled= false;
		document.form_store_locator_new.STORE_SEARCHzipcode1.disabled= false;
		document.form_store_locator_new.findStoreCityInput.disabled=true;
		document.form_store_locator_new.findStoreStateInput.disabled = true;
		document.form_store_locator_new.findStoreCityInput.value="";
		document.form_store_locator_new.findStoreStateInput.value="";
	}
	else if (form_store_locator_new.findByZipOrStateRadio_1.checked == true){
		document.form_store_locator_new.findStoreStateInput.disabled=false;
		document.form_store_locator_new.findStoreCityInput.disabled = false;
		document.form_store_locator_new.STORE_SEARCHradius1.disabled = true;
		document.form_store_locator_new.STORE_SEARCHzipcode1.disabled = true;
		document.form_store_locator_new.STORE_SEARCHradius1.value="";
		document.form_store_locator_new.STORE_SEARCHzipcode1.value="";
	}
}
$("#cartid :input").tooltip({
 
      // place tooltip on the right edge
      position: "center right",
 
      // a little tweaking of the position
      offset: [-2, 10],
 
      // use the built-in fadeIn/fadeOut effect
      effect: "fade",
 
      // custom opacity setting
      opacity: 0.7
 
});

$("#formsearchNew :input").tooltip({
 
      // place tooltip on the right edge
      position: "center right",
 
      // a little tweaking of the position
      offset: [-2, 10],
 
      // use the built-in fadeIn/fadeOut effect
      effect: "fade",
 
      // custom opacity setting
      opacity: 0.7
 
});
