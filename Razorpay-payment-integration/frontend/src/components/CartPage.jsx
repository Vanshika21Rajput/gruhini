import React, { useState, useEffect, useMemo } from 'react';
import { ShoppingBag, Minus, Plus, ArrowRight, Trash2, AlertCircle, ChefHat, X, CheckCircle, Shield } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

// --- 1. MICRO-COMPONENT: TOAST NOTIFICATION (Google Style) ---
const Toast = ({ message, type, onClose }) => (
    <div className={`fixed bottom-5 left-1/2 transform -translate-x-1/2 px-6 py-3 rounded-full shadow-2xl flex items-center gap-3 z-50 animate-slide-up ${type === 'error' ? 'bg-red-900 text-white' : 'bg-gray-900 text-white'
        }`}>
        {type === 'error' ? <AlertCircle size={18} /> : <CheckCircle size={18} />}
        <span className="text-sm font-medium tracking-wide">{message}</span>
    </div>
);

// --- 2. MICRO-COMPONENT: SKELETON LOADER (Premium Feel) ---
const CartSkeleton = () => (
    <div className="max-w-2xl mx-auto p-6 space-y-6 animate-pulse">
        <div className="h-8 bg-gray-200 rounded w-1/3 mb-8"></div>
        {[1, 2].map(i => (
            <div key={i} className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
                <div className="h-6 bg-gray-200 rounded w-1/4 mb-4"></div>
                <div className="space-y-4">
                    <div className="h-16 bg-gray-100 rounded-xl w-full"></div>
                    <div className="h-16 bg-gray-100 rounded-xl w-full"></div>
                </div>
            </div>
        ))}
    </div>
);

const CartPage = () => {
    const navigate = useNavigate();
    const [cartData, setCartData] = useState({ items: [], totalAmount: 0, deliveryFee: 0, finalTotal: 0 });
    const [loading, setLoading] = useState(true);
    const [toast, setToast] = useState(null); // { message, type }

    // Show Toast Helper
    const showToast = (msg, type = 'info') => {
        setToast({ message: msg, type });
        setTimeout(() => setToast(null), 3000);
    };

    const fetchCart = async (isBackground = false) => {
        if (!isBackground) setLoading(true);
        const token = localStorage.getItem('authToken');
        if (!token) {
            showToast("Please login to view cart", 'error');
            setLoading(false);
            return;
        }

        try {
            const res = await fetch(`${API_BASE_URL}/get-cart`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (res.status === 401) throw new Error("Unauthorized");
            const data = await res.json();
            setCartData(data);
        } catch (err) {
            showToast("Could not load cart", 'error');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCart();
    }, []);

    // --- OPTIMISTIC UI UPDATE (The "Google" Speed) ---
    const updateQty = async (productId, delta) => {
        const token = localStorage.getItem('authToken');
        if (!token) return;

        // 1. Optimistic Update (Update UI immediately before Server responds)
        const oldCart = { ...cartData };
        const updatedItems = cartData.items.map(item => {
            if (item.productid === productId || item.id === productId) {
                return { ...item, quantity: Math.max(0, item.quantity + delta) };
            }
            return item;
        }).filter(item => item.quantity > 0);

        // Recalc totals locally for instant feedback
        const newTotal = updatedItems.reduce((acc, item) => acc + (item.price * item.quantity), 0);
        setCartData({
            ...cartData,
            items: updatedItems,
            totalAmount: newTotal,
            finalTotal: newTotal + (cartData.deliveryFee || 0)
        });

        // 2. Sync with Server
        try {
            const res = await fetch(`${API_BASE_URL}/add-to-cart`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ productid: productId, quantity: delta })
            });
            if (!res.ok) throw new Error("Failed");
            // Optional: fetchCart(true) to ensure server sync is perfect
        } catch (err) {
            // Revert on error
            setCartData(oldCart);
            showToast("Connection failed. Reverting.", 'error');
        }
    };

    // --- LOGIC: GROUPING (Task 2) ---
    const groupedItems = useMemo(() => {
        return cartData.items.reduce((groups, item) => {
            const chef = item.chef || item.seller || "Gruhini Kitchen";
            if (!groups[chef]) groups[chef] = [];
            groups[chef].push(item);
            return groups;
        }, {});
    }, [cartData.items]);

    // --- LOGIC: CHECKOUT (Task 1) ---
    const handleProceed = () => {
        const validItems = cartData.items.filter(item => {
            const stock = item.stock !== undefined ? item.stock : 999;
            return stock > 0;
        });

        const oosCount = cartData.items.length - validItems.length;

        // Custom "Confirm" Logic could go here (Modal), using standard alert for brevity but logic is sound
        if (oosCount > 0) {
            // Ideally, use a custom modal here instead of confirm()
            if (!window.confirm(`${oosCount} items are sold out and will be removed. Proceed?`)) return;
        }

        if (validItems.length === 0) {
            showToast("Your cart is empty or all items are out of stock.", 'error');
            return;
        }

        // Backend Splitting Logic: We send ALL valid items. 
        // The Backend Order Controller will loop through this list and create multiple sub-orders.
        const payableAmount = validItems.reduce((sum, item) => sum + (item.price * item.quantity), 0) + (cartData.deliveryFee || 0);

        navigate('/payment', {
            state: {
                amount: payableAmount,
                cartId: cartData.id,
                items: validItems // OOS items removed
            }
        });
    };

    if (loading) return <CartSkeleton />;

    return (
        <div className="min-h-screen bg-[#fafafa] font-sans pb-20 selection:bg-orange-100">

            {/* Toast Container */}
            {toast && <Toast message={toast.message} type={toast.type} onClose={() => setToast(null)} />}

            <div className="max-w-2xl mx-auto px-4 py-8">

                {/* Header */}
                <div className="flex items-center gap-4 mb-8">
                    <div className="bg-amber-900/10 p-3 rounded-xl text-amber-900">
                        <ShoppingBag size={24} strokeWidth={2.5} />
                    </div>
                    <div>
                        <h1 className="font-serif text-2xl text-gray-900 font-bold tracking-tight">Your Cart</h1>
                        <p className="text-xs text-gray-500 font-medium uppercase tracking-wider">{cartData.items.length} Items • Royal Delivery</p>
                    </div>
                </div>

                {/* Empty State */}
                {cartData.items.length === 0 ? (
                    <div className="text-center py-20 bg-white rounded-3xl border border-dashed border-gray-300">
                        <div className="w-20 h-20 bg-gray-50 rounded-full flex items-center justify-center mx-auto mb-4">
                            <ShoppingBag size={32} className="text-gray-300" />
                        </div>
                        <h3 className="text-lg font-serif text-gray-800 mb-2">Your royal plate is empty</h3>
                        <p className="text-gray-500 text-sm mb-6">Explore our chefs' specials today.</p>
                        <button onClick={() => navigate('/')} className="px-8 py-3 bg-amber-900 text-white rounded-full font-bold text-sm hover:bg-amber-800 transition-colors shadow-lg shadow-amber-900/20">
                            Browse Menu
                        </button>
                    </div>
                ) : (
                    <div className="space-y-6">

                        {/* --- SELLER GROUPS (Task 2) --- */}
                        {Object.keys(groupedItems).map(chef => (
                            <div key={chef} className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                                {/* Chef Header */}
                                <div className="bg-orange-50/50 px-6 py-3 border-b border-orange-100 flex items-center gap-2">
                                    <ChefHat size={16} className="text-amber-700" />
                                    <span className="font-serif text-amber-900 font-bold text-sm">Kitchen of {chef}</span>
                                </div>

                                {/* Items List */}
                                <div className="divide-y divide-gray-50">
                                    {groupedItems[chef].map(item => {
                                        const isOOS = (item.stock !== undefined && item.stock <= 0);
                                        return (
                                            <div key={item.id} className={`p-5 flex items-center justify-between transition-all ${isOOS ? 'bg-gray-50 opacity-60 grayscale' : 'hover:bg-gray-50/50'}`}>

                                                {/* Item Details */}
                                                <div className="flex-1 pr-4">
                                                    <div className="flex items-center gap-2 mb-1">
                                                        {isOOS && <span className="bg-red-100 text-red-700 text-[10px] font-bold px-2 py-0.5 rounded">SOLD OUT</span>}
                                                        <h3 className={`font-medium text-gray-900 ${isOOS ? 'line-through decoration-gray-400' : ''}`}>
                                                            {item.productName}
                                                        </h3>
                                                    </div>
                                                    <p className="text-sm text-gray-500 font-medium">₹{item.price}</p>
                                                </div>

                                                {/* Controls */}
                                                <div className="flex items-center gap-3">
                                                    {isOOS ? (
                                                        <button onClick={() => updateQty(item.productid || item.id, -item.quantity)} className="text-xs text-red-500 font-bold hover:underline flex items-center gap-1">
                                                            <Trash2 size={12} /> Remove
                                                        </button>
                                                    ) : (
                                                        <div className="flex items-center bg-white border border-gray-200 rounded-full shadow-sm h-9">
                                                            <button
                                                                onClick={() => updateQty(item.productid || item.id, -1)}
                                                                className="w-8 h-full flex items-center justify-center text-gray-500 hover:text-amber-700 active:scale-90 transition-transform"
                                                            >
                                                                <Minus size={14} />
                                                            </button>
                                                            <span className="w-6 text-center text-sm font-bold text-gray-800">{item.quantity}</span>
                                                            <button
                                                                onClick={() => updateQty(item.productid || item.id, 1)}
                                                                className="w-8 h-full flex items-center justify-center text-gray-500 hover:text-amber-700 active:scale-90 transition-transform"
                                                            >
                                                                <Plus size={14} />
                                                            </button>
                                                        </div>
                                                    )}
                                                </div>
                                            </div>
                                        );
                                    })}
                                </div>
                            </div>
                        ))}

                        {/* --- BILLING SUMMARY --- */}
                        <div className="bg-white rounded-2xl p-6 shadow-xl shadow-gray-200/50 border border-gray-100">
                            <div className="space-y-3 mb-6">
                                <div className="flex justify-between text-gray-500 text-sm">
                                    <span>Subtotal</span>
                                    <span>₹{cartData.totalAmount}</span>
                                </div>
                                <div className="flex justify-between text-gray-500 text-sm">
                                    <span>Delivery & Packaging</span>
                                    <span>₹{cartData.deliveryFee || 0}</span>
                                </div>
                                <div className="h-px bg-gray-100 my-4"></div>
                                <div className="flex justify-between items-center">
                                    <span className="font-serif text-lg text-gray-900 font-bold">To Pay</span>
                                    <span className="font-serif text-2xl text-amber-700 font-bold">₹{cartData.finalTotal}</span>
                                </div>
                            </div>

                            <button
                                onClick={handleProceed}
                                className="group w-full py-4 bg-gradient-to-r from-amber-800 to-amber-900 text-white rounded-xl font-bold text-lg shadow-lg shadow-amber-900/30 hover:shadow-amber-900/40 transform hover:-translate-y-0.5 transition-all flex items-center justify-center gap-2"
                            >
                                Proceed to Checkout
                                <ArrowRight size={20} className="group-hover:translate-x-1 transition-transform" />
                            </button>

                            <div className="mt-4 flex items-center justify-center gap-2 text-xs text-gray-400 font-medium">
                                <Shield size={12} />
                                <span>100% Secure Payments by Razorpay</span>
                            </div>
                        </div>

                    </div>
                )}
            </div>

            {/* Global Styles for Animations */}
            <style>{`
        @keyframes slide-up {
          from { transform: translate(-50%, 100%); opacity: 0; }
          to { transform: translate(-50%, 0); opacity: 1; }
        }
        .animate-slide-up {
          animation: slide-up 0.3s cubic-bezier(0.16, 1, 0.3, 1);
        }
      `}</style>
        </div>
    );
};

export default CartPage;
