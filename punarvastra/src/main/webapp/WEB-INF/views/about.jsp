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

    <!-- Our Story Section -->
    <div class="about-story">
        <h1>Our story</h1>
        <p>PunarVastra is a thrift marketplace inspired by circular fashion in Nepal. We connect sellers who want to pass on quality garments with buyers who care about budget and sustainability.</p>
        <p>Every seller listing is reviewed by our team before it appears in the catalogue - so shoppers see only serious offers, and the platform stays trustworthy.</p>
    </div>

    <!-- Team Section -->
    <div class="team-section">
        <h2>Meet Our Team</h2>
        <p class="team-subtitle">Passionate minds behind PunarVastra</p>

        <div class="team-grid">
            <!-- Member 1: Yam Kumari Shrestha -->
            <div class="team-card">
                <div class="team-card__image">
                    <img src="${pageContext.request.contextPath}/static/images/team/yam.jpg"
                         alt="Yam Kumari Shrestha"
                         onerror="this.src='https://ui-avatars.com/api/?name=Yam+Kumari+Shrestha&background=1e3a5f&color=fff&size=120'">
                </div>
                <h3 class="team-card__name">Yam Kumari Shrestha</h3>
                <p class="team-card__role">Admin Dashboard & MVC Architecture</p>
                <p class="team-card__bio">Built admin analytics, product/category/order/user management, and complete MVC layer.</p>
            </div>

            <!-- Member 2: Laxmi Thapa -->
            <div class="team-card">
                <div class="team-card__image">
                    <img src="${pageContext.request.contextPath}/static/images/team/laxmi.jpg"
                         alt="Laxmi Thapa"
                         onerror="this.src='https://ui-avatars.com/api/?name=Laxmi+Thapa&background=1e3a5f&color=fff&size=120'">
                </div>
                <h3 class="team-card__name">Laxmi Thapa</h3>
                <p class="team-card__role">Database & Product Listing</p>
                <p class="team-card__bio">Designed complete database schema, product listing with filters, search, and product detail page.</p>
            </div>

            <!-- Member 3: Smriti Shahi -->
            <div class="team-card">
                <div class="team-card__image">
                    <img src="${pageContext.request.contextPath}/static/images/team/smriti.jpg"
                         alt="Smriti Shahi"
                         onerror="this.src='https://ui-avatars.com/api/?name=Smriti+Shahi&background=1e3a5f&color=fff&size=120'">
                </div>
                <h3 class="team-card__name">Smriti Shahi</h3>
                <p class="team-card__role">Cart, Wishlist, Orders & Seller Features</p>
                <p class="team-card__bio">Implemented shopping cart, wishlist, checkout, order placement, seller listings, and image upload.</p>
            </div>

            <!-- Member 4: Mahima Bhattarai -->
            <div class="team-card">
                <div class="team-card__image">
                    <img src="${pageContext.request.contextPath}/static/images/team/mahima.jpg"
                         alt="Mahima Bhattarai"
                         onerror="this.src='https://ui-avatars.com/api/?name=Mahima+Bhattarai&background=1e3a5f&color=fff&size=120'">
                </div>
                <h3 class="team-card__name">Mahima Bhattarai</h3>
                <p class="team-card__role">Authentication, Security & Filters</p>
                <p class="team-card__bio">Built login/register with BCrypt, session management, authentication/admin filters, CSRF protection.</p>
            </div>

            <!-- Member 5: Romiya Shahi -->
            <div class="team-card">
                <div class="team-card__image">
                    <img src="${pageContext.request.contextPath}/static/images/team/romiya.jpg"
                         alt="Romiya Shahi"
                         onerror="this.src='https://ui-avatars.com/api/?name=Romiya+Shahi&background=1e3a5f&color=fff&size=120'">
                </div>
                <h3 class="team-card__name">Romiya Shahi</h3>
                <p class="team-card__role">UI/UX Design & Frontend</p>
                <p class="team-card__bio">Designed complete responsive CSS, reusable templates, and interactive JavaScript.</p>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>

<style>
    .about-story {
        margin-bottom: 2.5rem;
    }
    .about-story h1 {
        margin-bottom: 1rem;
    }
    .about-story p {
        margin-bottom: 1rem;
        line-height: 1.6;
    }
    .team-section {
        margin-top: 2rem;
        text-align: center;
    }
    .team-section h2 {
        margin-bottom: 0.5rem;
    }
    .team-subtitle {
        color: var(--color-text-muted);
        margin-bottom: 2rem;
    }
    .team-grid {
        display: grid;
        gap: 1.5rem;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    }
    .team-card {
        background: var(--color-surface);
        border-radius: var(--radius);
        padding: 1.5rem;
        text-align: center;
        box-shadow: var(--shadow-card);
        transition: transform 0.2s ease;
    }
    .team-card:hover {
        transform: translateY(-5px);
    }
    .team-card__image {
        width: 120px;
        height: 120px;
        margin: 0 auto 1rem;
        border-radius: 50%;
        overflow: hidden;
        background: #eef2f7;
    }
    .team-card__image img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }
    .team-card__name {
        font-size: 1.1rem;
        margin: 0 0 0.25rem;
        color: var(--color-primary);
    }
    .team-card__role {
        font-size: 0.8rem;
        color: var(--color-accent);
        font-weight: 600;
        margin-bottom: 0.5rem;
    }
    .team-card__bio {
        font-size: 0.8rem;
        color: var(--color-text-muted);
        margin: 0;
        line-height: 1.4;
    }
    @media (max-width: 768px) {
        .team-grid {
            grid-template-columns: 1fr;
            max-width: 320px;
            margin: 0 auto;
        }
    }
</style>
</body>
</html>