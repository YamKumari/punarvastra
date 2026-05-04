<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="extraCss" value="about-contact.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main class="narrow prose">
    <h1>Our story</h1>
    <p>PunarVastra is a student-built thrift marketplace inspired by circular fashion in Nepal. We connect sellers who want to pass on quality garments with buyers who care about budget and sustainability.</p>
    <p>Every seller listing is reviewed by our team before it appears in the catalogue - so shoppers see only serious offers, and the platform stays trustworthy.</p>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html