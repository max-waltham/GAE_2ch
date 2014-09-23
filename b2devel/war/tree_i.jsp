<%@ page language="java" import="jp.blogspot.simple_asta.b2.server.B2"
	contentType="text/html;charset=utf-8"%>


<div id="tree" data-role="collapsibleset" data-theme="a"
	data-inset="false">
	<%
		String[][][] cl_u = B2.getCategoryList();
		for (int c = 0, l1 = cl_u.length; c < l1; c++) {
			String[][] catList = cl_u[c];
	%>
	<div data-role="collapsible">
		<h2><%=catList[0][0]%></h2>
		<ul data-role="listview">
			<%
				for (int t = 1, l2 = catList.length; t < l2; t++) {
						String[] tName = catList[t];
			%>
			<li><a href="javascript:void(0);"
				onclick="b3.listUp(<%=c%>,<%=t%>)"><%=tName[0]%></a></li>
			<%
				}
			%>
		</ul>
	</div>
	<%
		}
	%>
</div>
