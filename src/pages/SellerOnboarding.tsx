import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { Checkbox } from "@/components/ui/checkbox";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Link } from "react-router-dom";

const SellerOnboarding = () => {
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    // In a real app, you would handle form submission, data validation, and API calls here.
    console.log("Seller onboarding form submitted.");
    alert("Thank you for registering! We will review your application and get back to you soon.");
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-50 via-emerald-50 to-teal-50">
      <Header />
      <div className="container mx-auto px-4 py-12">
        <Card className="max-w-3xl mx-auto shadow-xl">
          <CardHeader className="text-center bg-gradient-ethnic p-6 rounded-t-lg">
            <CardTitle className="text-3xl font-heading font-bold text-primary-foreground">
              Join Gruh Aangan as a Seller
            </CardTitle>
            <CardDescription className="text-primary-foreground/90 text-sm">
              Empower yourself and share your homemade goodness with the world!
            </CardDescription>
          </CardHeader>
          <CardContent className="p-6 md:p-10">
            <form onSubmit={handleSubmit} className="space-y-6">
              {/* Personal Information */}
              <section>
                <h2 className="text-xl font-semibold text-ethnic-primary mb-4 border-b pb-2">
                  Personal Information
                </h2>
                <div className="grid md:grid-cols-2 gap-6">
                  <div>
                    <Label htmlFor="fullName" className="font-medium">Full Name</Label>
                    <Input id="fullName" type="text" placeholder="e.g., Sunita Sharma" required className="mt-1" />
                  </div>
                  <div>
                    <Label htmlFor="phoneNumber" className="font-medium">Phone Number</Label>
                    <Input id="phoneNumber" type="tel" placeholder="e.g., +919876543210" required className="mt-1" />
                  </div>
                  <div>
                    <Label htmlFor="email" className="font-medium">Email Address</Label>
                    <Input id="email" type="email" placeholder="e.g., sunita.sharma@example.com" required className="mt-1" />
                  </div>
                  <div>
                    <Label htmlFor="city" className="font-medium">City / Town</Label>
                    <Input id="city" type="text" placeholder="e.g., Jaipur" required className="mt-1" />
                  </div>
                </div>
                <div className="mt-6">
                  <Label htmlFor="fullAddress" className="font-medium">Full Address</Label>
                  <Textarea id="fullAddress" placeholder="Enter your full residential address" required className="mt-1" />
                </div>
              </section>

              {/* Business Information */}
              <section>
                <h2 className="text-xl font-semibold text-ethnic-primary mb-4 border-b pb-2">
                  Your Homemade Business
                </h2>
                <div>
                  <Label htmlFor="businessName" className="font-medium">Business/Shop Name (Optional)</Label>
                  <Input id="businessName" type="text" placeholder="e.g., Sunita's Kitchen" className="mt-1" />
                </div>
                <div className="mt-6">
                  <Label htmlFor="productCategories" className="font-medium">What do you primarily sell?</Label>
                  <div className="grid grid-cols-2 md:grid-cols-3 gap-4 mt-2">
                    {["Homemade Food", "Natural Cosmetics", "Handmade Crafts", "Pickles & Spices", "Baked Goods", "Other"].map(cat => (
                       <div key={cat} className="flex items-center space-x-2 p-2 border rounded-md hover:bg-accent/50">
                        <Checkbox id={`cat-${cat.toLowerCase().replace(/\s+/g, '-')}`} />
                        <Label htmlFor={`cat-${cat.toLowerCase().replace(/\s+/g, '-')}`} className="text-sm font-normal">
                          {cat}
                        </Label>
                      </div>
                    ))}
                  </div>
                </div>
                <div className="mt-6">
                  <Label htmlFor="shgAffiliation" className="font-medium">Are you part of a Self-Help Group (SHG)? (Optional)</Label>
                  <Input id="shgAffiliation" type="text" placeholder="Enter SHG Name, if any" className="mt-1" />
                </div>
                <div className="mt-6">
                  <Label htmlFor="businessStory" className="font-medium">Tell us about yourself and your products (Optional)</Label>
                  <Textarea id="businessStory" placeholder="Share your passion, what makes your products special, etc." rows={4} className="mt-1" />
                </div>
              </section>

              {/* Document Uploads (Mock) */}
              <section>
                <h2 className="text-xl font-semibold text-ethnic-primary mb-4 border-b pb-2">
                  Document Uploads (Mock)
                </h2>
                <p className="text-sm text-muted-foreground mb-4">
                  Please upload relevant documents like FSSAI registration (if applicable), ID proof, etc.
                  This is a mock section; no files will actually be uploaded.
                </p>
                <div className="grid md:grid-cols-2 gap-6">
                  <div>
                    <Label htmlFor="idProof" className="font-medium">ID Proof (e.g., Aadhaar, PAN)</Label>
                    <Input id="idProof" type="file" className="mt-1 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-primary/10 file:text-primary hover:file:bg-primary/20" />
                  </div>
                  <div>
                    <Label htmlFor="fssaiCert" className="font-medium">FSSAI Certificate (if applicable)</Label>
                    <Input id="fssaiCert" type="file" className="mt-1 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-primary/10 file:text-primary hover:file:bg-primary/20" />
                  </div>
                  <div>
                    <Label htmlFor="kitchenPhotos" className="font-medium">Photos of Your Kitchen/Workspace (Optional)</Label>
                    <Input id="kitchenPhotos" type="file" multiple className="mt-1 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-primary/10 file:text-primary hover:file:bg-primary/20" />
                  </div>
                </div>
              </section>

              {/* Terms and Conditions */}
              <div className="flex items-start space-x-3 pt-4">
                <Checkbox id="terms" required />
                <Label htmlFor="terms" className="text-sm font-normal leading-relaxed">
                  I agree to the <Link to="/seller-terms" className="text-primary hover:underline">Seller Terms and Conditions</Link> and <Link to="/privacy-policy" className="text-primary hover:underline">Privacy Policy</Link> of Gruh Aangan.
                </Label>
              </div>

              <Button type="submit" variant="ethnic" size="lg" className="w-full mt-8">
                Register as a Seller
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
      <Footer />
    </div>
  );
};

export default SellerOnboarding;
