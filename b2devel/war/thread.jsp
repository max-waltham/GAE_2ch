<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ page import="jp.blogspot.simple_asta.b2.server.B2"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>b3devel</title>
<script src="js_css/main.js"></script>
<link rel="stylesheet" href="js_css/main.css" />
<link rel="stylesheet"
	href="http://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.css" />
<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script
	src="http://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.js"></script>
</head>
<body>
	<%
		String c = request.getParameter("c");
		String t = request.getParameter("t");
		Integer cNum = Integer.parseInt(c);
		Integer tNum = Integer.parseInt(t);
		String[][] threadList = B2
				.getThreadList(B2.getCategoryList()[cNum][tNum]);%>
	<ul data-role="listview"><%for (String[] tread : threadList) {%>
		<li>
		 <a href="/threadContents.jsp?dat=<%= tread[1]%>"><span class="ui-li-desc"><%= tread[0]%></span></a>
		</li><%}%>
	</ul>
</body>
</html>