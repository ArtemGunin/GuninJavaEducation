<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
    <body>
    <div>
        <h3>Current request</h3>
        <b>IP: ${currentRequest.ip}</b><br>
        <b>User-Agent: ${currentRequest.userAgentHead}</b><br>
        <p>Request date and time: ${currentRequest.requestTime}</p><br>
    </div>
    <c:if test="${!repository.isEmpty()}">
    <div>
    <h3>Request list</h3>
    </div>
    </c:if>
    <c:forEach var="requestInfo" items="${repository}">
    <div>
        IP: ${requestInfo.ip}<br>
        User-Agent: ${requestInfo.userAgentHead}<br>
        Request date and time: ${requestInfo.requestTime}<br><br>
    </div>
    </c:forEach>
    </body>
</html>