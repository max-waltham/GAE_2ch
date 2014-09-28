<%@ page language="java" contentType="text/html;charset=utf-8"
	import="jp.blogspot.simple_asta.b2.server.B2"%>
<%
	String c = request.getParameter("c");
	String t = request.getParameter("t");
	Integer cNum = Integer.parseInt(c);
	Integer tNum = Integer.parseInt(t);
	String[][] threadList = B2
			.getThreadList(B2.getCategoryList()[cNum][tNum]);
%>
<%//<ul data-role="listview"> %>
	<%
		for (String[] tread : threadList) {
	%>
	<%//<li>	<li> %>
	<a href="javascript:void(0);" 
		onclick="b3.getThread('<%=tread[1]%>')"><div><%=tread[0]%></div></a><hr />
	<%
		}
	%>
<%//</ul> %>