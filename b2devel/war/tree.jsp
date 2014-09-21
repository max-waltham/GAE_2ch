<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ page import="jp.blogspot.simple_asta.b2.server.B2"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>b3devel</title>
<script src="js_css/main.js"></script>
<link rel="stylesheet" href="js_css/main.css" />
<link rel="stylesheet"
	href="http://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.css" />
<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script
	src="http://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.js"></script>
</head>
<body onload="onload()">
	<div id="tree" data-role="collapsibleset" data-theme="a"
		data-inset="false"><%
			String[][][] cl_u = B2.getCategoryList();
			for (int c = 0, l1 = cl_u.length; c < l1; c++) {
				String[][] catList = cl_u[c];%>
		<div data-role="collapsible">
			<h2><%=catList[0][0]%></h2>
			<ul data-role="listview"><%for (int t = 1, l2 = catList.length; t < l2; t++) {
							String[] tName = catList[t];%>
				<li><a href="./thread.jsp?c=<%= c%>&t=<%= t%>"><%= tName[0]%></a></li><%}%>
			</ul>
		</div><%}%>
	</div>
</body>
</html>