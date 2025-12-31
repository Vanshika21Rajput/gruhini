import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Shield, Lock, CreditCard, Smartphone, CheckCircle, ArrowLeft, AlertCircle, Wallet } from 'lucide-react';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'https://gruhini2.onrender.com';

// --- MICRO-COMPONENTS ---
const Toast = ({ message, type }) => (
    <div className={`fixed bottom-5 left-1/2 transform -translate-x-1/2 px-6 py-3 rounded-full shadow-2xl flex items-center gap-3 z-50 animate-bounce-up ${type === 'error' ? 'bg-red-900 text-white' : 'bg-emerald-900 text-white'
        }`}>
        {type === 'error' ? <AlertCircle size={18} /> : <CheckCircle size={18} />}
        <span className="text-sm font-medium tracking-wide">{message}</span>
    </div>
);

const VerificationStep = ({ label, status, index }) => {
    const isCompleted = status === 'completed';
    const isProcessing = status === 'processing';

    return (
        <div className={`flex items-center gap-4 p-3 rounded-xl transition-all duration-500 ${isProcessing ? 'bg-amber-50 scale-105' : ''}`}>
            <div className={`w-8 h-8 rounded-full flex items-center justify-center transition-colors duration-500 ${isCompleted ? 'bg-green-500 text-white' :
                isProcessing ? 'bg-amber-500 text-white animate-pulse' : 'bg-gray-100 text-gray-400'
                }`}>
                {isCompleted ? <CheckCircle size={16} /> : <span className="text-xs font-bold">{index + 1}</span>}
            </div>
            <span className={`text-sm font-medium ${isCompleted ? 'text-gray-900' : isProcessing ? 'text-amber-800' : 'text-gray-400'}`}>
                {label}
            </span>
            {isProcessing && <div className="ml-auto w-4 h-4 border-2 border-amber-500 border-t-transparent rounded-full animate-spin" />}
        </div>
    );
};

const PremiumPaymentUI = () => {
    const navigate = useNavigate();
    const location = useLocation();

    // STATE
    const [backendAmount, setBackendAmount] = useState(null);
    const [cartItems, setCartItems] = useState([]);
    const [loadingCart, setLoadingCart] = useState(true);
    const [selectedMethod, setSelectedMethod] = useState('upi');
    const [paymentStatus, setPaymentStatus] = useState('idle'); // idle, verifying, success
    const [steps, setSteps] = useState([
        { id: 1, label: 'Secure Handshake', status: 'pending' },
        { id: 2, label: 'Verifying Signature', status: 'pending' },
        { id: 3, label: 'Checking Stock', status: 'pending' },
        { id: 4, label: 'Confirming Payment', status: 'pending' },
        { id: 5, label: 'Updating Kitchen', status: 'pending' }
    ]);
    const [toast, setToast] = useState(null);

    // Initial Load Check
    useEffect(() => {
        if (!location.state?.amount) {
            navigate('/', { replace: true });
            return;
        }
        fetchCartDetails();
    }, []);

    const fetchCartDetails = async () => {
        const token = localStorage.getItem('authToken');
        try {
            const res = await fetch(`${API_BASE_URL}/get-cart`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = await res.json();
            setCartItems(data.items || []);
            setBackendAmount(data.totalAmount);
        } catch (err) {
            console.error("Cart fetch error", err);
        } finally {
            setLoadingCart(false);
        }
    };

    const paymentAmount = location.state?.amount || 0;
    const cartId = location.state?.cartId;

    const simulateVerification = async () => {
        setPaymentStatus('verifying');

        try {
            // 1. Real API Call
            const response = await fetch(`${API_BASE_URL}/create-order`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ amount: paymentAmount, cartId, currency: "INR" })
            });

            if (!response.ok) throw new Error("Gateway Rejected Order");

            // 2. Simulate Steps
            for (let i = 0; i < steps.length; i++) {
                setSteps(prev => prev.map((s, idx) => idx === i ? { ...s, status: 'processing' } : s));
                await new Promise(r => setTimeout(r, 800)); // Cinematic Delay
                setSteps(prev => prev.map((s, idx) => idx === i ? { ...s, status: 'completed' } : s));
            }

            setPaymentStatus('success');

        } catch (error) {
            setToast({ message: "Payment Failed. Try Again.", type: 'error' });
            setPaymentStatus('idle');
            setSteps(prev => prev.map(s => ({ ...s, status: 'pending' }))); // Reset
        }
    };

    if (paymentStatus === 'success') {
        return (
            <div className="min-h-screen bg-emerald-900 flex items-center justify-center text-center p-6 animate-fade-in">
                <div className="bg-white p-10 rounded-3xl shadow-2xl max-w-md w-full">
                    <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6 text-green-600">
                        <CheckCircle size={40} />
                    </div>
                    <h1 className="font-serif text-3xl font-bold text-gray-900 mb-2">Order Confirmed!</h1>
                    <p className="text-gray-500 mb-8">Your royal feast is being prepared.</p>
                    <button
                        onClick={() => window.location.href = '/menu.html'}
                        className="w-full py-4 bg-gray-900 text-white rounded-xl font-bold hover:bg-black transition-colors"
                    >
                        Return to Menu
                    </button>
                </div>
            </div>
        );
    }

    if (paymentStatus === 'verifying') {
        return (
            <div className="min-h-screen bg-gray-900 flex items-center justify-center p-6">
                <div className="bg-white p-8 rounded-3xl max-w-sm w-full shadow-2xl">
                    <div className="mb-6 text-center border-b border-gray-100 pb-6">
                        <h2 className="font-serif text-xl font-bold text-gray-900 mb-1">Processing Payment</h2>
                        <p className="text-xs text-gray-400 uppercase tracking-widest">Do not close window</p>
                    </div>
                    <div className="space-y-2">
                        {steps.map((step, i) => (
                            <VerificationStep key={step.id} index={i} {...step} />
                        ))}
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 font-sans text-gray-900 pb-20">
            {toast && <Toast {...toast} />}

            {/* Header */}
            <div className="bg-white border-b border-gray-100 sticky top-0 z-10 px-4 py-4 flex items-center justify-between shadow-sm">
                <button
                    onClick={() => navigate(-1)}
                    className="flex items-center gap-2 text-gray-500 hover:text-gray-900 font-medium transition-colors"
                >
                    <ArrowLeft size={18} /> <span className="hidden sm:inline">Back</span>
                </button>
                <div className="flex items-center gap-2 text-xs font-bold text-emerald-700 bg-emerald-50 px-3 py-1.5 rounded-full">
                    <Lock size={12} /> SECURE CHECKOUT
                </div>
            </div>

            <div className="max-w-4xl mx-auto px-4 py-8 grid grid-cols-1 md:grid-cols-2 gap-8">

                {/* LEFT: Payment Methods */}
                <div className="space-y-6">
                    <div>
                        <h1 className="font-serif text-2xl font-bold mb-1">Payment Method</h1>
                        <p className="text-gray-500 text-sm">Select how you want to pay</p>
                    </div>

                    <div className="space-y-3">
                        {['UPI', 'Credit Card', 'Cash on Delivery'].map((m) => {
                            const icons = { 'UPI': Smartphone, 'Credit Card': CreditCard, 'Cash on Delivery': Wallet };
                            const Icon = icons[m];
                            const isSelected = selectedMethod === m;
                            return (
                                <button
                                    key={m}
                                    onClick={() => setSelectedMethod(m)}
                                    className={`w-full p-4 rounded-xl border flex items-center gap-4 transition-all text-left ${isSelected
                                        ? 'border-amber-600 bg-amber-50 ring-1 ring-amber-600'
                                        : 'border-gray-200 bg-white hover:border-amber-300'
                                        }`}
                                >
                                    <div className={`p-2 rounded-lg ${isSelected ? 'bg-amber-100 text-amber-800' : 'bg-gray-100 text-gray-500'}`}>
                                        <Icon size={20} />
                                    </div>
                                    <span className={`font-bold ${isSelected ? 'text-amber-900' : 'text-gray-700'}`}>{m}</span>
                                    {isSelected && <CheckCircle size={18} className="ml-auto text-amber-600" />}
                                </button>
                            );
                        })}
                    </div>

                    <button
                        onClick={simulateVerification}
                        className="w-full py-4 bg-gray-900 text-white rounded-xl font-bold text-lg shadow-lg shadow-gray-900/20 hover:bg-black hover:-translate-y-0.5 transition-all flex items-center justify-center gap-2"
                    >
                        <Lock size={18} /> Pay ₹{paymentAmount.toFixed(2)}
                    </button>

                    <div className="flex items-center justify-center gap-2 text-xs text-gray-400">
                        <Shield size={12} /> Guaranteed Safe & Secure
                    </div>
                </div>

                {/* RIGHT: Order Summary */}
                <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 h-fit">
                    <h3 className="font-serif text-lg font-bold border-b border-gray-100 pb-4 mb-4">Order Summary</h3>

                    {loadingCart ? (
                        <div className="space-y-4 animate-pulse">
                            <div className="h-4 bg-gray-100 rounded w-3/4"></div>
                            <div className="h-4 bg-gray-100 rounded w-1/2"></div>
                        </div>
                    ) : (
                        <div className="space-y-3 max-h-60 overflow-y-auto pr-2 custom-scrollbar">
                            {cartItems.map((item, i) => (
                                <div key={i} className="flex justify-between text-sm group">
                                    <span className="text-gray-600 group-hover:text-gray-900 transition-colors">
                                        <span className="font-bold text-gray-900">{item.quantity}x</span> {item.productName || item.name}
                                    </span>
                                    <span className="font-medium">₹{item.price}</span>
                                </div>
                            ))}
                        </div>
                    )}

                    <div className="border-t border-dashed border-gray-200 mt-6 pt-4 space-y-2">
                        <div className="flex justify-between items-center text-lg font-bold text-gray-900">
                            <span>Total Payable</span>
                            <span>₹{paymentAmount.toFixed(2)}</span>
                        </div>
                    </div>
                </div>

            </div>

            <style>{`
                @keyframes bounce-up {
                    0% { transform: translate(-50%, 100%); opacity: 0; }
                    60% { transform: translate(-50%, -10%); opacity: 1; }
                    100% { transform: translate(-50%, 0); }
                }
                .animate-bounce-up { animation: bounce-up 0.4s cubic-bezier(0.18, 0.89, 0.32, 1.28) forwards; }
                .custom-scrollbar::-webkit-scrollbar { width: 4px; }
                .custom-scrollbar::-webkit-scrollbar-thumb { background: #e5e7eb; border-radius: 4px; }
            `}</style>
        </div>
    );
};

export default PremiumPaymentUI;
