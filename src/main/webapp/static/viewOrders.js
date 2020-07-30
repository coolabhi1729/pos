function getViewOrdersUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order/viewOrders";
}



function getViewOrdersList(){
	var url = getViewOrdersUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {

	   		displayViewOrdersList(data);  
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS
//----workign in this function
function displayViewOrdersList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var date = new Date(e.date);
		var buttonHtml = '<a href="/pos_project/api/order/viewOrders/invoice/' + e.id + '" target="_blank" class="btn btn-primary float-right">Generate Invoice</a>';
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td style="text-align:center">' + date + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function genInv(id){
	var url = "/pos_project/api/order/viewOrders/invoice/" + id;
	console.log(fss);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
 	
	   },
	   error: handleAjaxError
	});	

}
//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getViewOrdersList);
}

$(document).ready(init);
$(document).ready(getViewOrdersList);


