<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Users | User Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/confirm.js" defer></script>
</head>
<body>
<header class="topbar">
    <a class="brand" href="${pageContext.request.contextPath}/list">User Management</a>
</header>

<main class="container">
    <div class="page-head">
        <h1>Users</h1>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/new">+ Add user</a>
    </div>

    <c:if test="${not empty message}">
        <p class="flash"><c:out value="${message}"/></p>
    </c:if>

    <c:choose>
        <c:when test="${empty users}">
            <p class="empty">No users yet. Click <strong>Add user</strong> to create one.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>E-mail</th>
                    <th>Country</th>
                    <th class="actions-col">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td><c:out value="${u.id}"/></td>
                        <td><c:out value="${u.name}"/></td>
                        <td><c:out value="${u.email}"/></td>
                        <td><c:out value="${u.country}"/></td>
                        <td><div class="actions">
                            <a class="btn" href="${pageContext.request.contextPath}/edit?id=${u.id}">Edit</a>
                            <form method="post" action="${pageContext.request.contextPath}/delete" class="inline">
                                <input type="hidden" name="id" value="${u.id}">
                                <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>">
                                <button type="submit" class="btn btn-danger js-confirm"
                                        data-confirm="Delete this user?">Delete</button>
                            </form>
                        </div></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
