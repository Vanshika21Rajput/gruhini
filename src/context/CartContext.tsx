import React, { createContext, useContext, useState, useEffect } from 'react';
import { API_BASE_URL } from '@/lib/api';
import { toast } from 'sonner';

export interface CartItem {
    productid: number; // Changed from id to productid to match backend expectation
    name: string;
    price: number; // number (int)
    quantity: number;
    image: string;
    // Add other fields if needed for display
}

interface CartContextType {
    cart: CartItem[];
    addToCart: (item: CartItem) => Promise<void>;
    removeFromCart: (productId: number) => void;
    updateQuantity: (productId: number, quantity: number) => void;
    clearCart: () => void;
    total: number;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [cart, setCart] = useState<CartItem[]>([]);

    // Load cart from local storage or backend on mount
    useEffect(() => {
        // Implement fetch cart from backend if User is logged in
        const fetchCart = async () => {
            try {
                const token = localStorage.getItem('authToken');
                if (!token) return;

                // Assuming there's a GET /cart endpoint, if not we rely on local state or simple add-to-cart firing
                // For now, we'll sync with local storage for persistence
                const savedCart = localStorage.getItem('cart');
                if (savedCart) {
                    setCart(JSON.parse(savedCart));
                }
            } catch (e) {
                console.error("Error loading cart", e);
            }
        };
        fetchCart();
    }, []);

    const addToCart = async (item: CartItem) => {
        try {
            // Optimistic update
            setCart(prev => {
                const existing = prev.find(i => i.productid === item.productid);
                if (existing) {
                    return prev.map(i => i.productid === item.productid ? { ...i, quantity: i.quantity + item.quantity } : i);
                }
                return [...prev, item];
            });

            const token = localStorage.getItem('authToken');
            if (token) {
                // Backend Call
                await fetch(`${API_BASE_URL}/add-to-cart`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify({
                        productid: item.productid,
                        quantity: item.quantity
                    })
                });
            } else {
                // Store locally if guest (though backend seems to require login usually)
                // If pure guest cart is verified, we keep it here.
                localStorage.setItem('cart', JSON.stringify([...cart, item]));
            }
            toast.success("Added to cart");

        } catch (error) {
            console.error("Add to cart error", error);
            toast.error("Failed to add to cart");
        }
    };

    const removeFromCart = (productId: number) => {
        setCart(prev => prev.filter(item => item.productid !== productId));
    };

    const updateQuantity = (productId: number, quantity: number) => {
        setCart(prev => prev.map(item => item.productid === productId ? { ...item, quantity } : item));
    };

    const clearCart = () => {
        setCart([]);
        localStorage.removeItem('cart');
    };

    const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);

    return (
        <CartContext.Provider value={{ cart, addToCart, removeFromCart, updateQuantity, clearCart, total }}>
            {children}
        </CartContext.Provider>
    );
};

export const useCart = () => {
    const context = useContext(CartContext);
    if (context === undefined) {
        throw new Error('useCart must be used within a CartProvider');
    }
    return context;
};
