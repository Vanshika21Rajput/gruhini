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
    const token = localStorage.getItem('authToken');
    const headers = token ? { Authorization: `Bearer ${token}` } : {};

    // Try backend
    try {
        const controller = new AbortController();
        setTimeout(() => controller.abort(), 15000);

        const response = await fetch(`${window.CONFIG.BASE_URL}/products/${productId}`, {
            signal: controller.signal, headers
        });

        if (response.ok) {
            const dto = await response.json();
            currentProduct = await normalizeFromBackend(dto);

            // Fetch explore for recommendations
            try {
                const allRes = await fetch(`${window.CONFIG.BASE_URL}/explore`, { headers });
                if (allRes.ok) allProducts = await allRes.json();
            } catch {}
            return;
        }
    } catch (e) {
        console.log('Backend product fetch failed, trying explore:', e.message);
    }

    // Fallback: try /explore and find by ID
    try {
        const exploreRes = await fetch(`${window.CONFIG.BASE_URL}/explore`, { headers });
        if (exploreRes.ok) {
            allProducts = await exploreRes.json();
            const found = allProducts.find(p => String(p.id) === String(productId));
            if (found) {
                currentProduct = await normalizeFromBackend(found);
                return;
            }
        }
    } catch {}

    // Final fallback: local JSON
    try {
        const localRes = await fetch('real-products.json');
        if (localRes.ok) {
            allProducts = await localRes.json();
            currentProduct = allProducts.find(p => String(p.id) === String(productId));
            if (currentProduct) currentProduct = normalizeProductData(currentProduct);
        }
    } catch (e) {
        console.error('All fetches failed:', e);
    }
}

function normalizeProductData(item) {
    return {
        id:          item.id,
        title:       item.title || item.name,
        subtitle:    `${item.time || 'Fresh'} · ${item.category || 'Home Cooked'}`,
        price:       Number(String(item.price).replace(/[^0-9.]/g, '')) || 0,
        serves:      item.serves || '1',
        image:       item.img || item.image || null,
        images:      item.images || [],
        chef: {
            name:     item.chef || 'Home Chef',
            avatar:   item.avatar || null,
            location: item.loc || 'India',
            since:    item.since || '',
            verified: true,
            stat:     item.trustStat || 'Verified Kitchen',
            quote:    item.quote || '"Ghar ka khana, dil se banaya."'
        },
        audio:       item.audio || null,
        description: item.desc || item.description || '',
        whatYouGet: {
            portion:    item.whatYouGet?.portion    || 'Single meal',
            packaging:  item.whatYouGet?.packaging  || 'Sealed & hygienic',
            spiceLevel: item.whatYouGet?.spiceLevel || 'Medium',
            shelfLife:  item.whatYouGet?.shelfLife  || 'Best consumed same day'
        },
        ingredients: item.ingredients || ['Home Recipe', 'No Preservatives', 'Fresh Ingredients'],
        exclusions:  item.exclusions  || ['Preservatives', 'Artificial Colors', 'MSG / Ajinomoto'],
        recommends:  [],
        reviews:     item.reviews || [
            { name: 'Rohit', city: 'Delhi',  rating: 5, text: 'Bilkul ghar jaisa taste.' },
            { name: 'Sneha', city: 'Mumbai', rating: 5, text: 'Meri mummy ko bhi pasand aaya!' }
        ],
        stock:    item.stock    || 10,
        category: item.category || 'Meals'
    };
}

async function normalizeFromBackend(dto) {
    // ── Fetch seller details via sellerId ──
    let chefName = 'Home Kitchen', ownerName = '', avatar = null;
    let location = 'India', quote = '"Ghar ka khana, dil se banaya."';

    if (dto.sellerId) {
        try {
            const token = localStorage.getItem('authToken');
            const hdrs  = token ? { Authorization: `Bearer ${token}` } : {};
            const res   = await fetch(`${window.CONFIG.BASE_URL}/get-seller/${dto.sellerId}`, { headers: hdrs });
            if (res.ok) {
                const s = await res.json();
                chefName  = s.businessName || s.name || chefName;
                ownerName = s.name || '';
                avatar    = s.image || null;
                if (s.address) {
                    location = [s.address.city, s.address.state].filter(Boolean).join(', ') || location;
                }
                quote = s.Description || s.description || quote;
            }
        } catch {}
    }

    return {
        id:       dto.id,
        title:    dto.name || 'Dish',
        subtitle: `${dto.deliveryTime || 'Fresh'} · ${dto.category || 'Homemade'}`,
        price:    Number(dto.price) || 0,
        serves:   '1–2',
        image:    dto.image || null,        // ← null if not present, handled in renderProduct
        images:   [],
        chef: {
            name:     chefName,
            avatar:   avatar,               // ← null if no image, handled in renderProduct
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
        stock:       dto.stock    || 0,
        category:    dto.category || 'Meals'
    };
}

// ============ IMAGE HELPERS ============

function getImageUrl(img) {
    if (!img) return null;
    // Already works for Cloudinary URLs, relative paths, etc.
    // No processing needed — just return as-is
    return img;
}

function getAvatarUrl(avatar, name) {
    if (avatar) return avatar;
    // Always return ui-avatars as fallback — never blank
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(name || 'HK')}&background=8c6a38&color=fff&size=150&bold=true`;
}

// ============ RENDER ============
function renderProduct(product) {
    // ── Hero Image: handle null gracefully ──
    const heroImg      = document.getElementById('heroImg');
    const heroWrap     = heroImg ? heroImg.parentElement : null;
    const imageUrl     = getImageUrl(product.image);

    if (heroImg) {
        if (imageUrl) {
            heroImg.src = imageUrl;
            heroImg.alt = `${product.title} by ${product.chef.name}`;
            heroImg.onerror = () => {
                // Image URL broken — show gradient background instead of black
                heroImg.style.display = 'none';
                if (heroWrap) _applyHeroFallback(heroWrap, product);
            };
        } else {
            // No image at all
            heroImg.style.display = 'none';
            if (heroWrap) _applyHeroFallback(heroWrap, product);
        }
    }

    // ── Dish Identity ──
    document.getElementById('dishTitle').textContent    = product.title;
    document.getElementById('dishSubtitle').textContent = product.subtitle;
    document.getElementById('dishPrice').textContent    = formatPrice(product.price);
    document.getElementById('dishServes').textContent   = `Serves ${product.serves} · Freshly made`;

    // Mobile overlays
    const mobTitle = document.getElementById('mobTitle');
    const mobPrice = document.getElementById('mobPrice');
    if (mobTitle) mobTitle.textContent = product.title;
    if (mobPrice) mobPrice.textContent = formatPrice(product.price);

    // Sticky bar
    const stickyPrice = document.getElementById('stickyPrice');
    if (stickyPrice) stickyPrice.textContent = formatPrice(product.price);

    // ── Chef Card ──
    const chefAvatar   = document.getElementById('chefAvatar');
    const avatarUrl    = getAvatarUrl(product.chef.avatar, product.chef.name);
    if (chefAvatar) {
        chefAvatar.src = avatarUrl;
        chefAvatar.alt = product.chef.name;
        // ui-avatars never fails, but handle edge case
        chefAvatar.onerror = () => {
            chefAvatar.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(product.chef.name)}&background=8c6a38&color=fff&size=150&bold=true`;
        };
    }

    document.getElementById('chefName').textContent     = product.chef.name;
    document.getElementById('chefLocation').textContent = `${product.chef.location} · Home Kitchen`;
    document.getElementById('chefQuote').textContent    = product.chef.quote;
    document.getElementById('chefStat').textContent     = product.chef.stat;

    // ── Description ──
    document.getElementById('dishDescription').textContent = product.description;

    // ── Details Grid ──
    const wyg = product.whatYouGet || {};
    const detailPortion   = document.getElementById('detailPortion');
    const detailSpice     = document.getElementById('detailSpice');
    const detailPackaging = document.getElementById('detailPackaging');
    const detailShelf     = document.getElementById('detailShelf');
    if (detailPortion)   detailPortion.textContent   = wyg.portion    || 'Single meal';
    if (detailSpice)     detailSpice.textContent     = wyg.spiceLevel || 'Medium';
    if (detailPackaging) detailPackaging.textContent = wyg.packaging  || 'Sealed';
    if (detailShelf)     detailShelf.textContent     = wyg.shelfLife  || 'Same day';

    // ── Ingredients ──
    document.getElementById('ingredientsList').innerHTML = product.ingredients
        .map(ing => `<span class="ingredient-tag">${ing}</span>`)
        .join('');

    // ── Featured Review ──
    renderFeaturedReview(product.reviews);

    // ── Voice / Chef's Note ──
    const voiceSection   = document.getElementById('voiceNoteSection');
    const chefsNote      = document.getElementById('chefsNoteSection');
    const audioSource    = document.getElementById('audioSource');

    if (voiceSection) {
        if (product.audio && audioSource) {
            voiceSection.classList.remove('hidden');
            if (chefsNote) chefsNote.classList.add('hidden');
            audioSource.src = product.audio;
        } else {
            voiceSection.classList.add('hidden');
            if (chefsNote) {
                chefsNote.classList.remove('hidden');
                const chefsNoteText = document.getElementById('chefsNoteText');
                if (chefsNoteText) chefsNoteText.textContent = product.chef.quote;
            }
        }
    }

    document.title = `${product.title} by ${product.chef.name} - Gruhani`;
}

// ── Hero image fallback: styled gradient instead of pure black ──
function _applyHeroFallback(container, product) {
    container.style.background = 'linear-gradient(135deg, #1a0f0a 0%, #2c1810 40%, #3d1f0e 100%)';
    // Add a centered icon + dish name overlay
    const overlay = document.createElement('div');
    overlay.style.cssText = `
        position:absolute; inset:0; display:flex; flex-direction:column;
        align-items:center; justify-content:center; gap:16px; pointer-events:none;
    `;
    overlay.innerHTML = `
        <div style="width:90px;height:90px;border-radius:50%;background:rgba(197,160,89,0.12);
                    border:2px solid rgba(197,160,89,0.3);display:flex;align-items:center;justify-content:center;">
            <svg xmlns="http://www.w3.org/2000/svg" width="44" height="44" viewBox="0 0 24 24" fill="none"
                 stroke="rgba(197,160,89,0.7)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M3 11l19-9-9 19-2-8-8-2z"/>
            </svg>
        </div>
        <p style="font-family:'Playfair Display',serif;font-style:italic;font-size:18px;
                  color:rgba(197,160,89,0.6);letter-spacing:.05em;">
            ${product.title}
        </p>
    `;
    container.appendChild(overlay);
}

function renderFeaturedReview(reviews) {
    if (!reviews || reviews.length === 0) return;
    const featured   = reviews.find(r => r.text) || reviews[0];
    const reviewText = document.getElementById('reviewText');
    const reviewCite = document.getElementById('reviewCite');
    if (reviewText && featured.text) reviewText.textContent = `"${featured.text}"`;
    if (reviewCite && featured.name) reviewCite.textContent = `— ${featured.name}${featured.city ? ', ' + featured.city : ''}`;
}

function renderRecommendations() {
    if (!allProducts.length) return;

    const category = currentProduct.category || 'Meals';
    let recs = allProducts
        .filter(p => p.id !== currentProduct.id && (p.category || 'Meals') === category)
        .slice(0, 4);

    if (!recs.length) recs = allProducts.filter(p => p.id !== currentProduct.id).slice(0, 4);

    const listEl = document.getElementById('recommendsList');
    if (!listEl) return;

    listEl.innerHTML = recs.map(p => {
        const imgUrl = getImageUrl(p.img || p.image) || 'https://ui-avatars.com/api/?name=Dish&background=1a0f0a&color=c5a059&size=64';
        return `
        <div class="recommend-item" onclick="window.location.href='product.html?id=${p.id}'">
            <img src="${imgUrl}" alt="${p.title || p.name}" onerror="this.style.display='none'">
            <span class="name">${p.title || p.name}</span>
            <span class="price">${formatPrice(p.price)}</span>
            <button class="add-btn" onclick="event.stopPropagation(); addRecommendToCart('${p.id}')">+ Add</button>
        </div>`;
    }).join('');
}

// ============ ADD TO CART ============
async function addToCart() {
    const token = getAuthToken();
    if (!token) {
        localStorage.setItem('redirectAfterLogin', window.location.href);
        window.location.href = 'login.html';
        return;
    }

    const btn        = document.getElementById('addToPlateBtn');
    const mobileBtn  = document.getElementById('mobileAddBtn');
    const origHtml   = btn ? btn.innerHTML : '';

    if (btn)       { btn.innerHTML = 'Adding to plate...'; btn.disabled = true; }
    if (mobileBtn) { mobileBtn.innerHTML = '...'; mobileBtn.disabled = true; }

    try {
        const res = await fetch(`${window.CONFIG.BASE_URL}/add-to-cart`, {
            method:  'POST',
            headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
            body:    JSON.stringify({ productid: currentProduct.id, quantity: 1 })
        });

        if (res.ok) {
            if (btn) {
                btn.innerHTML = 'Saja di aapki thali! ✓';
                btn.classList.remove('bg-gradient-to-r', 'from-brown', 'to-[#4a2c20]');
                btn.classList.add('bg-green-600');
                btn.style.boxShadow = '0 0 30px rgba(34,197,94,0.5)';
            }
            if (mobileBtn) { mobileBtn.innerHTML = '✓'; mobileBtn.classList.add('bg-green-600'); }
            showToast('Saja di aapki thali! ✓', 'success');
            setTimeout(() => { window.location.href = 'cart.html'; }, 1200);
        } else {
            throw new Error('Add to cart failed');
        }
    } catch (err) {
        console.error('Add to cart error:', err);
        if (btn) { btn.innerHTML = 'Phir se koshish karein'; btn.disabled = false; }
        if (mobileBtn) { mobileBtn.innerHTML = 'Retry'; mobileBtn.disabled = false; }
        showToast('Phir se koshish karein', 'error');
        setTimeout(() => {
            if (btn) btn.innerHTML = origHtml;
            if (mobileBtn) mobileBtn.innerHTML = 'Add to Plate 🍽️';
        }, 3000);
    }
}

async function addRecommendToCart(productId) {
    const token = getAuthToken();
    if (!token) { localStorage.setItem('redirectAfterLogin', window.location.href); window.location.href = 'login.html'; return; }
    try {
        await fetch(`${window.CONFIG.BASE_URL}/add-to-cart`, {
            method:  'POST',
            headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
            body:    JSON.stringify({ productid: parseInt(productId, 10), quantity: 1 })
        });
        showToast('Added to plate!', 'success');
    } catch { showToast('Could not add item', 'error'); }
}

// ============ STICKY BAR ============
function setupStickyBar() {
    const stickyBar = document.getElementById('mobileStickyBar');
    const hero      = document.getElementById('heroSection');
    if (!stickyBar || !hero) return;

    const observer = new IntersectionObserver(entries => {
        entries.forEach(e => {
            if (e.isIntersecting) {
                stickyBar.classList.add('translate-y-full', 'opacity-0');
                stickyBar.classList.remove('translate-y-0', 'opacity-100');
            } else {
                stickyBar.classList.remove('translate-y-full', 'opacity-0');
                stickyBar.classList.add('translate-y-0', 'opacity-100');
            }
        });
    }, { threshold: 0 });

    observer.observe(hero);
}

// ============ VOICE PLAYER ============
function setupVoicePlayer() {
    const playBtn = document.getElementById('voicePlayBtn');
    const audio   = document.getElementById('audioPlayer');
    if (!playBtn || !audio) return;

    playBtn.addEventListener('click', () => {
        if (audio.paused) { audio.play(); playBtn.innerHTML = '⏸'; playBtn.setAttribute('aria-pressed', 'true'); }
        else              { audio.pause(); playBtn.innerHTML = '▶'; playBtn.setAttribute('aria-pressed', 'false'); }
    });
    audio.addEventListener('ended', () => { playBtn.innerHTML = '▶'; playBtn.setAttribute('aria-pressed', 'false'); });
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
    if (loadingText) { loadingText.textContent = message; loadingText.classList.add('text-red-500'); }
}

function toggleAccordion(id) {
    const content = document.getElementById(id);
    const icon    = document.getElementById(id + 'Icon');
    if (content.classList.contains('hidden')) {
        content.classList.remove('hidden');
        if (icon) icon.classList.add('open');
    } else {
        content.classList.add('hidden');
        if (icon) icon.classList.remove('open');
    }
}
