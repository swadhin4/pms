<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html >
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='0'>
<meta http-equiv='pragma' content='no-cache'>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap.min.css"></c:url>' />
<link rel="stylesheet" 	href='<c:url value="/resources/css/ionicons/css/ionicons.min.css"></c:url>'/>

<link rel="stylesheet"	href='<c:url value="/resources/css/jquery-jvectormap-1.2.2.css"></c:url>' />

<link rel="stylesheet"	href='<c:url value="/resources/theme1/font-awesome-4.7.0/css/font-awesome.min.css"></c:url>' />
<%-- <link rel="stylesheet"	href='<c:url value="/resources/theme1/css/ripples.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/main.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/responsive.css"></c:url>' /> --%>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/animate.min.css"></c:url>' />

<link rel="stylesheet" href='<c:url value="/resources/theme1/css/jquery-editable-select.min.css"></c:url>' />
<link rel="stylesheet" href='<c:url value="/resources/theme1/css/datepicker3.css"></c:url>' />
<%-- <link rel="stylesheet" href='<c:url value="/resources/css/bootstrap-datetimepicker.min.css"></c:url>' /> --%>


<link rel="stylesheet"	href='<c:url value="/resources/dist/css/pms-admintLTE.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/style.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/dist/css/skins/skin-black-light.min.css"></c:url>' />

 <script type="text/javascript" src='<c:url value="/resources/theme1/chart/jsapi.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery-2.1.4.min.js"></c:url>'></script>
<script type="text/javascript"  src='<c:url value="/resources/theme1/js/bootstrap-datepicker.js"></c:url>'></script>

<script type="text/javascript"  src='<c:url value="/resources/theme1/js/jquery-editable-select.js"></c:url>'></script>

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/tether.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/dist/js/adminlte.min.js"></c:url>'></script>

<%-- <script type="text/javascript" 	src='<c:url value="/resources/theme1/js/transition.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/dist/js/app.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/ripples.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/material.min.js"></c:url>'></script>

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.mmenu.min.all.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/count-to.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.inview.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/classie.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.nav.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/smooth-on-scroll.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/smooth-scroll.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/main.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/mdb.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/wow.js"></c:url>'></script>
 --%>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/angular.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jstorage.js"></c:url>'></script>
<script type="text/javascript"  src='<c:url value="/resources/theme1/js/angucomplete-alt.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/app-main.js"></c:url>'></script>
 <script type="text/javascript" src="<c:url value="/resources/theme1/js/moment.min.js"></c:url>"></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jstorage.js"></c:url>'></script>

<script type="text/javascript" src='<c:url value="/resources/js/sockjs.min.js"></c:url>'></script>
 <script type="text/javascript" src='<c:url value="/resources/js/stomp.min.js"></c:url>'></script>
 <script type="text/javascript" src="<c:url value="/resources/theme1/pms/websocket.js"></c:url>"></script>

<style>
	 .alert-success {
	
   			display:table;
    		background-color: rgb(122, 221, 0) !important;
    		border-color:#fff !important
    		
    }
    .alert-danger {
      	display:table;
   		 background-color: rgba(237, 73, 10, 0.92) !important;
   		 border-color:#fff !important
    		
    }
   
    .alert{
        padding: 7px;
            margin-bottom: 0px;
            border: 0px solid transparent !important;
    }
    .alert .close{
        color: #000 !important;
   		 opacity: 2.2 !important;
    }
   .alert-dismissable .close {
    position: relative;
    top: -2px;
    color: inherit;
      margin-right: 19px;
} 

.navbar-custom-menu .nav>li>a>.label {
   position: absolute;
    top: 7px;
    right: 4px;
    text-align: center;
    font-size: 14px;
    padding: 4px 5px;
    line-height: .9;
}

.content {
    min-height: 250px;
    padding: 0px;
    margin-right: auto;
    margin-left: auto;
    padding-left: 15px;
    padding-right: 15px;
}


.loader,
        .loader:after {
            border-radius: 50%;
            width: 10em;
            height: 10em;
        }
        .loader {            
            margin: 250px auto;
            font-size: 10px;
            position: relative;
            text-indent: -9999em;
            border-top: 1.1em solid rgba(255, 255, 255, 0.2);
            border-right: 1.1em solid rgba(255, 255, 255, 0.2);
            border-bottom: 1.1em solid rgba(255, 255, 255, 0.2);
            border-left: 1.1em solid #ffffff;
            -webkit-transform: translateZ(0);
            -ms-transform: translateZ(0);
            transform: translateZ(0);
            -webkit-animation: load8 1.1s infinite linear;
            animation: load8 1.1s infinite linear;
        }
        @-webkit-keyframes load8 {
            0% {
                -webkit-transform: rotate(0deg);
                transform: rotate(0deg);
            }
            100% {
                -webkit-transform: rotate(360deg);
                transform: rotate(360deg);
            }
        }
        @keyframes load8 {
            0% {
                -webkit-transform: rotate(0deg);
                transform: rotate(0deg);
            }
            100% {
                -webkit-transform: rotate(360deg);
                transform: rotate(360deg);
            }
        }
        #loadingDiv {
            position:absolute;;
            top: 52px;
   			 left: 0px;
            width:100%;
            height:100%;
            background-color:#000;
              opacity: 0.7;
   z-index: 9999;
   text-align: center;
        }

</style>
<script type="text/javascript">
/* $(window).on('load', function(){
  setTimeout(removeLoader, 2000); //wait for page load PLUS two seconds.
}); */
function removeLoader(){
    $( "#loadingDiv" ).fadeOut(500, function() {
      // fadeOut complete. Remove the loading div
     // $( "#loadingDiv" ).remove(); //makes page more lightweight 
  });  
}
</script>
</head>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
	 <header class="main-header">
	 <a href="../../index2.html" class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini"> <img src="${contextPath}/resources/img/logo.png" style="width: 100%;"></span>
     
      <span class="logo-lg"><img src="${contextPath}/resources/img/sigma.png" style="width: 100%;
   		 margin-top: 0px;"></span>
    </a>
    <nav class="navbar navbar-static-top">
	<%-- 	<div class="container-fluid">
			     	<div class="navbar-header">
                     <!--    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-backyard">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button> -->
                        <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </a>
                        
                        <div  class="navbar-brand">
                    <a id="menu-toggle" href="#" class="btn-menu toggle" style="font-size: 1.5em;">
                        <i class="fa fa-bars"></i>
                    </a>
    				<a class="navbar-brand site-name" href="#top">
    				<img src="${contextPath}/resources/img/sigma.png" style="width: 29%;
    margin-top: -3px;"></a>
                </div>
                    </div>
                    <div id="navbar-scroll" class="collapse  navbar-collapse navbar-backyard navbar-right">
                    <div class="navbar-custom-menu">
                <ul class="nav navbar-nav">
               <li class="dropdown notifications-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-bell-o fa-2x" style="    font-size: 1.5em;"></i>
              <span class="label label-danger" id="msgCountElmId">0</span>
            </a>
            <ul class="dropdown-menu" style="width: 500px;">
              <li class="header">You have <span id="msgCountElmId2">0</span> notifications</li>
              <li>
                <!-- inner menu: contains the actual data -->
                <ul class="menu" id="msgDropdown">
                </ul>
              </li>
              <li class="footer"><a href="#">View all</a></li>
            </ul>
          </li>
              <!-- User Account Menu -->
              <li class="dropdown user user-menu">
                <!-- Menu Toggle Button -->
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <!-- The user image in the navbar-->
                 <i class="fa fa-user" aria-hidden="true"></i>
                  <!-- hidden-xs hides the username on small devices so only the image appears. -->
                  <span class="hidden-xs">${user.firstName}  ${user.lastName}</span>
                </a>
                <ul class="dropdown-menu " style="margin-top: 9px;border:0px">
                 <li class="header" style="margin-left: 10px;">You have following Roles</li>
                  <ul class="menu">
                   <c:forEach items="${user.userRoles}" var="roles">
                  <li>
                    <a href="#">
                      <h4>
                       <i class="fa fa-users text-aqua"> ${roles.role.description}</i>
                      </h4>
                    </a>
                  </li>
                  </c:forEach>
                  </ul>
                               
                  <li class="user-footer">
                    <div class="pull-left">
                      <a href="${contextPath}/user/profile" class="btn btn-default btn-flat">Profile</a>
                    </div>
                    <div class="pull-right">
                    <sec:authorize access="isAuthenticated()">
                      <a href="${contextPath}/logout" class="btn btn-default btn-flat">Sign out</a>
                     </sec:authorize> 
                    </div>
                  </li>
                </ul>
              </li>
            </ul>
            </div>
                    </div>
		</div> --%>
		<a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </a>

      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <!-- Messages: style can be found in dropdown.less-->
          <!-- Notifications: style can be found in dropdown.less -->
          <li class="dropdown notifications-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-bell-o"></i>
              <span class="label label-warning" id="msgCountElmId">0</span>
            </a>
            <ul class="dropdown-menu">
              <li class="header">You have <span id="msgCountElmId2">0</span> notifications</li>
              <li>
                <!-- inner menu: contains the actual data -->
               <ul class="menu" id="msgDropdown">
                </ul>
              </li>
              <li class="footer"><a href="#">View all</a></li>
            </ul>
          </li>
          <!-- User Account: style can be found in dropdown.less -->
          <li class="dropdown user user-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <img src="${contextPath}/resources/img/swadhin.jpg" class="user-image" alt="User Image">
              <span class="hidden-xs">${user.firstName}  ${user.lastName}</span>
            </a>
            <ul class="dropdown-menu">
              <!-- User image -->
              <li class="user-header">
                <img src="${contextPath}/resources/img/swadhin.jpg" class="img-circle" alt="User Image">
                <p style="color: #000">
                  ${user.firstName}  ${user.lastName}
                  <small> ${user.company.companyName}</small>
                  <c:forEach items="${user.userRoles}" var="roles">
                  <small style="color: #000">${roles.role.description}</small>
                     </c:forEach>
                </p>
              </li>
              <!-- Menu Body -->
              <!-- Menu Footer-->
              <li class="user-footer">
                <div class="pull-left">
                  <a href="${contextPath}/user/profile" class="btn btn-default btn-flat">Profile</a>
                </div>
                <div class="pull-right">
                 <sec:authorize access="isAuthenticated()">
                  <a href="${contextPath}/logout" class="btn btn-default btn-flat">Sign out</a>
                  </sec:authorize>
                </div>
              </li>
            </ul>
          </li>
          <!-- Control Sidebar Toggle Button -->
          <li>
            <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
          </li>
        </ul>
      </div>
    </nav>
	</nav>
	</header>
    <!-- Sidebar -->
    <%-- <c:if test="${user.sysPassword eq 'NO'}">
    <div id="wrapper1">
    <div id="sidebar-wrapper" style="background-color:E1DDDD">
    
   <ul class="list-group">
  	<li class="list-group-item">
  		<a href="${contextPath}/appdashboard"><i class="fa fa-home" aria-hidden="true"></i> <span class="menulink">HOME</span> </a>
  	</li>
  	<li class="list-group-item">
  		<a href="${contextPath}/appdashboard"><i class="fa fa-home" aria-hidden="true"></i> <span class="menulink">PREFERENCES</span></a>
  	</li>
  	<li class="list-group-item">
	  <a  class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
	  <i class="fa fa-cog" arial="hidden"></i> <span class="menulink">MANAGE </span> </a>
	  <ul  role="menu" style="padding: 24px 12px;
    margin: -9px 0 0;
    font-size: 16px;">
	  	
	  	<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
	  	<li class="list-group-item"><a href="${contextPath}/user/details">
	  	<i class="fa fa-users" aria-hidden="true"></i> <span class="menulink">Users</span></a></li>
		</sec:authorize>
		<sec:authorize access="hasAnyRole('ROLE_SITE_STAFF','ROLE_SALES_MANAGER','ROLE_OPS_MANAGER')">
	    <li class="list-group-item"><a href="${contextPath}/site/details"><i class="fa fa-sitemap" aria-hidden="true"></i> <span class="menulink">Sites <span></a></li>
	    <li class="list-group-item"><a href="${contextPath}/asset/details"><i class="fa fa-sitemap" aria-hidden="true"></i> <span class="menulink">Assets</a> <span></li>
	    <li class="list-group-item"><a href="${contextPath}/incident/details"><i class="fa fa-ticket" aria-hidden="true"></i> <span class="menulink">Incidents<span></a></li>
	    <li class="list-group-item"><a href="${contextPath}/serviceprovider/details"><i class="fa fa-sitemap" aria-hidden="true"></i> <span class="menulink">Service Provider<span></a></li>
	    <li class="list-group-item"><a href="${contextPath}/reports/view"><i class="fa fa-sitemap" aria-hidden="true"></i> <span class="menulink">Reports<span></a></li>
	    </sec:authorize>
	    
	    <!-- <li class="divider"></li>
	    	<li><a href="#">Separated link</a></li> -->
	  </ul>      
      
  </li>
</ul>
  </div>
    </c:if> --%>
    <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- Sidebar user panel -->
      <div class="user-panel">
        <div class="pull-left image">
          <img src="${contextPath}/resources/img/swadhin.jpg" class="img-circle" alt="User Image">
        </div>
        <div class="pull-left info">
          <p >${user.firstName}  ${user.lastName}</p>
         
        </div>
      </div>
     <c:if test="${user.sysPassword eq 'NO'}">
      <!-- sidebar menu: : style can be found in sidebar.less -->
      <ul class="sidebar-menu" data-widget="tree">
        <li class="header">MAIN NAVIGATION</li>
       
        <li>
          <a href="${contextPath}/appdashboard">
            <i class="fa fa-dashboard"></i> <span>Dashboard</span>
          </a>
        </li>
        <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
    	 <li>
          <a href="${contextPath}/user/details">
            <i class="fa fa-users" aria-hidden="true"></i> <span>User</span>
          </a>
        </li>
        </sec:authorize>
        <sec:authorize access="hasAnyRole('ROLE_SITE_STAFF','ROLE_SALES_MANAGER','ROLE_OPS_MANAGER')">
        <li>
          <a href="${contextPath}/site/details"><i class="fa fa-sitemap" aria-hidden="true"></i>
             <span>Site</span>
          </a>
        </li>
        
           <li>
          <a href="${contextPath}/asset/details">
            <i class="fa fa-cubes" aria-hidden="true"></i> <span>Asset</span>
          </a>
        </li>
        
           <li>
          <a href="${contextPath}/serviceprovider/details">
            <i class="fa fa-building" aria-hidden="true"></i> <span>Service Provider</span>
          </a>
        </li>
        
          <li>
          <a href="${contextPath}/incident/details"><i class="fa fa-ticket" aria-hidden="true"></i> 
            <span>Incident</span>
          </a>
        </li>
        <li>
          <a href="${contextPath}/reports/view">
            <i class="fa fa-newspaper-o" aria-hidden="true"></i> <span>Reports</span>
          </a>
        </li>
       </sec:authorize>
      </ul>
      </c:if>
    </section>
  </aside>

