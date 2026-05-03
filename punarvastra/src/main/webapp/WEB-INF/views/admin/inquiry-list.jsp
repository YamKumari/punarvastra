<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Inquiries"/>
<c:set var="extraCss" value="admin.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page page--admin">
<header class="admin-top"><a href="${pageContext.request.contextPath}/admin/dashboard">← Dashboard</a></header>
<main class="admin-main">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <h1>Inquiries</h1>
    <c:forEach var="inq" items="${inquiries}">
        <article class="inquiry-card">
            <header><strong><c:out value="${inq.name}"/></strong> · <c:out value="${inq.email}"/> · <c:out value="${inq.createdAt}"/>
                <c:if test="${not inq.read}"><span class="pill">New</span></c:if>
            </header>
            <h3><c:out value="${inq.subject}"/></h3>
            <p><c:out value="${inq.message}"/></p>
            <c:if test="${not inq.read}">
                <form method="post" action="${pageContext.request.contextPath}/admin/inquiries">
                    <input type="hidden" name="csrf" value="${csrf}"/>
                    <input type="hidden" name="id" value="${inq.id}"/>
                    <button type="submit" class="btn btn--sm">Mark read</button>
                </form>
            </c:if>
        </article>
    </c:forEach>
</main>
</body>
</html>
