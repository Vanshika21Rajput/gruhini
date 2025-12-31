import React from 'react';
import { motion } from 'framer-motion';

const HeroSection: React.FC = () => {
  const scrollToAlchemy = () => {
    document.getElementById('taste-alchemy')?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <div className="relative h-[90vh] flex flex-col items-center justify-center text-center p-6 overflow-hidden">
      {/* Background */}
      <div className="absolute inset-0 z-[-2]">
        <div 
          className="absolute inset-0 bg-cover bg-center scale-105 blur-[1.5px] opacity-90 contrast-110"
          style={{ backgroundImage: "url('/src/assets/hero-homemade-food.jpg')" }} 
        />
        <div className="absolute inset-0 bg-gradient-to-b from-white/40 via-white/60 to-[#FAF9F6]" />
      </div>

      {/* Content */}
      <motion.div 
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 1 }}
        className="z-10 max-w-4xl"
      >
        <h1 className="font-['Noto_Sans_Devanagari'] text-5xl md:text-7xl leading-tight text-transparent bg-clip-text bg-gradient-to-b from-[#724a20] to-[#d3ab63] drop-shadow-2xl mb-6">
          स्वाद जो दिल छू जाए.<br />
          घर का खाना, सोने की थाली में.
        </h1>

        <p className="font-['Lato'] text-lg md:text-xl text-[#724a20] italic font-light tracking-wide mb-10 max-w-2xl mx-auto opacity-90">
          Handmade meals, cooked today by India's finest home chefs — tailored to your exact taste.
        </p>

        <button
          onClick={scrollToAlchemy}
          className="bg-gradient-to-b from-[#c5a059] to-[#8c6a38] text-[#1a0a00] px-12 py-4 rounded-full font-['Cinzel'] font-bold text-sm tracking-widest uppercase hover:-translate-y-1 transition-all shadow-[0_0_25px_rgba(197,160,89,0.3)]"
        >
          Find My Thali
        </button>

        <p className="mt-6 text-xs text-[#936734] font-['Lato'] tracking-widest uppercase opacity-70">
          Freshly cooked after you order • Nothing pre-made
        </p>
      </motion.div>

      <div className="absolute bottom-10 animate-bounce text-white/30 text-3xl cursor-pointer" onClick={scrollToAlchemy}>
        ↓
      </div>
    </div>
  );
};

export default HeroSection;
