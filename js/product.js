/**
 * Gruhini — Product Page Logic
 * Handles: data fetching, rendering, add-to-cart, voice player, sticky CTA
 */

// ============ STATE ============
let currentProduct = null;
let allProducts = [];

// ============ INITIALIZATION ============
document.addEventListener('DOMContentLoaded', init);

async function init() {
    const productId = new URLSearchParams(window.location.search).get('id');

    if (!productId) {
        showError('No product ID provided');
        return;
    }

    // Track page view
    trackEvent('product_view', { productId });

    try {
        await fetchProductData(productId);

        if (currentProduct) {
            renderProduct(currentProduct);
            renderRecommendations();
            setupStickyBar();
            setupVoicePlayer();
            hideLoading();
        } else {
            showError('Dish not found');
        }
    } catch (err) {
        console.error('Product init error:', err);
        showError('Unable to load dish details');
    }
}

// ============ DATA FETCHING ============
async function fetchProductData(productId) {
    try {
        const controller = new AbortController();
        setTimeout(() => controller.abort(), 15000);
        const token = localStorage.getItem('authToken');
        const headers = token ? { Authorization: `Bearer ${token}` } : {};

        const response = await fetch(`${window.CONFIG.BASE_URL}/products/${productId}`, {
            signal: controller.signal, headers
        });

        if (response.ok) {
            const dto = await response.json();
            currentProduct = await normalizeFromBackend(dto);
            
            // Also fetch explore for recommendations
            try {
                const allRes = await fetch(`${window.CONFIG.BASE_URL}/explore`, { headers });
                if (allRes.ok) {
                    allProducts = await allRes.json();
                }
            } catch {}
            return;
        }
    } catch (e) {
        console.log('Backend unavailable, falling back to local data');
    }

    // Fallback to local JSON
    try {
        const localRes = await fetch('real-products.json');
        if (localRes.ok) {
            allProducts = await localRes.json();
            currentProduct = allProducts.find(p => String(p.id) === String(productId));
            if (currentProduct) currentProduct = normalizeProductData(currentProduct);
        }
    } catch (e) {
        console.error('Local data fetch failed:', e);
    }
}

/**
 * Normalize local JSON data to match backend DTO structure
 */
function normalizeProductData(item) {
    return {
        id: item.id,
        title: item.title || item.name,
        subtitle: item.subtitle || `${item.time || 'Fresh'} · ${item.category || 'Home Cooked'}`,
        price: Number(String(item.price).replace(/[^0-9.]/g, '')) || 0,
        serves: item.serves || '1',
        image: item.img || item.image,
        images: item.images || [],
        chef: {
            name: item.chef || 'Home Chef',
            avatar: item.avatar,
            location: item.loc || 'India',
            since: item.since || '',
            verified: true,
            stat: item.trustStat || 'Verified Kitchen',
            quote: item.quote || '"Ghar ka khana, dil se banaya."'
        },
        audio: item.audio || null,
        description: item.desc || item.description || '',
        whatYouGet: item.whatYouGet || {
            portion: 'Single meal',
            packaging: 'Sealed & hygienic',
            spiceLevel: 'Medium',
            shelfLife: 'Best consumed same day'
        },
        ingredients: item.ingredients || ['Home Recipe', 'No Preservatives', 'Fresh Ingredients'],
        exclusions: item.exclusions || ['Preservatives', 'Artificial Colors', 'MSG / Ajinomoto'],
        recommends: [],
        reviews: item.reviews || [
            { name: 'Rohit', city: 'Delhi', rating: 5, text: 'Bilkul ghar jaisa taste.' },
            { name: 'Sneha', city: 'Mumbai', rating: 5, text: 'Meri mummy ko bhi pasand aaya!' }
        ],
        stock: item.stock || 10,
        category: item.category || 'Meals'
    };
}

async function normalizeFromBackend(dto) {
    // Fetch seller details using sellerId from ProductDto
    let chefName = 'Home Kitchen', ownerName = '', avatar = window.CONFIG.PLACEHOLDER;
    let location = 'India', quote = '"Ghar ka khana, dil se banaya."';

    if (dto.sellerId) {
        try {
            const token = localStorage.getItem('authToken');
            const hdrs = token ? { Authorization: `Bearer ${token}` } : {};
            const res = await fetch(`${window.CONFIG.BASE_URL}/get-seller/${dto.sellerId}`, { headers: hdrs });
            if (res.ok) {
                const s = await res.json();
                chefName  = s.businessName || s.name || chefName;
                ownerName = s.name || '';
                avatar    = s.image || avatar;
                if (s.address) {
                    location = [s.address.city, s.address.state].filter(Boolean).join(', ') || location;
                }
                quote = s.Description || s.description || quote;
            }
        } catch {}
    }

    return {
        id:          dto.id,
        title:       dto.name || 'Dish',
        subtitle:    `${dto.deliveryTime || 'Fresh'} · ${dto.category || 'Homemade'}`,
        price:       Number(dto.price) || 0,
        serves:      '1–2',
        image:       dto.image || window.CONFIG.PLACEHOLDER,
        images:      [],
        chef: {
            name:     chefName,
            avatar:   avatar,
            location: location,
            since:    '',
            verified: dto.verified || false,
            stat:     ownerName ? `by ${ownerName}` : 'Verified Kitchen',
            quote:    quote
        },
        audio:       null,
        description: dto.description || '',
        whatYouGet: {
            portion:    'Single serving',
            packaging:  'Sealed & hygienic',
            spiceLevel: 'Medium',
            shelfLife:  dto.deliveryTime || 'Same day'
        },
        ingredients: ['Home Recipe', 'No Preservatives', 'Fresh Ingredients'],
        exclusions:  ['Preservatives', 'Artificial Colors', 'MSG / Ajinomoto'],
        recommends:  [],
        reviews:     [],
        stock:       dto.stock || 0,
        category:    dto.category || 'Meals'
    };
}

// Helper: Format image URLs (same as sellers.html)
function getImageUrl(img) {
    if (!img) return window.CONFIG.PLACEHOLDER;
    // If it's already a full URL (Cloudinary), return as-is
    if (img.startsWith('http')) return img;
    // If it's the placeholder string that starts with /, handle specially
    if (img.startsWith('/')) return img;
    // For relative paths, encode and prepend public/
    const encodedPath = img.split('/').map(part => encodeURIComponent(part)).join('/');
    return `public/${encodedPath}`;
}

// Helper: Format avatar URLs with fallback initials
function getAvatarUrl(avatar, name) {
    if (!avatar) return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=8c6a38&color=fff&size=150&bold=true`;
    // If it's a full HTTP URL, return as-is
    if (avatar.startsWith('http')) return avatar;
    // If it starts with /, return as-is (absolute path)
    if (avatar.startsWith('/')) return avatar;
    // For relative paths, encode and prepend public/
    const encodedPath = avatar.split('/').map(part => encodeURIComponent(part)).join('/');
    return `public/${encodedPath}`;
}

function renderProduct(product) {
    // Hero Image (with proper URL formatting)
    const heroImg = document.getElementById('heroImg');
    const imageUrl = getImageUrl(product.image);
    heroImg.src = imageUrl;
    heroImg.alt = `${product.title} by ${product.chef.name} - ${product.chef.location}`;
    heroImg.onerror = () => { heroImg.src = window.CONFIG.PLACEHOLDER; };

    // Dish Identity
    document.getElementById('dishTitle').textContent = product.title;
    document.getElementById('dishSubtitle').textContent = product.subtitle;
    document.getElementById('dishPrice').textContent = formatPrice(product.price);
    document.getElementById('dishServes').textContent = `Serves ${product.serves} · Freshly made`;

    // Mobile header
    document.getElementById('mobTitle').textContent = product.title;
    document.getElementById('mobPrice').textContent = formatPrice(product.price);

    // Mobile sticky bar
    document.getElementById('stickyPrice').textContent = formatPrice(product.price);

    // Chef Card (with proper avatar URL formatting + ui-avatars fallback)
    const chefAvatar = document.getElementById('chefAvatar');
    const avatarUrl = getAvatarUrl(product.chef.avatar, product.chef.name);
    chefAvatar.src = avatarUrl;
    chefAvatar.alt = product.chef.name;
    chefAvatar.onerror = () => { 
        chefAvatar.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(product.chef.name)}&background=8c6a38&color=fff&size=150&bold=true`;
    };

    document.getElementById('chefName').textContent = product.chef.name;
    document.getElementById('chefLocation').textContent = product.chef.location + ' · Home Kitchen';
    document.getElementById('chefQuote').textContent = product.chef.quote;
    document.getElementById('chefStat').textContent = product.chef.stat;

    // Description
    document.getElementById('dishDescription').textContent = product.description;

    // Details Grid (compact 4-column)
    const wyg = product.whatYouGet || {};
    const detailPortion = document.getElementById('detailPortion');
    const detailSpice = document.getElementById('detailSpice');
    const detailPackaging = document.getElementById('detailPackaging');
    const detailShelf = document.getElementById('detailShelf');

    if (detailPortion) detailPortion.textContent = wyg.portion || 'Single meal';
    if (detailSpice) detailSpice.textContent = wyg.spiceLevel || 'Medium';
    if (detailPackaging) detailPackaging.textContent = wyg.packaging || 'Sealed';
    if (detailShelf) detailShelf.textContent = wyg.shelfLife || 'Same day';

    // Ingredients (inline tags)
    document.getElementById('ingredientsList').innerHTML = product.ingredients
        .map(ing => `<span class="ingredient-tag">${ing}</span>`)
        .join('');

    // Featured Review (single, prominent)
    renderFeaturedReview(product.reviews);

    // Voice note (optional - elements may not exist in new design)
    const voiceSection = document.getElementById('voiceNoteSection');
    const chefsNoteSection = document.getElementById('chefsNoteSection');
    const audioSource = document.getElementById('audioSource');
    const voiceLabel = document.getElementById('voiceLabel');

    if (voiceSection) {
        if (product.audio && audioSource) {
            voiceSection.classList.remove('hidden');
            if (chefsNoteSection) chefsNoteSection.classList.add('hidden');
            audioSource.src = product.audio;
            if (voiceLabel) voiceLabel.textContent = `Listen to ${product.chef.name.split(' ')[0]}`;
        } else {
            voiceSection.classList.add('hidden');
            if (chefsNoteSection) {
                chefsNoteSection.classList.remove('hidden');
                const chefsNoteText = document.getElementById('chefsNoteText');
                if (chefsNoteText) chefsNoteText.textContent = product.chef.quote;
            }
        }
    }

    // Page title
    document.title = `${product.title} by ${product.chef.name} - Gruhini`;
}

function renderFeaturedReview(reviews) {
    if (!reviews || reviews.length === 0) return;

    // Pick the best review (highest rating or first with text)
    const featured = reviews.find(r => r.text) || reviews[0];

    const reviewText = document.getElementById('reviewText');
    const reviewCite = document.getElementById('reviewCite');

    if (reviewText && featured.text) {
        reviewText.textContent = `"${featured.text}"`;
    }
    if (reviewCite && featured.name) {
        reviewCite.textContent = `— ${featured.name}${featured.city ? ', ' + featured.city : ''}`;
    }
}

function renderRecommendations() {
    if (!allProducts.length) return;

    const category = currentProduct.category || 'Meals';
    let recommendations = allProducts
        .filter(p => p.id !== currentProduct.id && (p.category || 'Meals') === category)
        .slice(0, 4);

    // Fallback to any products if no category match
    if (recommendations.length === 0) {
        recommendations = allProducts.filter(p => p.id !== currentProduct.id).slice(0, 4);
    }

    // Render as list items with +Add button
    document.getElementById('recommendsList').innerHTML = recommendations
        .map(p => `
            <div class="recommend-item" onclick="window.location.href='product.html?id=${p.id}'">
                <img src="${getImageUrl(p.img || p.image)}" alt="${p.title || p.name}" 
                     onerror="this.src='${window.CONFIG.PLACEHOLDER}'" loading="lazy">
                <span class="name">${p.title || p.name}</span>
                <span class="price">${formatPrice(p.price)}</span>
                <button class="add-btn" onclick="event.stopPropagation(); addRecommendToCart('${p.id}')">+ Add</button>
            </div>
        `).join('');
}

// ============ ADD TO CART ============
async function addToCart() {
    const token = getAuthToken();

    if (!token) {
        // Redirect to login with return URL
        localStorage.setItem('redirectAfterLogin', window.location.href);
        window.location.href = 'login.html';
        return;
    }

    const btn = document.getElementById('addToPlateBtn');
    const mobileBtn = document.getElementById('mobileAddBtn');
    const originalText = btn.innerHTML;

    // Loading state
    btn.innerHTML = 'Rasoi mein dekh rahe hain...';
    btn.disabled = true;
    if (mobileBtn) {
        mobileBtn.innerHTML = '...';
        mobileBtn.disabled = true;
    }

    trackEvent('add_to_cart', { productId: currentProduct.id, price: currentProduct.price });

    try {
        const response = await fetch(`${window.CONFIG.BASE_URL}/add-to-cart`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                productid: currentProduct.id,
                quantity: 1
            })
        });

        if (response.ok) {
            // Success!
            btn.innerHTML = 'Saja di aapki thali! ✓';
            btn.classList.remove('bg-gradient-to-r', 'from-brown', 'to-[#4a2c20]');
            btn.classList.add('bg-green-600');

            if (mobileBtn) {
                mobileBtn.innerHTML = '✓';
                mobileBtn.classList.add('bg-green-600');
            }

            showToast('Saja di aapki thali! ✓', 'success');

            // Subtle glow effect (confetti reserved for order-success page)
            btn.style.boxShadow = '0 0 30px rgba(34, 197, 94, 0.5)';

            // Redirect to cart
            setTimeout(() => {
                window.location.href = 'cart.html';
            }, 1200);
        } else {
            throw new Error('Add to cart failed');
        }
    } catch (err) {
        console.error('Add to cart error:', err);
        btn.innerHTML = 'Phir se koshish karein';
        btn.disabled = false;

        if (mobileBtn) {
            mobileBtn.innerHTML = 'Retry';
            mobileBtn.disabled = false;
        }

        showToast('Phir se koshish karein', 'error');

        // Reset button after delay
        setTimeout(() => {
            btn.innerHTML = originalText;
            if (mobileBtn) mobileBtn.innerHTML = 'Add to Plate 🍽️';
        }, 3000);
    }
}

async function addRecommendToCart(productId) {
    const token = getAuthToken();

    if (!token) {
        localStorage.setItem('redirectAfterLogin', window.location.href);
        window.location.href = 'login.html';
        return;
    }

    try {
        await fetch(`${window.CONFIG.BASE_URL}/add-to-cart`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ productid, quantity: 1 })
        });

        showToast('Added to plate!', 'success');
    } catch (err) {
        showToast('Could not add item', 'error');
    }
}

// ============ STICKY BAR ============
function setupStickyBar() {
    const stickyBar = document.getElementById('mobileStickyBar');
    const hero = document.getElementById('heroSection');

    if (!stickyBar || !hero) return;

    const observer = new IntersectionObserver(
        (entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    stickyBar.classList.add('translate-y-full', 'opacity-0');
                    stickyBar.classList.remove('translate-y-0', 'opacity-100');
                } else {
                    stickyBar.classList.remove('translate-y-full', 'opacity-0');
                    stickyBar.classList.add('translate-y-0', 'opacity-100');
                }
            });
        },
        { threshold: 0 }
    );

    observer.observe(hero);
}

// ============ VOICE PLAYER ============
function setupVoicePlayer() {
    const playBtn = document.getElementById('voicePlayBtn');
    const audio = document.getElementById('audioPlayer');

    if (!playBtn || !audio) return;

    playBtn.addEventListener('click', () => {
        if (audio.paused) {
            audio.play();
            playBtn.innerHTML = '⏸';
            playBtn.setAttribute('aria-pressed', 'true');
        } else {
            audio.pause();
            playBtn.innerHTML = '▶';
            playBtn.setAttribute('aria-pressed', 'false');
        }
    });

    audio.addEventListener('ended', () => {
        playBtn.innerHTML = '▶';
        playBtn.setAttribute('aria-pressed', 'false');
    });
}

// ============ CONFETTI ============
function triggerConfetti() {
    const container = document.getElementById('confettiContainer');
    if (!container) return;

    const colors = ['#c5a059', '#e2b091', '#4a351d', '#22c55e'];

    for (let i = 0; i < 30; i++) {
        const confetti = document.createElement('div');
        confetti.className = 'confetti-piece';
        confetti.style.left = `${Math.random() * 100}%`;
        confetti.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
        confetti.style.animationDelay = `${Math.random() * 0.3}s`;
        container.appendChild(confetti);
    }

    setTimeout(() => {
        container.innerHTML = '';
    }, 1500);
}

// ============ UI HELPERS ============
function hideLoading() {
    const loading = document.getElementById('loading');
    const content = document.getElementById('content');

    if (loading) loading.classList.add('hidden');
    if (content) content.classList.remove('hidden');
}

function showError(message) {
    const loadingText = document.getElementById('loadingText');
    if (loadingText) {
        loadingText.textContent = message;
        loadingText.classList.add('text-red-500');
    }
}

// Accordion toggle for policies — uses CSS class for rotation
function toggleAccordion(id) {
    const content = document.getElementById(id);
    const icon = document.getElementById(id + 'Icon');

    if (content.classList.contains('hidden')) {
        content.classList.remove('hidden');
        if (icon) icon.classList.add('open');
    } else {
        content.classList.add('hidden');
        if (icon) icon.classList.remove('open');
    }
}
