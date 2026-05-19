<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title><c:out value="${empty pageTitle ? 'Shop' : pageTitle}"/> - PunarVastra</title>
<link rel="preconnect" href="https://fonts.googleapis.com"/>
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin="anonymous"/>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&amp;family=Playfair+Display:wght@600;700&amp;display=swap" rel="stylesheet"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css?v=2"/>
<c:if test="${not empty extraCss}">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/${extraCss}"/>
</c:if>
