# 🚀 GRUHINI FINAL DEPLOYMENT CHECKLIST
**Status:** Ready for gruhani1.O push to GitHub  
**Last Updated:** March 26, 2026  
**Branch:** gruhani1.O

---

## ✅ CRITICAL FIXES COMPLETED

### Phase 1: Authentication & Role-Based Routing
- [x] **login.html** → Admin routing implemented (ROLE_ADMIN detected in JWT)
- [x] Role detection from `jwtClaims.roles` array
- [x] Auto-redirect based on role:
  - ADMIN → `admin-dashboard.html`
  - SELLER → `seller-dashboard.html`
  - USER → `index.html`

### Phase 2: User Orders & Status Tracking
- [x] **orders.html** → Fixed endpoint from `/get-orders` to `/view-order-user?orderStatus=`
- [x] Response format: `{ success: true, "Seller-Details": [...] }`
- [x] Added filter chips: All, Pending, Accepted, Delivered, Cancelled, Rejected
- [x] Added cancel order functionality
- [x] Added star rating + feedback modal for delivered orders

### Phase 3: Seller Management
- [x] **seller-dashboard.html** → Rebuilt with correct endpoints
  - Products: Add (multipart), Edit, Delete
  - Orders: Accept, Reject with reason message
  - OTP verification for delivery
- [x] **seller-profile-modern.html** → Real data display (to fix)

---

## ⚠️ REMAINING ISSUES (MUST FIX BEFORE DEPLOY)

### Issue 1: Seller Dashboard — Missing Description Display & Edit
**Current State:** Shows seller name (hardcoded)  
**Required:** 
- GET seller profile data (name, description, contact, etc.)
- Display description below name
- Add "Edit Description" button
- PATCH endpoint to update description

**Backend Endpoints Needed:**
```
GET /seller/profile                 → Returns seller details
PATCH /seller/profile              → Update seller description/bio
```

### Issue 2: Seller Profile Public View — No Real Data
**Current State:** Mock data with hardcoded seller name  
**Required:**
- GET `/public/seller-profile/{sellerId}` → Return seller + their products
- Show actual seller stats (total orders, rating, etc.)
- Load seller's products via `/explore?sellerId={id}`

**Backend Endpoints Needed:**
```
GET /public/seller-profile/{id}    → Seller details + stats
GET /explore?sellerId={id}         → Filter products by seller
```

### Issue 3: Product Management — Missing Description in Form
**Current State:** Add product form has all fields  
**Required:**
- Already in form ✅
- Ensure backend PATCH endpoint accepts description

### Issue 4: User Checkout — Missing Address Selection
**Current State:** Hardcoded address  
**Required:**
- GET `/user/addresses` → List user's saved addresses
- Multi-address selection in checkout
- Add new address modal
- POST `/add-address` and DELETE `/address/{id}`

**Backend Endpoints Needed:**
```
GET /user/addresses                → List all addresses
POST /add-address                  → Create new address
DELETE /address/{id}               → Remove address
PATCH /address/{id}                → Update address
```

### Issue 5: Sellers List Page
**Current State:** Links to `sellers.html` which doesn't exist  
**Fix:** Either create sellers.html or redirect to filtered menu view

---

## 📋 ALL REQUIRED ENDPOINTS (As Per Updated gruhani1.O)

### Authentication
```
POST /register              ✅ User registration
POST /register-seller       ✅ Seller registration
POST /logins                ✅ Login with JWT
POST /logout                ⚠️ Optional
```

### User Orders
```
GET /view-order-user?orderStatus=PENDING|ACCEPTED|DELIVERED|CANCELLED|REJECTED
POST /cancel-order/{orderId}
POST /feedback              → Rate/review order
```

### User Addresses
```
GET /user/addresses         ⚠️ Must implement
POST /add-address          ⚠️ Must implement
DELETE /address/{id}       ⚠️ Must implement
PATCH /address/{id}        ⚠️ Must implement
```

### Seller Profile
```
GET /seller/profile         ⚠️ Must implement
PATCH /seller/profile       ⚠️ Must implement (description/bio)
POST /add-product          ✅ Working
GET /seller/dishes         ✅ Working
PATCH /seller/update-product ⚠️ May need implementation
DELETE /seller/delete-product ⚠️ May need implementation
GET /seller/view-order-seller?orderStatus=
POST /seller/accept-order?orderId=
POST /seller/reject-order   ⚠️ Needs reason parameter
POST /seller/verify-otp?orderId=&otp=
```

### Seller Public Profile
```
GET /public/seller-profile/{sellerId}  ⚠️ Must implement
GET /explore?sellerId={id}             ✅ Filter products
GET /explore                           ✅ All products
POST /add-to-cart                      ✅ Working
```

### Admin Dashboard
```
GET /view-pending          ✅ Pending approvals
POST /accept-item          ✅ Approve products
POST /reject-item          ✅ Reject products
GET /admin/products-viewAll ⚠️ Verify /admin prefix
GET /admin/Sellers-viewAll  ⚠️ Verify /admin prefix
GET /admin/view-orders     ⚠️ Verify /admin prefix
GET /admin/approve-seller   ⚠️ Verify endpoint exists
```

### Cart & Checkout
```
GET /get-cart              ✅ Get user's cart
POST /place-order          ✅ Create order with addressId
POST /create-order         ⚠️ Check if this exists
```

---

## 🎨 UI/UX UPDATES NEEDED

### seller-dashboard.html
```html
<!-- AFTER seller name, BEFORE stats bar -->
<div class="seller-bio-section">
  <p id="sellerDescription">Loading...</p>
  <button onclick="editDescription()" class="btn-edit">Edit Bio</button>
</div>

<!-- EDIT MODAL -->
<div id="editBioModal">
  <textarea id="bioInput" placeholder="Tell customers about your kitchen..."></textarea>
  <button onclick="saveBio()">Save</button>
</div>
```

### seller-profile-modern.html
```html
<!-- Update to use real sellerId from URL param -->
<script>
const sellerId = new URLSearchParams(window.location.search).get('id');
// Load /public/seller-profile/{sellerId}
// Render stats: totalOrders, avgRating, joinDate
// Load products via /explore?sellerId={id}
</script>
```

### checkout.html
```html
<!-- ADD: Address Selection -->
<div class="address-section">
  <h3>Delivery Address</h3>
  <div id="addressOptions">
    <!-- Load from /user/addresses -->
  </div>
  <button onclick="openAddressModal()">+ Add New Address</button>
</div>
```

---

## 🔗 PAGE CONNECTIONS - FINAL MAP

```
index.html
├─ Menu → menu.html ✅
├─ About → about.html ✅
├─ Cart → cart.html ✅
├─ Orders → orders.html ✅ (if logged in)
├─ Favorites → favorites.html ⚠️ (endpoint missing)
└─ Seller → sellers.html ❌ (doesn't exist)

menu.html
├─ Product card → product.html?id={productId}
├─ Seller name → seller-profile-modern.html?id={sellerId}
└─ Add to cart → cart.html

cart.html
├─ Checkout → checkout.html
└─ Back → menu.html

checkout.html
├─ Place order → POST /place-order
├─ Address selection → /user/addresses
└─ Success → order-success.html

orders.html
├─ Filter menu ✅
├─ Cancel order ✅
├─ Rate order → feedback modal ✅
└─ Back → menu.html

seller-dashboard.html
├─ Products tab ✅
├─ Edit description ⚠️
├─ Orders tab ✅
└─ Profile → seller-profile-modern.html

seller-profile-modern.html
├─ Seller bio (using sellerId param) ⚠️
├─ Seller products (GET /explore?sellerId=) ⚠️
└─ Back → index.html

admin-dashboard.html
├─ Pending approvals ✅
├─ Products view ✅
├─ Sellers list ✅
└─ Orders view ✅
```

---

## 📦 FILES STATUS

| File | Status | Issues | Action |
|------|--------|--------|--------|
| login.html | ✅ FIXED | ADMIN routing | Ready |
| index.html | ✅ OK | Link sellers.html | Minor fix |
| menu.html | ✅ OK | — | Ready |
| product.html | ✅ OK | — | Ready |
| cart.html | ✅ OK | Razorpay removed | Ready |
| orders.html | ✅ REBUILT | Correct endpoint | Ready |
| checkout.html | ⚠️ NEEDS WORK | Multi-address missing | Update needed |
| seller-dashboard.html | ⚠️ NEEDS WORK | No description edit | Update needed |
| seller-profile-modern.html | ⚠️ NEEDS WORK | Mock data | Update needed |
| admin-dashboard.html | ✅ REBUILT | Endpoint prefixes? | Verify backend |

---

## 🚀 DEPLOYMENT STEPS

### Step 1: Backend Verification
```bash
# Verify these endpoints exist and respond correctly:
- POST /logins (returns JWT with jwtClaims.roles)
- GET /seller/profile (authenticated)
- PATCH /seller/profile (authenticated)
- GET /user/addresses
- POST /add-address
- GET /view-order-user?orderStatus=
```

### Step 2: Update Frontend Files
1. Copy `login-fixed.html` content to `login.html`
2. Copy new `orders.html` 
3. Update `seller-dashboard.html` with description edit
4. Update `seller-profile-modern.html` with real data loading
5. Update `checkout.html` with address selection

### Step 3: Add Missing Endpoints
- If `/seller/profile` doesn't exist → Backend needs to implement
- If `/user/addresses` doesn't exist → Backend needs to implement

### Step 4: Testing
```
1. Register as seller
2. Login as seller → should redirect to seller-dashboard.html
3. Add product with description
4. Edit seller bio
5. Create order with address selection
6. Admin login → should redirect to admin-dashboard.html
7. Cancel order from orders.html
8. Rate order
```

### Step 5: Deploy
```bash
git add .
git commit -m "Final: Role-based routing, seller descriptions, address management, order feedback"
git push origin gruhani1.O
```

---

## ⚡ QUICK WINS (Can do now)

- [x] Fix login routing ✅
- [x] Fix orders endpoint ✅
- [x] Add order cancellation ✅
- [x] Add order rating ✅
- [ ] Add seller description edit (10 min)
- [ ] Fix sellers.html link (2 min)
- [ ] Add address selection to checkout (15 min)
- [ ] Update seller profile to use URL param (10 min)

---

## 🎯 READY-TO-SHIP CRITERIA

- [ ] All endpoints verified against backend
- [ ] Seller description edit/display working
- [ ] Address selection in checkout
- [ ] Seller profile shows real data
- [ ] All links connected
- [ ] Mobile responsive throughout
- [ ] No console errors
- [ ] JWT role detection working
- [ ] Admin can approve/reject products
- [ ] Sellers can accept/reject orders

**Current Score: 7/10** → After fixes: **10/10**

