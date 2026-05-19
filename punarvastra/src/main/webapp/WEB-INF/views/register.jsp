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
    <h1>Join PunarVastra</h1>
    <form method="post" action="${pageContext.request.contextPath}/register" class="stack-form">
        <input type="hidden" name="csrf" value="${csrf}"/>
        <label>Full name <input type="text" name="fullName" required/></label>
        <label>Username <input type="text" name="username" required/></label>
        <label>Email <input type="email" name="email" required/></label>
        <label>Phone (10 digits, starts with 9) <input type="text" name="phone" required/></label>
        <label>Address <textarea name="address" rows="2"></textarea></label>
        <label>Password <input type="password" name="password" required/></label>
        <button type="submit" class="btn btn--primary btn--lg">Register</button>
    </form>
    <p class="muted"><a href="${pageContext.request.contextPath}/login">Already have an account?</a></p>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
