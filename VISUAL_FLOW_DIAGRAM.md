# 🎯 Trust-First Order System - Visual Flow Diagram

## Complete Order Flow (User → Seller → Completion)

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CUSTOMER JOURNEY                             │
└─────────────────────────────────────────────────────────────────────┘

1️⃣  CUSTOMER PLACES ORDER
    ├─ Selects address in checkout.html
    ├─ Reviews items & total (COD - Pay on Delivery)
    ├─ Clicks "Place Order"
    └─ POST /place-order → {orderId: 101, status: PENDING}

    📧 EMAIL SENT TO SELLER: "🔥 नया ऑर्डर आ गया!"
       └─ Order ID, Customer name/phone, Items, Delivery address

⏳  ORDER PENDING STATE (order-status.html)
    ├─ Page auto-loads with orderId from URL
    ├─ Polling starts: GET /orders/{orderId} every 5 seconds
    ├─ Shows: 🟡 PENDING status, order items, seller contact
    └─ No OTP yet (will appear after acceptance)

    👨‍💼 SELLER VIEWS IN DASHBOARD
       ├─ Sees new order in dashboard
       └─ Options: Accept or Reject

┌─────────────────────────────────────────────────────────────────────┐
│                    🔀 TWO POSSIBLE PATHS                             │
└─────────────────────────────────────────────────────────────────────┘

═══════════════════════════════════════════════════════════════════════
PATH A: SELLER ACCEPTS ORDER ✅
═══════════════════════════════════════════════════════════════════════

2A️⃣  SELLER ACCEPTS WITH DELIVERY DETAILS
     ├─ Opens "Accept Order" modal
     ├─ Enters delivery details:
     │  ├─ Delivery Time: "Today at 8:00 PM"
     │  ├─ Delivery Method: SELLER_DELIVERY / CUSTOMER_PICKUP / THIRD_PARTY
     │  └─ Delivery Notes: "Ring doorbell twice"
     ├─ Clicks "Confirm Accept"
     └─ POST /seller/accept-order → {orderId, deliveryTime, method, notes}

     🔐 BACKEND GENERATES OTP
        ├─ Generates 6-digit random: 847291
        ├─ Hashes OTP (BCrypt): $2y$10$...
        └─ Stores in Orders table

     📧 EMAIL 1 SENT TO CUSTOMER: "✅ Order Accepted!"
        ├─ Seller name & contact
        ├─ Delivery time: Today at 8:00 PM
        ├─ Delivery method: Seller delivers
        ├─ 🔐 YOUR OTP: 847291
        └─ Total: ₹696.80 (COD)

     📧 EMAIL 2 SENT TO ADMIN: "📦 Order Accepted - Order #101"
        ├─ Full order details
        ├─ Seller: Manju's Kitchen
        └─ Status update at 2024-01-15 14:35 UTC

✅  ORDER ACCEPTED STATE (order-status.html auto-updates)
     ├─ Polling detects status change to ACCEPTED
     ├─ Status bar: 🟢 ACCEPTED (with animation)
     ├─ OTP Section appears: 847291 (blurred until clicked)
     ├─ Delivery Info shows:
     │  ├─ Seller: Manju's Kitchen | 9876543210
     │  ├─ Time: Today at 8:00 PM
     │  └─ Method: Seller Delivery
     ├─ Buttons appear:
     │  ├─ Copy OTP
     │  └─ Resend OTP
     └─ Admin contact visible at bottom

🔄  OTP RESEND (if customer didn't receive email)
     ├─ Customer clicks "Resend OTP"
     └─ POST /orders/{orderId}/resend-otp
        ├─ Backend sends OTP email again
        └─ Rate limited (max 1 per 5 minutes)

         📧 EMAIL SENT TO CUSTOMER: "🔐 आपका OTP दोबारा भेजा गया"
            └─ OTP: 847291

🚚  DELIVERY STARTS
     ├─ Seller prepares and delivers food
     └─ Seller arrives at customer location with OTP request

3A️⃣  SELLER VERIFIES OTP ON DASHBOARD
     ├─ Goes to seller dashboard
     ├─ Finds order #101 in "Accepted Orders"
     ├─ Sees "Verify OTP" button
     ├─ Clicks and enters OTP: 847291
     ├─ Backend compares hashes (OTP matches ✅)
     ├─ Rate limit check passes (only 1st attempt)
     ├─ Post /seller/verify-otp (orderId=101, otp=847291)
     └─ Update order: status = DELIVERED

     📧 EMAIL 1 SENT TO CUSTOMER: "✅ डिलीवरी पूरी हुई!"
        ├─ Thank you message
        ├─ Order recap
        ├─ Seller contact for feedback
        └─ Rating/review link

     📧 EMAIL 2 SENT TO ADMIN: "✅ Delivery Verified - Order #101"
        ├─ Delivery completed at 2024-01-15 20:35 UTC
        ├─ Seller: Manju's Kitchen
        └─ Customer: Name, Phone

✅✅ ORDER COMPLETED (order-status.html final state)
     ├─ Status bar: 🏁 DELIVERED (with checkmark)
     ├─ Polling stops (order is final)
     ├─ All buttons disabled
     ├─ "Thank You" message appears
     └─ Seller contact still visible for feedback

═══════════════════════════════════════════════════════════════════════
PATH B: SELLER REJECTS ORDER ❌
═══════════════════════════════════════════════════════════════════════

2B️⃣  SELLER REJECTS ORDER
     ├─ Opens "Reject Order" modal (existing flow)
     ├─ Enters reason: "Out of ingredients"
     └─ POST /seller/reject-order → {orderId, reason}

     📧 EMAIL SENT TO CUSTOMER: "❌ Order Rejected"
        ├─ Seller: Manju's Kitchen
        ├─ Reason: Out of ingredients
        ├─ "We apologize for the inconvenience"
        └─ Link to similar sellers/dishes

❌  ORDER REJECTED STATE (order-status.html auto-updates)
     ├─ Polling detects status change to REJECTED
     ├─ Status bar: 🔴 REJECTED
     ├─ OTP section hidden
     ├─ Delivery info hidden
     ├─ Alternative sellers section shown
     └─ "Place New Order?" button

═══════════════════════════════════════════════════════════════════════

┌─────────────────────────────────────────────────────────────────────┐
│                   POLLING MECHANISM EXPLAINED                        │
└─────────────────────────────────────────────────────────────────────┘

Every 5 seconds on order-status.html:

    GET /orders/101
       ↓
    Backend returns current order state
       ↓
    Frontend checks if status changed
       ↓
    YES: Re-render UI with new status/OTP/delivery info ✨
    NO: Do nothing (keeps page fresh)
       ↓
    When status = DELIVERED: Stop polling

Cost: ~1 API call per 5 seconds per customer viewing order page
Benefit: Real-time updates without page refresh


┌─────────────────────────────────────────────────────────────────────┐
│                   EMAIL TRIGGER MATRIX (9 Total)                    │
└─────────────────────────────────────────────────────────────────────┘

Trigger Event              │ Email To      │ Email Quantity │ When
─────────────────────────────────────────────────────────────────────
1. Order Placed           │ Seller        │ 1              │ POST /place-order
2. Order Accepted         │ Customer      │ 1 (WITH OTP!)  │ POST /seller/accept-order
3. Order Accepted         │ Admin         │ 1              │ POST /seller/accept-order
4. Order Rejected         │ Customer      │ 1              │ POST /seller/reject-order
5. OTP Resent             │ Customer      │ 1              │ POST /orders/{id}/resend-otp
6. Delivery Verified      │ Customer      │ 1              │ POST /seller/verify-otp
7. Delivery Verified      │ Admin         │ 1              │ POST /seller/verify-otp
8. Product Updated        │ Admin         │ 1              │ Seller adds/edits/deletes
9. Support Inquiry        │ Admin         │ 1              │ From contact form

TOTAL EMAIL LOAD: ~2-3 emails per successful order


┌─────────────────────────────────────────────────────────────────────┐
│               ORDER STATE MACHINE (Backend Logic)                    │
└─────────────────────────────────────────────────────────────────────┘

┌────────────┐
│  PENDING   │  ← Order created, awaiting seller action
└────┬───────┘
     │
     ├─→ POST /seller/accept-order ──→ ┌──────────┐
     │                                   │ACCEPTED  │  ← OTP generated & sent
     │                                   └────┬─────┘
     │                                        │
     │                                        └─→ POST /seller/verify-otp ──→ ┌───────────┐
     │                                                                        │ DELIVERED │
     │                                                                        └───────────┘
     │
     └─→ POST /seller/reject-order ──→ ┌──────────┐
                                        │ REJECTED │  ← Cannot be undone
                                        └──────────┘

Valid Transitions:
✅ PENDING → ACCEPTED
✅ PENDING → REJECTED
✅ ACCEPTED → DELIVERED
❌ REJECTED → ANYTHING (final state)
❌ DELIVERED → ANYTHING (final state)
❌ Skip states (e.g., PENDING → DELIVERED without ACCEPTED)


┌─────────────────────────────────────────────────────────────────────┐
│                    FILE STRUCTURE & DEPENDENCIES                    │
└─────────────────────────────────────────────────────────────────────┘

checkout.html
  ├─ config.js (BASE_URL, token())
  ├─ js/utils.js (potential utility functions)
  └─ Endpoint: POST /place-order
     └─ Returns: {orderId, status}

order-status.html (NEW - 800 lines)
  ├─ config.js (BASE_URL, token())
  ├─ js/order-flow-utils.js (NEW)
  └─ Endpoints:
     ├─ GET /orders/{orderId}            [every 5 sec polling]
     └─ POST /orders/{orderId}/resend-otp [on user click]

seller-dashboard-enhanced.html
  ├─ config.js (BASE_URL, token())
  ├─ Existing endpoints (unchanged)
  └─ New endpoints:
     ├─ POST /seller/accept-order      [modal form submit]
     └─ POST /seller/reject-order      [existing flow]
     └─ POST /seller/verify-otp        [existing flow]

seller-profile-modern.html
  ├─ config.js (BASE_URL, token())
  ├─ No new endpoints
  └─ Display only (admin contact added)

js/order-flow-utils.js (NEW - 300 lines)
  ├─ Utility functions for all order operations
  ├─ Export: All functions as module
  └─ Used by: checkout.html, order-status.html, seller-dashboard


┌─────────────────────────────────────────────────────────────────────┐
│                   SECURITY & FRAUD PREVENTION                       │
└─────────────────────────────────────────────────────────────────────┘

Protection Layer 1: OTP
  ├─ 6-digit random code prevents unauthorized claims
  ├─ Hashed storage (BCrypt) prevents database breach impact
  ├─ 24-hour expiration prevents reuse
  └─ Only sent via email (not displayed in API) initially

Protection Layer 2: Authorization
  ├─ Seller can only accept/reject/verify own orders (verify seller_id)
  ├─ Customer can only view own orders (verify customer_id)
  ├─ Admin can view all orders (role-based)
  └─ JWT tokens validate all requests

Protection Layer 3: Rate Limiting
  ├─ OTP verification: Max 3 attempts per order
  ├─ OTP resend: Max 1 per 5 minutes per order
  ├─ Email sending: Rate limit by recipient
  └─ Polling: Reasonable interval (5 sec) + backend caching

Protection Layer 4: Data Validation
  ├─ Address validation (pincode format, phone format)
  ├─ OTP format validation (6 digits only)
  ├─ Delivery method validation (enum from list)
  ├─ Timestamp validation (prevent future times)
  └─ SQL injection prevention via parameterized queries


┌─────────────────────────────────────────────────────────────────────┐
│                    SUCCESS METRICS TO TRACK                         │
└─────────────────────────────────────────────────────────────────────┘

Metric                      │ Target    │ Formula
─────────────────────────────────────────────────────────────────────
Order Placement Success     │ 100%      │ Successful / Total Attempts
Seller Acceptance Rate      │ >80%      │ Accepted / Not Rejected
Average Acceptance Time     │ <30 min   │ (Accepted Time - Placed Time)
OTP Delivery Success        │ 99%+      │ Delivered / Sent
OTP Verification Success    │ >95%      │ Correct OTP / Total Attempts
Customer Satisfaction       │ 4.5+ ⭐   │ Rating from feedback form
Email Reliability           │ 99%+      │ Delivered / Sent
Fraud Prevention Rate       │ 100%      │ Invalid OTP rejected


┌─────────────────────────────────────────────────────────────────────┐
│                    KEY DIFFERENCES FROM RAZORPAY                    │
└─────────────────────────────────────────────────────────────────────┘

BEFORE (Razorpay):              VS    AFTER (Trust-First)
─────────────────────────────────────────────────────────────────────
Prepayment required        │        Cash on Delivery (COD)
Payment gateway friction   │        Direct order creation
$$ Payment processing      │        Free (backend email only)
Hidden seller contact      │        Visible seller contact
Order success page static  │        Real-time polling updates
No seller confirmation     │        Seller accepts with details
Customer info not shared   │        Address shared with seller
No fraud prevention        │        OTP verification for delivery
Hard to refund             │        Simple order cancellation
Global payment limits      │        Any payment method (include COD)


═════════════════════════════════════════════════════════════════════════════
                               STATUS SUMMARY
═════════════════════════════════════════════════════════════════════════════

✅ FRONTEND: 100% COMPLETE
   ├─ checkout.html ........................... Ready
   ├─ order-status.html ....................... Ready (NEW)
   ├─ seller-dashboard-enhanced.html ......... Ready
   ├─ seller-profile-modern.html ............. Ready
   └─ js/order-flow-utils.js ................. Ready (NEW)

⏳ BACKEND: 0% COMPLETE
   ├─ POST /place-order ....................... Not Started
   ├─ GET /orders/{orderId} ................... Not Started
   ├─ POST /seller/accept-order ............... Not Started
   ├─ POST /seller/reject-order ............... Not Started
   ├─ POST /seller/verify-otp ................. Not Started
   └─ POST /orders/{orderId}/resend-otp ...... Not Started

📊 DATABASE: 0% COMPLETE
   └─ Add 4 columns to orders table .......... Not Started

📧 EMAIL SERVICE: 0% COMPLETE
   ├─ Spring Mail Configuration ............. Not Started
   └─ Email Templates (6 scenarios) ......... Not Started

═════════════════════════════════════════════════════════════════════════════

This is a trust-first marketplace system optimized for Tier-2/Tier-3 Indian markets.
No payment processing needed. Just orders, emails, and OTP verification.

Ready for backend implementation! 🚀
