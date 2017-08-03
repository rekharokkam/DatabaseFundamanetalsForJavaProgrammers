<%@ page language = "java" contentType = "text/html; charset = UTF-8" pageEncoding = "UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix="sql" %>   
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Database Fundamentals for Java Programmers</title>
</head>
<body>
	<sql:setDataSource 
		var="myDS" driver="org.sqlite.JDBC" url="jdbc:sqlite:C:/Rekha/workspace/DatabaseFundamanetalsForJavaProgrammers/db/recClub.db"/>
		
	<sql:query var="activities" dataSource="${myDS}">
		SELECT * FROM activities ORDER BY name;
	</sql:query>
	
	<div align="center">
		<form>
			<table>
				
				<tr>
					<th>Get Session Info</th>
					<th>Activity</th>
					<th>Price</th>					
				</tr>
				
				<c:set var='index' value='1' scope="page"/>
				<fmt:setLocale value="en_US"/>
				
				<c:forEach var="item" items="${activities.rows}">
				
					<tr>
						<td><input type="checkbox" name="check-${index}" value="${index}"></td>
						<td><input type="text" name="prod-${index}" value="${item.name}" readonly style="text-align: center"/></td>
						<td><input type="text" name="price-${index}" value="<fmt:formatNumber type='currency' maxFractionDigits='2' value='${item.cost}'/>" readonly style="text-align: center"/></td>
					</tr>
				
					<c:set var="index" value="${index + 1}"></c:set>
				</c:forEach>		
				
				<tr>
					<td colspan="3"> <input type="submit" value="submit request"></td>
				</tr>
				
			</table>		
					
		</form>
	</div>

</body>
</html>