/**
 * Gruhini — Utility Functions
 * Shared helpers for product page and other pages
 */

/**
 * Format price to INR using Intl.NumberFormat
 * @param {number|string} value - Price value
 * @returns {string} Formatted INR string (e.g., "₹299")
 */
function formatPrice(value) {
    const num = Number(String(value).replace(/[^0-9.]/g, '')) || 0;
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR',
        maximumFractionDigits: 0
    }).format(num);
}

/**
 * Get proper image path with fallback
 * @param {string} path - Image path from data
 * @returns {string} Resolved image path
 */
function getImgPath(path) {
    if (!path) return window.CONFIG?.PLACEHOLDER || 'public/placeholder.jpg';
    if (path.startsWith('http') || path.startsWith('data:')) return path;
    if (path.startsWith('public/')) return path;
    return `public/${path}`;
}

/**
 * Show toast notification with aria-live support
 * @param {string} message - Toast message
 * @param {string} type - 'success' | 'error' | 'info'
 * @param {number} duration - Duration in ms (default 3000)
 */
function showToast(message, type = 'success', duration = 3000) {
    const toast = document.getElementById('toast');
    if (!toast) {
        console.warn('Toast container #toast not found');
        return;
    }

    // Clear existing
    toast.innerHTML = '';

    // Create toast element
    const toastEl = document.createElement('div');
    toastEl.className = `toast-item ${type}`;
    toastEl.setAttribute('role', 'status');

    // Style based on type
    const baseStyles = 'px-6 py-4 rounded-xl shadow-2xl font-medium text-sm flex items-center gap-3 animate-slideIn';
    const typeStyles = {
        success: 'bg-gradient-to-r from-green-600 to-green-500 text-white',
        error: 'bg-gradient-to-r from-red-600 to-red-500 text-white',
        info: 'bg-gradient-to-r from-amber-600 to-amber-500 text-white'
    };

    toastEl.className = `${baseStyles} ${typeStyles[type] || typeStyles.info}`;

    // Icon based on type
    const icons = {
        success: '✓',
        error: '✗',
        info: 'ℹ'
    };

    toastEl.innerHTML = `
        <span class="text-lg">${icons[type] || icons.info}</span>
        <span>${message}</span>
    `;

    toast.appendChild(toastEl);

    // Auto remove
    setTimeout(() => {
        toastEl.classList.add('animate-slideOut');
        setTimeout(() => toastEl.remove(), 300);
    }, duration);
}

/**
 * Simple analytics stub (console only in dev)
 * @param {string} event - Event name
 * @param {object} data - Event data
 */
function trackEvent(event, data = {}) {
    if (typeof console !== 'undefined') {
        console.log(`[Analytics] ${event}`, data);
    }
}

/**
 * Check if user is authenticated
 * @returns {boolean}
 */
function isAuthenticated() {
    return !!localStorage.getItem('authToken');
}

/**
 * Get auth token
 * @returns {string|null}
 */
function getAuthToken() {
    return localStorage.getItem('authToken');
}

/**
 * Debounce function for scroll handlers
 * @param {Function} func - Function to debounce
 * @param {number} wait - Wait time in ms
 * @returns {Function}
 */
function debounce(func, wait = 100) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Export for module usage (if needed)
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { formatPrice, getImgPath, showToast, trackEvent, isAuthenticated, getAuthToken, debounce };
}