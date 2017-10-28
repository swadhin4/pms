<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<html ng-app="chrisApp">
<head>
<title>Home</title>


<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/font-awesome-4.7.0/css/font-awesome.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/ripples.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/main.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/responsive.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/animate.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/dist/css/AdminLTE.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/style.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/dist/css/skins/skin-black-light.min.css"></c:url>' />

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery-2.1.4.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/angular.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/tether.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/ripples.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/material.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/wow.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.mmenu.min.all.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/count-to.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.inview.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/classie.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.nav.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/smooth-on-scroll.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/smooth-scroll.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/main.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/mdb.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/angular-route.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jstorage.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/app-main.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/controller.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js"></c:url>'></script>
</head>
<body>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<div class="content-wrap">
     <header class="hero-area" id="home">
      <div class="container" style="margin-top: 0px;">
          <div class="col-md-12">
            <div class="navbar navbar-inverse sticky-navigation navbar-fixed-top" role="navigation" data-spy="affix" data-offset-top="200">
              <div class="container">
                <div class="navbar-header">
                  <a class="logo-left " href="#/">
                  <img src="${contextPath}/resources/img/logo.png" style="width: 9%;">
                  <img src="${contextPath}/resources/img/sigma.png" style="width: 9%;">
                  </a>
                </div>
                <div class="navbar-right">
                       <ul class="nav navbar-nav">
                      
          
          <li class="dropdown notifications-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-bell-o"></i>
              <span class="label label-warning">5</span>
            </a>
            <ul class="dropdown-menu">
              <li class="header">You have 5 notifications</li>
              <li>
                <!-- inner menu: contains the actual data -->
                <ul class="menu">
                  <li>
                    <a href="#">
                      <i class="fa fa-users text-aqua"></i> Notification 1
                    </a>
                  </li>
                  <li>
                    <a href="#">
                      <i class="fa fa-warning text-yellow"></i> Notification 2
                    </a>
                  </li>
                  <li>
                    <a href="#">
                      <i class="fa fa-users text-red"></i> Notification 3
                    </a>
                  </li>
                  <li>
                    <a href="#">
                      <i class="fa fa-shopping-cart text-green"></i> Notification 4
                    </a>
                  </li>
                  <li>
                    <a href="#">
                      <i class="fa fa-user text-red"></i> Notification 5
                    </a>
                  </li>
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
        </div>   
             
        <div class="contents text-left">
          <h1 class="wow fadeInRight" data-wow-duration="1000ms" data-wow-delay="300ms" style="    margin-left: 11px;">Problem Management System</h1>
           <div class="col-md-6">
          <p class="wow fadeInRight" data-wow-duration="1000ms" data-wow-delay="400ms" style="text-align: justify">
          	Problem Management helps to identify the cause of an error in the IT infrastructure that is usually 
          	reported as occurrences of related incidents. <br>
          	
          	Resolving a problem means fixing the error that will stop these incidents from occurring in the future. 
          	</p>
          	<p class="wow fadeInLeft" data-wow-duration="1030ms" data-wow-delay="600ms" style="text-align: justify">
          	While Incident Management deals with fighting symptoms to incidents, Problem Management seeks to 
          	remove the causes of incidents permanently from the IT infrastructure.
          	<br>
          	 Problem resolution and elimination of root cause often calls for applying a change to the configuration 
          	 item in the existing IT environment
          	</p>
          	</div>
          	 <div class="col-md-12">
          	 <a href data-toggle="modal" data-target=".loginModal" class="btn btn-lg btn-primary wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="400ms">LOGIN</a>
	          <a href="${contextPath}/register" class="btn btn-lg btn-primary wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="400ms">REGISTER</a>
          </div>
        </div>  
        </div> 
    </header>
    
    		
						 <div class="modal fade loginModal" id="loginModal">
		<div class="modal-dialog" style="width: 50%;">
			<div class="modal-content">
			<form name="loginForm"  method="post" action="${contextPath}/j_spring_security_check">
				<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">	<span aria-hidden="true">&times;</span>
			</button>
			<h1 class="modal-title">Sign in</h1>
			</div>
			 <div class="modal-body" style="background-color: #eee">
			 	<div class="row">
			 	<div class="col-md-12">
			 	<div class="box">
					<div class="box-body">
						 <div class="col-lg-12 well">
	         		 <div class="row">
					<div class="col-sm-12">
						<div class="row">
							<div class="col-sm-12 form-group">
								<label><i class="fa fa-envelope" aria-hidden="true"></i> Email</label>
								<span ng-show="loginForm.j_username.$invalid && !loginForm.j_username.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter a valid email id
					</span>
								<input type="email" ng-model="user.email"  name="j_username" 
								placeholder="Enter email here.." class="form-control" required>
							</div>
						<div class="col-sm-12 form-group">
							<label><i class="fa fa-envelope" aria-hidden="true"></i> Password</label>
								<span ng-show="loginForm.j_password.$invalid && !loginForm.j_password.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter the password
					</span>
								<input type="password" ng-model="user.password" name="j_password" placeholder="Enter password.." class="form-control" required>
						</div>	
						</div>
					
					</div>
				
				</div>
		 </div>
					</div>
						</div>
						</div>
						</div>
					</div>
				<div class="modal-footer">
				    <button type="submit" class="btn btn-lg btn-info pull-left" ng-disabled="loginForm.$invalid">Sign in</button>	
					<button type="button" class="btn btn-lg btn-default pull-left"	id="loginCloseBtn" data-dismiss="modal">Close</button>
					<!-- <button type="submit" class="btn btn-primary" ng-disabled="loginForm.$invalid">LOGIN</button>
					<button type="reset" id="resetServiceAssetForm" class="btn btn-primary">RESET</button> -->
			</div>
									</form>
									
									
								</div>
							</div>

						</div>
</div>

</body>
<html>
