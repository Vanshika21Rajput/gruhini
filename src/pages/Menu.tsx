import React, { useState } from 'react';
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { getCuratedThali } from '@/utils/royalMenuData'; 
import { ShoppingBag, Star, Clock } from 'lucide-react';

const Menu = () => {
  const [filter, setFilter] = useState('All');
  const products = getCuratedThali(); // Your real data

  const handleImageError = (e: React.SyntheticEvent<HTMLImageElement, Event>) => {
    e.currentTarget.src = "/placeholder.svg";
  };

  return (
    <div className="min-h-screen bg-[#050201] text-[#e8e0d5] font-serif">
      <Header />

      {/* Royal Header */}
      <div className="relative pt-40 pb-20 text-center overflow-hidden">
        <div className="absolute inset-0 bg-[url('/assets/royal-bg.jpg')] bg-cover bg-center opacity-30 blur-sm"></div>
        <div className="absolute inset-0 bg-gradient-to-t from-[#050201] via-[#050201]/80 to-transparent"></div>
        
        <div className="relative z-10 max-w-4xl mx-auto px-6">
            <h1 className="text-5xl md:text-6xl font-['Noto_Sans_Devanagari'] mb-6 text-white drop-shadow-2xl">
              आज की रसोई
            </h1>
            <p className="text-[#d4af37] font-['Cinzel'] text-sm tracking-[0.3em] uppercase mb-8">
              Curated for You • Fresh & Homemade
            </p>
            
            {/* Filters */}
            <div className="flex flex-wrap justify-center gap-3">
              {['All', 'Thalis', 'Snacks', 'Desserts'].map(cat => (
                <button 
                  key={cat}
                  onClick={() => setFilter(cat)}
                  className={`px-6 py-2 rounded-full border text-xs font-bold uppercase tracking-widest transition-all ${filter === cat ? 'bg-[#d4af37] text-black border-[#d4af37]' : 'border-[#3e2613] text-[#8c6a38] hover:border-[#d4af37] hover:text-[#d4af37]'}`}
                >
                  {cat}
                </button>
              ))}
            </div>
        </div>
      </div>

      {/* Product Grid */}
      <div className="max-w-[1400px] mx-auto px-6 pb-32 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
        {products.map((item) => (
          <div key={item.id} className="group bg-[#120a05] rounded-2xl overflow-hidden border border-[#d4af37]/10 hover:border-[#d4af37]/40 transition-all duration-300 hover:-translate-y-2">
            
            {/* Image */}
            <div className="h-64 relative overflow-hidden">
              <img src={item.img} alt={item.title} className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110" onError={handleImageError} />
              <div className="absolute inset-0 bg-gradient-to-t from-[#120a05] to-transparent opacity-60"></div>
              
              <div className="absolute bottom-4 left-4 flex flex-col gap-1">
                 <span className="text-white font-['Playfair_Display'] text-xl leading-none">{item.title}</span>
                 <span className="text-[#d4af37] text-xs font-sans flex items-center gap-1">
                   <Clock size={12} /> {item.time}
                 </span>
              </div>
            </div>

            {/* Content */}
            <div className="p-5">
              <div className="flex items-center gap-3 mb-4 pb-4 border-b border-[#ffffff]/5">
                <img src={item.chefAvatar} className="w-10 h-10 rounded-full object-cover border border-[#d4af37]/50" onError={handleImageError} />
                <div>
                  <h4 className="font-['Cinzel'] font-bold text-[#e8e0d5] text-xs">{item.chefName}</h4>
                  <p className="text-[10px] text-gray-500 uppercase tracking-wide">{item.chefLoc}</p>
                </div>
              </div>

              <p className="text-xs text-gray-400 line-clamp-2 mb-6 font-sans leading-relaxed min-h-[40px]">
                {item.desc}
              </p>

              <div className="flex justify-between items-center">
                <span className="text-lg font-bold text-[#d4af37] font-['Cinzel']">{item.price}</span>
                <button className="flex items-center gap-2 px-5 py-2 bg-[#1a1008] border border-[#d4af37]/30 text-[#d4af37] rounded-full text-[10px] font-bold uppercase hover:bg-[#d4af37] hover:text-black transition-all">
                  <ShoppingBag size={12} /> Add
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      <Footer />
    </div>
  );
};

export default Menu;