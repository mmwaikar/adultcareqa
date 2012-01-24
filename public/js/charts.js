function createOptionTag (name, description) {
    var nameDesc = description + ' - ' + name;
    return '<option value="' + name + '">' + nameDesc + '</option>';
};

function showError (xhr) {
    $('#returnJson').html('Error: ' + xhr.status + ' ' + xhr.statusText);
    alert('error');
};

function populateColumns (data) {
    $.each(data, function(i, row) {
	       if (i !== 0)
		   $('#columnsSelect').append(createOptionTag(row.name, row.description));
	   });
};

function populateYear () {
    for (var year=2002; year<2012; year++) {
	$('#yearSelect').append('<option>'+year+'</option>');
    }
};

function renderChart (data) {
    // FusionCharts.setCurrentRenderer('javascript');
    var myChart = new FusionCharts("js/Column3D.swf",
				   "myChartId", "400", "300", "0", "1" );
    myChart.setJSONData(data);
    myChart.render("chartContainer");
};

$(function() {
      // sufficient to wait for dom ready
      // no need to use $(document).ready(function() {}
      $.getJSON('/qa/def', populateColumns).error(showError);
      populateYear();
});

$(function() {
      $('#drawChartButton').click(
	  function() {
	      var baseUrl = '/qa/charts/period/';
	      var year = $('#yearSelect').val();
	      var cols = $('#columnsSelect').val() || [];
	      var url = baseUrl + year + '/column/' + cols.join('');

	      $.getJSON(url, renderChart).error(showError);
	  });
});

// $(function() {
//       $(document).ready(function() {
// 			    $.ajax({
// 				       url: '/qa/def',
// 				       timeout: 5000,
// 				       type: 'GET',
// 				       dataType: 'json',
// 				       success: function(data) {
// 					   populateColumns(data);					   
// 				       },
// 				       error: function (xhr) {
// 					   showError(xhr);
// 					   $('#returnJson').html('Error: ' + xhr.status + ' ' + xhr.statusText);
// 					   alert('error');
// 				       }
// 				   });
// 			    populateYear();
// 			});
// });

// $(function() {
//       $('#drawChartButton').click(
// 	  function() {
// 	      var baseUrl = '/qa/charts/period/';
// 	      var year = $('#yearSelect').val();
// 	      var cols = $('#columnsSelect').val() || [];
// 	      var url = baseUrl + year + '/column/' + cols.join('');
// 	      var s = "";

// 	      $.ajax({
// 	      		 url: url,
// 	      		 timeout: 5000,
// 	      		 type: 'GET',
// 	      		 dataType: 'json',
// 	      		 success: function(data) {
// 			     renderChart(data);
// 	      		 },
// 	      		 error: function (xhr) {
// 	      		     $('#returnJson').html('Error: ' + xhr.status + ' ' + xhr.statusText);
// 	      		     alert('error');
// 	      		 }
// 	      	     });
// 	  });
// });