<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ page import="jp.blogspot.simple_asta.b2.server.B2"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>b3devel</title>
<script src="js_css/main.js"></script>
<link rel="stylesheet" href="js_css/main.css" />
</head>
<body>
	<%
		String dat = request.getParameter("dat");
	%><%= B2.getThreadDat(dat)%>
</body>

</html>