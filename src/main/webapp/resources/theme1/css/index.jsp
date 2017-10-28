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
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/style.css"></c:url>' />

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
                  <button class="menu-icon"  id="open-button">
                    <i class="mdi-navigation-menu"></i>
                  </button>             
                </div>
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
          	 <a href="${contextPath}/login" class="btn btn-lg btn-primary wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="400ms">LOGIN</a>
	          <a href="${contextPath}/register" class="btn btn-lg btn-primary wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="400ms">REGISTER</a>
          </div>
        </div>  
        </div> 
    </header>
</div>

</body>
<html>
