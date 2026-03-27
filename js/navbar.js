/**
 * Gruhini Unified Navbar Component
 * Consistent royal dark theme across all pages
 * 
 * Usage: Include this script and call initNavbar(activePage) with current page name
 */

function initNavbar(activePage = 'home') {
    const token = localStorage.getItem('authToken');
    const role = localStorage.getItem('userRole');
    const navLinks = document.getElementById('navLinks');

    if (!navLinks) return;

    // Consistent styling across all pages
    const linkClass = "text-xs font-bold uppercase tracking-widest text-amber-400/80 hover:text-white transition-colors";
    const activeClass = "text-xs font-bold uppercase tracking-widest text-amber-400 border-b border-amber-400 pb-1";
    const btnClass = "border border-amber-500/50 px-5 py-2 rounded-full text-amber-400 text-xs font-bold tracking-widest hover:bg-amber-500/20 hover:text-white transition-all uppercase";

    const getClass = (page) => page === activePage ? activeClass : linkClass;

    let html = `
        <a href="menu.html" class="${getClass('menu')}">Menu</a>
        <a href="sellers.html" class="${getClass('sellers')}">Our Kitchens</a>
        <a href="about.html" class="${getClass('about')}">About</a>
    `;

    if (!token) {
        // Guest
        html += `<a href="login.html" class="${btnClass}">Login</a>`;
    } else if (role === 'SELLER') {
        // Seller
        html += `<a href="seller-dashboard.html" class="${btnClass}">Dashboard</a>`;
    } else {
        // Customer
        html += `
            <a href="cart.html" class="${getClass('cart')}">Cart</a>
            <button onclick="localStorage.clear(); window.location.href='login.html'" class="${btnClass}">Logout</button>
        `;
    }

    navLinks.innerHTML = html;
}

// For pages that need separate treatment (light theme like menu, cart)
function initLightNavbar(activePage = 'menu') {
    const token = localStorage.getItem('authToken');
    const role = localStorage.getItem('userRole');
    const navLinks = document.getElementById('navLinks');

    if (!navLinks) return;

    const linkClass = "text-xs font-bold uppercase tracking-widest text-gray-600 hover:text-amber-700 transition-colors";
    const activeClass = "text-xs font-bold uppercase tracking-widest text-amber-700 border-b border-amber-700 pb-1";
    const btnClass = "border border-amber-300 px-5 py-2 rounded-full text-amber-700 text-xs font-bold tracking-widest hover:bg-amber-50 transition-all uppercase";

    const getClass = (page) => page === activePage ? activeClass : linkClass;

    let html = `
        <a href="sellers.html" class="${getClass('sellers')}">Our Kitchens</a>
        <a href="about.html" class="${getClass('about')}">About</a>
    `;

    if (!token) {
        html += `<a href="login.html" class="${btnClass}">Login</a>`;
    } else if (role === 'SELLER') {
        html += `<a href="seller-dashboard.html" class="${btnClass}">Dashboard</a>`;
    } else {
        html += `
            <a href="cart.html" class="${getClass('cart')}">Cart</a>
        `;
    }

    navLinks.innerHTML = html;
}
