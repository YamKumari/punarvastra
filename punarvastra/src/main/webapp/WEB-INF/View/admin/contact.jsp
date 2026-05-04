<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="extraCss" value="about-contact.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main class="narrow">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <h1>Contact</h1>
    <form method="post" action="${pageContext.request.contextPath}/contact" class="stack-form">
        <input type="hidden" name="csrf" value="${csrf}"/>
        <label>Name <input type="text" name="name" required/></label>
        <label>Email <input type="email" name="email" required/></label>
        <label>Subject <input type="text" name="subject" required/></label>
        <label>Message <textarea name="message" rows="5" required></textarea></label>
        <button type="submit" class="btn btn--primary">Send</button>
    </form>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>