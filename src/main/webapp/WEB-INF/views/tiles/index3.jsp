<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<html ng-app="chrisApp">
<head>
<title>Home</title>
  <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap/css/bootstrap.min.css"></c:url>' />
 <link rel="stylesheet"	href='<c:url value="/resources/theme1/font-awesome-4.7.0/css/font-awesome.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/small-business.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/dist/css/AdminLTE.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery-2.1.4.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/angular.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/popper.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jstorage.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/app-main.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/controller.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/register-controller.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js"></c:url>'></script>
<style>
@media (min-width: 1200px){
	.container {
	    max-width: 1300px;
	}
	}
	.bg-dark {
    background-color: #eee !important;
}
</style>
</head>
<body>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
   <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
      <div class="container">
      <a class="logo-left " href="#/">
                  <img src="${contextPath}/resources/img/logo.png" style="width: 15%;">
                  <img src="${contextPath}/resources/img/sigma.png" style="width: 15%;">
                  </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" 
        data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
          <ul class="navbar-nav ml-auto">
            <li class="nav-item active">
              <a href >Get Started<span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
              <a href >Support</a>
            </li>
            <li class="nav-item">
             <a href data-toggle="modal" data-target="#loginModal">Login</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <!-- Page Content -->
    <div class="container">

      <!-- Heading Row -->
      <div class="row my-4">
        <div class="col-lg-8">
          <img class="img-fluid rounded" src="${contextPath}/resources/img/mockup.jpg" alt="">
        </div>
        <!-- /.col-lg-8 -->
        <div class="col-lg-4">
    		<div class="modal-content">
			<form name="registerForm" ng-submit="saveCustomer(registerForm)" novalidate>
			 <div class="modal-body" style="background-color: #eee">
			 	<div class="row">
			 	<div class="col-md-12">
			 	<h3 class="modal-title">Get started - Request for free demo</h3>
			 	<div class="box">
					<div class="box-body">
						 <div class="col-lg-12">
	         		 <div class="row">
					<div class="col-sm-12">
						<div class="row">
							<div class="col-sm-12 form-group">
								<label><i class="fa fa-envelope" aria-hidden="true"></i> Email</label>
								<span ng-show="registerForm.email.$invalid && !registerForm.email.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter a valid email id
					</span>
								<input type="email" ng-model="customer.email" 
								name="email" placeholder="Enter email here.." class="form-control" required>
							</div>
						<div class=" col-sm-12 form-group">
							<label><i class="fa fa-file-text-o" aria-hidden="true"></i> Message</label>
							<span ng-show="registerForm.mailcontent.$invalid && !registerForm.mailcontent.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter your message
						</span>
							<textarea placeholder="Enter your message here.." rows="3" 
							ng-model="customer.message" name="mailcontent" class="form-control" required></textarea>
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
				    <button type="submit" class="btn btn-lg btn-info pull-left" ng-disabled="registerForm.$invalid">Submit</button>	
					<button type="button" class="btn btn-lg btn-default pull-left"	id="registerCloseBtn" data-dismiss="modal">Close</button>
			</div>
									</form>
									
									
								</div>
    	
        </div>
        <!-- /.col-md-4 -->
      </div>
      <!-- /.row -->
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
						 <div class="col-lg-12">
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
							<label><i class="fa fa-unlock-alt" aria-hidden="true"></i> Password</label>
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

      <!-- Content Row -->
      <div class="row">
        <div class="col-md-4 mb-4">
          <div class="card h-100">
            <div class="card-body">
              <h2 class="card-title">Card One</h2>
              <p class="card-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Rem magni quas ex numquam, maxime minus quam molestias corporis quod, ea minima accusamus.</p>
            </div>
            <div class="card-footer">
              <a href="#" class="btn btn-primary">More Info</a>
            </div>
          </div>
        </div>
        <!-- /.col-md-4 -->
        <div class="col-md-4 mb-4">
          <div class="card h-100">
            <div class="card-body">
              <h2 class="card-title">Card Two</h2>
              <p class="card-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quod tenetur ex natus at dolorem enim! Nesciunt pariatur voluptatem sunt quam eaque, vel, non in id dolore voluptates quos eligendi labore.</p>
            </div>
            <div class="card-footer">
              <a href="#" class="btn btn-primary">More Info</a>
            </div>
          </div>
        </div>
        <!-- /.col-md-4 -->
        <div class="col-md-4 mb-4">
          <div class="card h-100">
            <div class="card-body">
              <h2 class="card-title">Card Three</h2>
              <p class="card-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Rem magni quas ex numquam, maxime minus quam molestias corporis quod, ea minima accusamus.</p>
            </div>
            <div class="card-footer">
              <a href="#" class="btn btn-primary">More Info</a>
            </div>
          </div>
        </div>
        <!-- /.col-md-4 -->

      </div>
      <!-- /.row -->

    </div>
    <!-- /.container -->

    <!-- Footer -->
    <footer class="py-5 bg-dark">
      <div class="container">
        <p class="m-0 text-center text-white">Copyright &copy; Your Website 2017</p>
      </div>
      <!-- /.container -->
    </footer>


  </body>

<html>
