<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="extraCss" value="auth.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page page--auth">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main class="auth-card">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <h1>Welcome back</h1>
    <form method="post" action="${pageContext.request.contextPath}/login" class="stack-form">
        <input type="hidden" name="csrf" value="${csrf}"/>
        <label>Username <input type="text" name="username" value="<c:out value='${rememberedUsername}'/>" required autocomplete="username"/></label>
        <label>Password <input type="password" name="password" required autocomplete="current-password"/></label>
        <label class="checkbox"><input type="checkbox" name="rememberMe" value="on"/> Remember me (7 days)</label>
        <button type="submit" class="btn btn--primary btn--lg">Login</button>
    </form>
    <p class="muted"><a href="${pageContext.request.contextPath}/register">Create an account</a></p>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
