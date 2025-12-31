import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs));
}

// API Configuration
export const API_BASE_URL = "https://gruhini2.onrender.com";

// Types
export interface User {
    id: string;
    name: string;
    email: string;
    role: 'customer' | 'seller' | 'admin';
    token?: string;
    phone?: string;
    contact?: string;
    businessName?: string;
    categories?: string[];
}

export interface LoginResponse {
    success: boolean;
    message?: string;
    user?: User;
    token?: string;
}

export const isValidEmail = (email: string): boolean => {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
};

export const isValidPhone = (phone: string): boolean => {
    return /^\+?[\d\s-]{10,}$/.test(phone);
};

class ApiClient {
    private baseURL: string;

    constructor(baseURL: string) {
        this.baseURL = baseURL;
    }

    // Generic request method
    private async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
        const url = `${this.baseURL}${endpoint}`;
        const token = localStorage.getItem('authToken'); // Assuming token is stored here

        const headers = {
            'Content-Type': 'application/json',
            ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
            ...options.headers,
        };

        try {
            const response = await fetch(url, { ...options, headers });
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'API Request Failed');
            }

            return data;
        } catch (error) {
            console.error(`API Request failed for ${endpoint}:`, error);
            throw error;
        }
    }

    // Login method
    async login(credentials: any): Promise<LoginResponse> {
        try {
            const response = await fetch(`${this.baseURL}/logins`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    email: credentials.username, // UI sends 'username' as email
                    password: credentials.password
                }),
            });

            if (response.ok) {
                const data = await response.json();
                return {
                    success: true,
                    user: data,
                    token: data.token // Assuming token might be in response
                };
            } else {
                const errorData = await response.json().catch(() => ({}));
                return {
                    success: false,
                    message: errorData.message || 'Invalid email or password'
                };
            }
        } catch (error) {
            console.error('Login error:', error);
            return {
                success: false,
                message: 'Login failed. Please try again.'
            };
        }
    }

    // Register method (Customer)
    async register(data: any): Promise<LoginResponse> {
        try {
            const response = await fetch(`${this.baseURL}/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    name: data.name,
                    email: data.email,
                    contact: data.phone, // Backend expects 'contact'
                    password: data.password
                }),
            });

            const resData = await response.json();

            if (response.ok) {
                return {
                    success: true,
                    user: resData
                };
            } else {
                return {
                    success: false,
                    message: resData.message || "Registration failed"
                }
            }

        } catch (error) {
            return {
                success: false,
                message: 'Registration failed. Please try again.'
            };
        }
    }

    // Seller registration method
    async registerSeller(data: any): Promise<LoginResponse> {
        try {
            const response = await fetch(`${this.baseURL}/register-seller`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    name: data.name,
                    email: data.email,
                    phone: data.phone, // Backend expects 'phone' for seller
                    businessName: data.businessName,
                    categories: data.categories || []
                }),
            });

            const resData = await response.json();

            if (response.ok) {
                return {
                    success: true,
                    user: resData
                };
            } else {
                return {
                    success: false,
                    message: resData.message || "Seller registration failed"
                }
            }
        } catch (error) {
            return {
                success: false,
                message: 'Seller registration failed. Please try again.'
            };
        }
    }

    // Helper to check auth
    isAuthenticated(): boolean {
        return !!localStorage.getItem('authToken');
    }
}

export const apiClient = new ApiClient(API_BASE_URL);
