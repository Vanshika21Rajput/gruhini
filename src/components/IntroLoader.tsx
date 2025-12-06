import React, { useEffect, useState } from 'react';

interface IntroLoaderProps {
  onComplete: () => void;
  duration?: number;
}

const IntroLoader: React.FC<IntroLoaderProps> = ({ onComplete, duration = 3500 }) => {
  const [isVisible, setIsVisible] = useState(true);

  useEffect(() => {
    const fadeOutStartTime = duration - 1000;

    const fadeTimer = setTimeout(() => {
      // Start fade-out animation
    }, fadeOutStartTime);

    const completeTimer = setTimeout(() => {
      setIsVisible(false);
      onComplete();
    }, duration);

    return () => {
      clearTimeout(fadeTimer);
      clearTimeout(completeTimer);
    };
  }, [duration, onComplete]);

  if (!isVisible) {
    return null;
  }

  return (
    <div 
      className="fixed inset-0 bg-amber-50 flex flex-col items-center justify-center z-50"
      style={{ animation: `fadeOutAnimation ${duration / 1000}s ease-out forwards` }}
    >
      <style jsx global>{`
        @keyframes fadeOutAnimation {
          0% { opacity: 1; }
          70% { opacity: 1; }
          100% { opacity: 0; }
        }
        @keyframes pulseLogo {
          0%, 100% { transform: scale(1); }
          50% { transform: scale(1.05); }
        }
      `}</style>

      {/* Logo */}
      <img
        src="/gruhani-logo.jpg"
        alt="गृहिणी Logo"
        className="w-40 h-40 md:w-48 md:h-48 mb-6 rounded-2xl shadow-2xl"
        style={{ animation: `pulseLogo 2s infinite ease-in-out` }}
      />

      {/* Tagline */}
      <p className="text-xl md:text-2xl text-red-700 font-playfair px-4 text-center font-semibold">
        घर की रसोई से, दिल तक का सफर
      </p>
      
      <p className="text-sm md:text-base text-red-600 font-poppins px-4 text-center mt-2 opacity-80">
        Authentic Homemade Delights
      </p>
    </div>
  );
};

export default IntroLoader;