

chrisApp.controller('siteController',  ['$rootScope', '$scope', '$filter','siteService','authService',
                                        'siteCreationService','companyService','userService','districtService',
                                        'areaService','clusterService','countryService',
                              function  ($rootScope, $scope , $filter,siteService, authService,
                                        siteCreationService,companyService,userService,districtService,
                                        areaService,clusterService,countryService) {
		
		$scope.siteData={};
		$scope.sessionUser={};
		$scope.selectedArea={};
		$scope.area={
				selected:{},
				list:[]
		}
		$scope.district={
				selected:{},
				list:[]
		}
		$scope.cluster={
				selected:{},
				list:[]
		}
		$scope.salesTimes={
			selected:{},
			days:null,
			fromlist:[],
			toList:[]
		}
		$scope.deliveryTimes={
				selected:{},
				days:null,
				fromlist:[],
				toList:[]
		}
		$scope.siteAssignedUserList=[];
		$scope.siteUnAssignedUserList=[];
		$scope.fileExtension="";
		angular.element(document).ready(function(){
			
			$scope.getLoggedInUserAccess();
			 $(".licensedate").datepicker({
				 format:'dd-mm-yyyy',
			 });
			 
			 var time=0.00;
				var daysList = ['Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'];
				var dayLength=7;
				for(i=0;i<24;i++){
					time=time+1;
					
					$scope.salesTimes.fromlist.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					$scope.salesTimes.toList.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					
					$scope.deliveryTimes.fromlist.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					$scope.deliveryTimes.toList.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					dayLength=dayLength-1;
				}
				$scope.getOperationDetails();
			
				
				/* $('#siteInputFile').change( function(e) {
		             var ext=$('input#siteInputFile').val().split(".").pop().toLowerCase();
		             $scope.fileExtension = ext;
		             if($.inArray(ext, ["jpg","jpeg","JPEG", "JPG","PDF","pdf","doc","docx","DOC","DOCX","png","PNG"]) == -1) {
		            	 $('#messageWindow').show();
		            	 $('#errorMessageDiv').show();
		 				 $scope.errorMessage="Supported file types to upload are jpg, docx and png";
		                  $scope.isfileselected=false;
		                 return false;
		             }else if (e.target.files != undefined) {
		                 $scope.isfileselected=true;
		                 file = $('#siteInputFile').prop('files');
	                  	 var reader = new FileReader();
	                 	 reader.onload = $scope.onFileUploadReader;
	                 	 reader.readAsDataURL(file[0]);
		               }
				 });*/
		});
	/*	
		$scope.onFileUploadReader=function(event){
			  var uploadedData=event.target.result;
			  console.log( $scope.fileExtension);
			//  console.log(uploadedData);
			  $scope.uploadedFile=uploadedData;
		}*/
		
		 $scope.getLoggedInUserAccess =function(){
				
			 authService.loggedinUserAccess()
	    		.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				$scope.sessionUser=data;
	    				$scope.getLoggedInUser($scope.sessionUser);
	    				
	    			}
	            },
	            function(data) {
	                console.log('Unauthorized Access.')
	            }); 
				
		    }
		    
		$scope.getAllSites=function(){
			$scope.findAllSites();
		}
		
		
		
		$scope.getLoggedInUser=function(loginUser){
			userService.getLoggedInUser(loginUser)
    		.then(function(data) {
    			console.log(data)
    			if(data.statusCode == 200){
    				$scope.siteData ={};
    				$scope.sessionUser=angular.copy(data.object);
    				$scope.siteData.company=$scope.sessionUser.company;
    				$scope.getAllSites();
   				 	$('.dpedit').editableSelect();
    			}
            },
            function(data) {
                console.log('No User available')
            });
		}
		$scope.retrieveAllCountries=function(loginUser){
			countryService.retrieveAllCountries(loginUser)
    		.then(function(data) {
    			console.log(data)
    			if(data.length>0){
    				$.each(data,function(key,val){
    					if(val.countryId == val.countryId){
    						$scope.siteData.country=val;
    						return false;
    					}
    				})
    			}
    		},
    		 function(data) {
                console.log(data);
            });
		}
		$scope.getDistrictByCountry=function(loginUser){
			districtService.retrieveDistrictByCountry(loginUser)
    		.then(function(data) {
    			console.log(data)
    			if(data.statusCode == 200){
    				$scope.district.list=[];
    				$("#districtSelect").empty();
    				if(data.object.length>0){
    					$.each(data.object,function(key,val){
    						var district={
    								districtId:val.districtId,
    								districtCode:val.districtCode,
    								districtName:val.districtName
    						}
    						$scope.district.list.push(district);
    					});
    					var options = $("#districtSelect");
    					options.append($("<option />").val("").text("Select District"));
    					$.each($scope.district.list,function() {
    						options.append($("<option />").val(	this.districtId).text(this.districtName));
    					});
    					if($scope.operation == 'EDIT'){
	    					if($scope.selectedSite!=null){
	    					 $("#districtSelect option").each(function() {
	    							if ($(this).val() == $scope.selectedSite.district.districtId) {
	    								$(this).attr('selected', 'selected');
	    								$scope.getArea($scope.selectedSite.district);
	    							return false;
	    							}
	    					 	});
	    					}
    					}
    				}
    			}
            },
            function(data) {
                console.log(data)
            });
		}
		
		$scope.getArea=function(district){
			areaService.retrieveAllAreas(district)
    		.then(function(data) {
    			console.log(data)
    				$scope.area.list=[];
    			$("#areaSelect").empty();
    				if(data.length>0){
    					$.each(data,function(key,val){
    						var area={
    								areaId:val.areaId,
    								areaName:val.areaName,
    						}
    						$scope.area.list.push(area);
    					});
    					var options = $("#areaSelect");
    					options.append($("<option />").val("").text("Select Area"));
    					$.each($scope.area.list,function() {
    						options.append($("<option />").val(	this.areaId).text(this.areaName));
    					});
    					if($scope.operation == 'EDIT'){
	    					if($scope.selectedSite!=null){
	       					 $("#areaSelect option").each(function() {
	       							if ($(this).val() == $scope.selectedSite.area.areaId) {
	       								$(this).attr('selected', 'selected');
	       								$scope.getCluster($scope.selectedSite.district.districtId, $scope.selectedSite.area.areaId);
	       							return false;
	       							}
	       					 	});
	       					}
    					}
    				}
            },
            function(data) {
                console.log(data)
            });
		}

		$scope.getCluster=function(distId, areaId){
			clusterService.retrieveAllClusters(distId,areaId)
    		.then(function(data) {
    			console.log(data)
    				$scope.cluster.list=[];
    				$("#clusterSelect").empty();
    				if(data.length>0){
    					$.each(data,function(key,val){
    						var cluster={
    								clusterID:val.clusterID,
    								regionId:val.regionId,
    								districtId:val.districtId,
    								countryId:val.countryId,
    								areaId:val.area,
    								clusterName:val.clusterName,
    								clusterDesc:val.clusterDesc
    						}
    						$scope.cluster.list.push(cluster);
    					});
    						
						var options = $("#clusterSelect");
    					options.append($("<option />").val("").text("Select Cluster"));
    					$.each($scope.cluster.list,function() {
    						options.append($("<option />").val(	this.clusterID).text(this.clusterName));
    					});
    					
    					if($scope.operation == 'EDIT'){
	    					if($scope.selectedSite!=null){
	          					 $("#clusterSelect option").each(function() {
	          							if ($(this).val() == $scope.selectedSite.cluster.clusterID) {
	          								$(this).attr('selected', 'selected');
	          							return false;
	          							}
	          					 	});
	          				}
    					}
    					
    				}
            },
            function(data) {
                console.log(data)
            });
		}
		
		
		$scope.getAllCompanies=function(){
		}
		
		$scope.findAllSites=function(){
			$('#loadingDiv').show();
			siteService.retrieveAllSites()
			.then(function(data) {
    			console.log(data)
    				$scope.siteList=[];
    				if(data.length>0){
    					$.each(data,function(key,val){
    						$scope.siteList.push(val);
    					});
    					$scope.getSiteDetails($scope.siteList[0]);
    					$('#loadingDiv').hide();
    				}else{
    					console.log("No sites assigned to the user.")
    					$scope.InfoMessage="No sites assigned to the user."
    						$('#messageWindow').show();
    	    				$('#infoMessageDiv').show();
    	    				$('#loadingDiv').hide();
    				}
            },
            function(data) {
                console.log(data)
                	$scope.InfoMessage="No sites assigned to the user."
					$('#messageWindow').show();
    				$('#infoMessageDiv').show();
    				$('#infoMessageDiv').alert();
    				$('#loadingDiv').hide();
            });
			
			// $scope.getDistrictByCountry($scope.sessionUser);
			 //$scope.getArea();
			// $scope.getCluster();
		}
		
		//License related logic
		$scope.licenseDetails = [
	      {
	        'licenseName':'',
	        'validfrom':'',
	        'validto':''
	    }];
	              			 
		$scope.addNewLicense = function(licenseDetails){
			 $scope.licenseDetails.push({ 
			  'licenseName': "", 
			  'validfrom': "" ,
			  'validto': "",
			 });
			 $scope.LD = {};
			 
			 $(".licensedate").datepicker({
				 format:'dd-mm-yyyy',
			 },"destroy");
		};
			 
		$scope.removeLicense = function(){
			  var newDataList=[];
			  $scope.selectedAll = false;
			  angular.forEach($scope.licenseDetails, function(selected){
			      if(!selected.selected){
			         newDataList.push(selected);
			       }
			    }); 
			   $scope.licenseDetails = newDataList;
		};
		   
    $scope.checkAllLicense = function () {
    	if (!$scope.selectedAll) {
		   $scope.selectedAll = true;
		  } else {
		    $scope.selectedAll = false;
		     }
		   angular.forEach($scope.licenseDetails, function(licenseDetail) {
			   licenseDetail.selected = $scope.selectedAll;
		    });
		 };


		//Sales Operation and delivery operation details//
		              			 
	$scope.operatingTimes = [	
	         { id: 0,name: 'NO TIME'},                
  			 { id: 1,name: '1:00'},
             {id: 2, name: '2:00'},
             {id: 3, name: '3:00' },
             {id: 4, name: '4:00' },
             {id: 5, name: '5:00' },
             {id: 6, name: '6:00' },
             {id: 7, name: '7:00' },
             {id: 8, name: '8:00' },
             {id: 9, name: '9:00' },
             {id: 10,name: '10:00' },
             {id: 11,name: '11:00' },
             {id: 12,name: '12:00' },
             {id: 13, name: '13:00'},
             {id: 14, name: '14:00'},
             {id: 15, name: '15:00'},
             {id: 16, name: '16:00'},
             {id: 17, name: '17:00'},
             {id: 18, name: '18:00'},
             {id: 19, name: '19:00'},
             {id: 20, name: '20:00'},
             {id: 21, name: '21:00'},
             {id: 22, name: '22:00'},
             {id: 23, name: '23:00'},
             {id: 24, name: '24:00'},
		   ];
		              
		              
		   $scope.getOperationDetails=function(){
				$scope.salesoperationDetails = {
						selected:{},
						finalSelected:[],
						list:[
				        {
				        	 "opId":null,
				            'days':'Monday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Tuesday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Wednesday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Thursday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Friday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Saturday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Sunday',
				            'from':'',
				            'to':''
				        }
				       ]
					};
				
				
				//Delivery Operation details
				 $scope.deliveryoperationDetails = {
						 selected:{},
							finalSelected:[],
						 list:[
				        {
				        	 "opId":null,
				            'days':'Monday',
				            'from':'',
				            'to':''
				            
				        },
				        {
				        	 "opId":null,
				            'days':'Tuesday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Wednesday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Thursday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Friday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Saturday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Sunday',
				            'from':'',
				            'to':''
				        }]
					}
				 
				
		   }           			 

		              			
	   $scope.salesOperations=[];
    	$scope.deliveryOperations=[];          
 		$scope.setStartTime=function(value, targetId, from ){
 			if(value.from==null){
 				$("#"+targetId+""+from).attr("required",true);
 				$("#"+targetId+""+from).attr("style","background-color:#5ccaae;color:#fff")
 			}else{
 				var selectedStartTime = value.to;	
	 			$("#"+targetId+""+from).val(selectedStartTime.name)
	 			$("#"+targetId+""+from).attr("style","background-color:#5ccaae;color:#fff");
 			}
 		}
 		
 		$scope.setEndTime=function(value, targetId, to){
 			if(value.to==null){
 				$("#"+targetId+""+to).attr("required",true);
 				$("#"+targetId+""+to).attr("style","background-color:#5ccaae;color:#fff")
 			}else{
	 			var selectedEndTime = value.to;	
	 			$("#"+targetId+""+to).val(selectedEndTime.name);
	 			$("#"+targetId+""+to).attr("style","background-color:#5ccaae;color:#fff")
 			}
 		}
 
 		
 		
		$scope.changeOperationTime = function(operatingTime) {
	        $scope.myTime = operatingTime;    
	    };
		              			 
		             //Submeter details
		$scope.submeterDetails = [{
	        'subMeterNumber': '',
	        'subMeterUser':''
	        
	    }];
		
		$scope.defaultSalesOps = angular.copy($scope.salesoperationDetails);
 		$scope.defaultDeliveryOps = angular.copy($scope.deliveryoperationDetails);
 		$scope.defaultsSubmeterDetails = angular.copy($scope.submeterDetails);
 		
		              			 
		$scope.addNewSubmeter = function(submeterDetails){
			 $scope.submeterDetails.push({ 
			  'subMeterNumber': '', 
			 'subMeterUser': ''				 
			 });
			 $scope.SD = {};
		 };
		              				 
		 $scope.removeSubmeter = function(){
			  var newDataList=[];
			  $scope.selectedAll = false;
			  angular.forEach($scope.submeterDetails, function(selected){
			      if(!selected.selected){
			         newDataList.push(selected);
			       }
			    }); 
			        $scope.submeterDetails = newDataList;
		  };
		              			   
  	    $scope.checkAllSubmeter = function () {
  	    	if (!$scope.selectedAll) {
  			   $scope.selectedAll = true;
  			  } else {
  			    $scope.selectedAll = false;
  			     }
  			   angular.forEach($scope.submeterDetails, function(submeterDetail) {
  				   submeterDetail.selected = $scope.selectedAll;
  			    });
  	    };
  	    
  	    
  	    
  	  $scope.addNewSite=function(){
  		  	$scope.onedit=false;
  		  	$scope.operation ="ADD";
  		  	$scope.siteData={};
  		  	$('#siteModalLabel').text("Create New Site");
  		  	console.log($scope.salesoperationDetails);
			$scope.getAllCompanies()
			$scope.retrieveAllCountries($scope.sessionUser);
			$scope.getDistrictByCountry($scope.sessionUser);
			$('#clusterSelect').empty();
			$('#areaSelect').empty();
			$scope.area.list=[];
			$scope.cluster.list=[];
			$scope.licenseDetails=[];
			$scope.getOperationDetails();
			$scope.submeterDetails = [];
			$('#createSiteModal').modal('show');
		}
  	    
  	   
		 
  	    $scope.getSelectedDistrict=function(district){
	    	$scope.siteData.district = district;
	    }
  	    
  	    $scope.getSelectedArea=function(area){
  	    	$scope.siteData.area = area;
  	    }
  	    
  	    $scope.getSelectedCluster=function(cluster){
	    	$scope.siteData.cluster = cluster;
	    }
  	    
  	  $scope.IsValidDate = function (toDate, fromDate) {				 
			
			 var isValid = false;
			 console.log(toDate);
			 if(toDate == null || toDate == "" || typeof(toDate) === "undefined"){
				 isValid = true;					 
			 }
			 else{					 
				 var isSame;
				 var part1 = toDate.split('-');
				 var part2 = fromDate.split('-');				 
				 toDate =  new Date(part1[1]+'-'+part1[0]+'-'+part1[2]);
				 fromDate =  new Date(part2[1]+'-'+ part2[0]+'-'+ part2[2]);				 
		         isValid = moment(toDate).isAfter(fromDate);
		          if (!isValid){
		        	  isValid = moment(toDate).isSame(fromDate);
		          }	
			 }		         	          
	          //console.log(isValid);
	          return isValid;
	      }

  	    
	    $scope.saveSiteForm=function(formObj){
	    	$scope.salesOperations=[];
	    	$scope.deliveryOperations=[];
	    	
	    	var isValidLicense = true;	
	    	var keepGoing = true;
	    	var count = 0;	    		    	
	    	
	    	angular.forEach($scope.licenseDetails, function(licenseDetail){
	    		if (keepGoing){
	    			
	    			/*console.log(licenseDetail.validfrom);
		    		console.log(licenseDetail.validto);*/
		    		if($scope.IsValidDate(licenseDetail.validto,licenseDetail.validfrom)){
		    			//isValidLicense = true;
		    			$("#datepicker"+count+"from").css('border-color', 'lightgrey');
		    			$("#datepicker"+count+"to").css('border-color', 'lightgrey');		    			
		    		}
		    		else{
		    			isValidLicense = false;
		    			//keepGoing = false;
		    			$("#datepicker"+count+"from").css('border-color', 'red');
		    			$("#datepicker"+count+"to").css('border-color', 'red');	
		    					    			
		    		}
		    		count = count +1;
	    		}	
			    });

	    	
	    	
	    	$.each($scope.salesoperationDetails.list,function(key,val){
	    		//if(val.selected.from!=null && val.selected.to!=null ){
	    		var startTime="NO TIME";
    			var endTime="NO TIME";
    			
    			if(val.selected==null){
    				startTime="NO TIME";
    				endTime="NO TIME";
    			}else{
    				startTime = val.selected.from.name
    				endTime = val.selected.to.name
    			}
    			
		    		var salesOpsTimings={
		    				opId:val.opId,
		    				days:val.days,
		    				from:startTime,
		    				to:endTime
		    		}
		    		$scope.salesOperations.push(salesOpsTimings);
	    		//}
	    	});
	    		$.each($scope.deliveryoperationDetails.list,function(key,val){
	    			//if(val.selected.from.name!=null && val.selected.from.name!="" && val.selected.to.name!=null && val.selected.to.name!=""){
	    			var startTime="NO TIME";
	    			var endTime="NO TIME";
	    			
	    			if(val.selected==null){
	    				startTime="NO TIME";
	    				endTime="NO TIME";
	    			}else{
	    				startTime = val.selected.from.name
	    				endTime = val.selected.to.name
	    			}
		    		var deliveryOpsTimings={
		    				opId:val.opId,
		    				days:val.days,
		    				from:startTime,
		    				to:endTime
		    		}
		    		$scope.deliveryOperations.push(deliveryOpsTimings);
	    			//}
	    	});

	    	$scope.siteData.operator = $scope.sessionUser.company;
	    	var finalSiteObj = {
	    			siteData: $scope.siteData,
	    			siteLicense:$scope.licenseDetails,
	    			siteOperation:$scope.salesOperations,
	    			siteDelivery:$scope.deliveryOperations,
	    			siteSubmeter:$scope.submeterDetails,
	    			createdBy:$scope.sessionUser.loggedInUserMail
	    	
	    	};
	    	console.log(finalSiteObj);
	    	var isOperationDetails = false;
	    	if(finalSiteObj.siteOperation.length==7){
		    	$.each(finalSiteObj.siteOperation,function(key,val){
		    		if(val.from == "" && val.to==""){
		    				$('#modalMessageDiv').show();
		    				$('#messageWindow').hide();
		    				$('#successMessageDiv').hide();
		    				$('#errorMessageDiv').show();
		    				$('#errorMessageDiv').alert();
		    				$scope.modalErrorMessage = "Please enter sales operation timing for" + val.days;
		    				isOperationDetails=false;
		    			return false;
		    		}else{
		    			isOperationDetails=true;
		    		}
		    	})
	    	}
	    
	    	 if(finalSiteObj.siteDelivery.length==7){
		    	$.each(finalSiteObj.siteDelivery,function(key,val){
		    		if(val.from == "" && val.to==""){
		    			$('#modalMessageDiv').show();
	    				$('#messageWindow').hide();
	    				$('#successMessageDiv').hide();
	    				$('#errorMessageDiv').show();
	    				$('#errorMessageDiv').alert();
	    				$scope.modalErrorMessage = "Please enter delivery operation timing for"+ val.days;
	    				isOperationDetails=false;
		    			return false;
		    		}else{
		    			isOperationDetails=true;
		    		}
		    	})
	    	}
	    	/* if(isOperationDetails==true){
	    		$scope.createSiteVOObject(finalSiteObj);
	    		 console.log(finalSiteObj);
	    		$scope.modalErrorMessage ="";
	    	 }else{
	    		 $('#modalMessageDiv').show();
 				$('#messageWindow').hide();
 				$('#successMessageDiv').hide();
 				$('#errorMessageDiv').show();
 				$('#errorMessageDiv').alert();
 				$scope.modalErrorMessage = "Please enter all the required fields for operation timings";
	    	 }*/
	    	 
	    	 if(isOperationDetails==true && isValidLicense == true){
		    		$scope.createSiteVOObject(finalSiteObj);
		    		 console.log(finalSiteObj);
		    		$scope.modalErrorMessage ="";
		    	 }else if(isValidLicense == false) {
		    		 $('#modalMessageDiv').show();
	 				$('#messageWindow').hide();
	 				$('#successMessageDiv').hide();
	 				$('#errorMessageDiv').show();
	 				$('#errorMessageDiv').alert();
	 				$scope.modalErrorMessage = "ValidTo must be greater than ValidFrom in LicenseDetail Tab.Please enter correct date";
		    	 }
		    	 else{
		    		 $('#modalMessageDiv').show();
		 				$('#messageWindow').hide();
		 				$('#successMessageDiv').hide();
		 				$('#errorMessageDiv').show();
		 				$('#errorMessageDiv').alert();
		 				$scope.modalErrorMessage = "Please enter all the required fields for operation timings";
		    	 }
	    	
	    	
     };
	     $scope.createSiteVOObject=function(finalSiteObj){
	    	 $('#loadingDiv').show();
	    	// finalSiteObj.uploadedFile = $scope.uploadedFile;
	    	// finalSiteObj.extension=$scope.fileExtension;
	    	var finalObject = siteCreationService.createSiteObject(finalSiteObj)
	    	console.log(finalObject);
	    	siteCreationService.saveSiteObject(finalObject)
    		.then(function(data) {
    			console.log(data)
    			if(data.statusCode == 200){
    				$('#messageWindow').show();
    				$('#successMessageDiv').show();
    				$('#successMessageDiv').alert();
    				$('#errorMessageDiv').hide();
    				$('#siteModalCloseBtn').click();
    				$('#infoMessageDiv').hide();
    				$scope.successMessage = data.message;
    				 $('#loadingDiv').hide();
    				$scope.getAllSites();
    				
    			}else{
    				 $('#modalMessageDiv').show();
    				$('#messageWindow').hide();
    				$('#successMessageDiv').hide();
    				$('#errorMessageDiv').show();
    				$('#errorMessageDiv').alert();
    				$scope.modalErrorMessage = data.message;
    				$('#loadingDiv').hide();
    			}
            },
            function(data) {
                console.log(data)
                $('#messageWindow').hide();
                $('#modalMessageDiv').show();
                $('#successMessageDiv').hide();
                $('#errorMessageDiv').show();
				$('#errorMessageDiv').alert();
				$scope.modalErrorMessage = data.message;
				$('#loadingDiv').hide();
            });
	    	
	     }
	     $scope.closeMessageWindow=function(){
				$('#messageWindow').hide();
				$('#successMessageDiv').hide();
				$('#errorMessageDiv').hide();
				$('#modalMessageDiv').hide();
				$scope.modalErrorMessage="";
				$scope.successMessage="";
			}
	     //----------------------- View Site ------------------------------------
	     
	     
		 console.log("allsite retrived");
		
		 
		 $scope.getSiteDetails=function(site){
			console.log(site);
				$scope.getSelectedSiteData(site.siteId);
			 
			}
		 
		 $scope.getSelectedSiteData=function(siteId){
			 siteService.retrieveSiteDetails(siteId)
	    		.then(function(data) {
	    			console.log(data)
	    			var site=angular.copy(data);
	    			$scope.selectedSite={};
					$scope.selectedSite=angular.copy(site);
					$scope.selectedSite.siteName = site.siteName;
					$scope.selectedSite.siteNumber1 = site.siteNumber1;
					$scope.selectedSite.siteNumber2 = site.siteNumber2;
					$scope.selectedSite.siteAddress = site.address;
				
					$scope.selectedSite.district = site.district;
					$scope.selectedSite.area=site.area;
					$scope.selectedSite.cluster=site.cluster;
					
					$scope.district.selected=$scope.selectedSite.district;
					$scope.area.selected = $scope.selectedSite.area;
					$scope.cluster.selected = $scope.selectedSite.cluster;
					 
					$scope.selectedSite.retailerName=site.owner;
					$scope.selectedSite.primaryContact=site.primaryContact;
					$scope.selectedSite.secondaryContact=site.secondaryContact;
					
					$scope.selectedSite.LicenseDetail = site.siteLicense;
					$scope.selectedSite.SalesOperation = site.siteOperation;
					$scope.selectedSite.DeliveryOperation = site.siteDelivery;
					$scope.selectedSite.submeterDetails = site.siteSubmeter;
					 
					$scope.siteLicense = $scope.selectedSite.LicenseDetail;
					$scope.siteSalesOperation = $scope.selectedSite.SalesOperation;
					$scope.siteDeliveryOperation = $scope.selectedSite.DeliveryOperation;
					
					$scope.siteSubmeterDetails = $scope.selectedSite.submeterDetails;
					$scope.siteData.siteId = $scope.selectedSite.siteId;
	    			
	    		},function(data){
	    			console.log(data);
	    		})
		 }
		 
		 $scope.updateSiteModal = function(selectedSite) {
			 console.log(selectedSite);
			 $scope.operation ="EDIT";
			 $scope.siteData = angular.copy($scope.selectedSite);
			 $('#siteModalLabel').text("Update site");
			 $scope.retrieveAllCountries($scope.sessionUser);
			 $scope.onedit=false;
			// $scope.siteData = angular.copy(selectedSite);
			 console.log($scope.siteData);
			 $scope.getDistrictByCountry($scope.sessionUser);
			// $scope.getArea($scope.siteData.district)
			// $scope.getCluster($scope.siteData.district.districtId,$scope.siteData.area.areaId);
			 $scope.salesoperationDetails.list = [] ;
			 $scope.deliveryoperationDetails.list = [];
			 $.each($scope.siteData.SalesOperation,function(key,val){
				 var salesTimingObject={
						 opId:val.opId,
						 days:val.days,
						 from:val.from,
						 to:val.to,
						 selected:{
							 from:{
								 name:val.from
							 },
							 to:{
								 name:val.to
							 }
						 }
				 }
				 console.log(salesTimingObject.selected.from.name);
				 $scope.salesoperationDetails.list.push(salesTimingObject);
			 });
			 
			 $.each($scope.siteData.DeliveryOperation,function(key,val){
				 var deliveryTimingObject={
						 opId:val.opId,
						 days:val.days,
						 from:val.from,
						 to:val.to,
						 selected:{
							 from:{
								 name:val.from
							 },
							 to:{
								 name:val.to
							 }
						 }
							
				 }
				 
				 $scope.deliveryoperationDetails.list.push(deliveryTimingObject);
			 });
			 
			 
			 console.log($scope.selectedSite, $scope.salesoperationDetails,  $scope.deliveryoperationDetails)
			 
		/*	
			 $("#districtSelect option").each(function() {
					if ($(this).val() == $scope.selectedSite.district.districtId) {
						$(this).attr('selected', 'selected');
						$scope.district.selected.districtId = $scope.selectedSite.district.districtId;
						$scope.getArea($scope.district.selected);
					return false;
					}
				});
			 $("#areaSelect option").each(function() {
					if ($(this).val() == $scope.selectedSite.area.areaId) {
						$(this).attr('selected', 'selected');
						$scope.area.selected.areaId = $scope.selectedSite.area.areaId;
						$scope.getCluster($scope.district.selected.districtId, $scope.selectedSite.area.areaId);
					return false;
					}
				});
			 
			 $("#clusterSelect option").each(function() {
					if ($(this).val() == $scope.selectedSite.cluster.clusterID) {
						$(this).attr('selected', 'selected');
						$scope.cluster.selected.clusterID = $scope.selectedSite.cluster.clusterID;
					return false;
					}
				});*/
			 
			 $scope.licenseDetails = [] ;
			 $scope.licenseDetails =  selectedSite.LicenseDetail;
			 $scope.submeterDetails = [];
			 $scope.submeterDetails = selectedSite.submeterDetails;
			// $scope.showUpdateModal();
			 $('#createSiteModal').modal('show');
			}
		 
		 $scope.viewTabSelected=function(selectedSite, tabSelected){
			 $scope.updateSiteModal(selectedSite);
			 $('#'+tabSelected).click();
			 
		 }
		 
		 $scope.openCreateEquipment = function(selectedSite) {
			 
			 console.log("Equipment modal");
			 $scope.siteData=[];
			 $scope.siteData = angular.copy(selectedSite);
			 console.log($scope.siteData);
			 
			  }


$scope.saveAssetEquipmentForm=function(){
 console.log("save equipment form");

	 	
	var finalAssetEquipmentObj = {
			AssetEquipment: $scope.equipmentData,
			siteData:$scope.siteData 			
	
	};
	console.log(finalAssetEquipmentObj);
	
};

	$scope.manageUserAccess=function(selectedSite){
		$scope.manageUserAccessForSite(selectedSite);
	}
	
	$scope.manageUserAccessForSite=function(selectedSite){
		$('#assignUserModal').modal('show');
		userService.getUsersBySiteAccess(selectedSite.siteId)
		.then(function(data) {
			console.log(data)
			if(data.statusCode == 200){
				$scope.siteAssignedUserList=[];
				$scope.siteUnAssignedUserList=[];
				
				if(data.object.length>0){
					$.each(data.object,function(key,val){
						if(val.assignedUser!=null && val.accessId!=null){
							var assignedUserSiteData={
									accessId:val.accessId,
									user:val.assignedUser,
									company:val.assignedUser.company,
									site:val.site,
									firstName:val.assignedUser.firstName,
									lastName:val.assignedUser.lastName,
									email:val.assignedUser.email
							}
							$scope.siteAssignedUserList.push(assignedUserSiteData);
						}
							
						if(val.unAssignedUser!=null && val.accessId==null){
							if(val.unAssignedUser.userRoles.length>0){
								var unAssginedUsers ={
									user:val.unAssignedUser,
									firstName:val.unAssignedUser.firstName,
									lastName:val.unAssignedUser.lastName,
									email:val.unAssignedUser.email,
									role:val.unAssignedUser.userRoles[0].role
								}
							}
							$scope.siteUnAssignedUserList.push(unAssginedUsers);
						}
						
					});
				}
			}
				
	    },
	    function(data) {
	        console.log(data)
	    });
	}
	$scope.searchCriteria={};
	$scope.setText=function(searchType){
		$('#searchSelection').html(searchType+ ' <span class="fa fa-caret-down"></span>');
		$scope.searchCriteria.filter=searchType;
	}
	
	$scope.assignThisUserTo=function(selectedUser,selectedSite){
		console.log(selectedUser,selectedSite);
		$scope.assignUserToSite(selectedUser, selectedSite);
	}

	$scope.assignUserToSite=function(selectedUser,selectedSite){
		siteService.assignSiteAccess(selectedUser.user.userId,selectedSite.siteId)
		.then(function(data) {
			console.log(data)
			if(data.statusCode == 200){
				$scope.manageUserAccessForSite($scope.selectedSite);
				//Send notification after User is assigned - Start
				var fullName = selectedUser.user.firstName +" "+ selectedUser.user.lastName;
				var siteName = selectedSite.siteName;
				sendNotification(fullName +" is assigned to Site "+ siteName);
				
				//Send notification after User is assigned - End
			}
		},function(data) {
	        console.log(data)
	    });
	}
	
	
	$scope.revokeThisUserAccess=function(selectedUser,selectedSite){
		console.log(selectedUser,selectedSite);
		$scope.revokeUserAccessFromSite(selectedUser);
	}

	$scope.revokeUserAccessFromSite=function(selectedUser){
		siteService.removeSiteAccess(selectedUser.accessId)
		.then(function(data) {
			console.log(data)
			if(data.statusCode == 200){
				$scope.manageUserAccessForSite($scope.selectedSite);
				//Send notification after User is assigned - Start
				var fullName = selectedUser.user.firstName +" "+ selectedUser.user.lastName;
				var siteName = $scope.selectedSite.siteName;
				sendNotification(fullName +" is revoked from  Site "+ siteName);
				
				//Send notification after User is assigned - End
			}
		},function(data) {
	        console.log(data)
	    });
	}
	
$scope.disableRemoveButton = function(licenseId){
		
		console.log(licenseId);
		  $scope.selectedAll = false;
		  var keepGoing = true;
		  angular.forEach($scope.licenseDetails, function(selected){
			  console.log(selected);
			  if (keepGoing){
		      if(selected.selected){
		    	  
		    	  if(licenseId != "" && typeof(licenseId) === "number" && typeof(selected.licenseId) === "number"){
		    		  console.log("shibu11");
		    		  console.log(selected.licenseId);
		    		  $("#btnRemove").prop("disabled", true);
		    		  keepGoing = false;
		    	  }
		    	  else if(!(typeof(licenseId) === "number") && typeof(selected.licenseId) === "number"){
		    		  console.log("shibu55"); 
		    		  $("#btnRemove").prop("disabled", true);
		    		  keepGoing = false;
		    	  }
		    	  else{
		    		  console.log("jubu");
		    		  console.log(licenseId);
		    		  console.log(selected.licenseId);
		    		  $("#btnRemove").prop("disabled", false);
		    	  }			         
		       }
		      else if(!selected.selected){
		    	  if(!typeof(licenseId) === "number"){
		    		  console.log("IBM");
		    		  $("#btnRemove").prop("disabled", false);
		    	  }
		    	  else if(licenseId != ""){
		    		  $("#btnRemove").prop("disabled", false);  
		    	  }			    	  
		      }
			  }
		      
		    });
		   
	};

	
	
$scope.onlyNumbers = /^\d+$/;
$scope.filterValue = function($event){
    if(isNaN(String.fromCharCode($event.keyCode))){
        $event.preventDefault();
    }
};
			 
}]);



function validateDropdownValues(dropDownId){
	var scope = angular.element("#siteWindow").scope();
	 var valueId = parseInt($("#"+dropDownId).val());
	 if(valueId == ""){
	 
	 }else{
		 if(dropDownId.toUpperCase() == "DISTRICTSELECT"){
			 var district={
					 districtId:parseInt($("#districtSelect").val()),
			 		 districtName:$("#districtSelect option:selected").text()
			 }
			scope.district.selected=district;
			 scope.getArea(district);
			 scope.siteData.district = district;
		 }
		 
		 if(dropDownId.toUpperCase() == "AREASELECT"){
			 if(scope.siteData.district!=null){
				 var area={
						 areaId:parseInt($("#areaSelect").val()),
				 		 areaName:$("#areaSelect option:selected").text()
				 }
				scope.area.selected=area;
				scope.siteData.area = area;
				scope.getCluster(scope.district.selected.districtId,area.areaId )
			 }
				
		 }
		 
		 if(dropDownId.toUpperCase() == "CLUSTERSELECT"){
			 if(scope.siteData.district!=null && scope.siteData.area!=null ){
				 var cluster={
						 clusterID:parseInt($("#clusterSelect").val()),
				 		 clusterName:$("#clusterSelect option:selected").text()
				 }
					 scope.cluster.selected=cluster;
					 scope.siteData.cluster = cluster;
			 }else{
				 
			 }
			
		 }
	 }
}

function setTimeValues(dropDownId, option){
	var scope = angular.element("#siteWindow").scope();
	var selectedVal = dropDownId.id;
	console.log(selectedVal)
	if(option=="from"){
		$.each(scope.salesTimes.fromlist,function(key,val){
			if(selectedVal.toUpperCase() == "SALESSELECTFROM"+key){
				console.log("SALESSELECTFROM"+key)
				var object={
						opId:val.opId,
						days:val.days,
						val:$('#'+selectedVal).val(),
						text:$('#'+selectedVal).val()
				}
				scope.salesTimes.selected=object;
				console.log(scope.salesTimes.selected)
				return false;
			}
		});
		$.each(scope.salesTimes.fromlist,function(key,val){
			if(selectedVal.toUpperCase() == "DELIVERYFROM"+key){
				console.log("DELIVERYFROM"+key)
				var object={
						opId:val.opId,
						days:val.days,
						val:$('#'+selectedVal).val(),
						text:$('#'+selectedVal).val()
				}
				scope.salesTimes.selected=object;
				console.log(scope.salesTimes.selected)
				return false;
			}
		});
	}
	if(option=="to"){
		$.each(scope.salesTimes.toList,function(key,val){
			if(selectedVal.toUpperCase() == "SALESSELECTTO"+key){
				console.log("SALESSELECTTO"+key)
				var object={
						opId:val.opId,
						days:val.days,
						val:$('#'+selectedVal).val(),
						text:$('#'+selectedVal).val()
				}
				scope.salesTimes.selected=object;
				console.log(scope.salesTimes.selected)
				return false;
			}
		});
		$.each(scope.salesTimes.toList,function(key,val){
			if(selectedVal.toUpperCase() == "DELIVERYTO"+key){
				console.log("DELIVERYTO"+key)
				var object={
						opId:val.opId,
						days:val.days,
						val:$('#'+selectedVal).val(),
						text:$('#'+selectedVal).val()
				}
				scope.salesTimes.selected=object;
				console.log(scope.salesTimes.selected)
				return false;
			}
		});
	}
		
	}
	
