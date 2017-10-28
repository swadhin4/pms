<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<html ng-app="chrisApp">
<head>
<title>Home</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='0'>
<meta http-equiv='pragma' content='no-cache'>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/site-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/site-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<style>
	.main-box.no-header {
    padding-top: 20px;
}

.currentSelected{
	background:rgba(60, 141, 188, 0.58);
    color:#fff;
}
.currentSelected a{
	color:#fff
}
#errorMessageDiv, #successMessageDiv, #infoMessageDiv{
    top: 0%;
    left: 50%;
   /*  width: 45em; */
    height: 3em;
    margin-top: 4em;
    margin-left: -4em;
    border: 1px solid #ccc;
    background-color: #fff;
    position: fixed;
}
 #modalMessageDiv{
   top: -7%;
    left: 47%;
    /* width: 45em; */
    height: 3em;
    margin-top: 4em;
    margin-left: -15em;
    border: 1px solid #ccc;
    background-color: #fff;
    position: fixed;
    }
.messageClose{
	background-color: #000;
    padding: 8px 8px 10px;
    position: relative;
    left: 8px;
}

.reqDiv.required .control-label:after {
  content:"*";
  color:red;
}



</style>

<script>
$(function() {
	 $('.toggle-on').removeAttr('style');
	 $('.toggle-off').removeAttr('style');
	 
   $('body').on('focus',".licensedate", function(){
       $(this).datepicker({
    	   format:'dd-mm-yyyy',
       });
   });
   $('.select2').select2();
   
   var isIE = window.ActiveXObject || "ActiveXObject" in window;
   if (isIE) {
       $('.modal').removeClass('fade');
   }
   
   $('input').each(function (i, e) {
	    var label;
	    switch ($(e).attr('id')) {
	        case 'lat':
	            label = 'Please enter latitude ranging from [ -90 to 90 ]';
	            break;
	        case 'lng':
	            label = 'Please enter longitude ranging from [ -180 to 180 ]';
	            break;
	    }
	    $(e).tooltip({ 'trigger': 'focus', 'title': label });
	});
   
   $("#lat").keyup(function () {
	    if (this.value.match(/^[-]?(\d+)?([.]?\d{0,30})?$/)) {
	    	var latValue =  $("#lat").val();
	    	if ((latValue > -90 && latValue < 90) || latValue == "-"){
	    		return true;
	    	}
	    	else{
	    		$("#lat").val("");
	    	}	    		
	       
	    } else {
	    
	    	$("#lat").val("");
	    }
	});
	
	$("#lng").keyup(function () {
	    if (this.value.match(/^[-]?(\d+)?([.]?\d{0,30})?$/)) {
	    	var lngValue =  $("#lng").val();
	    	if ((lngValue > -180 && lngValue < 180) || lngValue == "-"){
	    		return true;
	    	}
	    	else{
	    		$("#lng").val("");
	    	}	    		
	       
	    } else {
	    
	    	$("#lng").val("");
	    }
	});
   
  })
  
  function validate_tab(thisform) {          
    
         $('.tab-pane input, .tab-pane select, .tab-pane textarea').on(
                'invalid', function() {
                
                    // Find the tab-pane that this element is inside, and get the id
                    var $closest = $(this).closest('.tab-pane');
                    var id = $closest.attr('id');
                    //alert(id);
                    // Find the link that corresponds to the pane and have it show
                    $('.nav a[href="#' + id + '"]').tab('show');
                }); 
    }
  
</script>
</head>
<div id="page-content-wrapper" >
	<div class="page-content">
		<div class="container-fluid" ng-controller="siteController" id="siteWindow">
		<div style="display:none" id="loadingDiv"><div class="loader">Loading...</div></div>
			<section class="content" style="min-height: 35px; display: none"
				id="messageWindow">
				<div class="row">
					<div class="col-md-12">
						<div class="alert alert-success alert-dismissable"
							id="successMessageDiv"
							style="display: none; height: 34px; white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Success! </strong> {{successMessage}} 
							<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
						</div>
						<div class="alert alert-info alert-dismissable"
							id="infoMessageDiv"
							style="display: none; height: 34px; white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Info! </strong> {{InfoMessage}} 
							<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
						</div>
						<div class="alert alert-danger alert-dismissable" id="errorMessageDiv"
							style="display: none;  height: 34px;white-space: nowrap;">
							<button type="button" class="close">x</button>
							<strong>Error! </strong> {{errorMessage}} <span class="messageClose" ng-click="closeMessageWindow()">X</span>
						</div>
					</div>
				</div>
			</section>	
			<section class="content">
				<div class="row">
				<div class="col-md-6">
				<div class="row">
						<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">List of Sites</h3>
								<div class="box-tools pull-right">
									<sec:authorize access="hasAnyRole('ROLE_SALES_MANAGER', 'ROLE_OPS_MANAGER')">
										<a href  data-toggle="modal" class="btn btn-success" ng-click="addNewSite()" >
										<i class="fa fa-sitemap" arial-hidden="true"></i> Add Site</a>
									</sec:authorize>	
									
								</div>
							</div>
							<div class="box-body" style="height:70%" >
								<div class="row">
	 								<div class="col-md-12">
										<input type="text" class="form-control"	placeholder="Search Site" ng-model="searchSite">
									 </div>
										<div class="col-md-12" ng-if="siteList.length> 0">
											<ul class="products-list product-list-in-box">
												<li class="item"
													ng-repeat="site in siteList | filter:searchSite">
													<div class="product-img" style="margin-top: -12px;">
														<img src="${contextPath}/resources/theme1/img/site-icon.png"
															alt="Product Image">
													</div>
													<div class="product-info">
														<a href="javascript:void(0)"
															ng-click="getSiteDetails(site)" class="product-title">{{site.siteName}}
														</a> <span class="product-description">
															{{site.address}} </span>
													</div>
												</li>

											</ul>
										</div>
										
										
									</div>
              				 </div>
                            
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
										<a  class="btn btn-danger">Total Sites :  <span class="badge">{{siteList.length}}</span></a>
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
										<!-- 	<a href class="btn btn-info"><i class="fa fa-user" arial-hidden="true"></i> Add User</a> -->
										</div>
									</div>
								</div>
							</div>
						</div>
							</div>
							
						</div>
						
								<div class="col-md-6">
						<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Site Detail</h3>
								<div class="box-tools pull-right">
								<sec:authorize access="hasAnyRole('ROLE_SALES_MANAGER', 'ROLE_OPS_MANAGER')">
								<div class="btn-group pull-right" ng-if="siteList.length> 0">
								<a href class="btn btn-success" style="margin-right: 5px;"
									 data-toggle="modal" ng-click="manageUserAccess(selectedSite)">Manage User Access <span class="fa fa-user"></span></a>
									<a href class="btn btn-success" style="margin-right: 5px;"
									 data-toggle="modal" ng-click="updateSiteModal(selectedSite)">Edit Site <span class="fa fa-edit"></span></a>
									<button type="button"
										class="btn btn-success dropdown-toggle pull-right"
										style="margin-right: 5px;" data-toggle="dropdown">
										Manage Asset <span class="caret"></span>
									</button>

									<ul class="dropdown-menu" role="menu">
										<li></li>
										<li><a href data-toggle="modal" >Add
												an Equipment</a></li>
										<li><a href data-toggle="modal">Add	a Service</a></li>
										<li><a href="#" >View Asset
												</a></li>
									</ul>

								</div>
								</sec:authorize>
								</div>
							</div>
							<div class="box-body" style="overflow-y:auto;overflow-x:hidden;height:72%">
							   <div >
								<div class="row">
									
									<div class="col-md-12">
									 <div class="table-responsive">
										<table class="table no-margin">
											<thead>											
											<tr><td style="width:40%">Site Name</td><td align="right">{{selectedSite.siteName}}</td>
											</tr>
											<tr><td style="width:40%">Address</td><td align="right">{{selectedSite.siteAddress}}</td>
											</tr>
											</thead>
											<tbody>
											<tr><td>Site Owner</td><td align="right">{{selectedSite.retailerName}}</td></tr>
											<tr><td>Retailer</td><td align="right">{{sessionUser.company.companyName}}</td></tr>
											<tr><td>Electricity ID</td><td align="right">{{selectedSite.electricityId}}</td></tr>
											<tr><td>Site Number1</td><td align="right">{{selectedSite.siteNumber1}}</td></tr>
											<tr><td>Site Number2</td><td align="right">{{selectedSite.siteNumber2}}</td></tr>
											<!-- <tr><td>Contact</td><td align="right">{{selectedSite.contactName}}</td></tr>
											<tr><td>Phone</td><td align="right">{{selectedSite.primaryContact}}</td></tr> -->
											</tbody>
										</table>
										</div>
									</div>
								</div>
								
								
										<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Contact Information</h3>
							<div class="box-tools pull-right">
								<div class="btn-group pull-right">
									<a href="" class="btn btn-info" style="margin-right: 5px;" title="Edit contact information"
									data-toggle="modal" ng-click="viewTabSelected(selectedSite,'siteContactLink')">
									<span class="fa fa-edit fa-2x" style="    font-size: 1.2em;"></span></a>
								</div>
								</div>
							</div>
							<div class="box-body">
								
              <div class="table-responsive">
                <table class="table no-margin">
                  <thead>
                  <tr>
                    <th>Name</th><td>{{selectedSite.contactName}}</td>
                    </tr>
                    <tr>
                    <th>Email</th><td>{{selectedSite.email}}</td>
                    </tr>
                    <tr>
                    <th>Address</th><td>{{selectedSite.address}} </td>
                    </tr>
                    <tr>
                    <th>Latitude</th><td>{{selectedSite.latitude}} </td>
                    </tr>
                      <th>Longitude</th><td>{{selectedSite.longitude}} </td>
                    </tr>
                    <tr>
                    <th>Contact No</th><td><i class="fa fa-phone-square" aria-hidden="true"></i> {{selectedSite.primaryContact}}<br> 
                    					<i class="fa fa-phone-square" aria-hidden="true"></i> {{selectedSite.secondaryContact}}</td>
                    </tr>
                  </tr>
                  </thead>

                  </tbody>
                </table>
              </div>
              <!-- /.table-responsive -->
            </div>
						</div>
								
							<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Licence Information</h3>
							<div class="box-tools pull-right">
								<div class="btn-group pull-right">
									<a href="" class="btn btn-info" style="margin-right: 5px;" title="Edit License information"
									data-toggle="modal" ng-click="viewTabSelected(selectedSite,'siteLicenceLink')">
									<span class="fa fa-edit fa-2x" style="font-size: 1.0em;"></span></a>
								
								</div>
								</div>
							</div>
							<div class="box-body">
								
              <div class="table-responsive">
                <table class="table no-margin">
                  <thead>
                  <tr>
                    <th>Name</th>
                    <th>Valid From</th>
                    <th>Valid To</th>
                    <th>Attachment</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr ng-repeat="license in selectedSite.LicenseDetail">
                    <td><a href>{{license.licenseName}}</a></td>
                    <td>{{license.validfrom}}</td>
					<td>{{license.validto}}</td>
                    <td><span class="label label-success">Link</span></td>
                   
                  </tr>

                  </tbody>
                </table>
              </div>
              <!-- /.table-responsive -->
            </div>
           
								
							
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
											
										</div>
										<!-- /.description-block -->
									</div>
								</div>
							</div>
						</div>
						
							<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Operation Timings</h3>
							<div class="box-tools pull-right">
								<a href="" class="btn btn-info" style="margin-right: 5px;" title="Edit Operation timings"
									data-toggle="modal" ng-click="viewTabSelected(selectedSite,'siteOperationLink')">
									<span class="fa fa-edit fa-2x" style="font-size: 1.0em;"></span></a>
								
								</div>
							</div>
							<div class="box-body">
							<div class="row">
								<div class="col-md-6">			
						  <div class="table-responsive" style="overflow-x:hidden">
							<table class="table no-margin">
							  <thead>
							  <tr>
								<th>Weekdays</th>
								<th>Sales</th>
							  </tr>
							  </thead>
							  <tbody>
							  <tr ng-repeat="timing in selectedSite.SalesOperation">
								<td>{{timing.days}}</td>
								<td ng-if="timing.from == 'NO TIME' && timing.to == 'NO TIME'">
								<a href data-toggle="modal" ng-click="updateSiteModal(selectedSite)">
								<span class="label label-warning">Not Operating</span></a></td>
								<td ng-if="timing.from == 'NO TIME' && timing.to != 'NO TIME'">
								<a href data-toggle="modal" ng-click="updateSiteModal(selectedSite)">
								<span class="label label-danger">
								<i class="fa fa-exclamation-triangle" aria-hidden="true"></i> Update Start Time</span></a></td>	
								<td ng-if="timing.from != 'NO TIME' && timing.to == 'NO TIME'">
								<a href data-toggle="modal" ng-click="updateSiteModal(selectedSite)">
								<span class="label label-danger">
								<i class="fa fa-exclamation-triangle" aria-hidden="true"></i> Update End Time</span></a></td>
								<td ng-if="timing.from != 'NO TIME' && timing.to != 'NO TIME'">
								<span class="label label-success">{{timing.from}} - {{timing.to}} </span> </td>													 
							  </tr>

							  </tbody>
							</table>
						  </div>
						  </div>
						  <div class="col-md-6">			
						  <div class="table-responsive">
							<table class="table no-margin">
							  <thead>
							  <tr>
								<th>Weekdays</th>
							
								<th>Delivery</th>
								
							  </tr>
							  </thead>
							  <tbody>
							  <tr ng-repeat="timing in selectedSite.DeliveryOperation">
								<td>{{timing.days}}</td>
								<td ng-if="timing.from == 'NO TIME' && timing.to == 'NO TIME'"><span class="label label-warning">Not Operating</span></td>
								<td ng-if="timing.from == 'NO TIME' && timing.to != 'NO TIME'"><span class="label label-warning">Not Operating</span></td>	
								<td ng-if="timing.from != 'NO TIME' && timing.to == 'NO TIME'"><span class="label label-warning">Not Operating</span></td>
								<td ng-if="timing.from != 'NO TIME' && timing.to != 'NO TIME'"><span class="label label-success">{{timing.from}} - {{timing.to}} </span> </td>
												 
							  </tr>

							  </tbody>
							</table>
						  </div>
						  </div>
						  </div>
              
            </div>
           
								
							
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
											
										</div>
										<!-- /.description-block -->
									</div>
								</div>
							</div>
						</div>
										<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Submeter Details</h3>
							<div class="box-tools pull-right">
								<div class="btn-group pull-right">
									<a href="" class="btn btn-info" style="margin-right: 5px;" title="Edit Submeter details"
									data-toggle="modal" ng-click="viewTabSelected(selectedSite,'siteSubmeterLink')">
									<span class="fa fa-edit fa-2x" style="font-size: 1.0em;"></span></a>
								
								</div>
								</div>
							</div>
							<div class="box-body">
								
              <div class="table-responsive">
                <table class="table no-margin">
                  <thead>
                  <tr>
                    <th>Submeter Number</th>
                    <th>User</th>
                    
                  </tr>
                  </thead>
                 <tbody ng-repeat="submeter in selectedSite.submeterDetails">
																		<tr>
																			<td>{{submeter.subMeterNumber}}</td>
																			<td>{{submeter.subMeterUser}}</td>
																		</tr>
																	</tbody>
                </table>
              </div>
              <!-- /.table-responsive -->
            </div>
           
								
							
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
											
										</div>
										<!-- /.description-block -->
									</div>
								</div>
							</div>
						</div>
								</div>
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
											
										</div>
									</div>
								</div>
							</div>
							</div>
								<div class="box-footer">
								<div class="row">
									<div class="col-sm-4 col-xs-6">
										<div class="description-block border-right">
											<a href class="btn btn-success pull-left"> {{selectedSite.district.districtName}}</a>
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-4 col-xs-6">
										<div class="description-block border-right">
											<a href class="btn btn-success"> {{selectedSite.area.areaName}}</a>
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-4 col-xs-6">
										<div class="description-block">
											<a href class="btn btn-success pull-right"> {{selectedSite.cluster.clusterName}}</a>
										</div>
									</div>
								</div>
							</div>
						</div>
							
						</div>
						
						
						<div class="modal fade" id="createSiteModal" data-keyboard="false" data-backdrop="static">
	<div class="modal-dialog" style="width:82%;">
      <div class="modal-content">
       <form  name="createsiteform" ng-submit="saveSiteForm(createsiteform)">
        <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title"><span id="siteModalLabel">Create New Site</span>   |  <a class="btn btn-info">Company <span class="badge">{{sessionUser.company.companyName}}</span></a></h4>
			<div class="alert alert-danger alert-dismissable" id="modalMessageDiv"
				style="display: none;  height: 34px;white-space: nowrap;">
				<strong>Error! </strong> {{modalErrorMessage}} 
				<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
			</div>

		</div>
		 <div class="modal-body" style="background-color: #eee">
            
            <div class="nav-tabs-custom">
            <ul class="nav nav-tabs" style="background-color: rgba(60, 141, 188, 0.34);">
            
							<li class="active">
				       		 <a  href="#siteDetailsTab" data-toggle="tab" aria-expanded="true" id="siteViewLink"><b>Site Details</b></a>
							</li>
							<li><a href="#siteContactsTab" data-toggle="tab" aria-expanded="true" id="siteContactLink"><b>Site Contacts</b></a>
							</li>
							<li><a href="#licenseTab" data-toggle="tab" aria-expanded="true" id="siteLicenceLink"><b>License Details</b></a>
							</li>
				  			<li><a href="#operationTab" data-toggle="tab" aria-expanded="true" id="siteOperationLink"><b>Site Operation info</b></a>
							</li>
							<li><a href="#submeterTab" data-toggle="tab" aria-expanded="true" id="siteSubmeterLink"><b>Submeter Details</b></a>
							</li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="siteDetailsTab">
				<div class="row">
					<div class="box-body">
						<div class="col-md-3 col-sm-3">
							<div class="col-md-12">
							 <label for="country">Country</label>       
								<a class="btn btn-info" style="width:100%"><span class="badge">{{siteData.country.countryName}}</span></a>
								<!-- <a class="btn btn-warning" style="width:100%"><span class="badge">{{siteData.siteId}}</span></a> -->
							</div>
						</div>
						
						 <div class="col-md-9 col-sm-9">
							<div class="row">
							<div class="col-md-8 reqDiv required">
							  <input type="hidden"  class="form-control" placeholder="Enter Site Name" 
                 					name="siteName" ng-model="siteData.siteId" >	
							 <label class="control-label" for="siteName">Site Name</label>
								  <input type="text" maxlength="50" class="form-control" placeholder="Enter Site Name" 
                 					name="siteName" ng-model="siteData.siteName" required >	
							 </div>
							<div class="col-md-4 reqDiv required">
							 <label class="control-label" for="owner">Site Owner</label>
								  <input type="text" maxlength="50" class="form-control" placeholder="Enter Site Owner" 
                  				name="owner" ng-model="siteData.owner" required>	
							</div>
							</div>
								<div class="row">
							 <div class="col-xs-4">
				                <label for="district">District</label>
				                 <!--  <select class="form-control" ng-model="district.selected" id="districtSelect"
				                 ng-options="val as val.districtName for val in district.list" ng-change="getSelectedDistrict(district.selected)" required>
				                  </select> -->
				                  <select name="districtSelect" id="districtSelect" class="form-control" 
								  onchange="validateDropdownValues('districtSelect')">
									
								 </select>
								<input type="hidden" ng-model="district.selected">
				                </div>
				                <div class="col-xs-4">
				                <label for="area">Area</label>
				                <!--  <select class="form-control" ng-model="area.selected" ng-change="getSelectedArea(area.selected)"
				                 ng-options="val as val.areaName for val in area.list">
				                  </select> -->
				                  <select name="areaSelect" id="areaSelect" class="form-control" 
								  onchange="validateDropdownValues('areaSelect')">
									
								</select>
								<input type="hidden" ng-model="area.selected">
				                  
				                </div>
				                <div class="col-xs-4">
				                <label for="cluster">Cluster</label>
				                 <!--  <select class="form-control" ng-model="cluster.selected" ng-change="getSelectedCluster(cluster.selected)"
				                   ng-options="val as val.clusterName for val in cluster.list track by val.clusterID">
				                  </select> -->
				                  
				                  <select name="clusterSelect" id="clusterSelect" class="form-control" 
								onchange="validateDropdownValues('clusterSelect')">
									
								</select>
								<input type="hidden" ng-model="cluster.selected">
	               			 </div>
								</div>
								
								<div class="row">
								<div class="col-xs-4">
					               <label for="electricityId">Electricity ID</label>
									  <input type="text" maxlength="50" class="form-control" 
									  placeholder="Enter Electricity ID Number" name="electricityId" ng-model="siteData.electricityId">
					                </div>
									<div class="col-xs-4 reqDiv required">
					                <label class="control-label" for="siteNumber1">Site Number1</label>
					                  <input type="text" maxlength="11" class="form-control" placeholder="Enter Site Number1" 
					                  name="sitenumber1" ng-model="siteData.siteNumber1" required ng-pattern="onlyNumbers" ng-keypress="filterValue($event)">
					                </div>
					                <div class="col-xs-4">
					                <label for="siteNumber2">Site Number2</label>
					                  <input type="text" maxlength="11" class="form-control" placeholder="Enter Site Number2" 
					                  name="sitenumber2" ng-model="siteData.siteNumber2" ng-pattern="onlyNumbers" ng-keypress="filterValue($event)">
					               </div>
								</div>
								<div class="row">
									   <div class="col-xs-6">
					                <label for="fileInput">File Input</label>
					                  <input type="file" id="siteInputFile" class="form-control" 
					                  name="inputfilepath"  accept="image/*,.doc, .docx,.pdf" >
					                </div>
								</div>
								
						</div>
						</div>
						</div>
						</div>
              
              <div class="tab-pane" id="siteContactsTab">
				  <div class="row">
				   <div class="col-md-6">
					<div class="box-body">
						 <div class="row">
						  <div class="col-md-12">
                <div class="col-xs-6 reqDiv required">
                <label class="control-label" for="contactName">Contact Name</label>
                  <input type="text" maxlength="50" class="form-control" 
                  placeholder="Enter site contact name" name="contactname" ng-model="siteData.contactName" required>
                </div>
                <div class="col-xs-6">
                <div class="form-group reqDiv required"  >
                <label class="control-label" for="emailAddress">Email Address</label>
                  <input type="email" class="form-control"  maxlength="50"
                  placeholder="Enter email" name="email" ng-model="siteData.email" required>
                 </div>
                </div>
              </div>
			  </div>
              
            <div class="row">
                <div class="col-md-12">
                <div class="col-xs-6">
                <label for="longitude">Longitude</label>
                  <input type="text" rel="txtTooltip" data-toggle="tooltip"  id="lng" maxlength="30" class="form-control" 
                  placeholder="Enter longitude" name="longitude" ng-model="siteData.longitude"  >
                </div>
                <div class="col-xs-6">
                <label for="latitude">Latitude</label>
                  <input type="text" rel="txtTooltip" data-toggle="tooltip" id="lat" maxlength="30" class="form-control" 
                  placeholder="Enter latitude" name="latitude" ng-model="siteData.latitude" >
                </div>
              
                </div>
              </div>
			  
			  <div class="row">
                <div class="col-md-12">
                 <div class="col-xs-6">
                <div class="form-group reqDiv required"  >
                <label class="control-label" for="primaryContactNum">Primary contact No</label>
                  <input type="text" ng-pattern="onlyNumbers" ng-keypress="filterValue($event)" 
                   maxlength="11" class="form-control" placeholder="Enter Primary contact No" 
                  name="primaryphoneno" ng-model="siteData.primaryContact" required>
                </div>
                </div>
                <div class="col-xs-6">
                <label for="secondaryContactNum">Secondary Contact No</label>
                  <input type="text" min="0" maxlength="11" class="form-control" placeholder="Enter Secondary Contact No" 
                  name="secondaryphoneno" ng-model="siteData.secondaryContact" 
                  ng-pattern="onlyNumbers" ng-keypress="filterValue($event)">
                </div>
                </div>
              </div>
				   </div>
				   </div>
				<div class="col-md-6">
				<div class="box-body">
				<div class="row">
				<div class="col-md-12">
				
				<label for="siteAddress">Site Address</label>                  
				  <textarea class="form-control" style="width: 100%;
   				 height: 176px;" rows="3" placeholder="Enter site address" name="address" 
   				 ng-model="siteData.address"></textarea>
				
				</div>
			   </div>
	   		   </div>
	    	</div>
            </div>
		</div>
			
              <div class="tab-pane " id="licenseTab">
				<div class="row">
					<div class="box-body">
					   <div class="row">
					    <div class="col-md-12">
							<div class="form-group">
              <input ng-hide="!licenseDetails.length" type="button" class="btn btn-danger pull-right" style="margin-right: 5px;" ng-click="removeLicense()" id="btnRemove"
                 value="Remove">&nbsp;&nbsp;
              <input type="button" class="btn btn-success addnew pull-right" style="margin-right: 5px;" onclick="" ng-click="addNewLicense()" value="Add New">
           </div>
                
              <table id="example2" class="table table-bordered table-hover table-condensed">
                <thead>
                <tr>
                <th><input type="checkbox" ng-model="selectedAll" ng-click="checkAllLicense()" /></th>
                  <th><b>License Name</b></th>
                  <th><b>Valid From</b></th>
                  <th><b>Valid To</b></th>
                  <th><b>Attach File</b></th>                  
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="licenseDetail in licenseDetails">
                	<td class="reqDiv required"><label class="control-label"></label>
                     <input type="checkbox" ng-change="disableRemoveButton(licenseDetail.licenseId)"
                      ng-model="licenseDetail.selected"/></td>
                  <td>
                   <input type="hidden" class="form-control" ng-model="licenseDetail.licenseId">
                  <input type="text" class="form-control" ng-model="licenseDetail.licenseName" maxlength="50" required>
                   </td>
                  <td>
                  <div class="input-group date">
                  <div class="input-group-addon">
                    <i class="fa fa-calendar"></i>
                  </div>
                  <input type="text" class="form-control licensedate pull-right dtfrom{{$index}}" id="datepicker{{$index}}from" 
                  ng-model="licenseDetail.validfrom" ng-required="licenseDetail.licenseName">
                </div>                  
                   
                  </td>
                  <td>
                  
                  <div class="input-group date">
                  <div class="input-group-addon">
                    <i class="fa fa-calendar"></i>
                  </div>
                  <input type="text" class="form-control licensedate pull-right dtto{{$index}}" id="datepicker{{$index}}to" 
                  ng-model="licenseDetail.validto" ng-required="licenseDetail.licenseName">
                </div>                  
                  
                  </td>
                  <td>
                  <input type="file" id="exampleInputFile"> 
                  </td>
                  
                </tr>
                </tbody>
                </table>
						</div>
						</div>
						</div>
						</div>
						</div>
					
				    <div class="tab-pane " id="operationTab" >
				<div class="row">
					<div class="box-body">
					   <div class="row">
					    <div class="col-md-12">
							 <div class="col-md-6" >
           <div class="box-header pull-center" style="background-color:lightblue;height:35px">
              <span class="pull-center">
              <b>Sales Operation Schedule</b>
              </span>
            </div>
              <table id="example2" class="table table-bordered table-hover table-condensed">
                <thead>
                <tr>
                
                  <th><b>Operating Days</b></th>
                  <th style="width:43%"><b>Select Sales timing (24 hrs)</b></th>
                  <th><b>Sales Operation Timinings</b></th>               
                  
                </tr>
                </thead>
                <tbody>
             
          <tr  ng-repeat="salesoperationdetail in salesoperationDetails.list"> 
                               	                     
                  <td >
                  
                  <label class="control-label">{{salesoperationdetail.days}}</label>
                  
                  </td>
                  <td >
	               <div class="input-group">
	                 <input type="hidden" ng-model="salesoperationdetail.opId">
	                <select class="form-control" style="width: 50%;"  id="salesoperationdetailFrom{{$index}}"
	                ng-model="salesoperationdetail.selected.from" ng-change="setStartTime(salesoperationdetail, 'salesDayFrom' , {{$index}})" 
	                ng-options="val.name for val in  operatingTimes" >
	                <option value="">Start Time</option>
	                </select>
	                
	                   
	                <select class="form-control" style="width: 50%;" id="salesoperationdetailTo{{$index}}"
	                ng-model="salesoperationdetail.selected.to" ng-change="setEndTime(salesoperationdetail, 'salesDayTo', {{$index}})"
	                ng-options="val.name for val in  operatingTimes" >
	                 <option value="">End Time</option>
             			  </select>
	               	
	              </div>
                
                  </td>
                  
                  <td>
                 
                                  <div class="input-group">
    <input type="text" class="form-control"  placeholder="Start" id="salesDayFrom{{$index}}" ng-model="salesoperationdetail.selected.from.name" readonly />
    <span class="input-group-addon">-</span>
    <input type="text" class="form-control" placeholder="End" id="salesDayTo{{$index}}" ng-model="salesoperationdetail.selected.to.name" readonly />

                  </td>                  
                </tr>
                </tbody>
              </table>
              </div>
              
              <div class="col-md-6" >
              <div class="box-header pull-center" style="background-color:lightblue;height:35px">
              <span class="pull-center">
              <b>Delivery Operation Schedule</b>
              </span>
            </div>
            <div class="box-body no-padding">
              <table id="example2" class="table table-bordered table-hover table-condensed " style="height:50px;">
                <thead>
                <tr>
                
                  <th><b>Operating Days</b></th>
                  <th style="width:45%"><b>Select Delivery Timing (24 HRS)</b></th>
                  <th><b>Operating Timings</b></th>         
                  
                </tr>
                </thead>
                <tbody>
                <tr style="height:30px" ng-repeat="deliveryoperationdetail in deliveryoperationDetails.list" >                	                     
                  <td >
                  <hr style="padding:0px; margin:0px;"><label class="control-label">{{deliveryoperationdetail.days}}</label></td>
                     <td >
	               <div class="input-group">
	                   <input type="hidden" ng-model="deliveryoperationdetail.opId">
			                <select class="form-control  " style="width: 50%;"  id="deliveryoperationdetailFrom{{$index}}" 
			                ng-model="deliveryoperationdetail.selected.from" ng-change="setStartTime(deliveryoperationdetail,'deliveryDayFrom',{{$index}})"
			                ng-options="val.name for val in  operatingTimes" >
			                <option value="">Start Time</option>
			                </select>
			                <select class="form-control  " style="width: 50%;" id="deliveryoperationdetailTo{{$index}}"
			                ng-model="deliveryoperationdetail.selected.to" ng-change="setEndTime(deliveryoperationdetail,'deliveryDayTo',{{$index}})"
			                ng-options="val.name  for val in  operatingTimes" >
			                 <option value="">End Time</option>
	              			  </select>
	              			  
	               
	              </div>
                
                  </td>
                  <td>
                 
                                  <div class="input-group">
    <input type="text" class="form-control"  placeholder="Start" id="deliveryDayFrom{{$index}}" ng-model="deliveryoperationdetail.selected.from.name" readonly />
    <span class="input-group-addon">-</span>
    <input type="text" class="form-control" placeholder="End" id="deliveryDayTo{{$index}}" ng-model="deliveryoperationdetail.selected.to.name" readonly />

                  </td>                 
                </tr>
                </tbody>
              </table>
              </div>
              </div>
						</div>
						</div>
						</div>
						</div>
						</div>	
				    <div class="tab-pane " id="submeterTab">
				<div class="row">
					<div class="box-body">
					   <div class="row">
					    <div class="col-md-12">
							
           <div class="form-group">
              <input ng-hide="!submeterDetails.length" type="button" class="btn btn-danger pull-right"  style="margin-right: 5px;" ng-click="removeSubmeter()" value="Remove">
              <input type="button" class="btn btn-success addnew pull-right" style="margin-right: 5px;" ng-click="addNewSubmeter()" value="Add New">
           </div>
              <table id="example2" class="table table-bordered table-hover table-condensed ">
                <thead>
                <tr>
                <th><input type="checkbox" ng-model="selectedAll" ng-click="checkAllSubmeter()" /></th>
                  <th>Submeter Number</th>
                  <th>User</th>                                    
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="submeterDetail in submeterDetails">
                	<td class="reqDiv required"><label class="control-label"></label>
                        <input type="checkbox" ng-model="submeterDetail.selected"/></td>                     
                  
                  <td>               
                  <input type="hidden" class="form-control" ng-model="submeterDetail.subMeterId">   
                  <input type="text" maxlength="50" class="form-control" ng-model="submeterDetail.subMeterNumber" required>
                  </td>
                  <td>                  
                  <input type="text" maxlength="50" class="form-control" ng-model="submeterDetail.subMeterUser" 
                  ng-required="submeterDetail.subMeterNumber">                
                  </td>
                                    
                </tr>
                </tbody>
                </table>
						</div>
						</div>
						</div>
						</div>
						</div>
							
		
		</div>
		</div>
		</div>
       
				<div class="modal-footer">
					<button type="button" class="btn btn-default pull-left"	id="siteModalCloseBtn" data-dismiss="modal">Close</button>
					<button type="submit" onclick="validate_tab(createsiteform)" class="btn btn-success" >Save changes</button>
					<button type="reset" id="resetAddSiteForm" class="btn btn-success">RESET</button>
				</div>
				</form>
			</div>
			</div>
           </div>
           
           
           	<div class="modal fade" id="assignUserModal" data-keyboard="false" data-backdrop="static">
	<div class="modal-dialog" style="width:82%;">
      <div class="modal-content">
       <form  name="usersiteform" ng-submit="saveUserSiteForm(usersiteform)">
        <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title"><span id="siteModalLabel">Manage User</span>   <a class="btn btn-warning">Site <span class="badge">{{selectedSite.siteName}} - {{selectedSite.siteId}}</span></a></h4>
			<div class="alert alert-danger alert-dismissable" id="modalMessageDiv"
				style="display: none;  height: 34px;white-space: nowrap;">
				<strong>Error! </strong> {{modalErrorMessage}} 
				<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
			</div>

		</div>
		 <div class="modal-body" style="background-color: #eee">
			<div class="row">
				<div class="box">
					<div class="row">
						<div class="col-md-12">
							<div class="col-md-6">
							 	<div class="box box-primary">
								<div class="box-header pull-center"
									style="background-color: lightblue; height: 35px">
									<span class="pull-center"> <b>Search New User to Assign</b>
									</span>
								</div>
								 <div class="box-body no-padding">
										<input type="text" class="form-control" placeholder="Search by Name or Email" ng-model="unassignedUser" required style="width:100%">
									<table ng-if="unassignedUser!=null"
										class="table table-bordered table-hover table-condensed "
										style="height: 50px;">
										<thead>
											<tr>
												<th><b>Name</b></th>
												<th><b>Email</b></th>
												<th><b>Role</b></th>
												<th><b>Assign</b></th>
											</tr>
										</thead>
										<tbody>
											<tr ng-repeat="val in siteUnAssignedUserList | searchFor : unassignedUser">
												<td>{{val.firstName}} {{val.lastName}}</td>
												<td>{{val.email}}</td>
												<th><b>{{val.role.description}}</b></th>
												<td ng-if="val.role.roleName != 'ROLE_ADMIN'"><b>
												<a href ng-click="assignThisUserTo(val, selectedSite)"><span class="label label-danger">
												<i class="fa fa-check-circle-o" arial-hidden="true"></i> Click to Assign
												</span></a></b></td>
											</tr>
										</tbody>
									</table>
									</div>
								</div>
							</div>
							<div class="col-md-6">
							<div class="box box-primary">
								<div class="box-header pull-center"
									style="background-color: lightblue; height: 35px">
									<span class="pull-center"> <b>User assigned to Site</b>
									</span>
									
								</div>
								  <div class="box-body no-padding">
								  	<input type="text" class="form-control" placeholder="Search by Name or Email" ng-model="assignedUser" required style="width:100%">
									<table 
										class="table table-bordered table-hover table-condensed "
										style="height: 50px;">
										<thead>
											<tr>
												<th><b>Name</b></th>
												<th><b>Email</b></th>
												<th><b>Revoke</b></th>
											</tr>
										</thead>
										<tbody>
											<tr ng-repeat="val in siteAssignedUserList | searchFor : assignedUser">
												<td><b>{{val.firstName}} {{val.lastName}}</b></td>
												<td><b>{{val.email}}</b></td>
												<td ng-if="sessionUser.loggedInUserMail!='val.email'">
												<b><a href ng-click="revokeThisUserAccess(val, selectedSite)">
												<span class="label label-success">
												<i class="fa fa-check-circle-o" arial-hidden="true"></i> Click to Revoke
												</span></a></b></td>
											</tr>
										</tbody>
									</table>
								  </div>
								  </div>
							</div>
							
							
						</div>
					</div>
					
				</div>
			</div>
		</div>
		 <div class="modal-footer">
					<a href class="btn btn-default pull-left"	id="siteModalCloseBtn" data-dismiss="modal">Close</a>
					<a href class="btn btn-danger">Users Assigned<span class="badge">{{siteAssignedUserList.length}}</span></a>
					<!-- <a href class="btn btn-warning">Users Not Assigned<span class="badge">{{siteUnAssignedUserList.length}}</span></a> -->
				</div>
		 </form>
		 </div>
		 </div>
		 </div>
						</div>
						</section>
				</div>
				
				
		
		</div>
	</div>
</div>
</section>
</div>
</div>
</div>
