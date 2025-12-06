import Header from "@/components/Header";
import Footer from "@/components/Footer";

const ShippingInfo = () => {
  return (
    <div className="min-h-screen bg-background">
      <Header />
      <div className="container mx-auto px-4 py-6">
        <h1 className="text-3xl font-bold mb-6">Shipping Information</h1>
        <p>This is a placeholder page for Shipping Information.</p>
        <p>Future content will include details about shipping rates, delivery times, tracking, and international shipping options.</p>
      </div>
      <Footer />
    </div>
  );
};

export default ShippingInfo;
