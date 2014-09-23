<%@ page language="java" contentType="text/html;charset=utf-8"
	import="jp.blogspot.simple_asta.b2.server.B2"%><%
	String dat = request.getParameter("dat");
%><%=B2.getThreadDat(dat)%>