<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<body class="skin-blue sidebar-mini">
	<div class="wrapper" style="height: 90%; background-color:transparent;
    position: relative;
    overflow-x: hidden;
    overflow-y: auto;">
			<tiles:insertAttribute name="header" />
			<tiles:insertAttribute name="body" />
			<tiles:insertAttribute name="footer" />
	</div>

</body>
