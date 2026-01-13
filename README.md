# 🍛 GRUHINI - Ghar Jaisa Khana, Ghar Ke Log

<div align="center">

![Gruhini Logo](public/logo.jpg)

**India's Premier Home Kitchen Marketplace**

[![Live Demo](https://img.shields.io/badge/🌐_Live_Demo-gruhini--app1.onrender.com-gold?style=for-the-badge)](https://gruhini-app1.onrender.com)
[![Backend](https://img.shields.io/badge/🔧_Backend-gruhinibackend.onrender.com-green?style=for-the-badge)](https://gruhinibackend.onrender.com)
[![Made in India](https://img.shields.io/badge/🇮🇳_Made_in-India-orange?style=for-the-badge)]()

*Connecting home chefs with food lovers who crave authentic, homemade meals*

---

</div>

## ✨ About Gruhini

**Gruhini** (गृहिणी - meaning "homemaker" in Hindi) is a revolutionary platform that bridges the gap between talented home chefs and customers seeking authentic, home-cooked meals. We believe that the best food comes from home kitchens, made with love and traditional recipes passed down through generations.

### 🎯 Mission
> *"To empower home chefs, especially women, to monetize their culinary skills while providing customers with authentic, preservative-free, homemade food."*

---

## 🚀 Features

### For Customers 🍽️
| Feature | Description |
|---------|-------------|
| **🎡 Royal Thali Roller** | Interactive rotating dish showcase on homepage |
| **📋 The Royal Menu** | Browse dishes by category (Thalis, Snacks, Desserts, Crafts) |
| **👩‍🍳 Kitchen Profiles** | View home chef profiles and their specialties |
| **🛒 Smart Cart** | Single-kitchen ordering for freshness |
| **🔐 Secure Auth** | JWT-based authentication with Google Sign-In |

### For Home Chefs 👩‍🍳
| Feature | Description |
|---------|-------------|
| **📊 Seller Dashboard** | Real-time stats, earnings, and order management |
| **🍴 Dish Management** | Add, edit, and manage your menu |
| **🏪 Kitchen Toggle** | Open/close your kitchen with one click |
| **📈 Analytics** | Track your bestsellers and customer insights |

---

## 🛠️ Tech Stack

### Frontend
```
├── HTML5 + Tailwind CSS (CDN)
├── Vanilla JavaScript (ES6+)
├── Responsive Design (Mobile-First)
├── Progressive Enhancement
└── Dynamic Data Fetching with Fallbacks
```

### Backend (Spring Boot)
```
├── Java 17 + Spring Boot 3.x
├── Spring Security + JWT Authentication
├── MongoDB (Database)
├── RESTful API Design
└── Deployed on Render.com
```

### Key Endpoints
| Endpoint | Description | Auth |
|----------|-------------|------|
| `GET /explore` | List all products | Public |
| `GET /sellers` | List all sellers | Public |
| `POST /logins` | User login | Public |
| `POST /register` | User registration | Public |
| `POST /register-seller` | Seller registration | Public |
| `GET /seller/stats` | Seller dashboard stats | JWT |
| `POST /add-to-cart` | Add item to cart | JWT |
| `GET /get-cart` | Get user's cart | JWT |

---

## 📂 Project Structure

```
Gruhini/
├── 📄 index.html          # Homepage with Thali Roller
├── 📄 menu.html           # Product listing
├── 📄 product.html        # Product details
├── 📄 cart.html           # Shopping cart
├── 📄 login.html          # Auth (Login/Register)
├── 📄 sellers.html        # All home chefs
├── 📄 seller-profile.html # Individual chef profile
├── 📄 seller-dashboard.html # Seller management
├── 📄 about.html          # About Gruhini
├── 📄 order-success.html  # Order confirmation
│
├── 📁 js/
│   └── config.js          # API configuration
│
├── 📁 public/             # Images & assets
│   ├── Neelam Joshi/      # Chef images
│   ├── Manju vijayvargiye/
│   ├── Sakshi Tolani/
│   ├── dolly aunty/
│   └── logo.jpg
│
├── 📄 real-products.json  # Fallback product data
│
└── 📁 gruhini-master3/    # Spring Boot Backend
    └── src/main/java/com/example/Gruhani/
        ├── Controllers/
        ├── models/
        ├── Repositories/
        └── Configuration/
```

---

## 🏃‍♂️ Quick Start

### Frontend (Static Files)
Simply open `index.html` in a browser or deploy to any static hosting:
- Netlify
- Vercel
- Render Static Site
- GitHub Pages

### Backend Configuration
1. Update `js/config.js`:
```javascript
window.CONFIG = {
    BASE_URL: 'https://your-backend-url.com',
    RAZORPAY_KEY: 'rzp_test_YOUR_KEY',
    PLACEHOLDER: 'https://...'
};
```

2. Backend Environment Variables:
```env
MONGODB_URI=mongodb+srv://...
JWT_SECRET=your-secret-key
```

---

## 🌟 Featured Home Chefs

| Chef | Specialty | Location |
|------|-----------|----------|
| 👩‍🍳 **Neelam Joshi** | Rajasthani Thalis, Dal Bati | Indore, MP |
| 👩‍🍳 **Manju Vijayvargiye** | Sweets & Namkeen | Bhopal, MP |
| 👩‍🍳 **Sakshi Tolani** | Artisan Cakes | Pune, MH |
| 👩‍🍳 **Dolly Aunty** | Handmade Crafts & Soaps | Mumbai, MH |

---

## 📱 Screenshots

### Homepage - Royal Thali Roller
*Interactive rotating showcase of trending dishes*

### The Royal Menu
*Beautiful grid layout with category filtering*

### Seller Dashboard
*Complete kitchen management for home chefs*

---

## 🔒 Security Features

- ✅ JWT-based stateless authentication
- ✅ BCrypt password hashing
- ✅ CORS configuration for cross-origin requests
- ✅ Role-based access control (CUSTOMER/SELLER)
- ✅ Secure token storage in localStorage

---

## 🚀 Deployment

### Frontend: Render Static Site
```bash
# Build command: (not required - static files)
# Publish directory: ./
```

### Backend: Render Web Service
```bash
# Build command:
cd gruhini-master3 && ./mvnw clean package -DskipTests

# Start command:
java -jar gruhini-master3/target/*.jar
```

---

## 🤝 Contributing

We welcome contributions! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👏 Acknowledgments

- All our amazing home chefs who make Gruhini possible
- The people of Narmadapuram (Hoshangabad) for inspiring this platform
- Google Fonts for beautiful typography
- Tailwind CSS for rapid styling

---

<div align="center">

**Made with ❤️ in India**

*Gruhini - Where Every Meal Tells a Story*

[![GitHub stars](https://img.shields.io/github/stars/Vanshika21Rajput/gruhini?style=social)]()
[![Twitter Follow](https://img.shields.io/twitter/follow/gruhini?style=social)]()

</div>
