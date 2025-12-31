import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCuratedThali } from '@/utils/royalMenuData'; // Ensure this file exists from previous steps

const LivingThali = () => {
  const navigate = useNavigate();
  const items = getCuratedThali(); 
  const [activeIndex, setActiveIndex] = useState(0);
  const [isHovered, setIsHovered] = useState(false);

  // AUTO-ROTATE: Fixed to 3 seconds for faster/engaging feel
  useEffect(() => {
    const interval = setInterval(() => {
      if (!isHovered) rotate(1);
    }, 3000); // <--- FIXED SPEED
    return () => clearInterval(interval);
  }, [activeIndex, isHovered]);

  const rotate = (dir: number) => {
    setActiveIndex((prev) => {
      let next = prev + dir;
      if (next < 0) next = items.length - 1;
      if (next >= items.length) next = 0;
      return next;
    });
  };

  const activeItem = items[activeIndex];

  const handleImageError = (e: React.SyntheticEvent<HTMLImageElement, Event>) => {
    e.currentTarget.src = "/placeholder.svg";
  };

  return (
    <section id="living-thali" className="py-24 bg-[#050201] flex flex-col items-center overflow-hidden relative">
      {/* Decorative BG Glow */}
      <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[800px] h-[800px] bg-[#d4af37]/5 blur-[100px] rounded-full pointer-events-none" />

      <h2 className="font-['Cinzel'] text-4xl text-[#d4af37] mb-3 font-bold text-center z-10">Today's Royal Menu</h2>
      <span className="font-sans text-xs text-[#8c6a38] tracking-[0.3em] uppercase mb-16 z-10">Fresh from Home Kitchens</span>

      <div className="relative w-full max-w-[1000px] h-auto md:h-[500px] bg-[#120a05]/80 backdrop-blur-md rounded-[40px] border border-[#d4af37]/20 shadow-2xl grid grid-cols-1 md:grid-cols-[1.1fr_1fr] overflow-hidden z-10">
        
        {/* LEFT: WHEEL */}
        <div className="relative h-[400px] md:h-full flex items-center justify-center bg-gradient-to-br from-[#1a1008] to-[#050201]">
          {/* Stable Center */}
          <div className="absolute z-20 w-36 h-36 rounded-full border-[3px] border-[#d4af37] shadow-[0_0_50px_rgba(212,175,55,0.15)] overflow-hidden flex items-center justify-center bg-black">
             <img src={activeItem.img} className="w-full h-full object-cover opacity-90 transition-all duration-500" onError={handleImageError} />
          </div>

          {/* Rotating Plate */}
          <div 
            className="w-[380px] h-[380px] rounded-full relative transition-transform duration-700 cubic-bezier(0.2, 0.8, 0.2, 1)"
            style={{ 
              background: 'conic-gradient(from 0deg, #3e2723, #5d4037, #8c6a38, #5d4037, #3e2723)',
              transform: `rotate(${activeIndex * -60}deg)`
            }}
          >
            <div className="absolute inset-2 border border-dashed border-[#d4af37]/30 rounded-full" />
            {items.map((item, i) => (
              <div 
                key={item.id}
                onClick={() => { setActiveIndex(i); setIsHovered(true); }}
                className={`absolute w-[80px] h-[80px] rounded-full bg-[#0a0502] border-2 overflow-hidden top-1/2 left-1/2 -mt-[40px] -ml-[40px] cursor-pointer transition-all duration-500 shadow-xl ${i === activeIndex ? 'border-[#fff] scale-110 z-10' : 'border-[#8c6a38] opacity-60 grayscale'}`}
                style={{ transform: `rotate(${i * 60}deg) translate(140px) rotate(${-i * 60 - (activeIndex * -60)}deg)` }}
              >
                <img src={item.img} className="w-full h-full object-cover" onError={handleImageError} />
              </div>
            ))}
          </div>
        </div>

        {/* RIGHT: DETAILS */}
        <div className="relative p-10 flex flex-col justify-center text-left">
          <div className="flex items-center gap-4 mb-6">
            <img src={activeItem.chefAvatar} className="w-14 h-14 rounded-full border border-[#d4af37] object-cover" onError={handleImageError} />
            <div>
              <p className="font-['Cinzel'] text-[#d4af37] text-lg leading-none mb-1">{activeItem.chefName}</p>
              <p className="text-[11px] text-gray-500 uppercase tracking-widest">{activeItem.chefLoc}</p>
            </div>
          </div>

          <h3 className="font-['Playfair_Display'] text-4xl text-white mb-4 leading-tight">{activeItem.title}</h3>
          
          <div className="flex flex-wrap gap-3 mb-6">
             <span className="px-3 py-1 border border-[#d4af37]/30 text-[#d4af37] text-[10px] uppercase rounded-full">{activeItem.time}</span>
             <span className="px-3 py-1 border border-green-800/50 text-green-400 text-[10px] uppercase rounded-full">{activeItem.trustStat}</span>
          </div>

          <p className="text-sm text-gray-400 font-light leading-relaxed mb-8 border-l-2 border-[#d4af37]/50 pl-4">
            "{activeItem.desc}"
          </p>

          <div className="flex items-center justify-between mt-auto border-t border-white/5 pt-6">
            <span className="text-3xl font-['Cinzel'] text-[#d4af37] font-bold">{activeItem.price}</span>
            <button 
              onClick={() => navigate('/menu')}
              className="px-8 py-3 bg-[#d4af37] text-black font-bold font-['Cinzel'] text-xs uppercase tracking-widest hover:bg-white transition-colors"
            >
              Add to Plate
            </button>
          </div>
        </div>

      </div>
    </section>
  );
};

export default LivingThali;