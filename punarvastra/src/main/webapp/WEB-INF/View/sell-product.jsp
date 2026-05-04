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
    <h1>List an item</h1>
    <p class="muted">Your listing stays <strong>pending</strong> until an administrator approves it. Rejected items can be sent back for review from <a href="${pageContext.request.contextPath}/my-listings">My listings</a>.</p>
    <form method="post" action="${pageContext.request.contextPath}/sell" enctype="multipart/form-data" class="stack-form">
        <input type="hidden" name="csrf" value="${csrf}"/>
        <label>Title <input type="text" name="title" required maxlength="150"/></label>
        <label>Description <textarea name="description" rows="4" required></textarea></label>
        <label>Price (NPR) <input type="number" name="price" step="0.01" min="0" required/></label>
        <label>Size <input type="text" name="size" placeholder="S, M, L, Free..." required/></label>
        <label>Condition
            <select name="productCondition" required>
                <option>New</option>
                <option>Like New</option>
                <option>Good</option>
                <option>Fair</option>
            </select>
        </label>
        <label>Brand <input type="text" name="brand"/></label>
        <label>Stock <input type="number" name="stock" min="0" value="1" required/></label>
        <label>Category
            <select name="categoryId" required>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat.id}"><c:out value="${cat.name}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Photo (jpg/png, max 5MB; optional - placeholder used if empty)
            <input type="file" name="image" accept=".jpg,.jpeg,.png"/>
        </label>
        <button type="submit" class="btn btn--primary">Submit for review</button>
    </form>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
