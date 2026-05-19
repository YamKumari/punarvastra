<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="extraCss" value="profile.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main class="narrow">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <h1>Profile</h1>
    <form method="post" action="${pageContext.request.contextPath}/profile" class="stack-form">
        <input type="hidden" name="csrf" value="${csrf}"/>
        <input type="hidden" name="action" value="profile"/>
        <label>Full name <input type="text" name="fullName" value="<c:out value='${profileUser.fullName}'/>" required/></label>
        <label>Phone <input type="text" name="phone" value="<c:out value='${profileUser.phone}'/>" required/></label>
        <label>Address <textarea name="address" rows="3"><c:out value="${profileUser.address}"/></textarea></label>
        <button type="submit" class="btn btn--primary">Save profile</button>
    </form>
    <h2>Change password</h2>
    <form method="post" action="${pageContext.request.contextPath}/profile" class="stack-form">
        <input type="hidden" name="csrf" value="${csrf}"/>
        <input type="hidden" name="action" value="password"/>
        <label>Current password <input type="password" name="currentPassword" required/></label>
        <label>New password <input type="password" name="newPassword" required/></label>
        <button type="submit" class="btn btn--ghost">Update password</button>
    </form>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
