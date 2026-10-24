<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error | User Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="topbar">
    <a class="brand" href="${pageContext.request.contextPath}/list">User Management</a>
</header>
<main class="container narrow">
    <h1>Error <c:out value="${requestScope['jakarta.servlet.error.status_code']}"/></h1>
    <%-- Generic message only: stack traces and SQL errors are never shown to the user --%>
    <c:choose>
        <c:when test="${requestScope['jakarta.servlet.error.status_code'] eq 404}">
            <p>The page or user you are looking for does not exist.</p>
        </c:when>
        <c:when test="${requestScope['jakarta.servlet.error.status_code'] eq 403}">
            <p>Your request was rejected. Reload the page and try again.</p>
        </c:when>
        <c:otherwise>
            <p>Something went wrong. Please try again later.</p>
        </c:otherwise>
    </c:choose>
    <p><a class="btn" href="${pageContext.request.contextPath}/list">Back to users</a></p>
</main>
</body>
</html>
