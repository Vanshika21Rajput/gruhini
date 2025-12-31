/**
 * GRUHINI API CONFIGURATION
 * Centralized Base URL for all frontend API calls.
 * Change this ONE place to switch between Localhost and Production.
 */

const API_CONFIG = {
    // BASE_URL: 'https://api.gruhini.com/api/v1', // Production Example
    BASE_URL: 'https://gruhini2.onrender.com', // Production
    APP_URL: 'http://localhost:5173', // React App Localhost
};

// Helper for standardized fetch with Auth
const api = {
    // Generic Fetch Wrapper
    async request(endpoint, options = {}) {
        const url = `${API_CONFIG.BASE_URL}${endpoint}`;

        // Auto-attach Token if exists
        const token = localStorage.getItem('authToken');
        const headers = {
            'Content-Type': 'application/json',
            ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
            ...options.headers
        };

        try {
            const response = await fetch(url, { ...options, headers });

            // Handle Global Errors (401, 500)
            if (response.status === 401) {
                console.warn('Unauthorized. Redirecting to login.');
                // Optional: window.location.href = 'login.html';
            }

            // Return JSON or throws error
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.message || 'API Request Failed');
            }
            return data;
        } catch (error) {
            console.error(`API Error [${endpoint}]:`, error);
            if (error.name === 'TypeError' && error.message === 'Failed to fetch') {
                throw new Error("Cannot connect to server. Is the Backend running on Port 8080?");
            }
            throw error; // Re-throw for UI to handle
        }
    }
};
