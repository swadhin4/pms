chrisApp.controller('assetController', 
		 ['$rootScope', '$scope', '$filter','authService','userService',
		  'siteService','serviceProviderService','assetService',
		  function  ($rootScope, $scope , $filter,authService,userService,
				  siteService,serviceProviderService,assetService) {
			 
			 
			 console.log("allasset retrived");
			 $scope.equipmentData ={};
			 $scope.serviceData ={};
			 $scope.selectedRow =0;
			 $scope.selectedAsset={};
			 $scope.asset={
					 selected:{},
					 list:[]
			 }
			 $scope.assetCategory={
					 selected:{},
					 equipmentList:[],
					 serviceList:[]
					 
			 }
			 $scope.assetLocation={
					 selected:{},
					 list:[]
			 }
			 $scope.serviceProvider={
					 selected:{},
					 list:[]
			 }
			 $scope.accessSite={
					 selected:{},
					 list:[]
			 }
			 $scope.rowHighilited=function(row)
			    {
			      $scope.selectedRow = row;    
			    }	
			 
			 angular.element(document).ready(function () {
				 $scope.getLoggedInUserAccess();
				
					$("#drpIsAsset").change(
							function() {

								var selectedText = $(this).find("option:selected")
										.text();
								var powersensorSelectedText = $('#drpIsPowersensor :selected').text();

								var selectedValue = $(this).val();
								if (selectedText == "NO") {
									$('#drpIsPowersensor').val("");
									$('#txtSensorNumber').val("");
									$("#drpIsPowersensor").prop("disabled", true);
									$("#txtSensorNumber").prop("disabled", true);
									$scope.equipmentData.pwSensorNumber=null;
								} else if (selectedText == "YES" && powersensorSelectedText == "Select Sensor Attached") {
									$("#drpIsPowersensor").prop("disabled", false);
									$("#txtSensorNumber").prop("disabled", false);
									$("#drpIsPowersensor").attr('required', true);
								} else if (selectedText == "YES"
										&& powersensorSelectedText == "YES") {
									$("#txtSensorNumber").prop("disabled", false);
									$("#txtSensorNumber").attr('required', true);
								} else if (selectedText == "YES"
										&& powersensorSelectedText == "NO") {
									$("#txtSensorNumber").prop("disabled", true);
									$('#txtSensorNumber').val("");
								}
								else if (selectedText == "YES"	) {
									$("#drpIsPowersensor").prop("disabled", false);
									$("#txtSensorNumber").prop("disabled", false);
							}
								$scope.equipmentData.isAssetElectrical=selectedValue;

							});

					$("#drpIsPowersensor").change(
							function() {

								var selectedText = $(this).find("option:selected")
										.text();
								var assetSelectedText = $('#drpIsAsset :selected')
										.text();

								var selectedValue = $(this).val();
								if (selectedText == "NO"
										&& assetSelectedText == "YES") {
									$("#txtSensorNumber").prop("disabled", true);
								} else if (selectedText == "YES"
										&& assetSelectedText == "YES") {
									$("#txtSensorNumber").prop("disabled", false);
									$("#txtSensorNumber").attr('required', true);
								}
								$scope.equipmentData.isPWSensorAttached=selectedValue;
							});
					
			
					 var commDate=null;
					 var decomDate=null;
					 
					 $("#commission").datepicker({
						 format:"dd-mm-yyyy"
					 })
					 
					  
					
					$('#commission').change(function(){
						
						
				 
					});
					 
					 $("#decommission").datepicker({ todayHighlight:'TRUE',
						    autoclose: true,format:"dd-mm-yyyy"
			         });
					 
					$('#decommission').change(function(){
						
					});
					
			 });
			 
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
			 $scope.getLoggedInUser=function(loginUser){
					userService.getLoggedInUser(loginUser)
		    		.then(function(data) {
		    			console.log(data)
		    			if(data.statusCode == 200){
		    				$scope.equipmentData ={};
		    				$scope.sessionUser=angular.copy(data.object);
		    				$scope.equipmentData.company=$scope.sessionUser.company;
		    				$scope.getAllAsset();
		    				$scope.getServiceProviders($scope.equipmentData.company);
		    				$scope.retrieveAssetCategories();
		    				$scope.getAssetLocations();
		    				//$scope.testSaveAssetObject();
		    				$scope.getUserSiteAccess();
		    			}
		            },
		            function(data) {
		                console.log('No User available')
		            });
				}
			 
			 $scope.getAllAsset=function(){
				 $('#loadingDiv').show();
				 assetService.findAllAssets()
						.then(function(data) {
		    			console.log(data);
		    				if(data.length>0){
		    				$scope.asset.list=[];
			    			$.each(data,function(key,val){
			    			/*	var asset={
			    						assetId:val.assetId,
			    						assetName:val.assetName,
			    						assetCode:val.assetCode,
			    						categoryId:val.categoryId,
			    						assetCategory:val.assetCategoryName,
			    						locationId:val.locationId,
			    						assetLocation:val.locationName,
			    						assetType:val.assetType,
			    						modelNumber:val.modelNumber,
			    						dateofcommissioning:val.commisionedDate,
			    						dateofdecommissioning:val.deCommissionedDate,
			    						serviceProviderId:val.serviceProviderId,
			    						serviceprovider:val.serviceProviderName,
			    						
			    				};*/
			    				$scope.asset.list.push(val);
			    				//$scope.asset.selected=$scope.asset.list[0];
			    				$scope.getAssetDetails($scope.asset.list[0]);
			    				$('#messageWindow').hide();
			    				$('#infoMessageDiv').hide();
			    				$('#loadingDiv').hide();
			    			})
		    			 
		    			  }else{
		    				  $scope.InfoMessage="No assets available for the user"
									$('#messageWindow').show();
				    				$('#infoMessageDiv').show();
				    				$('#infoMessageDiv').alert();
				    				$('#loadingDiv').hide();
		    			  	}
		    				
			            },
			            function(data) {
			                console.log('Unable to get asset list')
			                	$scope.InfoMessage="No assets available for the user"
								$('#messageWindow').show();
			    				$('#infoMessageDiv').show();
			    				$('#infoMessageDiv').alert();
			    				$('#loadingDiv').hide();
			            });
				 }
			 
			 $scope.getAssetDetails=function(asset){
				 console.log("shibasish");
				$scope.selectedAsset=angular.copy(asset);
				console.log($scope.selectedAsset)
			
			}
			 
			 $scope.addEquipment=function(){
				 $scope.equipmentData={};
				 $('#resetAssetForm').click();
				 $("#categorySelect option").each(function() {
						if ($(this).val() == "") {
							$(this).attr('selected', 'selected');
							 $scope.assetCategory.selected=null;
							return false;
						}
				 	});
			 	
			 	$("#locationSelect option").each(function() {
					if ($(this).val() == "") {
						$(this).attr('selected', 'selected');
						 $scope.assetLocation.selected=null;
						return false;
					}
				});
			 	
			 	$("#spSelect option").each(function() {
					if ($(this).val() == "") {
						$(this).attr('selected', 'selected');
						 $scope.serviceProvider.selected=null;
						return false;
					}
				});
				 $scope.accessSite.selected={};
				 $("#siteSelect option").each(function(){
				 		if($(this).val() == ""){
				 			$(this).attr('selected', 'selected');
				 			 $scope.accessSite.selected=null;
							return false;
				 		}
				 	});
				 	$("#drpIsAsset option").each(function(){
			 		if($(this).val() == ""){
			 			$(this).attr('selected', 'selected');
			 			$scope.equipmentData.isAssetElectrical=null;
						return false;
			 		}
			 	});
			 	
			 	$("#drpIsPowersensor option").each(function(){
			 		if($(this).val() == ""){
			 			$(this).attr('selected', 'selected');
			 			$scope.equipmentData.isPWSensorAttached=null;
						return false;
			 		}
			 	});
				 $('#equipmentModal').modal('show');
				 $('#assetModalLabel').text("Add new Asset");
			 }
			 
			 $scope.addService=function(){
				 $scope.serviceData={};
				 $('#resetServiceAssetForm').click();
					$("#categorySelect option").each(function() {
						if ($(this).val() == "") {
							$(this).attr('selected', 'selected');
							 $scope.assetCategory.selected=null;
							return false;
						}
				 	});
			 	
			 	$("#locationSelect option").each(function() {
					if ($(this).val() == "") {
						$(this).attr('selected', 'selected');
						 $scope.assetLocation.selected=null;
						return false;
					}
				});
			 	
			 	$("#spSelect option").each(function() {
					if ($(this).val() == "") {
						$(this).attr('selected', 'selected');
						 $scope.serviceProvider.selected=null;
						return false;
					}
				});
			 	
			
			 	
			 	$("#siteSelect option").each(function(){
			 		if($(this).val() == ""){
			 			$(this).attr('selected', 'selected');
			 			 $scope.accessSite.selected=null;
						return false;
			 		}
			 	});
				 $('#serviceModal').modal('show');
				 $('#assetServiceModalLabel').text("Add new Asset");
			 }
			 
			 
			 $scope.editAsset=function(){
				 if($scope.selectedAsset.assetType == 'E'){
					 $('#equipmentModal').modal('show');
					 $('#assetModalLabel').text("Update Asset");
						 console.log($scope.selectedAsset);
						 $scope.equipmentData=angular.copy($scope.selectedAsset);
						 	$("#categorySelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.categoryId) {
									$(this).attr('selected', 'selected');
									$scope.assetCategory.selected.assetCategoryId = $scope.selectedAsset.categoryId;
									return false;
								}
						 	});
					 	
					 	$("#locationSelect option").each(function() {
							if ($(this).val() == $scope.selectedAsset.locationId) {
								$(this).attr('selected', 'selected');
								$scope.assetLocation.selected.locationId = $scope.selectedAsset.locationId;
								return false;
							}
						});
					 	
					 	$("#spSelect option").each(function() {
							if ($(this).val() == $scope.selectedAsset.serviceProviderId) {
								$(this).attr('selected', 'selected');
								$scope.serviceProvider.selected.serviceProviderId = $scope.selectedAsset.serviceProviderId;
								return false;
							}
						});
					 	
					 	$("#drpIsAsset option").each(function(){
					 		if($(this).val() == $scope.selectedAsset.isAssetElectrical){
					 			$(this).attr('selected', 'selected');
					 			$scope.equipmentData.isAssetElectrical=$scope.selectedAsset.isAssetElectrical;
								return false;
					 		}
					 	});
					 	
					 	$("#drpIsPowersensor option").each(function(){
					 		if($(this).val() == $scope.selectedAsset.isPWSensorAttached){
					 			$(this).attr('selected', 'selected');
					 			$scope.equipmentData.isPWSensorAttached=$scope.selectedAsset.isPWSensorAttached;
								return false;
					 		}
					 	});
					 	
					 	$("#siteSelect option").each(function(){
					 		if($(this).val() == $scope.selectedAsset.siteId){
					 			$(this).attr('selected', 'selected');
					 			$scope.accessSite.selected.siteId = $scope.selectedAsset.siteId;
								return false;
					 		}
					 	});
					 
					 }
				 
				 else if($scope.selectedAsset.assetType == 'S'){
					 $('#serviceModal').modal('show');
					 $('#assetServiceModalLabel').text("Update Asset");
						 console.log($scope.selectedAsset);
						 $scope.serviceData=angular.copy($scope.selectedAsset);
						 	$("#serviceCategorySelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.categoryId) {
									$(this).attr('selected', 'selected');
									$scope.assetCategory.selected.assetCategoryId = $scope.selectedAsset.categoryId;
								return false;
							}
						});
					 	
					 	$("#serviceLocationSelect option").each(function() {
							if ($(this).val() == $scope.selectedAsset.locationId) {
								$(this).attr('selected', 'selected');
								$scope.assetLocation.selected.locationId= $scope.selectedAsset.locationId;
								return false;
							}
						});
					 	if($scope.selectedAsset.serviceProviderId !=  null){
						 	$("#serviceSPSelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.serviceProviderId) {
									$(this).attr('selected', 'selected');
									$scope.serviceProvider.selected.serviceProviderId = $scope.selectedAsset.serviceProviderId;
									return false;
								}
							});
					 	}
					 	
					 	if($scope.selectedAsset.siteId !=  null){
						 	$("#serviceSiteSelect option").each(function(){
						 		if($(this).val() == $scope.selectedAsset.siteId){
						 			$(this).attr('selected', 'selected');
						 			$scope.accessSite.selected.siteId = $scope.selectedAsset.siteId;
									return false;
						 		}
						 	});
					 	}
					 }
			 	}
			 $scope.getUserSiteAccess=function(){
				 $('#loadingDiv').show();
				 userService.getUserSiteAccess()
					.then(function(data) {
		    			console.log(data);
		    			if(data.statusCode == 200){
		    				if(data.object.length>0){
		    					$scope.accessSite.list=[];
		    					$("#siteSelect").empty();
		    					$("#serviceSiteSelect").empty();
			    				$.each(data.object,function(key,val){
			    					var accessedSite={
				    						accessId:val.accessId,
				    						site:val.site,
				    						siteId:val.site.siteId,
				    						siteName:val.site.siteName
				    				}
			    					$scope.accessSite.list.push(accessedSite);
			    					 $('#loadingDiv').hide();
			    				});
			    				
			    				var options = $("#siteSelect");
		    					options.append($("<option />").val("").text("Select Site"));
		    					$.each($scope.accessSite.list,function() {
									options.append($("<option />").val(	this.siteId).text(this.siteName));
								});
		    					var options = $("#serviceSiteSelect");
		    					options.append($("<option />").val("").text("Select Site"));
		    					$.each($scope.accessSite.list,function() {
									options.append($("<option />").val(	this.siteId).text(this.siteName));
								});
		    				}
		    				
		    			}
		            },
		            function(data) {
		                console.log('Unable to get access list')
		            });
			 }
			 $scope.testSaveAssetObject=function(){
				 $scope.equipmentData={
							'assetId' : null,
							'assetName' : 'TestAsset',
							'categoryId' : 1,
							'locationId':1,
							'assetType':'E',
							'assetCode':'AC1',
							'modelNumber':'Model1',						
							'content':'Content1',						
							'imagePath':'',
							'documentPath':'',
							'serviceProviderId':14,
							'commisionedDate':'05-05-2015',
							'deCommissionedDate':'05-07-2017',
							'comments':'No comments',
							'isAssetElectrical':1,
							'isPWSensorAttached':1,
							'pwSensorNumber':'CEN12345666',
							'site':'Shell UK',
							'siteId':"1"
						}
				 $scope.saveAssetInfo($scope.equipmentData);
			 }
			 
			 $scope.getServiceProviders=function(customer){
				 $('#loadingDiv').show();
					serviceProviderService.getServiceProviderByCustomer(customer)
					.then(function(data) {
		    			console.log(data);
		    			$scope.serviceProvider.list=[];
		    			$("#spSelect").empty();
		    			$("#serviceSPSelect").empty();
		    			if(data.statusCode == 200){
		    				$.each(data.object,function(key,val){
		    					$scope.serviceProvider.list.push(val);
		    				});
		    			}
		    			var options = $("#spSelect");
    					options.append($("<option />").val("").text(
						"Select Service Provider"));
    					$.each($scope.serviceProvider.list,function() {
							options.append($("<option />").val(	this.serviceProviderId).text(this.name));
						});
    					
    					var options = $("#serviceSPSelect");
    					options.append($("<option />").val("").text("Select Service Provider"));
    					$.each($scope.serviceProvider.list,function() {
							options.append($("<option />").val(	this.serviceProviderId).text(this.name));
						});
    					 $('#loadingDiv').hide();
		            },
		            function(data) {
		                console.log('Unable to get  Service Provider list')
		                $('#loadingDiv').hide();
		            });
				}
			 
			 $scope.retrieveAssetCategories=function(){
				 $('#loadingDiv').show();
				 assetService.retrieveAssetCategories()
				 .then(function(data) {
		    			console.log(data);
		    			$scope.assetCategory.equipmentList=[];
		    			$scope.assetCategory.serviceList = []
		    			
		    			$("#categorySelect").empty();
		    			$("#serviceCategorySelect").empty();
		    			$.each(data,function(key,val){
		    				if(val.assetType=='E'){
		    					$scope.assetCategory.equipmentList.push(val);
		    				}
		    				
		    				else if(val.assetType=='S'){
		    					$scope.assetCategory.serviceList.push(val);
		    				}
		    				
		    			});	
		    			var options = $("#categorySelect");
    					options.append($("<option />").val("").text(
						"Select Category"));
    					$.each($scope.assetCategory.equipmentList,function() {
							options.append($("<option />").val(	this.assetCategoryId).text(	this.assetCategoryName));
						});
    					
    					var options = $("#serviceCategorySelect");
    					options.append($("<option />").val("").text(
						"Select Category"));
    					$.each($scope.assetCategory.serviceList,function() {
							options.append($("<option />").val(	this.assetCategoryId).text(	this.assetCategoryName));
						});
    					 $('#loadingDiv').hide();
		            },
		            function(data) {
		                console.log('Asset Categories retrieval failed.')
		                $('#loadingDiv').hide();
		            });
			 }
				
			 
			 $scope.getAssetLocations=function(){
				 $('#loadingDiv').show();
				 assetService.getAssetLocations()
				 .then(function(data) {
		    			console.log(data);
		    			$scope.assetLocation.list=[];
		    			$("#locationSelect").empty();
		    			$("#serviceLocationSelect").empty();
		    			$.each(data,function(key,val){
		    				$scope.assetLocation.list.push(val);
		    				
		    			});	
		    			
		    			var options = $("#locationSelect");
    					options.append($("<option />").val("").text(
						"Select Location"));
    					$.each($scope.assetLocation.list,function() {
							options.append($("<option />").val(	this.locationId).text(	this.locationName));
						});
    					
    					var options = $("#serviceLocationSelect");
    					options.append($("<option />").val("").text(
						"Select Location"));
    					$.each($scope.assetLocation.list,function() {
							options.append($("<option />").val(	this.locationId).text(	this.locationName));
						});
		    			console.log($scope.assetLocation.list);
		    			 $('#loadingDiv').hide();
		            },
		            function(data) {
		                console.log('Asset Locations retrieval failed.')
		            });
				 
			 }
			 
			 $scope.IsValidDate = function (decommissionDate, commissionDate) {				 
					
				 var isValid = false;
				 console.log(decommissionDate);
				 if(decommissionDate == null || decommissionDate == "" || typeof(decommissionDate) === "undefined"){
					 isValid = true;					 
				 }
				 else{					 
					 var isSame;
					 var part1 = decommissionDate.split('-');
					 var part2 = commissionDate.split('-');				 
					 decommissionDate =  new Date(part1[1]+'-'+part1[0]+'-'+part1[2]);
					 commissionDate =  new Date(part2[1]+'-'+ part2[0]+'-'+ part2[2]);				 
			         isValid = moment(decommissionDate).isAfter(commissionDate);
			          if (!isValid){
			        	  isValid = moment(decommissionDate).isSame(commissionDate);
			          }	
				 }		         	          
		          
		          return isValid;
		      }
			
			/* $scope.saveAssetEquipment=function(){
				 $scope.equipmentData.categoryId=$scope.assetCategory.selected.assetCategoryId;
				 $scope.equipmentData.locationId=$scope.assetLocation.selected.locationId;
				 $scope.equipmentData.siteId=$scope.accessSite.selected.siteId;
				 if($scope.serviceProvider.selected!=null){
					 $scope.equipmentData.serviceProviderId=$scope.serviceProvider.selected.serviceProviderId;
				 }
				 console.log($scope.equipmentData);
				$scope.saveAssetInfo($scope.equipmentData);
			 }*/
			 
			 $scope.saveAssetEquipment=function(){				 	          
		          
		          if($scope.IsValidDate($scope.equipmentData.deCommissionedDate,$scope.equipmentData.commisionedDate)){
		        	  $('#modalMessageDiv').hide();
		        	  $scope.equipmentData.categoryId=$scope.assetCategory.selected.assetCategoryId;
						 $scope.equipmentData.locationId=$scope.assetLocation.selected.locationId;
						 $scope.equipmentData.siteId=$scope.accessSite.selected.siteId;
						 if($scope.serviceProvider.selected!=null){
							 $scope.equipmentData.serviceProviderId=$scope.serviceProvider.selected.serviceProviderId;
						 }
						 console.log($scope.equipmentData);
						 
						$scope.saveAssetInfo($scope.equipmentData);
	    				
		          }
		          else{
		        	  $scope.modalErrorMessage = "Decommission date should be after Commission date";
		                $('#modalMessageDiv').show();
	    				$('#modalMessageDiv').alert();							
		          }		          
				 
			 }
			 
			 $scope.saveAssetService =function(){
				 
				 if($scope.IsValidDate($scope.serviceData.deCommissionedDate,$scope.serviceData.commisionedDate)){
		        	  $('#serviceModalMessageDiv').hide();
		        	  $scope.serviceData.categoryId=$scope.assetCategory.selected.assetCategoryId;
						 $scope.serviceData.locationId=$scope.assetLocation.selected.locationId;
						 $scope.serviceData.siteId=$scope.accessSite.selected.siteId;
						 if($scope.serviceProvider.selected!=null){
						 $scope.serviceData.serviceProviderId=$scope.serviceProvider.selected.serviceProviderId;
						 }
						 $scope.saveAssetInfo($scope.serviceData);
	    				
		          }
		          else{
		        	  $scope.modalErrorMessage = "Decommission date should be after Commission date";
		                $('#serviceModalMessageDiv').show();
	    				$('#serviceModalMessageDiv').alert();							
		          }		     
				
			 }
			 $scope.saveAssetInfo=function(assetData){
				 $('#loadingDiv').show();
				 assetService.saveAssetObject(assetData)
				 .then(function(data) {
		    			console.log(data);
		    			if(data.statusCode == 200){
		    				$scope.successMessage = data.message;
		    				$('#messageWindow').show();
		    				$('#successMessageDiv').show();
		    				$('#successMessageDiv').alert();
		    				$('#infoMessageDiv').hide();
		    				if(assetData.assetType == 'E'){
		    					$('#assetModalCloseBtn').click();
		    				}
		    				else if(assetData.assetType == 'S'){
		    					$('#serviceModalCloseBtn').click();
		    				}
		    				$scope.getAllAsset();
		    				$('#loadingDiv').hide();
		    			}
		            },
		            function(data) {
		                console.log('Error while saving asset data')
		                $scope.modalErrorMessage = data.message;
		                $('#modalMessageDiv').show();
	    				$('#modalMessageDiv').alert();
	    				if(assetData.assetType == 'E'){
	    					$('#modalMessageDiv').show();
		    				$('#modalMessageDiv').alert();
	    				}
	    				else if(assetData.assetType == 'S'){
	    					$('#serviceModalMessageDiv').show();
		    				$('#serviceModalMessageDiv').alert();
	    				}
	    				$('#loadingDiv').hide();
		            });
			 }
			 
	
			 $scope.closeMessageWindow=function(){
				 $('#messageWindow').hide();
				 $('#modalMessageDiv').hide();
			 }
			 
			
}]);
function validateDropdownValues(dropDownId, assetType){
	var scope = angular.element("#assetWindow").scope();
	 var valueId = parseInt($("#"+dropDownId).val());
	 if(valueId == ""){
		 
	 }else{
		 if(assetType == 'E'){
				 if(dropDownId.toUpperCase() == "CATEGORYSELECT"){
					 var category={
							 assetCategoryId:parseInt($("#categorySelect").val()),
					 		 assetCategoryName:$("#categorySelect option:selected").text()
					 }
					 scope.assetCategory.selected = category;
				 }
				 if(dropDownId.toUpperCase() == "LOCATIONSELECT"){
					 var location={
							 locationId:parseInt($("#locationSelect").val()),
					 		 locationName:$("#locationSelect option:selected").text()
					 }
					 scope.assetLocation.selected =location;
				 }
				 if(dropDownId.toUpperCase() == "SPSELECT"){
					 var serviceProvider={
							 serviceProviderId:parseInt($("#spSelect").val()),
					 		 serviceProviderName:$("#spSelect option:selected").text()
					 }
					 scope.serviceProvider.selected = serviceProvider;
				 }
				 if(dropDownId.toUpperCase() == "SITESELECT"){
					 var site={
							 siteId:parseInt($("#siteSelect").val()),
					 		 siteName:$("#siteSelect option:selected").text()
					 }
					 scope.accessSite.selected =site;
				 }
				 scope.equipmentData.assetType=assetType;
		 }else if(assetType == 'S'){
			 if(dropDownId.toUpperCase() == "SERVICECATEGORYSELECT"){
				 var category={
						 assetCategoryId:parseInt($("#serviceCategorySelect").val()),
				 		 assetCategoryName:$("#serviceCategorySelect option:selected").text()
				 }
				 scope.assetCategory.selected = category;
			 }
			 if(dropDownId.toUpperCase() == "SERVICELOCATIONSELECT"){
				 var location={
						 locationId:parseInt($("#serviceLocationSelect").val()),
				 		 locationName:$("#serviceLocationSelect option:selected").text()
				 }
				 scope.assetLocation.selected =location;
			 }
			 if(dropDownId.toUpperCase() == "SERVICESPSELECT"){
				 var serviceProvider={
						 serviceProviderId:parseInt($("#serviceSPSelect").val()),
				 		 serviceProviderName:$("#serviceSPSelect option:selected").text()
				 }
				 
				 scope.serviceProvider.selected = serviceProvider;
			 }
			 if(dropDownId.toUpperCase() == "SERVICESITESELECT"){
				 var site={
						 siteId:parseInt($("#serviceSiteSelect").val()),
				 		 siteName:$("#siteSelect option:selected").text()
				 }
				 scope.accessSite.selected =site;
			 }
			 scope.serviceData.assetType=assetType;
		 }
	 }
}