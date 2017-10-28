<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<html>
<script>
var webContextPath="${pageContext.request.contextPath}";
</script>
 <script type="text/javascript" src='<c:url value="/resources/theme1/chart/loader.js"></c:url>'></script>
 <script type="text/javascript" src='<c:url value="/resources/theme1/js/dashboard.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
 <style>
 </style>
<head>
<title>Home</title>
</head>
<div id="page-content-wrapper">
	<div class="page-content">
		<div class="container-fluid">
			<section class="content">
			<div class="box" >
							<div class="box-header with-border">
								<h3 class="box-title">Summarized view of Site and Service Providers Incidents</h3>
									<div class="box-tools pull-right" style="margin-top: 0px;">
										<sec:authorize access="hasAnyRole('ROLE_SITE_STAFF','ROLE_SALES_MANAGER', 'ROLE_OPS_MANAGER')">
										<!-- <a href class="btn btn-success pull-right" id="syncBtn">
											Synchronize Views <i class="fa fa-refresh fa-spin  fa-fw"></i>
											<span class="sr-only">Loading...</span>
									  	</a> -->
										</sec:authorize>	
									</div>
								</div>
				<div class="box-body">				
				<div class="row">
				<div class="col-md-12" id="dashboard_div">
					<div id="siteDiv" >
						<div class="col-md-6 col-sm-6" >
							  <!--Divs that will hold each control and chart-->
							  <div id="filter_site_pie_div" align="left"></div>
							  <div id="chart_site_pie_div" align="left"></div>
						</div>
						<div class="col-md-6 col-sm-6">
							  <!--Divs that will hold each control and chart-->
							  <div id="filter_site_bar_div" align="left"></div>
							  <div id="chart_site_bar_div" align="left" ></div>
						</div>
					</div>
				</div>
		</div>
		<div class="row">
			<div class="col-md-12" id="dashboard_SP_div">
				<div id="spDiv" >
					<div class="col-md-6 col-sm-6" >
						  <!--Divs that will hold each control and chart-->
						  <div id="filter_SP_pie_div" align="left"></div>
						  <div id="chart_SP_pie_div" align="left"></div>
					</div>
					<div class="col-md-6 col-sm-6">
						  <!--Divs that will hold each control and chart-->
						  <div id="filter_SP_bar_div" align="left"></div>
						  <div id="chart_SP_bar_div" align="left"></div>
					</div>
				</div>
			</div>
		</div>	
</div>
</div>

			</section>
				
		</div>
	</div>
</div>
</section>
</div>
</div>
</div>