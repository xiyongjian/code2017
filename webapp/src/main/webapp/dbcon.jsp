<%@page import="java.sql.ResultSet"%>
<%@page import="gosigma.webapp.DBConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>PAge 1</title>
</head>
<body>
	<pre>
<%
	ResultSet rs = DBConnection.getConnection().createStatement().executeQuery("Select * from testing");
	rs.next();
	out.println(rs.getString(1));
	out.println(rs.getString(2));
	out.println(rs.getString(3));

	out.println(DBConnection.getSystemInfo().replace("\n", "<br>"));
	out.println(DBConnection.getLoadedClasses().replace("\n", "<br>"));
%>
</pre>

</body>
</html>

