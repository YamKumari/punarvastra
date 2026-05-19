<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Categories"/>
<c:set var="extraCss" value="admin.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page page--admin">
<header class="admin-top"><a href="${pageContext.request.contextPath}/admin/dashboard">← Dashboard</a></header>
<main class="admin-main">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <h1>Categories</h1>
    <table class="data-table">
        <thead><tr><th>ID</th><th>Name</th><th>Slug</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="cat" items="${categories}">
            <tr>
                <td><c:out value="${cat.id}"/></td>
                <td><c:out value="${cat.name}"/></td>
                <td><c:out value="${cat.slug}"/></td>
                <td>
                    <a class="btn btn--sm" href="?edit=${cat.id}">Edit</a>
                    <form method="post" style="display:inline" onsubmit="return confirm('Delete?');">
                        <input type="hidden" name="csrf" value="${csrf}"/>
                        <input type="hidden" name="action" value="delete"/>
                        <input type="hidden" name="id" value="${cat.id}"/>
                        <button type="submit" class="btn btn--sm btn--danger">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <h2>Save category</h2>
    <form method="post" class="stack-form admin-form">
        <input type="hidden" name="csrf" value="${csrf}"/>
        <input type="hidden" name="action" value="save"/>
        <c:if test="${editCategory.id ne null}">
            <input type="hidden" name="id" value="${editCategory.id}"/>
        </c:if>
        <label>Name <input type="text" name="name" required value="<c:out value='${editCategory.name}'/>"/></label>
        <label>Slug <input type="text" name="slug" required value="<c:out value='${editCategory.slug}'/>"/></label>
        <label>Description <textarea name="description" rows="2"><c:out value="${editCategory.description}"/></textarea></label>
        <button type="submit" class="btn btn--primary">Save</button>
    </form>
</main>
</body>
</html>
