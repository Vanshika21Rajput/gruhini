# 🔧 GRUHINI TRUST-FIRST ORDER FLOW - BACKEND ENDPOINTS MAPPING

## Overview
This document outlines all the backend endpoints required for the **Trust-First Direct Order System** (Razorpay removed). The frontend is ready and expects these endpoints to work correctly.

---

## 📋 ENDPOINTS SUMMARY

### **1. USER PLACE ORDER**
**Endpoint:** `POST /place-order`
**Location:** OrderController.java or Order_Controls.java
**Expected Behavior:**
- Accept delivery address and cart items
- Create order with status = `PENDING`
- Send email to seller: "New Order Received #12345"
- Return order ID and status

```
REQUEST:
{
  "addressId": "addr-100",
  "deliveryAddress": {
    "fullName": "Amit Kumar",
    "phone": "+91-9876543210",
    "street": "123 Main St",
    "city": "Mumbai",
    "pincode": "400001",
    "instructions": "Ring bell twice"
  },
  "cartItems": [
    {"id": 1, "name": "Dal Baati", "price": 250, "quantity": 2}
  ],
  "subtotal": 500,
  "taxes": 25,
  "deliveryFee": 49,
  "total": 574,
  "paymentMethod": "COD"
}

RESPONSE:
{
  "orderId": 12345,
  "status": "PENDING",
  "message": "Order created successfully!"
}
```

---

### **2. GET ORDER DETAILS**
**Endpoint:** `GET /orders/{orderId}`
**Expected Behavior:**
- Fetch order with all details
- Include seller info, items, address, status, OTP (if accepted)
- Include delivery time and method (if accepted)

```
RESPONSE:
{
  "id": 12345,
  "userEmail": "customer@example.com",
  "status": "ACCEPTED",
  "total": 574,
  "items": [
    {
      "id": 1,
      "name": "Dal Baati Churma",
      "price": 250,
      "quantity": 2
    }
  ],
  "seller": {
    "id": 100,
    "businessName": "Neelam Joshi",
    "phone": "+91-9876543210",
    "location": "Indore, MP"
  },
  "deliveryAddress": {
    "fullName": "Amit Kumar",
    "phone": "+91-9876543210",
    "street": "123 Main St",
    "city": "Mumbai",
    "pincode": "400001"
  },
  "deliveryTime": "Today at 8:00 PM",
  "deliveryMethod": "SELLER_DELIVERY",
  "otp": "482915",
  "createdAt": "2024-03-26T14:30:00Z"
}
```

---

### **3. SELLER ACCEPT ORDER (CRITICAL)**
**Endpoint:** `POST /seller/accept-order`
**Location:** Seller_dashboard_controls.java or new OrderController
**Expected Behavior:**
- Accept order with delivery time and method
- Generate 6-digit OTP
- Save OTP in database
- Send email to customer with: OTP, Delivery Time, Delivery Method, Seller Contact
- Update order status to `ACCEPTED`
- Send email to admin (gruhani214@gmail.com) with notification

```
REQUEST:
{
  "orderId": 12345,
  "deliveryTime": "Today at 8:00 PM",
  "deliveryMethod": "SELLER_DELIVERY",
  "deliveryNotes": "Ring bell twice, gate is red"
}

RESPONSE:
{
  "success": true,
  "orderId": 12345,
  "status": "ACCEPTED",
  "otp": "482915",
  "message": "Order accepted! OTP sent to customer."
}

EMAIL TO CUSTOMER (gruhani214+test@gmail.com):
Subject: Your Gruhini Order #12345 is Being Prepared! 🍳
Body:
- Order has been accepted by [Seller Name]
- Delivery Time: [deliveryTime]
- Delivery Method: [deliveryMethod]
- Your OTP: 482915 (6 digits)
- Seller Contact: [Phone]
- Message: "Please provide this OTP when receiving the order"
```

---

### **4. SELLER REJECT ORDER**
**Endpoint:** `POST /seller/reject-order`
**Location:** Seller_dashboard_controls.java
**Expected Behavior:**
- Update order status to `REJECTED`
- Save rejection reason
- Send email to customer with rejection reason
- Notify seller dashboard

```
REQUEST:
{
  "orderId": 12345,
  "rejectionReason": "Stock खत्म हो गया, माफ कीजिए"
}

RESPONSE:
{
  "success": true,
  "orderId": 12345,
  "status": "REJECTED",
  "message": "Order rejected. Customer notified."
}

EMAIL TO CUSTOMER:
Subject: Your Gruhini Order #12345 Could Not Be Accepted ❌
Body:
- Reason: [rejectionReason]
- Alternative: "You can contact the seller directly: [Phone]"
```

---

### **5. VERIFY OTP & DELIVER**
**Endpoint:** `POST /seller/verify-otp?orderId={orderId}&otp={otp}`
**Location:** Seller_dashboard_controls.java
**Expected Behavior:**
- Compare OTP with database
- If match: Update status to `DELIVERED`
- Send email to customer: "Your order delivered!" + Order summary
- Return success/failure

```
REQUEST:
GET /seller/verify-otp?orderId=12345&otp=482915

RESPONSE (Success):
{
  "success": true,
  "message": "OTP verified! Order delivered.",
  "status": "DELIVERED"
}

RESPONSE (Failure):
{
  "success": false,
  "message": "Invalid OTP. Please try again."
}

EMAIL TO CUSTOMER (on success):
Subject: ✅ Your Gruhini Order #12345 Delivered!
Body:
- Order has been delivered
- Total paid: ₹574
- Thank you messages
```

---

### **6. RESEND OTP**
**Endpoint:** `POST /orders/{orderId}/resend-otp`
**Expected Behavior:**
- Fetch existing OTP from database
- Resend via email to customer
- Return success message

```
RESPONSE:
{
  "success": true,
  "message": "OTP resent to customer@example.com"
}
```

---

### **7. SELLER LOAD ORDERS (ALREADY EXISTS)**
**Endpoint:** `GET /seller/view-order-seller?orderStatus={status}`
**Current Implementation:** ✅ Already in place
**Note:** System expects `orderStatus` field in response with values: PENDING, ACCEPTED, REJECTED, DELIVERED

---

## 📧 EMAIL TRIGGERS (Backend Implementation)

### **When User Places Order:**
- **To:** Seller Email
- **Subject:** 🍳 New Order Received! #[OrderID]
- **Content:** Order details, items, delivery address, customer contact

### **When Seller Accepts Order:**
- **To:** Customer Email
- **Subject:** ✅ Your Order is Being Prepared! #[OrderID]
- **Content:** Delivery time, method, seller contact, OTP, instructions

### **When Seller Rejects Order:**
- **To:** Customer Email
- **Subject:** ❌ Order Could Not Be Accepted
- **Content:** Rejection reason, alternative products/suggestions

### **When Seller Adds/Edits/Deletes Product:**
- **To:** Admin (gruhani214@gmail.com)
- **Subject:** [ADMIN] New Catalog Update from [SellerName]
- **Content:** Action type (Add/Edit/Delete), product details, seller name, approval link

### **When OTP Verified & Delivered:**
- **To:** Customer Email
- **Subject:** ✅ Your Order Delivered!
- **Content:** Order summary, total amount, thank you message, feedback link

---

## 🔑 KEY BUSINESS RULES

1. **Order Creation:**
   - Status starts as `PENDING`
   - Cart items must be cleared after order creation
   - Delivery fee = ₹49 (configurable)
   - Tax rate = 5% (configurable)

2. **Seller Accept Flow:**
   - Must include delivery time and method
   - OTP must be 6 digits (random)
   - OTP sent to customer ONLY when seller accepts
   - Email to customer is CRITICAL for payment instruction

3. **Payment on Delivery:**
   - No prepayment required
   - Customer pays seller during delivery
   - OTP prevents unauthorized claims

4. **Admin Notifications:**
   - Admin email: gruhani214@gmail.com
   - Admin phone: +91 9131206200
   - Seller must be able to contact admin for issues

---

## 🔄 COMPLETE FLOW DIAGRAM

```
USER:
1. Add to Cart
        ↓
2. Checkout Page
   - Add/Select Address
   - Click "Place Order"
        ↓
3. Backend: POST /place-order
   - Create Order (PENDING)
   - Email to Seller
        ↓
4. Redirect to order-status.html?orderId=12345
        ↓
5. Order Status Page (Polling every 5s)
   - Show: "Waiting for seller acceptance"
   - Show: Seller contact details
   - Show: OTP box (hidden until accepted)

SELLER:
1. See New Order in Dashboard
   - Shows: Items, Customer Info, Delivery Address
   - Buttons: "Accept" / "Reject"
        ↓
2. Click "Accept" 
   - Opens Modal: "Accept Order"
   - Must enter: Delivery Time + Delivery Method
        ↓
3. Backend: POST /seller/accept-order
   - Generate OTP
   - Email to Customer (with OTP!)
   - Email to Admin
   - Update Status to ACCEPTED

CUSTOMER (on Polling):
1. Page Auto-Refreshes (GET /orders/{id})
   - Status changes to ACCEPTED ✅
   - OTP revealed: 482915
   - Delivery time shown
   - Delivery method shown
        ↓
2. Shares OTP with Seller (via call/message)

SELLER (Delivery):
1. Delivers food to customer
2. Gets OTP from customer
3. Enters OTP in Seller Dashboard
4. Clicks "Verify OTP & Deliver"
        ↓
5. Backend: POST /seller/verify-otp
   - Match OTP
   - Update Status to DELIVERED
   - Send confirmation email to customer
```

---

## 🛠️ TESTING CHECKLIST

- [ ] Place order and verify PENDING status
- [ ] Seller accept with delivery time/method
- [ ] Customer receives OTP email
- [ ] OTP displays on order-status.html
- [ ] Wrong OTP verification fails
- [ ] Correct OTP marks as DELIVERED
- [ ] Admin receives seller action emails
- [ ] Seller rejection sends email to customer
- [ ] Resend OTP works correctly
- [ ] Multi-language support (Hindi/English)

---

## 📝 NOTES FOR DEVELOPMENT

1. **Database Changes Needed:**
   - Add `deliveryTime` field to Orders table
   - Add `deliveryMethod` field to Orders table
   - Add `otp` field to Orders table
   - Add `rejectionReason` field to Orders table
   - Update status enum: PENDING, ACCEPTED, REJECTED, DELIVERED, CANCELLED

2. **Email Service:**
   - Configure with Spring Mail (Java Backend)
   - Use HTML templates for professional emails
   - Add retry logic (3 attempts for failed emails)

3. **Security:**
   - OTP must be hashed before storage
   - OTP valid for 1 hour
   - Rate limit OTP resend (max 3 per 10 minutes)
   - Validate authorization on all seller actions

4. **Frontend Dependencies:**
   - order-status.html ← Polling, displays OTP
   - checkout.html ← Now uses /place-order instead of Razorpay
   - seller-dashboard-enhanced.html ← Accept/reject modals

---

**Status:** 🔴 **CRITICAL** - These endpoints MUST be implemented for app to function
**Last Updated:** March 26, 2024
**Frontend Ready:** ✅ Yes
**Backend Ready:** ⏳ Awaiting Implementation
