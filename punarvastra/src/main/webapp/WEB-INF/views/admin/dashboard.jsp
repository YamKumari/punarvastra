<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Admin"/>
<c:set var="extraCss" value="admin.css"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/WEB-INF/templates/head.jsp"/>
    <!-- Chart.js CDN -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
</head>
<body class="page page--admin">
<header class="admin-top">
    <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/products">Products</a>
    <a href="${pageContext.request.contextPath}/admin/products?status=PENDING">Pending listings</a>
    <a href="${pageContext.request.contextPath}/admin/categories">Categories</a>
    <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
    <a href="${pageContext.request.contextPath}/admin/users">Users</a>
    <a href="${pageContext.request.contextPath}/admin/inquiries">Inquiries</a>
    <a href="${pageContext.request.contextPath}/admin/analytics">Analytics</a>
    <a href="${pageContext.request.contextPath}/">Storefront</a>
</header>
<main class="admin-main">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <h1>Dashboard</h1>

    <!-- KPI Cards -->
    <div class="stat-grid">
        <div class="stat-card"><span class="stat-card__label">Products (all)</span><span class="stat-card__value"><c:out value="${totalProducts}"/></span></div>
        <div class="stat-card"><span class="stat-card__label">Orders</span><span class="stat-card__value"><c:out value="${totalOrders}"/></span></div>
        <div class="stat-card"><span class="stat-card__label">Users</span><span class="stat-card__value"><c:out value="${totalUsers}"/></span></div>
        <div class="stat-card"><span class="stat-card__label">Revenue (month)</span><span class="stat-card__value">Rs. <c:out value="${revenueMonth}"/></span></div>
        <div class="stat-card stat-card--warn"><span class="stat-card__label">Pending listings</span><span class="stat-card__value"><c:out value="${pendingListings}"/></span></div>
        <div class="stat-card stat-card--warn"><span class="stat-card__label">Pending users</span><span class="stat-card__value"><c:out value="${pendingUsers}"/></span></div>
        <div class="stat-card"><span class="stat-card__label">Low stock (&lt;3)</span><span class="stat-card__value"><c:out value="${lowStock}"/></span></div>
    </div>

    <!-- Charts Section -->
    <div class="charts-section">
        <h2>Weekly Analytics (Last 7 Days)</h2>
        <div class="charts-grid">
            <!-- Chart 1: Weekly Orders -->
            <div class="chart-card">
                <h3>Weekly Orders</h3>
                <canvas id="ordersChart" height="200"></canvas>
            </div>

            <!-- Chart 2: Weekly Users -->
            <div class="chart-card">
                <h3>New Users</h3>
                <canvas id="usersChart" height="200"></canvas>
            </div>

            <!-- Chart 3: Weekly Products -->
            <div class="chart-card">
                <h3>New Products</h3>
                <canvas id="productsChart" height="200"></canvas>
            </div>

            <!-- Chart 4: Weekly Revenue -->
            <div class="chart-card">
                <h3> Weekly Revenue (Rs.)</h3>
                <canvas id="revenueChart" height="200"></canvas>
            </div>
        </div>
    </div>

    <!-- Recent Orders Table -->
    <h2>Recent orders</h2>
    <table class="data-table">
        <thead>
        <tr><th>ID</th><th>User</th><th>Total</th><th>Status</th></tr>
        </thead>
        <tbody>
        <c:forEach var="o" items="${recentOrders}">
            <tr>
                <td><a href="${pageContext.request.contextPath}/admin/orders?id=${o.id}">#<c:out value="${o.id}"/></a></td>
                <td><c:out value="${o.userId}"/></td>
                <td>Rs. <c:out value="${o.totalAmount}"/></td>
                <td><c:out value="${o.status}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>

<script>
    // Prepare data from Servlet
    const orderDates = [
        <c:forEach var="d" items="${orderDates}" varStatus="status">
        "${d}"${!status.last ? ',' : ''}
        </c:forEach>
    ];
    const orderCounts = [
        <c:forEach var="c" items="${orderCounts}" varStatus="status">
        ${c}${!status.last ? ',' : ''}
        </c:forEach>
    ];
    const userDates = [
        <c:forEach var="d" items="${userDates}" varStatus="status">
        "${d}"${!status.last ? ',' : ''}
        </c:forEach>
    ];
    const userCounts = [
        <c:forEach var="c" items="${userCounts}" varStatus="status">
        ${c}${!status.last ? ',' : ''}
        </c:forEach>
    ];
    const productDates = [
        <c:forEach var="d" items="${productDates}" varStatus="status">
        "${d}"${!status.last ? ',' : ''}
        </c:forEach>
    ];
    const productCounts = [
        <c:forEach var="c" items="${productCounts}" varStatus="status">
        ${c}${!status.last ? ',' : ''}
        </c:forEach>
    ];
    const revenueDates = [
        <c:forEach var="d" items="${revenueDates}" varStatus="status">
        "${d}"${!status.last ? ',' : ''}
        </c:forEach>
    ];
    const revenueAmounts = [
        <c:forEach var="c" items="${revenueAmounts}" varStatus="status">
        ${c}${!status.last ? ',' : ''}
        </c:forEach>
    ];

    // Chart 1: Weekly Orders (Bar Chart)
    const ordersCtx = document.getElementById('ordersChart').getContext('2d');
    new Chart(ordersCtx, {
        type: 'bar',
        data: {
            labels: orderDates,
            datasets: [{
                label: 'Orders',
                data: orderCounts,
                backgroundColor: 'rgba(30, 58, 95, 0.7)',
                borderColor: '#1e3a5f',
                borderWidth: 1,
                borderRadius: 5
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: { legend: { position: 'top' } },
            scales: { y: { beginAtZero: true, title: { display: true, text: 'Number of Orders' } } }
        }
    });

    // Chart 2: Weekly Users (Line Chart)
    const usersCtx = document.getElementById('usersChart').getContext('2d');
    new Chart(usersCtx, {
        type: 'line',
        data: {
            labels: userDates,
            datasets: [{
                label: 'New Users',
                data: userCounts,
                backgroundColor: 'rgba(230, 126, 34, 0.2)',
                borderColor: '#e67e22',
                borderWidth: 2,
                tension: 0.3,
                fill: true,
                pointBackgroundColor: '#e67e22',
                pointRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: { legend: { position: 'top' } },
            scales: { y: { beginAtZero: true, title: { display: true, text: 'Number of Users' } } }
        }
    });

    // Chart 3: Weekly Products (Bar Chart)
    const productsCtx = document.getElementById('productsChart').getContext('2d');
    new Chart(productsCtx, {
        type: 'bar',
        data: {
            labels: productDates,
            datasets: [{
                label: 'New Products',
                data: productCounts,
                backgroundColor: 'rgba(39, 174, 96, 0.7)',
                borderColor: '#27ae60',
                borderWidth: 1,
                borderRadius: 5
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: { legend: { position: 'top' } },
            scales: { y: { beginAtZero: true, title: { display: true, text: 'Number of Products' } } }
        }
    });

    // Chart 4: Weekly Revenue (Line Chart)
    const revenueCtx = document.getElementById('revenueChart').getContext('2d');
    new Chart(revenueCtx, {
        type: 'line',
        data: {
            labels: revenueDates,
            datasets: [{
                label: 'Revenue (Rs.)',
                data: revenueAmounts,
                backgroundColor: 'rgba(192, 57, 43, 0.2)',
                borderColor: '#c0392b',
                borderWidth: 2,
                tension: 0.3,
                fill: true,
                pointBackgroundColor: '#c0392b',
                pointRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: { legend: { position: 'top' } },
            scales: { y: { beginAtZero: true, title: { display: true, text: 'Amount (Rs.)' } } }
        }
    });
</script>

<style>
    .charts-section {
        margin: 2rem 0;
    }
    .charts-section h2 {
        margin-bottom: 1.5rem;
    }
    .charts-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
        gap: 1.5rem;
        margin-bottom: 2rem;
    }
    .chart-card {
        background: var(--color-surface);
        border-radius: var(--radius);
        padding: 1rem;
        box-shadow: var(--shadow-card);
    }
    .chart-card h3 {
        text-align: center;
        margin-bottom: 1rem;
        font-size: 0.95rem;
        color: var(--color-text-muted);
    }
    canvas {
        max-height: 220px;
        width: 100%;
    }
    @media (max-width: 900px) {
        .charts-grid {
            grid-template-columns: 1fr;
        }
    }
</style>
</body>
</html>