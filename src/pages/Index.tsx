import Header from "@/components/Header";
import HeroSection from "@/components/HeroSection";
import TrustFeatures from "@/components/TrustFeatures";
import CustomizationSection from "@/components/CustomizationSection";
import CategoryGrid from "@/components/CategoryGrid";
import MostSoldProducts from "@/components/MostSoldProducts";
import TrustSection from "@/components/TrustSection";
import Footer from "@/components/Footer";
import QuickActions from "@/components/QuickActions";

const Index = () => {
  return (
    <div className="min-h-screen bg-background font-body">
      <Header />
      <HeroSection />
      <QuickActions />
      <TrustFeatures />
      <CustomizationSection />
      <CategoryGrid />
      <MostSoldProducts />
      <TrustSection />
      <Footer />
    </div>
  );
};

export default Index;