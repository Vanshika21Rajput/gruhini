import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import HeroSection from "@/components/royal/HeroSection";
import TasteAlchemy from "@/components/royal/TasteAlchemy";
import LivingThali from "@/components/royal/LivingThali";

const Index = () => {
  const navigate = useNavigate();

  // Scroll to top on load
  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  return (
    <div className="min-h-screen bg-[#050201] text-[#e8e0d5] selection:bg-[#d4af37] selection:text-black">
      
      {/* 1. Live Kitchen Ticker */}
      <div className="bg-[#0f0805] text-[#d4af37] text-[10px] py-3 border-b border-[#d4af37]/20 font-serif tracking-widest uppercase relative z-50">
        <div className="whitespace-nowrap overflow-hidden flex">
          <div className="animate-[ticker_30s_linear_infinite] flex gap-10 min-w-full">
            <span>✦ Live: Neelam Joshi (Indore) is plating Dal Bati...</span>
            <span>✦ Order Update: Manju Ji's Samosas just sold out!</span>
            <span>✦ Trending: 450 people ordered Gajar Halwa today...</span>
            <span>✦ Freshness: All meals cooked <span className="text-white font-bold">after</span> order placement.</span>
          </div>
        </div>
      </div>

      <Header />
      
      <main className="relative">
        {/* FOLD 1: Hero (Emotion) */}
        <HeroSection />

        {/* FOLD 2: Personalization (Tech) */}
        <TasteAlchemy />

        {/* FOLD 3: Discovery (The Product) */}
        <LivingThali />
        
        {/* FOLD 4: Trust/Stats (Social Proof) */}
        <section className="py-20 px-6 border-t border-[#d4af37]/10 bg-gradient-to-b from-[#050201] to-[#120a05]">
          <div className="max-w-6xl mx-auto text-center">
            <h3 className="font-['Cinzel'] text-2xl text-[#d4af37] mb-12">Trusted by 10,000+ Foodies</h3>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
              {[
                { label: "Home Chefs", val: "500+" },
                { label: "Meals Served", val: "1.2 Lakh" },
                { label: "Happy Tummies", val: "98%" }
              ].map((stat, idx) => (
                <div key={idx} className="p-8 bg-[#1a1008] rounded-2xl border border-[#d4af37]/20 hover:border-[#d4af37]/50 transition-colors">
                  <div className="text-4xl font-bold text-white mb-2 font-serif">{stat.val}</div>
                  <div className="text-sm text-[#8c6a38] uppercase tracking-widest">{stat.label}</div>
                </div>
              ))}
            </div>
            
            <button 
              onClick={() => navigate('/menu')}
              className="mt-16 px-10 py-4 bg-[#d4af37] text-black font-bold font-['Cinzel'] rounded-full hover:bg-white transition-all shadow-[0_0_30px_rgba(212,175,55,0.2)]"
            >
              Explore Full Menu
            </button>
          </div>
        </section>
      </main>

      <Footer />
    </div>
  );
};

export default Index;