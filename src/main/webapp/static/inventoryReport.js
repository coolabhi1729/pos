
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory_report";
}




function getInventoryList(){
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);  
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayInventoryList(data){
	var $tbody = $('#inventory-report-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.brand+ '</td>'
		+ '<td>' + e.category+ '</td>'
		+ '<td>' + e.quantity+ '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getInventoryList);
}

$(document).ready(init);
$(document).ready(getInventoryList);

