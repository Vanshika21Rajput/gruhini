import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';

const KNOB_LABELS: Record<string, string[]> = {
  comfort: ["Hostel", "Mom's Hand", "Sunday Lunch", "Nani's"],
  spice: ["Kashmiri", "Punjabi", "Kolhapuri", "Naga"],
  indulgence: ["Satvik", "Balanced", "Sunday Feast", "Royal"]
};

const TasteAlchemy: React.FC = () => {
  const [prefs, setPrefs] = useState({ comfort: 30, spice: 75, indulgence: 45 });
  const [isSaved, setIsSaved] = useState(false);

  useEffect(() => {
    const saved = localStorage.getItem('gruhiniTaste');
    if (saved) {
      setPrefs(JSON.parse(saved));
      setIsSaved(true);
    }
  }, []);

  const getLabel = (val: number, type: 'comfort' | 'spice' | 'indulgence') => {
    const arr = KNOB_LABELS[type];
    const index = Math.min(Math.floor((val / 100) * arr.length), arr.length - 1);
    return arr[index];
  };

  const handleSave = () => {
    localStorage.setItem('gruhiniTaste', JSON.stringify(prefs));
    document.getElementById('living-thali')?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <section id="taste-alchemy" className="py-24 px-4 bg-gradient-to-b from-[#FAF9F6] via-[#F5F4F0] to-[#FAF9F6] text-center relative z-10">
      <h2 className="font-['Noto_Sans_Devanagari'] text-4xl text-[#d4af37] mb-16">
        अपना स्वाद चुनें<br/>
        <span className="block font-['Cinzel'] text-sm text-gray-500 mt-2 tracking-[0.2em] uppercase">Personalize Your Experience</span>
      </h2>

      {isSaved ? (
        <motion.div 
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          className="max-w-xl mx-auto bg-gradient-to-br from-[#1e150a] to-black border border-[#8c6a38]/50 rounded-2xl p-10 shadow-2xl"
        >
          <h3 className="font-['Cinzel'] text-2xl text-[#d4af37] mb-2">Welcome Back!</h3>
          <p className="font-['Lato'] text-gray-300 mb-8">
            We remember your taste: <br/>
            <span className="text-[#e2b091] font-bold">{getLabel(prefs.spice, 'spice')} Spice • {getLabel(prefs.comfort, 'comfort')} Comfort</span>
          </p>
          <div className="flex gap-4 justify-center">
            <button onClick={() => setIsSaved(false)} className="text-xs text-gray-500 hover:text-[#d4af37] underline">Edit Preferences</button>
            <button onClick={() => document.getElementById('living-thali')?.scrollIntoView({ behavior: 'smooth' })} className="bg-[#d4af37] text-black px-8 py-3 rounded-full font-bold text-xs uppercase shadow-lg hover:shadow-xl hover:bg-white transition-all">
              See My Menu
            </button>
          </div>
        </motion.div>
      ) : (
        <div className="relative w-full max-w-4xl mx-auto">
          <div className="relative bg-gradient-to-b from-[#8c6a38] to-[#5c4018] rounded-[80px] p-[2px] shadow-[0_20px_50px_rgba(0,0,0,0.6)]">
            <div className="bg-gradient-to-b from-[#c4a166] to-[#a67c3b] rounded-[78px] p-12 border border-white/10 shadow-inner flex flex-wrap justify-center gap-12">
              {['comfort', 'spice', 'indulgence'].map((type) => (
                <div key={type} className="relative w-32 flex flex-col items-center group">
                  <div className="mb-8 font-['Cinzel'] text-xs font-bold text-[#2a1a00] uppercase tracking-widest opacity-80">{type}</div>
                  <div className="w-24 h-24 rounded-full bg-gradient-to-br from-[#5e4525] to-[#8c6a38] shadow-xl flex items-center justify-center relative">
                    <div 
                      className="w-20 h-20 rounded-full bg-gradient-to-br from-[#a68345] via-[#d4af37] to-[#a68345] shadow-[inset_0_2px_5px_rgba(255,255,255,0.4)] cursor-grab active:cursor-grabbing transform transition-transform"
                      style={{ transform: `rotate(${ (prefs[type as keyof typeof prefs] / 100 * 270) - 135 }deg)` }}
                    >
                      <div className="absolute top-2 left-1/2 -translate-x-1/2 w-[3px] h-4 bg-[#2a1a00] rounded-full"></div>
                    </div>
                  </div>
                  <input type="range" min="0" max="100" value={prefs[type as keyof typeof prefs]} onChange={(e) => setPrefs({...prefs, [type]: parseInt(e.target.value)})} className="absolute inset-0 w-full h-full opacity-0 cursor-pointer z-10" />
                  <div className="mt-4 bg-[#1a0a00] text-[#d4af37] px-3 py-1 rounded text-[10px] font-['Cinzel'] border border-[#8c6a38] uppercase">
                    {getLabel(prefs[type as keyof typeof prefs], type as any)}
                  </div>
                </div>
              ))}
            </div>
          </div>
          <div className="max-w-xl mx-auto -mt-8 pt-16 pb-8 px-10 bg-gradient-to-b from-[#25120a] to-[#0a0502] rounded-b-[50px] border-x border-b border-[#d4af37]/20 text-center backdrop-blur-md relative z-[-1]">
            <p className="font-['Playfair_Display'] text-lg text-gray-300 italic mb-6">"You prefer bold, slow-cooked flavours with a touch of heat."</p>
            <button onClick={handleSave} className="bg-gradient-to-b from-[#c5a059] to-[#8c6a38] text-[#1a0a00] px-8 py-3 rounded-full font-['Cinzel'] font-bold text-sm uppercase hover:-translate-y-1 transition-all shadow-[0_0_20px_rgba(197,160,89,0.2)]">Discover My Thali</button>
          </div>
        </div>
      )}
    </section>
  );
};

export default TasteAlchemy;
