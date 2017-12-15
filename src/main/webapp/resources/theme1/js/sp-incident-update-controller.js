
chrisApp.controller('spIncidentUpdateController',  ['$rootScope', '$scope', '$filter','siteService','authService',
                                        'siteCreationService','companyService','userService','districtService',
                                        'areaService','clusterService','countryService','assetService',
                                        'ticketCategoryService','ticketService','serviceProviderService','statusService',
                              function  ($rootScope, $scope , $filter,siteService, authService,
                                        siteCreationService,companyService,userService,districtService,
                                        areaService,clusterService,countryService,assetService,
                                        ticketCategoryService,ticketService,serviceProviderService,statusService) {
		
		
		$scope.ticket={
			 selected:{},
			 list:[]
		}
		
		$scope.assetTypechecked = 'undefined';
		$scope.linkedTicketDetails=[];
		$scope.selectedLinkedTicketDetails=[];
		$scope.escalationLevelDetails=[];
		$scope.ticketHistoryDetail={};
		$scope.ticketData={};
		$scope.selectedSite={};
		$scope.selectedAsset={};
		$scope.selectedCategory={};
		$scope.selectedTicketStatus={};
		$scope.selectedCloseCode={};
		$scope.sessionUser={};
		$scope.slaPercent = '60';
		$scope.ticketComments=[];
		$scope.errorMessage="Logged, Service Provider WIP, Pending Review options are allowed to select ";
		$scope.files = [];
		
		
		$scope.statusList=[];
		$scope.status={
				selected:{},
				 list:[]	
		}
		$scope.accessSite={
				 selected:{},
				 list:[]
		 }
		$scope.selectedAsset={
				 selected:{},
				 list:[]
		 }
		$scope.selectedCategory={
				 selected:{},
				 list:[]
		 }
		$scope.selectedTicketStatus={
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
		
		$scope.selectedEscalation={};
		
		$scope.categoryList=[];
		
		
		$scope.siteAssignedUserList=[];
		$scope.siteUnAssignedUserList=[];
		
		$scope.incidentImages=[];
		$scope.totalIncidentImageSize = 0;
		var viewMode = null
		angular.element(document).ready(function(){
			console.log("loaded");
			$scope.initalizeCloseDiv();			
			$scope.selectedTicket={};
			$scope.getStatus();
			$scope.getSelectedStatus();
			viewMode = $('#mode').val();
			if(viewMode.toUpperCase() == 'EDIT'){
				$scope.getIncidentSelected();
				$scope.setTicketraisedOnDate();
				
			}
			
			
		});
		
		
		$scope.getSelectedStatus=function(){
			/*var selectedStatusId = $('#statusSelect').text();
			console.log("IBM status");
			console.log(selectedStatusId);
			//parseInt($("#"+dropDownId).val());
*/		
			var selectedStatusId=parseInt($('#statusSelect').val());
			 $scope.ticketData.status=$('#statusSelect option:selected').text();
			 console.log("IBM status");
				console.log(selectedStatusId);
				console.log($scope.ticketData.status);
		}
		
		$scope.getIncidentSelected=function(){
			ticketService.getSelectedTicketFromSession()
			.then(function(data){
				console.log(data)
				if(data.statusCode == 200){
					$scope.ticketData=angular.copy(data.object);
					$scope.setSLAWidth($scope.ticketData);
					$scope.getTicketCategory();
					$scope.getLinkedTicketDetails($scope.ticketData.ticketId);
					$scope.getEscalationLevel();
					$scope.getTicketHistory();
					if($scope.ticketData.statusId == 6){
						$scope.getCloseCode();
						$('#ticketCloseDiv').show();
						$('#closeNote').prop("disabled", true);
						$.each($scope.closeCodeList,function(key,val){
							if(val.id == $scope.ticketData.closeCode){
								$scope.ticketDate.codeClosed=val.code;
								$('#closeCode').prop("disabled", true);
								return false;
							}
						});
						
						$('#closeCode').prop("disabled",true);
					}
					if(data.object.ticketComments.length>0){
						 $scope.ticketData.comments = data.object.ticketComments;
						 $scope.ticketComments=[];
						 $.each(data.object.ticketComments,function(key,val){
							  $scope.ticketComments.push(val);
						 })
						 
					}
					
					if($scope.ticketData.attachments.length>0){
						$scope.ticketData.files=[];
						$.each($scope.ticketData.attachments,function(key,val){
							var  fileInfo={
									fileName: val.substring(val.lastIndexOf("/")+1),
									filePath: val
							}
							$scope.ticketData.files.push(fileInfo);
						});
					}
					
					
				}
			},function(data){
				console.log(data)
			});
		}
		
		$scope.setSLAWidth=function(ticketData){
            if(ticketData.slaPercent > 100){
                $scope.ticketData.width = 100;                
            }
            else{
                $scope.ticketData.width = ticketData.slaPercent;
            }                
        }
		
		
		
		$scope.linkedTicket={
				'ticketNumber':''
		};
		$scope.ticketComment={
				'comment':''
		};
		
		
		$scope.getEscalationLevel=function(){
			if(viewMode.toUpperCase()=='EDIT'){
					$.each($scope.ticketData.escalationLevelList,function(key,val){
						var escLevelData={
								escId:val.escId,
								spId:val.serviceProviderId,
								escLevelId:val.levelId,
								escLevelDesc:val.escalationLevel,
								escTo:val.escalationPerson,
								escEmail:val.escalationEmail,
								ticketNumber:$scope.ticketData.ticketNumber,
								ticketId:$scope.ticketData.ticketId,
								escStatus:val.status	
						};
						$scope.escalationLevelDetails.push(escLevelData);
					});
				}
		}
		
		
		
		
		$scope.getTicketCategory=function(){
			$('#loadingDiv').show();
			ticketCategoryService.retrieveAllCategories()
			.then(function(data) {
    			console.log(data)
    				$scope.categoryList=[];
    				if(data.length>0){
    					$.each(data,function(key,val){
    						var category={
    	            				categoryId:val.id,
    	            				categoryName:val.ticketCategory
    	            		}
    						$scope.categoryList.push(category);
    					});
    					$('#loadingDiv').hide();
    					$("#ticketCategorySelect").empty();
    					var options = $("#ticketCategorySelect");
    					options.append($("<option />").val("").text("Select category"));
    					$.each($scope.categoryList,function() {
    						options.append($("<option />").val(	this.categoryId).text(	this.categoryName));
    					});
    					if(viewMode.toUpperCase() == 'EDIT'){
    						$("#ticketCategorySelect option").each(function() {
    							console.log($(this).val());
								if ($(this).val() == $scope.ticketData.categoryId) {
									$(this).attr('selected', 'selected');
									return false;
								}
						 	});
    						$scope.selectedCategory.selected = $scope.ticketData.categoryName;
    					}
    				}else{
    					console.log("No categories found")
    				}
    				$('#loadingDiv').hide();
            },
            function(data) {
                console.log(data)
                console.log("No categories found")
				$('#loadingDiv').hide();
            });
			
		}
		
		$scope.getTicketPriority=function(){
			$scope.priorityList=[{
				'priorityId':1,
				'priorityCode':'P1',
				'priorityName':'Critical'
			},
			{
				'priorityId':2,
				'priorityCode':'P2',
				'priorityName':'High'
			},
			{
				'priorityId':3,
				'priorityCode':'P3',
				'priorityName':'Medium'
			},
			{
				'priorityId':4,
				'priorityCode':'P4',
				'priorityName':'Low'
			}];
			
			$("#prioritySelect").empty();
			
			var options = $("#prioritySelect");
			options.append($("<option />").val("").text(
			"Select Priority"));
			$.each($scope.priorityList,function() {
				options.append($("<option />").val(	this.priorityId).text(	this.priorityName));
			});
			
			var category={
					categoryId:$scope.ticketData.categoryId
			}
			$scope.setTicketPriorityAndSLA(category);
			
		}
		
		$scope.initalizeCloseDiv=function(){
			$('#ticketCloseDiv').hide();
			$("#closeCodeSelect").attr('required', false);
			$("#raisedOn").attr('required', false);
			$("#closeNote").attr('required', false);
		}
		
		$scope.getStatus=function(){
			/*if(viewMode.toUpperCase()=='NEW'){
				$("#statusSelect").empty();
				var options = $("#statusSelect");
				options.append($("<option />").val("").text("Select status"));
				options.append($("<option />").val(1).text("RAISED"));
			}else if(viewMode.toUpperCase()=='EDIT'){*/
				statusService.retrieveAllStatus()
                .then(function(data){
                	$("#statusSelect").empty();
    				var options = $("#statusSelect");
    				options.append($("<option />").val("").text("Select status"));
    				$.each(data,function(){
    					options.append($("<option />").val(	this.statusId).text(	this.status));
    				});
    				$("#statusSelect option").each(function() {
						console.log($(this).val());
						if ($(this).val() == $scope.ticketData.statusId) {
							$(this).attr('selected', 'selected');
							return false;
						}
				 	});
                },function(data){
                	console.log(data);
                });
			//}
		}
		
		$scope.getCloseCode=function(){
			$scope.closeCodeList =[{
				'id':1,
				'code':'Root Cause Fixed'
			},
			{
				'id':2,
				'code':'Workaround Provided'
			}];
			
			$("#closeCodeSelect").empty();
			
			var options = $("#closeCodeSelect");
			options.append($("<option />").val("0").text("Select option"));
			$.each($scope.closeCodeList,function() {
				options.append($("<option />").val(	this.id).text(	this.code));
			});
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
	    					
		    				$.each(data.object,function(key,val){
		    					var accessedSite={
			    						accessId:val.accessId,
			    						site:val.site,
			    						siteId:val.site.siteId,
			    						siteName:val.site.siteName
			    				}
		    					$scope.accessSite.list.push(accessedSite);
		    					
		    				});
		    				
		    				var options = $("#siteSelect");
	    					options.append($("<option />").val("").text("Select Site"));
	    					$.each($scope.accessSite.list,function() {
								options.append($("<option />").val(	this.siteId).text(this.siteName));
							});
	    					$('#loadingDiv').hide();
	    				}
	    				
	    			}
	            },
	            function(data) {
	                console.log('Unable to get access list')
	            });
		 }
		
		
		$scope.getAsset=function(selectedSite){
			console.log(selectedSite);
			 $('#loadingDiv').show();
			 assetService.findAllAssets(selectedSite.siteId)
				.then(function(data) {
 			console.log(data);
 				if(data.length>0){
					$scope.assetList=[];
					$.each(data,function(key,val){
	    				$scope.assetList.push(val);
	    			});
					if($scope.assetType == null){
						//alert($scope.assetType);
					}else{
						//alert($scope.assetType);
						$scope.populateAssetType($scope.assetType);
					}
					$scope.getTicketCategory();
 				  }
 				$('#loadingDiv').hide();
				},  function(data) {
	                console.log('Unable to get asset list')
	                $('#loadingDiv').hide();
				});
			
		}
		
		 $scope.populateAssetType=function(type){
			 
			 var selectedSite = $('#siteSelect').val();
			 if(selectedSite == ""){
				 //alert ("No Site Selected");
				 
			 }else{
			 if(type.toUpperCase()=='EQUIPMENT'){
				 $scope.assetTypechecked = 'E';
					console.log($scope.assetTypechecked);
					var equipmentList = [];
					$.each($scope.assetList,function(key,val){
						if(val.assetType == 'E' && val.siteId == $scope.accessSite.selected.siteId){
							equipmentList.push(val);
						}
					});
				 $("#assetSelect").empty();
				 var options = $("#assetSelect");
	    			options.append($("<option />").val("").text(
	    			"Select asset"));
	    			$.each(equipmentList,function() {
	    					options.append($("<option />").val(	this.assetId).text(	this.assetName));
	    			});
			 }else if(type.toUpperCase()=='SERVICE'){
				 $scope.assetTypechecked = 'S';
					console.log($scope.assetTypechecked);
					var serviceList = [];
					$.each($scope.assetList,function(key,val){
						if(val.assetType == 'S' && val.siteId == $scope.accessSite.selected.siteId){
							serviceList.push(val);
						}
					});
				 $("#assetSelect").empty();
				 var options = $("#assetSelect");
	    			options.append($("<option />").val("").text(
	    			"Select asset"));
	    			$.each(serviceList,function() {
	    					options.append($("<option />").val(	this.assetId).text(	this.assetName));
	    			});
			 }
			 }
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
			     
	     //----------------------- View ticket ------------------------------------
	     
	     
		 
		 $scope.getTicketDetails=function(ticket){
			console.log(ticket);
			$scope.selectedTicket={};
			//$scope.selectedTicket=angular.copy(ticket);
			ticketService.retrieveTicketDetails(ticket)
			.then(function(data){
				console.log(data);
			},function(data){
				
			});
		}

		 
		 $scope.setTicketServiceProvider=function(asset){
			 $scope.ticketData.sp=asset.serviceProviderId;
		 }
		 
		 $scope.setTicketPriorityAndSLA=function(ticketCategory){
			 
			 var spId = $scope.ticketData.sp;
			 if(viewMode.toUpperCase()=='EDIT'){
				 spId=parseInt($scope.ticketData.assignedTo);
		      }
			 if(spId==undefined){
				 //alert("No Site Selected");
				 return false;
			 }else{
				 $('#loadingDiv').show();
			 ticketService.getTicketPriorityAndSLA(spId,ticketCategory.categoryId)
			 .then(function(data){
				 console.log(data);
				 if(data.statusCode == 200){
					 $scope.ticketData.priorityId = data.object.priorityId;
					 $scope.ticketData.priorityDescription = data.object.priorityName;
					 $scope.ticketData.sla=data.object.ticketSlaDueDate;
					 $scope.ticketData.categoryId=data.object.ticketCategoryId;
					 $scope.ticketData.unit= data.object.units;
					 $scope.ticketData.duration = data.object.duration;
					 if(viewMode.toUpperCase()=='EDIT'){
						 $.each($scope.priorityList,function(key,val){
							 if(val.priorityId == $scope.ticketData.priorityId){
								 $('#prioritySelect').val(""+val.priorityId+"").prop('selected', true);
								 return false;
							 }
						 });
					 }
				 }
				 $('#loadingDiv').hide();
			 },function(data){
				 console.log(data);
				 $('#loadingDiv').hide();
			 });
			 }
			
		 }
		 $scope.setTicketraisedOnDate=function(){
			 
			$scope.CurrentDate = new Date();
			 $scope.CurrentDate = $filter('date')(new Date(), 'dd-MM-yyyy');
			 $scope.ticketData.raisedOn = $scope.CurrentDate;
		 }
		
		 
		 $scope.saveTicket=function(){				 	          
			 console.log("save ticket called");
			 
			 $scope.ticketData.siteId=$scope.accessSite.selected.siteId;
			 $scope.ticketData.siteName=$scope.accessSite.selected.siteName;
			 
			 $scope.ticketData.assetId=$scope.assetList.selected.assetId;
			 $scope.ticketData.assetName=$scope.assetList.selected.assetName;
			 
			 $scope.ticketData.categoryId=$scope.categoryList.selected.categoryId;
			 $scope.ticketData.categoryName=$scope.categoryList.selected.categoryName;
			 
			 $scope.ticketData.statusId=$('#statusSelect').val();
			 
			 $scope.ticketData.ticketStartTime = $('#ticketStartTime').val();
			 //console.log(moment($scope.ticketData.ticketStartTime, 'YYYY-MM-DD h:m:s A').format('YYYY-MM-DD HH:mm:ss'));
			 //$scope.ticketData.ticketStartTime = moment($scope.ticketData.ticketStartTime, 'DD-MM-YYYY HH:MM').format('DD-MM-YYYY HH:MM');
			
			 
			 console.log($scope.accessSite);
			 console.log($scope.accessSite.selected);
			 
			 console.log($scope.ticketData);
			 
			 $scope.persistTicket($scope.ticketData);
		 }
		 
		 $scope.updateTicket=function(){
			 $scope.ticketData.statusId=parseInt($('#statusSelect').val());
			 $scope.ticketData.status=$('#statusSelect option:selected').text();
			 if(parseInt($("#closeCodeSelect").val())!=0){
				 $scope.ticketData.closeCode =  parseInt($("#closeCodeSelect").val());
			 }
			 console.log($scope.ticketData);
			 $scope.persistTicket($scope.ticketData);
			 
		 }
		 $scope.persistTicket=function(ticketData){
			 $('#loadingDiv').show();
			 ticketService.saveTicket(ticketData,"sp")
			 .then(function(data){
				 console.log(data);
				 if(data.statusCode == 200){
					 $scope.successMessage = data.message;
					 	$('#messageWindow').show();
	    				$('#successMessageDiv').show();
	    				$('#successMessageDiv').alert();
	    				$('#errorMessageDiv').hide();
	    				if(viewMode.toUpperCase()=='NEW'){
	    					window.location.href=hostLocation+"/incident/details";
	    				}else{
	    					
	    				}
				 }
				 $('#loadingDiv').hide();
			 },function(data){
				console.log(data); 
				$('#loadingDiv').hide();
				$('#messageWindow').show();
				$('#successMessageDiv').hide();
				$('#errorMessageDiv').show();
				$('#errorMessageDiv').alert();
			 });
		 }

		 $scope.closeMessageWindow=function(){
			 $('#messageWindow').hide();
			 $('#modalMessageDiv').hide();
			 
		 }
	$scope.getTicketComment=function(){
		$scope.ticketComments;
	}
	
	$scope.addNewComment = function(){
		console.log("comment added");
		$scope.CurrentDate = new Date();
		$scope.CurrentDate = $filter('date')(new Date(), 'dd-MM-yyyy');
		if($scope.ticketComment.comment != ""){
			/*$scope.ticketComments.push({ 
				  ticket_id:$scope.ticketData.ticketId,
				  ticketNumber : $scope.ticketData.ticketNumber,
				  createdBy:$scope.sessionUser.email,
				  createdDate:$scope.CurrentDate,
				  Comment:$scope.ticketComment.comment
			 });*/
			var ticketComment={
					  ticketId:$scope.ticketData.ticketId,
					  ticketNumber : $scope.ticketData.ticketNumber,
					  comment:$scope.ticketComment.comment
			}
			 console.log(ticketComment);
			
			 ticketService.saveComment(ticketComment,"sp")
			 .then(function(data){
				 console.log(data);
				 if(data.statusCode == 200){
					 $("#ticketMessage").val("");
					 $scope.ticketComments.push(data.object);
				 }
			 },function(data){
				 console.log(data);
			 });
		}
		 
	};
	
	$scope.LinkNewTicket = function(){
		if($scope.linkedTicket.ticketNumber != ""){
			var linkedTicket={
					parentTicketId:$scope.ticketData.ticketId,
					parentTicketNo:$scope.ticketData.ticketNumber,
					linkedTicketNo:$scope.linkedTicket.ticketNumber
			}
			ticketService.linkTicket(linkedTicket,"sp")
			.then(function(data){
				console.log(data);
				if(data.statusCode == 200){
				console.log("Linked ticket added");
				 $("#linkedTicket").val("");
				 $scope.getLinkedTicketDetails(linkedTicket.parentTicketId);
				}
			},function(data){
				console.log(data);
			});
			
		}
		 
	};
	

	
	$scope.getLinkedTicketDetails=function(linkedTicket){
		ticketService.getLinkedTickets(linkedTicket,"sp")
		.then(function(data){
			console.log(data);
			if(data.statusCode == 200){
			 $("#linkedTicket").val("");
			 if(data.object.linkedTickets.length>0){
				 $scope.ticketData.linkedTickets = data.object.linkedTickets;
				 var valueId = parseInt($("#statusSelect").val());
				 //Enable save button if status is Logged and linked ticket is available.
				 if(valueId == 2){
					 $("#btnSavePrimary").prop("disabled", false);
				 }
			 }
			 else if(data.object.linkedTickets.length == 0){
				 var valueId = parseInt($("#statusSelect").val());
				 //Enable save button if status is Logged and linked ticket is available.
				 if(valueId == 2){
					 $("#btnSavePrimary").prop("disabled", true);
				 }
			 }
			 
			}
		},function(data){
			console.log(data);
		});
	}
	
	$scope.closeLinkedTicketConfirmation=function(){
		//console.log($scope.selectedEscalation);
		if($scope.selectedLinkedTicketDetails.length ==0 ){
			$('#messageWindow').show();
			$('#errorMessageDiv').show();
			$('#errorMessageDiv').alert();
			$scope.errorMessage="At a time select 1 opened linked ticket to close the status."
		}
		else if($scope.selectedLinkedTicketDetails.length > 0){
			//console.log($scope.escalationLevelDetails);
			$('#confirmClose').modal('show');
		}
	}
	
	$scope.unlinkTicketConfirmation=function(index, linkedTicket){
		var valueId = parseInt($("#statusSelect").val());
		if($scope.ticketData.linkedTickets.length == 1 && valueId == 2){			 
			 //provide warning message and donot allow to unlink.
			$('#confirmLoggedUnlinkChange').modal('show');
		 }
		else{
			$('#confirmUnlink').modal('show');
			$scope.unlinkTicketIndex = index;
			$scope.unlinkTktObject = angular.copy(linkedTicket);
		}
		
		
		
	}
	

	
	$scope.getSelectedEscalation=function(selectedEscalation){
		$scope.selectedEscalation = angular.copy(selectedEscalation)
		console.log($scope.selectedEscalation);
	}
	$scope.escalateTicket=function(){
		console.log($scope.selectedEscalation);
		if($scope.selectedEscalation != "undefined"){
			console.log($scope.escalationLevelDetails);
			//call API to save in DB
			//call API to save in DB
			ticketService.escalateTicket($scope.selectedEscalation)
			.then(function(data){
				console.log(data);
				if(data.statusCode ==200){
					$scope.selectedEscalation.escStatus = data.object.escalationStatus;
					angular.forEach($scope.escalationLevelDetails, function(escalation){
						if(escalation.escId == data.object.escId){
							escalation.escStatus = $scope.selectedEscalation.escStatus;
							return false;
						}
						
					});
					
				}
				initializeEscalateTicket();
				$scope.getLinkedTicketDetails($scope.ticketData.ticketId);
			},function(data){
				console.log(data);
			});
		}
		console.log("initialized");
		//$scope.initializeEscalateTicket();
		
	}
	
	$scope.getTicketHistory=function(){
		var ticketId =  $scope.ticketData.ticketId;
		ticketService.getTicketHistory(ticketId)
		.then(function(data){
			console.log(data);
			if(data.statusCode == 200){
				var ticketHistory={};
				ticketHistory.ticketId=$scope.ticketData.ticketNumber;
				ticketHistory.ticketStartDate=$scope.ticketData.raisedOn;
				ticketHistory.ticketCloseDate=$scope.ticketData.closedOn;
				if(data.object.length>0){
					
					var	history=[];
					
					$.each(data.object,function(key,val){
						var ticketHistory={
								name:val.who,
								date:val.timeStamp,
								description:val.message	
						};
						history.push(ticketHistory)
					});
					ticketHistory.history=history;
					
				}else{
					
				}
				$scope.ticketHistoryDetail=angular.copy(ticketHistory);
				
				
			}
		},function(data){
			console.log(data)
		});
		
/*		$scope.ticketHistoryDetail={
			'ticketId':'INC001222',
			'ticketStartDate':'15-7-2017',
			'ticketCloseDate':'20-7-2017',
			'history' : [{ 'date': "20-7-2017",
	            'name': "Ranjan Nayak",
	            'description': "closed the ticket "
	        },
	        {   'date': "17-7-2017",
	            'name': "Malay Panigrahi",
	            'description': "changes status from WIP to Fixed "
	        },			
	        {   'date': "16-7-2017",
	            'name': "Swadhin Mohanta",
	            'description': "changes status from Logged to WIP "
	        },
	        {
	            'date': "16-7-2017",
	            'name': "shibasish mohanty",
	            'description': "changes status from Raised to Logged "	            
	        }	        
	        ]
		}*/
		
	}
	
	$scope.openFileAttachModal=function(){
		$scope.incidentImageList=[{
			id:0,
			attachment:""
		}];
		$('#incidentImage0').val('');
		$('#fileAttachModal').modal('show');
		$('#fileAttachModal').removeData();
		
	}
	
	$scope.addNewImage=function(){
		var length=$scope.incidentImageList.length;
		var imageComponent={
				id:length,
				attachment:""	
		}
		$scope.incidentImageList.push(imageComponent);
	};
	
	$scope.removeImage=function(indexPos){
		$scope.incidentImageList.splice(indexPos,1);
		  $.each($scope.incidentImages,function(key,val){
			 if(val.incidentImgPos==indexPos) {
					$scope.incidentImages.splice(indexPos,1);					
					return false;
			 }
		  });
		//console.log($scope.incidentImageList);
		console.log($scope.incidentImages);
	}
	
    $scope.getIndexedName = function (val, e) {
    	console.log(val.id);
		var incidentImageId=val.id;
		$scope.incidentImageId=incidentImageId;
		 var ext=$("input#"+incidentImageId).val().split(".").pop().toLowerCase();
		 var fileSize = Math.round((val.files[0].size / 1024)) ;
		 $scope.totalIncidentImageSize = $scope.totalIncidentImageSize + fileSize ;
		 console.log($('#'+incidentImageId).val());
	     $scope.fileExtension = ext;
	     $scope.indexPos=incidentImageId.charAt(incidentImageId.length-1);
	     if($.inArray(ext, ["jpg","jpeg","JPEG", "JPG","PDF","pdf","doc","docx","DOC","DOCX","png","PNG"]) == -1) {
	    	 $scope.incidentImageModalErrorMessage="Supported file types to upload are jpg, docx, png and pdf";
        	 $('#incidentImageModalMessageDiv').show();
        	 $('#incidentImageModalMessageDiv').alert();
        	 $('#fileerrorincident').text('Supported file types to upload are jpg, docx, png and pdf');
              $scope.isfileselected=false;	    	 
	         return false;
	     }else if($scope.totalIncidentImageSize > 500){
	    	 $scope.incidentImageModalErrorMessage="File size exceeds Max limit (500KB).";
	    	 $('#fileerrorincident').text('File size exceeds Max limit (500KB)');
        	 $('#incidentImageModalMessageDiv').show();
        	 $('#incidentImageModalMessageDiv').alert();
              $scope.isfileselected=false;
         }
	     else if (e.target.files != undefined) {
	    	// console.log(URL.createObjectURL(e.target.files[0]));
	         $scope.isfileselected=true;
	         file = $('#'+incidentImageId).prop('files');
	      	 var reader = new FileReader();
	     	 reader.onload = $scope.onFileUploadReader;
	     	 reader.readAsDataURL(file[0]);
	       }
    };
    $scope.onFileUploadReader=function(e){
    	var fileUrl = e.target.result;
		console.log(fileUrl);
		var incidentImage={
					fileName:$scope.ticketData.ticketNumber,
					incidentImgId:$scope.incidentImageId,
					incidentImgPos:$scope.indexPos,
					base64ImageString:e.target.result,
					fileExtension: $scope.fileExtension
				}
		
		$scope.incidentImages.push(incidentImage);
		console.log($scope.incidentImages);
	}
    
   
    // NOW UPLOAD THE FILES.
    $scope.uploadFiles = function () {
    	if($scope.incidentImages.length>0){
			 $scope.ticketData.incidentImageList=$scope.incidentImages;
			 $('#btnUploadCancel').click();
		 }
    }

	$scope.openAssetModal=function(){
		 var selectedSite = $('#siteSelect').val();
		 if(selectedSite == ""){
			// alert ("No Site Selected");
			 
		 }else{
			if($scope.assetTypechecked == 'E'){
				$scope.addEquipmentModal();
			}
			if($scope.assetTypechecked == 'S'){
				$scope.addServiceModal();
			}
		 }
	}
	
	$scope.addEquipmentModal=function(){
		//var scope = angular.element("#incidentWindow").scope();
		 console.log("shibasish");
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
		 //$scope.accessSite.selected={};
		/* $("#siteSelect option").each(function(){
		 		if($(this).val() == ""){
		 			$(this).attr('selected', 'selected');
		 			 $scope.accessSite.selected=null;
					return false;
		 		}
		 	});*/
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
		 $('#assetModalLabel').text("Add new Asset for "+ $scope.accessSite.selected.siteName);
	}

	$scope.addServiceModal=function(){
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
	 	
	 	
		 $('#serviceModal').modal('show');
		 $('#assetServiceModalLabel').text("Add new Asset for "+ $scope.accessSite.selected.siteName);
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
    			
    			console.log($scope.assetCategory.equipmentList);
    			console.log("shibu3333");
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
    				$('#loadingDiv').hide();
    				var ticketSite={
    						siteId : data.object.siteId
    				}
    				$('#assetModalCloseBtn').click();
    				$scope.getAsset(ticketSite);
    			}
            },
            function(data) {
                console.log('Error while saving asset data')
                $scope.modalErrorMessage = data.message;
                $('#modalMessageDiv').show();
				$('#modalMessageDiv').alert();
				$('#loadingDiv').hide();
            });
	 }
	 
	 $scope.getSelectedLinkedTicket=function(selectedLinkedTicket){
			
		 console.log($scope.ticketData.linkedTickets);
		 console.log(selectedLinkedTicket);
		 var selectedLinkedTicketCount = 0;
			$scope.selectedLinkedTicket = angular.copy(selectedLinkedTicket)
			if($scope.selectedLinkedTicketDetails != "undefined"){
				selectedLinkedTicketCount = $scope.ticketData.linkedTickets.length;
			}
			
			//$scope.selectedLinkedTicketDetails.push($scope.selectedLinkedTicket);
			//console.log($scope.selectedLinkedTicketDetails);
			
			if($scope.selectedLinkedTicket != "undefined"){
				//$scope.selectedEscalation.escStatus = 'escalated';
				if(selectedLinkedTicketCount == 0){
					$scope.selectedLinkedTicketDetails.push($scope.selectedLinkedTicket);
				}
				else if(selectedLinkedTicketCount >0){
					for(key in selectedLinkedTicket){
						if(key=="selected" && selectedLinkedTicket.selected==true){
							if(selectedLinkedTicket.closedFlag=="CLOSED"){
								$('#closedBtn').prop("disabled",true);
							return false;
							}else{
							$scope.selectedLinkedTicketDetails.push($scope.selectedLinkedTicket);
							$('#closedBtn').prop("disabled",false);
							return false;
							}
						}
						
						if(key=="selected" && selectedLinkedTicket.selected==false){
							$scope.selectedLinkedTicketDetails.pop($scope.selectedLinkedTicket);
						}
					}
					
				}
				
				console.log($scope.selectedLinkedTicketDetails);
				
			}
			
		}

	 $scope.closeLinkedTicket=function(){
		 if($scope.selectedLinkedTicketDetails.length==1){
			 var linkedTicket = {};
			 linkedTicket.detail= $scope.selectedLinkedTicketDetails[0];
			 linkedTicket.status="CLOSED";
			 ticketService.changeLinkedTicketStatus(linkedTicket,"sp")
			 .then(function(data){
				 if(data.statusCode==200){
					 if(data.object.linkedTickets>0){
						 $scope.ticketData.linkedTickets = data.object.linkedTickets
					 }
					 $scope.getLinkedTicketDetails($scope.ticketData.ticketId);
					 $scope.selectedLinkedTicketDetails = [];
					 $('#confirmClose').modal('hide');
				 }
			 },function(data){
				 console.log(data);
			 });
		 }else{
			 alert("At a time single ticket status can be closed")
		 }
	 }
	 
	 $scope.unlinkTicket=function(){
		 var linkedTicket = angular.copy($scope.unlinkTktObject);
		 ticketService.deleteLinkedTicket(linkedTicket,"sp")
		 .then(function(data){
			 console.log(data);
			 if(data.statusCode == 200){
				 $scope.ticketData.linkedTickets =  data.object;
				 for(key in linkedTicket){
						if(key=="selected" && $scope.selectedLinkedTicket.selected==false){
							$scope.selectedLinkedTicketDetails.pop($scope.selectedLinkedTicket);
						}
					}
				 $scope.getLinkedTicketDetails($scope.ticketData.ticketId);
				 $('#confirmUnlink').modal('hide');
			 }
		 },function(data){
			 console.log(data);
		 });
	 }

		$scope.openChatBox=function(){
				$('#chatWindow').fadeIn();
				$scope.chatBoxView="ON";
		}
		$scope.closeWindow=function(){
			$('#chatWindow').fadeOut();
		}
			 
}]);


function getSelectedSite(dropDownId){
	var scope = angular.element("#spIncidentUpdateWindow").scope();
	if(dropDownId.toUpperCase() == "SITESELECT"){
		 var site={
				 siteId:parseInt($("#siteSelect").val()),
		 		 siteName:$("#siteSelect option:selected").text()
		 }
		 scope.accessSite.selected =site;
		 scope.getAsset(site);
	 }
}

function getSelectedAsset(dropDownId){
	var scope = angular.element("#spIncidentUpdateWindow").scope();
	if(dropDownId.toUpperCase() == "ASSETSELECT"){
		 var asset={
				 assetId:parseInt($("#assetSelect").val()),
		 		 assetName:$("#assetSelect option:selected").text()
		 }
		 scope.assetList.selected = asset;
		 $.each(scope.assetList,function(key,val){
			if(val.assetId == asset.assetId){
				console.log(val);
				$('#assignedTo').val(val.serviceProviderName);
				scope.setTicketServiceProvider(val);
				return false;
			} 
		 });
		 
	 }
}

function getSelectedCategory(dropDownId){
	var scope = angular.element("#spIncidentUpdateWindow").scope();
	if(dropDownId.toUpperCase() == "TICKETCATEGORYSELECT"){
		 var category={
				 categoryId:parseInt($("#ticketCategorySelect").val()),
		 		 categoryName:$("#ticketCategorySelect option:selected").text()
		 }
		 scope.categoryList.selected =category;
		// scope.getTicketPriority();
		 scope.setTicketPriorityAndSLA(scope.categoryList.selected);
	 }
}

function getSelectedPriority(dropDownId){
	var scope = angular.element("#incidentWindow").scope();
	if(dropDownId.toUpperCase() == "PRIORITYSELECT"){
		 var priority={
				 categoryId:parseInt($("#prioritySelect").val()),
		 		 categoryName:$("#prioritySelect option:selected").text()
		 }
		 scope.priorityList.selected =priority;
	 }
}

function ticketStatusChange(dropDownId){
	var scope = angular.element("#spIncidentUpdateWindow").scope();
	console.log("status change");
	var valueId = parseInt($("#"+dropDownId).val());
	//scope.selectedSite = valueId;
	if( valueId == 1 || valueId == 5 || valueId == 6 || valueId == 7){
		
		$("#btnSavePrimary").prop("disabled", true);	
		$('#confirmStatusChange').modal('show');
		
	}
	else if(valueId == 2 && scope.ticketData.linkedTickets.length == 0 ){
		$("#btnSavePrimary").prop("disabled", true);	
		$('#confirmLoggedStatusChange').modal('show');
	}
	else{
		//$('#errorMessageDiv').hide();
		$("#btnSavePrimary").prop("disabled", false);
		
	}
	
	if(dropDownId.toUpperCase() == "STATUSSELECT"){
		 var ticketStatus={
				 statusId:parseInt($("#statusSelect").val()),
		 		 statusName:$("#statusSelect option:selected").text()
		 }
		 scope.statusList.selected =ticketStatus;
	 }
	console.log(valueId);
	//console.log(selectedStatusId);
}

function validateDropdownValues(dropDownId, assetType){
	var scope = angular.element("#spIncidentUpdateWindow").scope();
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

function initializeEscalateTicket(){
	var scope = angular.element("#spIncidentUpdateWindow").scope();
	var escalated=false;		
	var escalationLevelCount = scope.escalationLevelDetails.length;
	if(escalationLevelCount > 0){			
		
		for(var i = 0; i <= escalationLevelCount-1; i++){				
			//if(scope.escalationLevelDetails[i].escStatus!=null){
			if(scope.escalationLevelDetails[i].escStatus!=null && scope.escalationLevelDetails[i].escStatus.toUpperCase() == 'ESCALATED'){					
				$("#chkEscalation"+i).prop("disabled", true);	
				$("#chkEscalation"+(i+1)).prop("disabled", false);
				escalated=true;					
				$("#chkEscalation"+i).prop("checked", false);					
			}
			else if(escalated){					
				if((i) < escalationLevelCount-1){
					$("#chkEscalation"+(i+1)).prop("disabled", true);
				}					
				if(scope.escalationLevelDetails[i].escStatus!=null && scope.escalationLevelDetails[i].escStatus.toUpperCase() == 'ESCALATED'){
				if((i) == escalationLevelCount-1){
					$("#chkEscalation"+(i+1)).prop("disabled", false);						
				}					
				}
				else if(i == escalationLevelCount-1){
					if(scope.escalationLevelDetails[i].escStatus!=null && scope.escalationLevelDetails[i-1].escStatus.toUpperCase() != 'ESCALATED'){
						$("#chkEscalation"+i).prop("disabled", true);
					}
					if(scope.escalationLevelDetails[i].escStatus!=null && $scope.escalationLevelDetails[i-1].escStatus.toUpperCase() == 'ESCALATED'){
						$("#chkEscalation"+i).prop("disabled", false);
					}
				}					
				else if(scope.escalationLevelDetails[i].escStatus!=null && scope.escalationLevelDetails[i].escStatus.toUpperCase() != 'ESCALATED'){
					if(scope.escalationLevelDetails[i].escStatus!=null && $scope.escalationLevelDetails[i-1].escStatus.toUpperCase() == 'ESCALATED'){							
						$("#chkEscalation"+i).prop("disabled", false);
					}
				}
				else if(scope.escalationLevelDetails[i].escStatus!=null && scope.escalationLevelDetails[i].escStatus.toUpperCase() != 'ESCALATED'){
					if((i) == escalationLevelCount-1){
						$("#chkEscalation"+i).prop("disabled", false);							
					}						
				}					
			}
			//}
			}
		// enable only 1st level
		
		if(!escalated){
			$("#chkEscalation0").prop("disabled",false);
			for(var i = 1; i <= escalationLevelCount-1; i++){
				$("#chkEscalation"+i).prop("disabled", true);
			}
		}
		
	}
};

function getTicketHistory(){
	var scope = angular.element("#spIncidentUpdateWindow").scope();
	scope.getTicketHistory();
}


	
