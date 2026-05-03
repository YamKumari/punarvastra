<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="site-header">
    <div class="site-header__inner">
        <a class="site-logo" href="${pageContext.request.contextPath}/" title="PunarVastra - home">
            <span class="site-logo__mark" aria-hidden="true">PV</span>
            <span class="site-logo__text">PunarVastra</span>
        </a>
        <form class="site-search" action="${pageContext.request.contextPath}/search" method="get" role="search">
            <input type="search" name="q" placeholder="Search second-hand fashion..." value="<c:out value='${searchQ}'/>" aria-label="Search catalogue"/>
            <button type="submit" class="btn btn--accent">Search</button>
        </form>
        <div class="site-header__actions">
            <a class="icon-link" href="${pageContext.request.contextPath}/cart" aria-label="Shopping basket">
                <span class="icon-link__label">Basket</span>
                <span class="badge">${cartCount}</span>
            </a>
            <c:if test="${not empty sessionScope.currentUser}">
                <a class="icon-link" href="${pageContext.request.contextPath}/wishlist" aria-label="Wishlist">
                    <span class="icon-link__label">Wishlist</span>
                    <span class="badge">${wishCount}</span>
                </a>
            </c:if>
            <c:choose>
                <c:when test="${not empty sessionScope.currentUser}">
                    <div class="user-menu">
                        <button type="button" class="user-menu__btn" id="userMenuBtn" aria-expanded="false">
                            <c:out value="${sessionScope.currentUser.username}"/>
                        </button>
                        <ul class="user-menu__dropdown" id="userMenuPanel" hidden>
                            <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
                            <li><a href="${pageContext.request.contextPath}/orders">My orders</a></li>
                            <li><a href="${pageContext.request.contextPath}/my-listings">My listings</a></li>
                            <li><a href="${pageContext.request.contextPath}/sell">Sell item</a></li>
                            <c:if test="${sessionScope.currentUser.admin}">
                                <li><a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a></li>
                            </c:if>
                            <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                        </ul>
                    </div>
                </c:when>
                <c:otherwise>
                    <a class="btn btn--ghost" href="${pageContext.request.contextPath}/login">Login</a>
                    <a class="btn btn--primary" href="${pageContext.request.contextPath}/register">Register</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>
