
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand_report";
}



function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {

	   		displayBrandList(data);  
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS
//----workign in this function
function displayBrandList(data){
	var $tbody = $('#brand-report-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}


//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getBrandList);
}

$(document).ready(init);
$(document).ready(getBrandList);

