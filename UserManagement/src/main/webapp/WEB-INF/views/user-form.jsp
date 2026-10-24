<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="editing" value="${user.id gt 0}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${editing ? 'Edit user' : 'Add user'} | User Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="topbar">
    <a class="brand" href="${pageContext.request.contextPath}/list">User Management</a>
</header>

<main class="container narrow">
    <h1>${editing ? 'Edit user' : 'Add user'}</h1>

    <form method="post" novalidate
          action="${pageContext.request.contextPath}/${editing ? 'update' : 'insert'}">
        <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>">
        <c:if test="${editing}">
            <input type="hidden" name="id" value="${user.id}">
        </c:if>

        <label for="name">Name *</label>
        <input id="name" name="name" type="text" maxlength="120" required
               value="<c:out value='${user.name}'/>">
        <c:if test="${not empty errors.name}"><p class="error"><c:out value="${errors.name}"/></p></c:if>

        <label for="email">E-mail *</label>
        <input id="email" name="email" type="email" maxlength="220" required
               value="<c:out value='${user.email}'/>">
        <c:if test="${not empty errors.email}"><p class="error"><c:out value="${errors.email}"/></p></c:if>

        <label for="country">Country</label>
        <input id="country" name="country" type="text" maxlength="120"
               value="<c:out value='${user.country}'/>">
        <c:if test="${not empty errors.country}"><p class="error"><c:out value="${errors.country}"/></p></c:if>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">${editing ? 'Save changes' : 'Create user'}</button>
            <a class="btn" href="${pageContext.request.contextPath}/list">Cancel</a>
        </div>
    </form>
</main>
</body>
</html>
