import Header from "@/components/Header";
import Footer from "@/components/Footer";

const HelpCenter = () => {
  return (
    <div className="min-h-screen bg-background">
      <Header />
      <div className="container mx-auto px-4 py-6">
        <h1 className="text-3xl font-bold mb-6">Help Center</h1>
        <p>This is a placeholder page for the Help Center.</p>
        <p>Future content will include FAQs, troubleshooting guides, and contact information for support.</p>
      </div>
      <Footer />
    </div>
  );
};

export default HelpCenter;
