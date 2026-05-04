<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Error - PunarVastra</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/error.css"/>
</head>
<body class="page page--error">
<main class="narrow center-text">
    <h1>Something went wrong</h1>
    <p>Please try again later or contact support.</p>
    <a class="btn btn--primary" href="${pageContext.request.contextPath}/">Home</a>
</main>
</body>
</html>
