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

<link rel="stylesheet" media="screen" href='<c:url value="/resources/theme1/css/bootstrap-datetimepicker-standalone.css"></c:url>' />
<link rel="stylesheet" media="screen" href='<c:url value="/resources/theme1/css/bootstrap-datetimepicker.css"></c:url>' />
<link rel="stylesheet" media="screen" href='<c:url value="/resources/css/bootstrap-datetimepicker.min.css"></c:url>' />

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.min.js"></c:url>'></script>

<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/sp-incident-update-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/site-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/asset-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/service-provider-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>



<style>
	.main-box.no-header {
    padding-top: 20px;
}

.table tbody tr.currentSelected {
background-color: rgba(60, 141, 188, 0.58) !important;
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
    left: 37%;
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
.popover {max-width:500px;}



</style>

<script>

$(function() {
	
	
	$('#datetimepicker1').datetimepicker({
		format:'DD-MM-YYYY HH:MM A',
	});

	$('#datetimepicker2').datetimepicker({
		format:'DD-MM-YYYY HH:MM A',
	});
	$('#datetimepicker3').datetimepicker({
		format:'DD-MM-YYYY HH:MM A',
	});
	/* $('#ticketList').DataTable({
		"processing": true
		
	}); */
	
	$('.toggle-on').removeAttr('style');
	 $('.toggle-off').removeAttr('style');
	 
   
   $('.select2').select2();
   
   var isIE = window.ActiveXObject || "ActiveXObject" in window;
   if (isIE) {
       $('.modal').removeClass('fade');
   } 
  
      
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
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<div class="content-wrapper">
	<div ng-controller="spIncidentUpdateController" id="spIncidentUpdateWindow">
	<div style="display:none" id="loadingDiv"><div class="loader">Loading...</div></div>
	<section class="content" style="min-height:35px;display:none" id="messageWindow">
				<div class="row">
					<div class="col-md-12">
						<div class="alert alert-success alert-dismissable" id="successMessageDiv"
							style="display: none;  height: 34px;white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Success! </strong> {{successMessage}} 
							<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
						</div>
						<div class="alert alert-danger alert-dismissable"
							id="errorMessageDiv"
							style="display: none; height: 34px; white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Error! </strong> {{errorMessage}} 
							<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
						</div>
					</div>
				</div>
			</section>
	<section class="content">
		<div class="row">
		<div class="col-md-12">
			<div class="box" >
				<div class="box-header with-border">
					<h3 class="box-title"><a href="${contextPath}/sp/incident/details/" title="View All Incidents"><i class="fa fa-th-list" aria-hidden="true"></i></a> 
					Update Incident - {{ticketData.ticketNumber}}</h3>
					<div class="box-tools pull-right" style="margin-top: 0px;">
						<input type="hidden" id="mode" value="${mode}">
						  <a  href ng-click="openChatBox();" >
        	<img src="${contextPath}/resources/img/chatbot.png" style="width: 12%;" 
        	class="pull-right" data-toggle="tooltip" data-original-title="Incident work notes">
        </a>
					</div>
				</div>
				<div class="box-body">
				<div class="row">
				<div class="col-md-12">
				<div class="nav-tabs-custom">
		            <ul class="nav nav-tabs">
		              <li class="active"><a href="#primaryinfo" data-toggle="tab">Primary Details</a></li>		              
		              <li><a href="#linkedticket" data-toggle="tab" >External Ticket of Service provider</a></li>
		              <li><a href="#escalate" data-toggle="tab" onclick="initializeEscalateTicket()">Escalation</a></li>              
		              <li><a href="#tickethistory" data-toggle="tab" onclick="getTicketHistory()">Ticket History</a></li>
		            </ul>
	             <div class="tab-content">
	              <div class="active tab-pane" id="primaryinfo">
	              <div class="row">
				      <div class="col-lg-3 col-xs-6">
			          <div class="small-box bg-yellow">
			            <div class="inner">
			              <p>Raised BY</p>
			            </div>
			            <a href="#" class="small-box-footer">
			             {{ticketData.raisedBy}}
			            </a>
			          </div>
			        </div>
			         <div class="col-lg-3 col-xs-6">
			          <div class="small-box bg-yellow">
			            <div class="inner">
			              <p>Assigned To</p>
			            </div>
			            <a href="#" class="small-box-footer">
			             {{ticketData.assignedSP}}
			            </a>
			          </div>
			        </div>
			        <div class="col-lg-3 col-xs-6">
			          <div class="small-box bg-yellow">
			            <div class="inner">
			              <p>Raised On</p>
			            </div>
			            <a href="#" class="small-box-footer">
			            {{ticketData.raisedOn}}
			            </a>
			          </div>
			        </div>
			         <div class="col-lg-3 col-xs-6">
			          <div class="small-box bg-yellow">
			            <div class="inner">
			              <p>Asset</p>
			            </div>
			            <a href="#" class="small-box-footer">
			            {{ticketData.assetName}}
			            </a>
			          </div>
			        </div>
			        </div>
			        <div class="row">
			        <div class="col-md-12">
			         SLA {{ticketData.slaPercent}} %
					 <dl class="dl-horizontal">
			             <div class="progress">
			                 <div class="progress-bar" 
			                 ng-class="{'progress-bar-danger': ticketData.slaPercent >=100, 'progress-bar-warning': ticketData.slaPercent>75 && ticketData.slaPercent<100, 'progress-bar-info': ticketData.slaPercent>0 && ticketData.slaPercent<75}" 
			                 role="progressbar" ng-style="{width: ticketData.width+'%'}"></div>
			             </div>                                           
			    	 </dl>
			    	 </div>
	        	</div>
	        	<div class="row">
	        	 <div class="col-md-12">
                      <form name="updateticketform" ng-submit="updateTicket()" >
						 <div class="row">
						 <div class="col-md-12 reqDiv required">	
							 <label class="control-label" for="title">Ticket Title</label>
							 <input type="text" class="form-control" placeholder="Enter ticket title" name="title"
					                   ng-model="ticketData.ticketTitle"  required readonly >
								  	
							 </div>
						 </div>
							<div class="row">							
							 <div class="col-md-12 reqDiv required">							 	
							 <label class="control-label" >Ticket Description</label>
								  <textarea class="form-control" style="width: 100%;
   				 height: 100px;" rows="3" placeholder="Enter ticket description" name="title" 
   				 ng-model="ticketData.description" required readonly></textarea>	
							 </div>
							
							</div>
								<div class="row">
							 <div class="col-xs-4 reqDiv required">
				                <label class="control-label">Site</label>
				                <input type="text" ng-model="ticketData.siteName" class="form-control"  required readonly>			                 
								<input type="hidden" ng-model="selectedSite.selected">
				                </div>
				                
				                <div class="col-xs-4 reqDiv required">
				                <label class="control-label">Ticket Category</label>
				                 <input type="text"  class="form-control"  
					                  name="priority" ng-model="ticketData.categoryName"  required readonly> 
				                
	               			 </div>
	               			 
								</div>
								
								<div class="row">
								
					                
									<div class="col-xs-4 reqDiv required">
					                <label class="control-label" >Priority</label>
					              <%--   <select name="prioritySelect" id="prioritySelect" class="form-control" required disabled="disabled"
								onchange="getSelectedPriority('prioritySelect')">
									
								</select> --%>
					           <input type="text"  class="form-control"  
					                  name="priority" ng-model="ticketData.priorityDescription"  required readonly> 
					                </div>
					                <div class="col-xs-4 reqDiv required">
					                <label class="control-label">SLA</label>
					                <div class="form-group">
                <div class='input-group date' id='datetimepicker2'>
                    <input type='text' class="form-control" ng-model="ticketData.sla" id="sla" disabled="disabled"/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
					              
					               </div>
					               <div class="col-xs-4 reqDiv required">
					                <label class="control-label" >Status</label>
					                  <select name="statusSelect" id="statusSelect" class="form-control" required
								onchange="ticketStatusChange('statusSelect')">
									
								</select>
								<input type="hidden" ng-model="selectedTicketStatus.selected">
					                </div>
					                
								</div>
																
								<div class="row">
								<div class="col-xs-4 reqDiv required">
                <label class="control-label">Issue Start Time</label>
                <div class="form-group">
                 <input type='text' class="form-control" ng-model="ticketData.ticketStartTime" disabled="disabled"/>
            </div>
				
            </div>
           <div class="col-xs-4">
            		<label class="control-label">Attachments</label>
					      <label class="form-control">Please click <a href data-toggle="modal" ng-click="openFileAttachModal()"><b>Here</b></a> to View/Update File.</label>
					  </div>
					                
		                <div class="col-xs-4">
		             	  <label class="control-label">Files Attached</label><br>
		                 <a href class="btn btn-warning" style="width:100%">Count <span class="badge">{{incidentImages.length}}</span> 
		                 Size <span class="badge">{{totalIncidentImageSize}} KB</span></a>
		                </div>

							</div>
														
							<div class="row" id="ticketCloseDiv">
							<div class="col-xs-4 reqDiv required">
					                <label class="control-label" >Has issue been fixed permanently ?</label>
					                 <select name="closeCodeSelect" id="closeCodeSelect" class="form-control" >
									</select> 
									<%-- <input type="text" id="closeCode" ng-model="ticketData.codeClosed" class="form-control"> --%>
								<input type="hidden" ng-model="selectedCloseCode.selected">
					                </div>
					        <div class="col-xs-4 reqDiv required">
					                <label class="control-label">Restoration confirmed by</label>
					                  <input type="text" class="form-control" 
					                  name="closedBy" id="closedBy" ng-model="ticketData.closedBy"  disabled="disabled" >
					                </div>
							<div class="col-md-4 reqDiv required">
							  	
							 <label class="control-label" >Restoration notes</label>
								  <textarea class="form-control" style="width: 100%;
   				 height: 70px;" rows="3" placeholder="Enter ticket close note" name="closeNote" id="closeNote" 
   				 ng-model="ticketData.closeNote"></textarea>	
							 </div>
							</div>
							<div class="row" >
							
							<div class="col-md-12">
							<div class="pull-right">
							<button type="submit" id="btnSavePrimary" class="btn btn-success " >Save changes</button>
							<!-- <button type="reset" id="reseCreateTicketForm" class="btn btn-success ">RESET</button> -->
							</div>
							 </div>
							</div>
							</form>
							</div>
							</div>	
				  </div>
	              <div class="tab-pane" id="escalate">
						<table id="example2"
							class="table table-hover table-condensed">
							<thead>
								<tr>
									<th></th>
									<th><b>Escalation Level</b></th>
									<th><b>Name</b></th>
									<th><b>Email</b></th>
									<th><b>Status</b></th>

								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="escalation in escalationLevelDetails">
									<td></td>
									<td><input type="hidden" class="form-control"
										ng-model="escalation.escId"> <label
										class="control-label">{{escalation.escLevelDesc}}</label>
									</td>
									<td><label class="control-label">{{escalation.escTo}}</label>

									</td>

									<td><label class="control-label">{{escalation.escEmail}}</label>

									</td>

									<td><span class="label label-danger">{{escalation.escStatus}}</span>
									</td>

								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td>
										
									</td>
								</tr>

							</tbody>
						</table>
					</div>
					<div class="tab-pane table-responsive" id="linkedticket">
					<div class="form-group">
						<div class="col-sm-2">
							<label for="inputSkills" class="control-label">Linked
								Ticket</label>
						</div>

						<div class="col-sm-8">
							<input type="text" class="form-control" id="linkedTicket" maxlength="20"
								ng-model="linkedTicket.ticketNumber">
						</div>
						<div class="col-sm-2">
							<a  class="btn btn-success" ng-click="LinkNewTicket()"><i class="fa fa-link" aria-hidden="true"></i>Raise and save ticket</a>
						</div>
					</div>
					<!-- <div class="row""> -->							
							<label style="color:red">if you don't have linked ticket Number please enter 'NA' .</label>							
						<!-- </div> -->
					
					</br>

					<div class="form-group" ng-if="ticketData.linkedTickets.length>0">
						<div class="box box-success">
							<div class="box-header with-border">
								<h3 class="box-title">Linked ticket details</h3>
							</div>
							<div class="box-body">
								<table id="example2"
									class="table table-hover table-condensed">
									<thead>
										<tr>
											<th style="width: 5px;"></th>
											<th><b>Linked Ticket Number</b></th>
											<th><b>Status</b></th>
										</tr>
									</thead>
									<tbody>
										<tr ng-repeat="linkedTkt in ticketData.linkedTickets">

											<td><input type="checkbox"
												id="chkLinkedTicket{{$index}}"
												ng-model="linkedTkt.selected"
												ng-change="getSelectedLinkedTicket(linkedTkt)" /></td>
											<td>
												<label class="control-label">{{linkedTkt.spLinkedTicket}}</label>
											</td>

											<td><span ng-class="{{linkedTkt.closedFlag == 'CLOSED'}} ? 'label label-danger' : 'label label-success'">{{linkedTkt.closedFlag}}</span>
											</td>
											<td>
												<a type="button"
													class="btn btn-info pull-right"	ng-click="unlinkTicketConfirmation($index,linkedTkt)" confirm="Are you sure?">
													<i class="fa fa-chain-broken" aria-hidden="true"></i> UNLINK
												</a>
											</td>
										</tr>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td>
												<button type="button" id="closedBtn"	class="btn btn-success pull-right"	ng-click="closeLinkedTicketConfirmation()">Close and save ticket</button>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					</div>
						<div class="tab-pane table-responsive" id="tickethistory">
							<div class="box">
								<div class="box-header with-border">
									<h3 class="box-title">Ticket History</h3>
								</div>
								<div class="box-body">

									<div class="table-responsive">
										<div class="row">
											<div class="col-md-12">
										<ul class="timeline">
											<li class="time-label" ng-if="ticketHistoryDetail.history.length > 0">
											<span class="bg-red" ng-show="ticketHistoryDetail.ticketCloseDate != null ">
													{{ticketHistoryDetail.ticketCloseDate}} </span>
													 <span class="bg-aqua" ng-show="ticketHistoryDetail.ticketCloseDate == null ">
                   									 {{CurrentDate}}
                  								</span>
													</li>
											<li class="time-label" ng-if="ticketHistoryDetail.history.length == 0"><span class="bg-aqua">
													Ticket is not yet updated since the creation date. </span></li>		
											<li
												ng-repeat="tktHistory in ticketHistoryDetail.history">
												<i class="fa fa-user bg-aqua"></i>

												<div class="timeline-item">
													<span class="time"><i class="fa fa-clock-o"></i>
														{{tktHistory.date}}</span>

													<h3 class="timeline-header">
														{{tktHistory.description}} by {{tktHistory.name}}
													</h3>

												</div>
											</li>

											<li class="time-label"><span class="bg-green">
													Created On : {{ticketData.raisedOn}} </span></li>

											<li><i class="fa fa-clock-o bg-gray"></i></li>
										</ul>
									</div>
								</div>
							</div>
						</div>

					</div>
				</div>
	              </div>
	            </div>
				</div>
				
				 <div class="col-md-6" id="chatWindow" style="display:none">
          <div class="box" >
          	<div style="width:30%;position:fixed;bottom:13px;right:0px;margin:0;background-color:#283e34">
            <div class="box-header with-border">
              <h3 class="box-title" style="color:#fff">Work Notes</h3>
             	 <div class="box-tools pull-right">
                    <a href="javascript:void(0);"  class="badge bg-yellow" data-toggle="tooltip" ng-click="closeWindow();">
                   		 <i class="fa fa-minus" aria-hidden="true"></i>
	                </a>
                </div>
            </div>
            <div class="box-body">
              <div class="row">
	   			 <div class="col-md-12">			 
                  <div class="direct-chat-messages" style="height: 430px;" id="messageWindow">
                  <div ng-repeat="ticket in ticketComments" id="messagebox">
                    <div class="direct-chat-msg"  >
                      <div class="direct-chat-info clearfix">
                        <span class="direct-chat-name pull-left" style="color:#fff">{{ticket.createdBy}}</span>
                        <span class="direct-chat-timestamp pull-right" style="color:#fff">{{ticket.createdDate}}</span>
                      </div>
                      <img class="direct-chat-img" src="${contextPath}/resources/img/swadhin.jpg" alt="message user image">
                     	 
                      <div class="direct-chat-text" id="audioMessage">
                       {{ticket.comment}}
                      </div>
                      
                    </div>

					</div>
                  </div>
                </div>
              </div>
            </div>
            <div class="box-footer">
              <div class="row">
                <div class="col-sm-12">
                      <input type="text" name="ticketMessage" id="ticketMessage" ng-model="ticketComment.comment"
                      placeholder="Type Message ..." class="form-control" ng-enter="addNewComment()">
                </div>
              </div>
            </div>
            </div>
          </div>
          
        </div>
       
			
			<!-- 		<div class="col-md-3" style="background-color:#E2E0DF">
					<div class="box box-warning direct-chat direct-chat-warning">
                <div class="box-header with-border">
                  <h3 class="box-title">Work Notes</h3>                  
                </div>
                <div class="box-body">
                  <div class="direct-chat-messages" style="height:475px" >
                    Message. Default to the left
                    <div class="direct-chat-msg" ng-repeat="ticket in ticketComments">
                      <div class="direct-chat-info clearfix">
                        <span class="direct-chat-name pull-left">{{ticket.createdBy}}</span>
                        <span class="direct-chat-timestamp pull-right">{{ticket.createdDate}}</span>
                      </div>
                                          
                      <div class="direct-chat-text">
                        {{ticket.comment}}
                      </div>
                     
                    </div>

                  </div>
                  
                </div>
                <div class="box-footer">
                  
                    <div class="input-group">
                      <input type="text" name="ticketMessage" id="ticketMessage" ng-model="ticketComment.comment" placeholder="Enter Comment ..." class="form-control">
                          <span class="input-group-btn">
                            <button type="button" class="btn btn-success btn-flat" ng-click="addNewComment()">Post</button>
                          </span>
                    </div>
                  
                </div>
              </div>
					</div> -->
				</div>
					
				<div class="modal fade" id="confirmUnlink" role="dialog" aria-labelledby="confirmDeleteLabel" aria-hidden="true" data-keyboard="false" data-backdrop="static"> 
  <div class="modal-dialog"> 
    <div class="modal-content"> 
      <div class="modal-header"> 
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>  
        <h4 class="modal-title">Unlink ticket</h4> 
      </div> 
      <div class="modal-body"> 
        <p>Are you sure, you want to unlink this ticket ?</p>  
      </div> 
    <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">No</button>  
        <button type="button" class="btn btn-danger" id="confirm" ng-click="unlinkTicket()">Yes</button>
      </div>  
    </div>  
  </div>  
</div>

	<div class="modal fade" id="confirmStatusChange" role="dialog" aria-labelledby="confirmDeleteLabel" aria-hidden="true" data-keyboard="false" data-backdrop="static"> 
  <div class="modal-dialog"> 
    <div class="modal-content"> 
      <div class="modal-header label-warning" > 
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>  
        <h4 class="modal-title">Status warning</h4> 
      </div> 
      <div class="modal-body"> 
        <p>Please select status as 'Logged' or 'Service Provider WIP' or 'Pending Review' .</p>  
      </div> 
    <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">OK</button>  
        <!-- <button type="button" class="btn btn-danger" id="confirm" ng-click="closeLinkedTicket()">Yes</button> -->
      </div>  
    </div>  
  </div>  
</div> 

<div class="modal fade" id="confirmLoggedStatusChange" role="dialog" aria-labelledby="confirmDeleteLabel" aria-hidden="true" data-keyboard="false" data-backdrop="static"> 
  <div class="modal-dialog"> 
    <div class="modal-content"> 
      <div class="modal-header label-warning" > 
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>  
        <h4 class="modal-title">Status warning</h4> 
      </div> 
      <div class="modal-body"> 
        <p>Please make sure that you have entered a reference ticket number in 'External Ticket of Service Provider'  Tab .</p>  
      </div> 
    <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">OK</button>  
        <!-- <button type="button" class="btn btn-danger" id="confirm" ng-click="closeLinkedTicket()">Yes</button> -->
      </div>  
    </div>  
  </div>  
</div>

<div class="modal fade" id="confirmLoggedUnlinkChange" role="dialog" aria-labelledby="confirmDeleteLabel" aria-hidden="true" data-keyboard="false" data-backdrop="static"> 
  <div class="modal-dialog"> 
    <div class="modal-content"> 
      <div class="modal-header label-warning" > 
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>  
        <h4 class="modal-title">Unlink warning</h4> 
      </div> 
      <div class="modal-body"> 
        <p>There should be minimum one reference ticket if status of Parent ticket is 'Logged'.</p>  
      </div> 
    <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">OK</button>  
        <!-- <button type="button" class="btn btn-danger" id="confirm" ng-click="closeLinkedTicket()">Yes</button> -->
      </div>  
    </div>  
  </div>  
</div> 
				
				<div class="modal" id="fileAttachModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
      <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="upload-avatar-title">View / Upload files</h4>
          <div class="alert alert-danger alert-dismissable" id="incidentImageModalMessageDiv"
				style="display: none;  height: 34px;white-space: nowrap;">
				<strong>Error! </strong> {{incidentImageModalErrorMessage}} 
				<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
			</div>
        </div>
        <div class="modal-body">
        <div class="nav-tabs-custom">
        	<ul class="nav nav-tabs" style="background-color: rgba(60, 141, 188, 0.34);">
            
							<li class="active">
				       		 <a  href="#attachmentViewTab" data-toggle="tab" aria-expanded="true" id="siteViewLink"><b>View Attachment</b></a>
							</li>
							<li><a href="#addAttachmentTab" data-toggle="tab" aria-expanded="true" id="siteContactLink"><b>Add New Files</b></a>
							</li>
							
            </ul>
        	<div class="tab-content">
               <div class="tab-pane active" id="attachmentViewTab">
                	<div class="box-body">
								
              		<div class="table-responsive">
                <table class="table no-margin">
                  <thead>
                  <tr>
                    <th>File Name</th>
                    <th>Download</th>
                    <th></th>
                    
                  </tr>
                  </thead>
                 <tbody ng-repeat="file in ticketData.files">
					<tr>
						<td>{{file.fileName}}</td>
						<td><a href="${contextPath}/selected/file/download?keyname={{file.filePath}}" download><i class="fa fa-cloud-download fa-2x" aria-hidden="true"></i></a></td>
						<td>  </td>
					</tr>
				</tbody>
                </table>
              </div>
              
            </div>
                
                </div>
                
                <div class="tab-pane" id="addAttachmentTab">
                	<p>Please choose a file to upload. image/*,.doc, .docx,.pdf only.</p>
          <form role="form">
          <div class="controls">                       
              <div class="entry input-group col-xs-12">
                <input type="button" class="btn btn-success addnew" style="margin-right: 5px;" onclick="" ng-click="addNewImage()" value="Add New">
                <div  style="overflow-y:auto;height:250px">
                <table id="example2" class="table  table-hover table-condensed">
                 <tbody>
                <tr ng-repeat="incidentImage in incidentImageList">
                  <td>
                  <input type="file" id="incidentImage{{$index}}" class="form-control" 
                  name="incidentImage[{{$index}}]" accept="image/*,.doc, .docx,.pdf" 
                  onchange="angular.element(this).scope().getIndexedName(this, event)" style="width:80%">
                   <a class="btn btn-danger" href  ng-click="removeImage($index)" >
                               <i class="fa fa-trash-o" aria-hidden="true" style="font-size: 1.4em;"></i>
                  </a>
                  </td>
                </tr>
                <tr>
                	<td colspan="2">Total Size : {{totalIncidentImageSize}} KB</td>
                </tr>
                </tbody>
                </table>
                </div>
              </div>
              
           
          </div>            
            </br>
            <button type="button" class="btn btn-default" id="btnUploadCancel" data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-success" ng-click="uploadFiles()" value="Upload">Upload</button>
          </form>
                </div>
                
            </div>
        </div>
          
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
<!-- </div> -->

</div>
				</div>
			</div>
			</div>	
		</section>	
							
					
	</div>
	</div>

