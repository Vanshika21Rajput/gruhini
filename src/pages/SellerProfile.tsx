import React, { useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { realProducts } from "@/data/products";
import { realSellers } from "@/data/sellers";
import { Star, MapPin, ShieldCheck, Award, Clock } from "lucide-react";

const SellerProfile = () => {
  const { sellerId } = useParams();
  const navigate = useNavigate();

  // Find seller logic
  const seller = realSellers.find(
    (s) => s.id === sellerId || s.name.toLowerCase().replace(/\s+/g, "-") === sellerId
  );

  const sellerProducts = realProducts.filter((p) => p.seller === seller?.name);

  // Helper for image fallback
  const handleImageError = (e: React.SyntheticEvent<HTMLImageElement, Event>) => {
    e.currentTarget.src = "/placeholder.svg";
  };

  if (!seller) {
    return (
      <div className="min-h-screen bg-[#F9F5EB] flex flex-col items-center justify-center text-[#2c1810]">
        <h1 className="font-['Cinzel'] text-3xl mb-4">Chef Not Found</h1>
        <button 
          onClick={() => navigate('/menu')}
          className="text-[#d3ab63] underline font-['Lato']"
        >
          Return to Menu
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#F9F5EB] font-serif text-[#2c1810]">
      <Header />

      {/* --- ROYAL COVER SECTION --- */}
      <div className="relative h-64 md:h-80 bg-[#050201] overflow-hidden">
        {/* Background Pattern/Image */}
        <div 
          className="absolute inset-0 opacity-60 bg-cover bg-center"
          style={{ backgroundImage: "url('/assets/royal-bg.jpg')" }} // Reusing your royal background
        />
        <div className="absolute inset-0 bg-gradient-to-t from-[#050201] via-transparent to-black/30" />
        
        {/* Breadcrumb / Back */}
        <div className="absolute top-24 left-6 z-10">
           <Link to="/menu" className="text-white/60 hover:text-[#d4af37] font-['Lato'] text-sm uppercase tracking-widest transition-colors">
             ← Back to Menu
           </Link>
        </div>
      </div>

      {/* --- PROFILE HEADER --- */}
      <div className="max-w-7xl mx-auto px-6 relative z-10 -mt-20 mb-12">
        <div className="flex flex-col md:flex-row items-end md:items-end gap-8">
          
          {/* Avatar */}
          <div className="relative group">
            <div className="absolute inset-0 rounded-full bg-[#d4af37] blur-md opacity-40 group-hover:opacity-60 transition-opacity" />
            <img
              src={seller.image}
              alt={seller.displayName}
              className="relative w-40 h-40 rounded-full border-4 border-[#d4af37] shadow-2xl object-cover bg-black"
              onError={handleImageError}
            />
            {seller.isVerified && (
              <div className="absolute bottom-2 right-2 bg-[#d4af37] text-[#1a0a00] p-1.5 rounded-full shadow-lg border border-white/20" title="Verified Chef">
                <ShieldCheck size={18} />
              </div>
            )}
          </div>

          {/* Info Block */}
          <div className="flex-1 pb-2 text-center md:text-left">
            <h1 className="text-4xl md:text-5xl font-['Cinzel'] text-[#2c1810] drop-shadow-sm mb-2">
              {seller.displayName}
            </h1>
            
            <div className="flex flex-wrap items-center justify-center md:justify-start gap-4 text-[#8c6a38] font-['Lato'] text-sm uppercase tracking-wide">
              <span className="flex items-center gap-1">
                <MapPin size={14} /> {seller.location}
              </span>
              <span className="w-1 h-1 rounded-full bg-[#d4af37]" />
              <span className="flex items-center gap-1 text-[#2c1810] font-bold">
                <Star size={14} className="fill-[#d4af37] text-[#d4af37]" /> {seller.rating} Rating
              </span>
              <span className="w-1 h-1 rounded-full bg-[#d4af37]" />
              <span>{seller.experience} Experience</span>
            </div>
          </div>

          {/* Action */}
          <div className="pb-4 hidden md:block">
            <button className="px-8 py-3 bg-[#2c1810] text-[#d4af37] rounded-full font-['Cinzel'] font-bold text-xs uppercase tracking-widest hover:bg-[#d4af37] hover:text-[#2c1810] transition-colors shadow-lg">
              Contact Chef
            </button>
          </div>
        </div>
      </div>

      {/* --- CONTENT GRID --- */}
      <div className="max-w-7xl mx-auto px-6 pb-20 grid grid-cols-1 lg:grid-cols-[1fr_2.5fr] gap-12">
        
        {/* LEFT: STORY & STATS */}
        <aside className="space-y-8">
          
          {/* Bio Card */}
          <div className="bg-white p-8 rounded-2xl border border-[#d3ab63]/30 shadow-[0_10px_30px_rgba(147,103,52,0.1)] relative overflow-hidden">
            <div className="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-transparent via-[#d4af37] to-transparent" />
            <h3 className="font-['Cinzel'] text-xl text-[#2c1810] mb-6 flex items-center gap-2">
              <Award size={18} className="text-[#d4af37]" /> My Story
            </h3>
            <p className="font-['Lato'] text-gray-600 leading-relaxed italic text-sm">
              "{seller.bio}"
            </p>
            
            <div className="mt-8 space-y-4">
              <div className="flex items-center justify-between text-xs border-b border-[#f3e6d2] pb-2">
                <span className="text-gray-400 uppercase tracking-wider">Member Since</span>
                <span className="font-bold text-[#8c6a38]">{seller.memberSince}</span>
              </div>
              <div className="flex items-center justify-between text-xs border-b border-[#f3e6d2] pb-2">
                <span className="text-gray-400 uppercase tracking-wider">Hygiene</span>
                <span className="font-bold text-green-700 flex items-center gap-1">
                  <ShieldCheck size={12} /> {seller.hygieneStatus}
                </span>
              </div>
              <div className="flex items-center justify-between text-xs pb-2">
                <span className="text-gray-400 uppercase tracking-wider">Weekly Orders</span>
                <span className="font-bold text-[#2c1810]">{seller.ordersThisWeek}+</span>
              </div>
            </div>
          </div>

          {/* Review Snippet */}
          {seller.reviews.length > 0 && (
            <div className="bg-[#2c1810] p-8 rounded-2xl text-[#f9f5eb] shadow-xl relative">
              <div className="absolute top-4 right-4 text-[#d4af37] opacity-20 text-6xl font-serif">"</div>
              <h3 className="font-['Cinzel'] text-lg text-[#d4af37] mb-4">Top Review</h3>
              <p className="font-['Playfair_Display'] italic text-lg leading-relaxed mb-4 opacity-90">
                {seller.reviews[0].comment}
              </p>
              <div className="flex items-center gap-2 text-xs uppercase tracking-widest text-[#d4af37]/80">
                <span>— {seller.reviews[0].user}</span>
                <div className="flex">
                  {[...Array(5)].map((_, i) => (
                    <Star key={i} size={10} className={`${i < seller.reviews[0].rating ? 'fill-[#d4af37]' : 'fill-gray-600'} text-transparent`} />
                  ))}
                </div>
              </div>
            </div>
          )}
        </aside>

        {/* RIGHT: ROYAL MENU */}
        <main>
          <div className="flex items-center justify-between mb-8">
            <h2 className="font-['Cinzel'] text-3xl text-[#2c1810]">
              Signature Menu
            </h2>
            <span className="text-xs font-['Lato'] text-[#8c6a38] uppercase tracking-widest border border-[#8c6a38] px-3 py-1 rounded-full">
              {sellerProducts.length} Items Available
            </span>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            {sellerProducts.map((product) => (
              <div 
                key={product.id} 
                className="group bg-white rounded-xl overflow-hidden border border-[#d3ab63]/20 shadow-sm hover:shadow-[0_15px_40px_rgba(147,103,52,0.15)] hover:-translate-y-1 transition-all duration-300"
              >
                {/* Product Image */}
                <div className="h-56 relative overflow-hidden">
                  <img 
                    src={product.image} 
                    alt={product.name} 
                    className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110"
                    onError={handleImageError}
                  />
                  <div className="absolute top-3 left-3 bg-black/70 backdrop-blur-sm text-[#d4af37] text-[10px] px-3 py-1 rounded uppercase tracking-wide font-bold border border-white/10">
                    {product.category}
                  </div>
                  {product.badge && (
                    <div className="absolute top-3 right-3 bg-[#d4af37] text-[#1a0a00] text-[10px] px-3 py-1 rounded font-bold shadow-lg">
                      {product.badge}
                    </div>
                  )}
                </div>

                {/* Product Info */}
                <div className="p-6">
                  <div className="flex justify-between items-start mb-2">
                    <h3 className="font-['Playfair_Display'] text-xl text-[#2c1810] leading-tight">
                      {product.name}
                    </h3>
                    <div className="flex items-center gap-1 bg-[#f9f5eb] px-2 py-1 rounded">
                      <Star size={12} className="fill-[#f89420] text-[#f89420]" />
                      <span className="text-xs font-bold">{product.rating}</span>
                    </div>
                  </div>

                  <p className="text-sm text-gray-500 font-['Lato'] line-clamp-2 mb-4 leading-relaxed">
                    {product.description}
                  </p>

                  {/* Pricing & Add */}
                  <div className="flex items-center justify-between pt-4 border-t border-[#f3e6d2]">
                    <div className="flex flex-col">
                      <span className="text-xs text-gray-400 uppercase">Price</span>
                      <span className="font-['Cinzel'] text-lg font-bold text-[#8c6a38]">{product.price}</span>
                    </div>
                    
                    <button className="px-6 py-2 bg-transparent border border-[#2c1810] text-[#2c1810] rounded-full text-xs font-bold uppercase hover:bg-[#2c1810] hover:text-[#d4af37] transition-all">
                      Add to Plate
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {sellerProducts.length === 0 && (
            <div className="text-center py-20 bg-white/50 rounded-2xl border border-dashed border-[#d3ab63]">
              <p className="font-['Cinzel'] text-gray-400">No dishes currently listed.</p>
            </div>
          )}
        </main>
      </div>

      <Footer />
    </div>
  );
};

export default SellerProfile;
