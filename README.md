# GRUHINI — Ghar Jaisa Khana, Ghar Ke Log

<div align="center">

<img src="https://raw.githubusercontent.com/Vanshika21Rajput/gruhini/gruhani1.O/logo.jpg" width="180" alt="Gruhini Logo" />

<br/>

[![Live Demo](https://img.shields.io/badge/🌐_Live_Demo-gruhini--app1.onrender.com-DAA520?style=for-the-badge&logoColor=white)](https://gruhini-app1.onrender.com)
[![Backend API](https://img.shields.io/badge/🔧_Backend_API-gruhani--backend.onrender.com-2E8B57?style=for-the-badge)](https://gruhani-backend.onrender.com)
[![Made in India](https://img.shields.io/badge/🇮🇳_Made_in-India-FF9933?style=for-the-badge)]()
[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)]()
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)]()
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Production-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)]()

*Connecting home chefs with food lovers who crave authentic, homemade meals*

---

### 📸 Live Screenshots

<table>
  <tr>
    <td align="center" width="50%">
      <img src="https://raw.githubusercontent.com/Vanshika21Rajput/gruhini/gruhani1.O/home-page.png" alt="Homepage" width="100%"/>
      <br/><sub><b>Homepage — स्वाद जो दिल छू जाए</b></sub>
    </td>
    <td align="center" width="50%">
      <img src="https://raw.githubusercontent.com/Vanshika21Rajput/gruhini/gruhani1.O/products.png" alt="Menu Page" width="100%"/>
      <br/><sub><b>The Royal Menu — Browse by Category</b></sub>
    </td>
  </tr>
  <tr>
    <td align="center" width="50%">
      <img src="https://raw.githubusercontent.com/Vanshika21Rajput/gruhini/gruhani1.O/product-details.png" alt="Product Detail" width="100%"/>
      <br/><sub><b>Product Detail — Verified Seller Profile</b></sub>
    </td>
    <td align="center" width="50%">
      <img src="https://raw.githubusercontent.com/Vanshika21Rajput/gruhini/gruhani1.O/Suji%20Cake.jpeg" alt="Sample Dish" width="100%"/>
      <br/><sub><b>Real Dish — Semolina Suji Cake by Home Chef</b></sub>
    </td>
  </tr>
</table>

</div>

---

## ⚡ Production Reliability — Load Tested & Verified

> This is not a demo project. Gruhini is deployed in production on Render, serving real local vendors in Narmadapuram (Hoshangabad), Madhya Pradesh.

| Metric | Result |
|--------|--------|
| 🟢 **Error Rate @ 100 concurrent users** | **0%** (Apache JMeter validated) |
| 🟡 **Error Rate @ 200 concurrent users** | 14–18% → diagnosed as server resource saturation (502s) |
| 🔧 **Fix Proposed** | Redis caching + connection pool tuning |
| 🔒 **Auth Model** | JWT-based stateless zero-trust RBAC |
| 🧪 **Testing** | JUnit + Mockito on service & repository layers |
| 🚀 **Deployment** | Live on Render (cloud) with real user traffic |

---

## 🧠 Backend Architecture — My Contribution

> I (Vanshika Rajput) architected and built the **entire backend** end-to-end — API design, security layer, database modeling, performance testing, and frontend-backend integration.

### What I built:

- **45+ RESTful APIs** covering product catalog, cart workflows, and end-to-end order processing across Buyer, Seller, and Admin roles
- **Zero-trust API security** via JWT + Spring Security — stateless RBAC with no server-side session state across all 3 user roles
- **N+1 query elimination** using Spring Data JPA/Hibernate — applied `JOIN FETCH` and lazy loading strategies to reduce redundant DB calls and improve read performance
- **Load testing infrastructure** — dynamic JWT token extraction across real user journeys via Apache JMeter; diagnosed and proposed fix for 200-user failure threshold
- **Unit testing** on service and repository layers using JUnit + Mockito
- **Clean architecture** — Controller → Service → Repository pattern for separation of concerns and testability
- **Full frontend-backend integration** — connected Vanilla JS frontend to Spring Boot APIs with proper CORS, error handling, and fallback data

---

## 🛠️ Tech Stack

### Backend *(Primary — built by me)*
```
├── Java 17
├── Spring Boot 3.x
├── Spring Security (JWT-based zero-trust auth)
├── Spring MVC (REST API design)
├── Spring Data JPA / Hibernate (ORM, query optimization)
├── PostgreSQL (relational DB, joins, aggregations)
├── JUnit + Mockito (service/repo unit testing)
├── Apache JMeter (load & stress testing)
├── Maven (build tool)
└── Deployed on Render.com
```

### Frontend *(Integrated by me)*
```
├── HTML5 + Tailwind CSS (CDN)
├── Vanilla JavaScript (ES6+)
├── Responsive Design (Mobile-First)
└── Dynamic API fetching with fallbacks
```

---

## 📡 Core API Reference

### 🌐 Public Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/explore` | List all products | 
| `GET` | `/sellers` | List all sellers |
| `POST` | `/logins` | User login + JWT issue |
| `POST` | `/register` | Customer registration |
| `POST` | `/register-seller` | Seller onboarding |

### 🛒 Buyer Endpoints *(JWT required)*
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/add-to-cart` | Add item to cart |
| `GET` | `/get-cart` | Fetch user cart |
| `POST` | `/place-order` | End-to-end order processing |

### 👩‍🍳 Seller Dashboard Endpoints *(JWT + SELLER role)*
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/seller/add-product` | Add new dish with image upload (multipart) |
| `GET` | `/seller/get-All-products` | Get all products (filter by status) |
| `DELETE` | `/seller/delete-product` | Delete a product by ID |
| `PATCH` | `/seller/update-product` | Update product details |
| `POST` | `/seller/accept-order` | Accept list of orders by ID |
| `POST` | `/seller/reject-order` | Reject list of orders by ID |
| `GET` | `/seller/view-order-seller` | View orders (filter by status) |
| `POST` | `/seller/verify-otp` | Verify delivery OTP for order |
| `POST` | `/seller/update-profile` | Update seller profile |
| `GET` | `/seller/get-seller-profile` | Get seller profile details |

### 🔐 Admin Endpoints *(JWT + ADMIN role)*
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/admin/orders` | All orders overview |

*Total: 45+ endpoints across Buyer, Seller, and Admin roles*

---

## 🔒 Security Design

```
Request → Spring Security Filter Chain
            ↓
        JWT Validation (stateless — no session)
            ↓
        Role Extraction (CUSTOMER / SELLER / ADMIN)
            ↓
        RBAC → Route Authorization
            ↓
        Controller → Service → Repository
```

- BCrypt password hashing
- Stateless JWT — no server-side session storage
- Role-Based Access Control across all 3 user types
- CORS configuration for cross-origin frontend requests

---

## 📊 Load Testing Report (Apache JMeter)

**Test Setup:**
- Dynamic JWT token extraction per virtual user
- Real user journeys simulated (login → browse → cart → order)
- Tested on production environment (Render)

**Results:**

```
100 Virtual Users  → Error Rate: 0.00%  ✅  STABLE
200 Virtual Users  → Error Rate: 14–18% ⚠️  SERVER RESOURCE SATURATION (502s)
```

**Root Cause Analysis:**
Server resource saturation under 200 concurrent users — connection pool exhaustion causing 502 Bad Gateway responses.

**Fix Proposal Engineered:**
1. **Redis caching** — cache frequently read product/catalog data to reduce DB hits
2. **Connection pool tuning** — increase HikariCP pool size, tune timeout settings
3. **Horizontal scaling** — Render instance upgrade for more memory/CPU headroom

---

## 📂 Project Structure

```
gruhini/                                 # Branch: gruhani1.O
├── 📄 .gitignore
├── 📄 Dockerfile                        # Container config (testing done)
├── 📄 mvnw / mvnw.cmd                   # Maven wrapper
├── 📄 pom.xml                           # Dependencies & build config
├── 📄 package.json / package-lock.json
│
└── 📁 src/main/java/com/example/Gruhani/
    ├── 📁 Configuration/                # Spring Security, JWT, CORS config
    ├── 📁 Controllers/                  # REST API endpoints (45+)
    ├── 📁 Enums/                        # ProductStatus and other enums
    ├── 📁 Exceptions/                   # Custom exception handling
    ├── 📁 Repositories/                 # Spring Data JPA repos
    ├── 📁 dtos/                         # Request/Response DTOs
    ├── 📁 models/                       # JPA entity models
    ├── 📁 service/                      # Business logic layer
    ├── 📁 proto/                        # Proto definitions
    ├── 📄 GruhaniApplication.java       # Spring Boot entry point
    └── 📄 jwtfilter.java                # JWT request filter
```

---

## 🏃 Local Setup

### Backend
```bash
# Clone the repo and switch to backend branch
git clone https://github.com/Vanshika21Rajput/gruhini.git
cd gruhini
git checkout gruhani1.O

# Set environment variables
export POSTGRES_URI=your_postgres_connection_string
export JWT_SECRET=your_secret_key

# Build and run
./mvnw clean package -DskipTests
java -jar target/*.jar
```

### Frontend
```bash
# Update API base URL
# Edit js/config.js:
window.CONFIG = {
    BASE_URL: 'http://localhost:8080',
}

# Open index.html in browser or serve with Live Server
```

---

## 👩‍🍳 Real Vendors on Platform

| Chef | Specialty | Location |
|------|-----------|----------|
| **Neelam Joshi** | Rajasthani Thalis, Dal Bati | Narmadapuram, MP |
| **Manju Vijayvargiye** | Sweets & Namkeen | Bhopal, MP |
| **Sakshi Tolani** | Artisan Cakes | Pune, MH |
| **Dolly Aunty** | Handmade Crafts & Soaps | Mumbai, MH |

---

## 👩‍💻 About the Developer

**Vanshika Rajput** — Backend Engineering Undergraduate, MITS Gwalior (CGPA 8.89)

- 400+ DSA problems solved (LeetCode, GFG) | 100-Day Streak
- Postman API Student Expert
- First Runner-Up — Manthan Hackathon, MANIT Bhopal (SafeChat project)
- Top 5 — HackSagon National Hackathon, IIITM Gwalior
- Winner — GFG Mock Placement Drive (200+ students)

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Vanshika_Rajput-0A66C2?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/vanshika-rajput-68791a35a/)
[![GitHub](https://img.shields.io/badge/GitHub-Vanshika21Rajput-181717?style=flat-square&logo=github)](https://github.com/Vanshika21Rajput)

---

## 📜 License

MIT License — see [LICENSE](LICENSE) for details.

---

<div align="center">

**Made with ❤️ in India**

*Gruhini — Where Every Meal Tells a Story*

</div>
