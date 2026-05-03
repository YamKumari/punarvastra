<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Users"/>
<c:set var="extraCss" value="admin.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page page--admin">
<header class="admin-top"><a href="${pageContext.request.contextPath}/admin/dashboard">← Dashboard</a></header>
<main class="admin-main">
  <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
  <h1>Pending account approvals</h1>
  <table class="data-table">
    <thead><tr><th>ID</th><th>Name</th><th>Username</th><th>Email</th><th></th></tr></thead>
    <tbody>
    <c:forEach var="u" items="${pendingUsers}">
      <tr>
        <td><c:out value="${u.id}"/></td>
        <td><c:out value="${u.fullName}"/></td>
        <td><c:out value="${u.username}"/></td>
        <td><c:out value="${u.email}"/></td>
        <td>
          <form method="post" class="inline-form" action="${pageContext.request.contextPath}/admin/users">
            <input type="hidden" name="csrf" value="${csrf}"/>
            <input type="hidden" name="userId" value="${u.id}"/>
            <input type="hidden" name="approved" value="true"/>
            <button type="submit" class="btn btn--sm btn--primary">Approve</button>
          </form>
          <form method="post" class="inline-form" action="${pageContext.request.contextPath}/admin/users">
            <input type="hidden" name="csrf" value="${csrf}"/>
            <input type="hidden" name="userId" value="${u.id}"/>
            <input type="hidden" name="approved" value="false"/>
            <button type="submit" class="btn btn--sm btn--danger">Reject</button>
          </form>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  <h2>All users</h2>
  <table class="data-table">
    <thead><tr><th>ID</th><th>Username</th><th>Role</th><th>Approved</th></tr></thead>
    <tbody>
    <c:forEach var="u" items="${users}">
      <tr>
        <td><c:out value="${u.id}"/></td>
        <td><c:out value="${u.username}"/></td>
        <td><c:out value="${u.role}"/></td>
        <td><c:out value="${u.approved}"/></td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</main>
</body>
</html>
