
function getSalesUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/sales_report";
}

function submitSales(event){
	//Set the values to update
	var $form = $("#sales-report-form");
	var json = toJson($form);
	var url = getSalesUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   	if (!$.trim(response)){   
		    console.log("What follows is blank: " + response);
		}
		else{   
		    console.log("What follows is not blank: " + response);
		}
	   		displaySalesList(response);
	   },
	   error: handleAjaxError
	});

	return false;
}

//UI DISPLAY METHODS
//----workign in this function
//function argument changes to response from data.

function displaySalesList(response){
	var $tbody = $('#sales-report-table').find('tbody');
	$tbody.empty();
	for(var i in response){
		var e = response[i];
		var row = '<tr>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + e.revenue + '</td>'//use help from order.js
		+ '</tr>';
        $tbody.append(row);
	}
}


//INITIALIZATION CODE
function init(){
	$('#submit-data').click(submitSales);
}

$(document).ready(init);
//$(document).ready(getSalesList);

