"use strict";
app.controller('ReportsController',['$scope','$http','$filter','ngTableParams', 'ngDialog', function($scope,$http,$filter,ngTableParams, ngDialog){
	/* categoryList: List of Book Categories
	 * authorList : List of Book Authors
	 * reportDetails : data of generated report
	 * reportDetailsSum : aggregated data of reportDetails used for Data Visualization
	 * generatedPdf : PDF File in Base64 format
	 * message : Message to be displayed to user
	 */
	$scope.categoryList=[];
	$scope.authorList=[];
	$scope.reportDetails=[];
	$scope.reportDetailsSum={};
	$scope.message='';
	$scope.firstReportEntry=true;
	$scope.selectedIndex=0;
	$scope.generatedPdf='';
	//Ng-Table Section Start
	$scope.booksReportTable = new ngTableParams({
        page: 1,            // show first page
        count: 10,          // count per page
        filter: {
			bookNo:'',
			bookTitle:''
		},
		sorting: {
			'book.bookNo':'asc'
		}
        			
    }, {
       
        getData: function($defer, params) {
        	//Enable Sorting Feature of Ng-Table
        	params.total($scope.reportDetails.length); // length of data
        	var orderedData = params.sorting() ?
                    $filter('orderBy')($scope.reportDetails, params.orderBy()) :
                    $scope.reportDetails;
             
           // Enable Filter Feature of Ng-Table
            var orderedData =$filter('filter')(
    			$filter('filter')(orderedData,
				{book:{bookNo:params.filter().bookNo}}),
				{book:{bookTitle:params.filter().bookTitle}});
            
            $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            
        }
    });
	
	//Ng-Table Section End
	
	/* Called when Data is successfully received from Server
	 * Set PDF data(in Base64 format) received from server to generatedPDF
	 * Show report data in HTML view by setting $scope.reportDetails = data from server's.reportDetails and reloading ngTable
	 * Then draw graph showing aggregated report values by setting $scope.reportDetailsSum = data from server's.reportDetailsSum and calling generateGraph();
	 */
	var successfulDataFetch = function(data){
		if(data.result=="SUCCESS"){	
			$scope.firstReportEntry=false;
			$scope.generatedPdf = data.pdfBytes;
			$scope.reportDetails=data.reportDetails;
			$scope.reportDetailsSum=data.reportDetailsSum;
			$scope.booksReportTable.reload();
			$scope.generateGraph();
		} else{
			$scope.message=data.result+" Please try again.";
		}
	};
	
	//Get Book Category List
	$http.get('jspController/CategoryList.jsp').success(function(data){
		$scope.categoryList=data.categoryList;
		$scope.report.requestCategory=$scope.categoryList[0];
	}).error(function(data,status){
		console.log("Error Occured while getting book category list. Error Code : "+status);
	});
	
	//Get Book Authors List
	$http.get('jspController/AuthorsList.jsp').success(function(data){
		$scope.authorList=data.authorList;
		$scope.report.requestAuthor=$scope.authorList[0];
	}).error(function(data,status){
		console.log("Error Occured while getting book author list. Error Code : "+status);
	});
	
	/* Checks Form Inputs (like from date and to date)
	 * If valid then check which type of report is to be generated(like on basis of book title or author or category or by considering all three)
	 * On basis of request type ajax request is sent and if data is received successfully from server then call successfulDataFetch(data) method
	 */
	$scope.generateReports = function(){
		if(!$scope.fromDate || !$scope.toDate){
			$scope.message="Invalid Date";
			return
		}
		$scope.report.fromDate = convertDate($scope.fromDate);
		$scope.report.toDate = convertDate($scope.toDate);
		if(new Date($scope.report.fromDate)>new Date($scope.report.toDate)){
			$scope.message="From Date cannot be greater than to Date";
			return;
		}
		if($scope.report.requestType=="all"){
			$http.get('jspController/Operator/ReportByAll.jsp',{params:{jsonReport : $filter('json')($scope.report)}}).success(function(data){
				successfulDataFetch(data);
				$scope.selectedIndex=0;
			}).error(function(data,status){
				alert("Error Occured.");
				console.log("Error Occured while getting report by all. Error Code : "+status);
			});
		} else if($scope.report.requestType=="title"){
			if(!$scope.report.requestTitle || $scope.report.requestTitle==""){
				$scope.message="Please Enter Book Title";
				return;
			}
			$http.get('jspController/Operator/ReportByTitle.jsp',{params:{jsonReport : $filter('json')($scope.report)}}).success(function(data){
				successfulDataFetch(data);
				$scope.selectedIndex=1;
			}).error(function(data,status){
				alert("Error Occured.");
				console.log("Error Occured while getting report by title. Error Code : "+status);
			});
		} else if($scope.report.requestType=="author"){
			$http.get('jspController/Operator/ReportByAuthor.jsp',{params:{jsonReport : $filter('json')($scope.report)}}).success(function(data){
				successfulDataFetch(data);
				$scope.selectedIndex=2;
				$scope.selectedValue=$scope.report.requestAuthor;
			}).error(function(data,status){
				alert("Error Occured.");
				console.log("Error Occured while getting report by author. Error Code : "+status);
			});
		} else if($scope.report.requestType=="category"){
			$http.get('jspController/Operator/ReportByCategory.jsp',{params:{jsonReport : $filter('json')($scope.report)}}).success(function(data){
				successfulDataFetch(data);
				$scope.selectedIndex=3;
				$scope.selectedValue=$scope.report.requestCategory;
			}).error(function(data,status){
				alert("Error Occured.");
				console.log("Error Occured while getting report by category. Error Code : "+status);
			});
		}
		$scope.message="";
	};
	
	//Generate PDF. Opens PDF in new dialog using generated PDF (i.e. Pdf in Base64)
	$scope.generatePdf = function(){
		ngDialog.open({
			template: 'partials/Operator/generatedPdf.html',
			className: 'ngdialog-theme-plain',
			scope : $scope				//generatePdf Dialog can access scope variables of Report Controller
		});	
	//	window.open(generatedPdf,'_blank');
	};
		
	//Generates chart on basis of report details sum
	$scope.generateGraph = function(){
		var canvasWidth = 600; //width
	    var canvasHeight = 600;  //height
	    var outerRadius = 200;   //radius
	    var color = d3.scale.category20(); //built in range of colors
	    var dataSet = [];
	    //Calculate sum of all magnitudes for calculating percentage in chart
	    var sumOfMagnitudes = $scope.reportDetailsSum.issuePending + $scope.reportDetailsSum.issueCancelled + $scope.reportDetailsSum.issueClosed + $scope.reportDetailsSum.returnPending + $scope.reportDetailsSum.returnCancelled + $scope.reportDetailsSum.returnClosed; 
	   
	    /* Checks each data element(like issue Pending Count, issue Cancelled Count,etc.). If they are not 0 then add them to dataSet
	     * dataSet is used to draw chart as it contains legend labels and magnitude required to draw chart
	     */
	    if($scope.reportDetailsSum.issuePending!=0){
	    	dataSet.push({"legendLabel":"Issue Pending Count", "magnitude":$scope.reportDetailsSum.issuePending});
	    }
	    if($scope.reportDetailsSum.issueCancelled!=0){
	    	dataSet.push({"legendLabel":"Issue Cancelled Count", "magnitude":$scope.reportDetailsSum.issueCancelled});
	    }
	    if($scope.reportDetailsSum.issueClosed!=0){
	    	dataSet.push({"legendLabel":"Issue Closed Count", "magnitude":$scope.reportDetailsSum.issueClosed});
	    }
	    if($scope.reportDetailsSum.returnPending!=0){
	    	dataSet.push({"legendLabel":"Return Pending Count", "magnitude":$scope.reportDetailsSum.returnPending});
	    }
	    if($scope.reportDetailsSum.returnCancelled!=0){
	    	dataSet.push({"legendLabel":"Return Cancelled Count", "magnitude":$scope.reportDetailsSum.returnCancelled});
	    }
	    if($scope.reportDetailsSum.returnClosed!=0){
	    	dataSet.push({"legendLabel":"Return Closed Count", "magnitude":$scope.reportDetailsSum.returnClosed});
	    }
	    angular.element("section").html("");
	    var vis = d3.select("section")
	      .append("svg:svg") //create the SVG element inside the <section>
	        .data([dataSet]) //associate our data with the document
	        .attr("width", canvasWidth) //set the width of the canvas
	        .attr("height", canvasHeight) //set the height of the canvas
	        .append("svg:g") //make a group to hold our pie chart
	        .attr("transform", "translate(" + 1.5*outerRadius + "," + 1.5*outerRadius + ")"); // relocate center of pie to 'outerRadius,outerRadius'

	    // This will create <path> elements for us using arc data...
	    var arc = d3.svg.arc()
	      .outerRadius(outerRadius);

	    var pie = d3.layout.pie() //this will create arc data for us given a list of values
	      .value(function(d) { return d.magnitude; }) // Binding each value to the pie
	      .sort( function(d) { return null; } );

	    // Select all <g> elements with class slice (there aren't any yet)
	    var arcs = vis.selectAll("g.slice")
	      // Associate the generated pie data (an array of arcs, each having startAngle,
	      // endAngle and value properties) 
	      .data(pie)
	      // This will create <g> elements for every "extra" data element that should be associated
	      // with a selection. The result is creating a <g> for every object in the data array
	      .enter()
	      // Create a group to hold each slice (we will have a <path> and a <text>
	      // element associated with each slice)
	      .append("svg:g")
	      .attr("class", "slice");    //allow us to style things in the slices (like text)

	    arcs.append("svg:path")
	      //set the color for each slice to be chosen from the color function defined above
	      .attr("fill", function(d, i) { return color(i); } )
	      //this creates the actual SVG path using the associated data (pie) with the arc drawing function
	      .attr("d", arc);

	    // Add a legendLabel to each arc slice...
	    arcs.append("svg:text")
	      .attr("transform", function(d) { //set the label's origin to the center of the arc
	        //we have to make sure to set these before calling arc.centroid
	        d.outerRadius = outerRadius + 50; // Set Outer Coordinate
	        d.innerRadius = outerRadius + 45; // Set Inner Coordinate
	        return "translate(" + arc.centroid(d) + ")";
	      })
	      .attr("text-anchor", "middle") //center the text on it's origin
	      .style("fill", "Purple")
	      .style("font", "bold 12px Arial")
	      .text(function(d, i) { return dataSet[i].legendLabel; }); //get the label from our original data array

	    // Add a magnitude value to the larger arcs, translated to the arc centroid and rotated.
	    arcs.filter(function(d) { return d.endAngle - d.startAngle > 0; }).append("svg:text")
	      .attr("dy", ".35em")
	      .attr("text-anchor", "middle")
	      //.attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")rotate(" + angle(d) + ")"; })
	      .attr("transform", function(d) { //set the label's origin to the center of the arc
	        //we have to make sure to set these before calling arc.centroid
	        d.outerRadius = outerRadius; // Set Outer Coordinate
	        d.innerRadius = outerRadius/2; // Set Inner Coordinate
	        return "translate(" + arc.centroid(d) + ")rotate(" + angle(d) + ")";
	      })
	      .style("fill", "White")
	      .style("font", "bold 12px Arial")
	      .text(function(d) { return Math.round(d.data.magnitude*100/sumOfMagnitudes) + "%" + " ("+d.data.magnitude+")"; });

	    // Computes the angle of an arc, converting from radians to degrees.
	    function angle(d) {
	      var a = (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
	      return a > 90 ? a - 180 : a;
	    }
	};
	
	/* Saves Pdf on client side
	 * convertBase64ToBlob() is a function to convert base64 string to blob. Written in common.js
	 * saveAs is a built-in function of FileSaver.js Library to save blob as file.
	 */
	$scope.savePdf = function(){
		var pdfBlob = convertBase64ToBlob($scope.generatedPdf, "application/pdf"); 
		saveAs(pdfBlob, "report.pdf");
	};
}]);