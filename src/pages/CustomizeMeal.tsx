import { useState, useEffect } from "react"; // Added useEffect
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { toast } from "sonner"; // For notifications
import { Badge } from "@/components/ui/badge";
import { Slider } from "@/components/ui/slider";
import { Textarea } from "@/components/ui/textarea";
import { Star, Users, Clock, ChefHat, Languages, Volume2 } from "lucide-react";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { realSellers } from "@/data/sellers";

const CustomizeMeal = () => {
  const [spiceLevel, setSpiceLevel] = useState([3]);
  const [sweetness, setSweetness] = useState([2]);
  const [saltiness, setSaltiness] = useState([3]);
  const [oilLevel, setOilLevel] = useState([2]); // Added oilLevel state
  const [customNotes, setCustomNotes] = useState(""); // State for special instructions
  const [selectedChef, setSelectedChef] = useState<number | null>(null); // Typed selectedChef
  const [voiceLang, setVoiceLang] = useState("hi-IN");
  const [isSpeaking, setIsSpeaking] = useState(false);

  const TASTE_PROFILE_STORAGE_KEY = 'gruhiniTasteProfile';

  // Load profile on component mount
  useEffect(() => {
    loadTasteProfile();
  }, []);

  const saveTasteProfile = () => {
    const profile = {
      spiceLevel,
      sweetness,
      saltiness,
      oilLevel,
      // Not saving customNotes in profile, as it's usually per-order
    };
    localStorage.setItem(TASTE_PROFILE_STORAGE_KEY, JSON.stringify(profile));
    toast.success("Taste profile saved!");
  };

  const loadTasteProfile = () => {
    const savedProfile = localStorage.getItem(TASTE_PROFILE_STORAGE_KEY);
    if (savedProfile) {
      try {
        const profile = JSON.parse(savedProfile);
        if (profile.spiceLevel) setSpiceLevel(profile.spiceLevel);
        if (profile.sweetness) setSweetness(profile.sweetness);
        if (profile.saltiness) setSaltiness(profile.saltiness);
        if (profile.oilLevel) setOilLevel(profile.oilLevel);
        toast.info("Taste profile loaded.");
      } catch (error) {
        toast.error("Could not load taste profile.");
        console.error("Error loading taste profile:", error);
      }
    }
  };

  const handlePlaceOrder = () => {
    if (!selectedChef) {
      toast.error("Please select a chef before placing an order.");
      return;
    }
    const orderDetails = {
      chef: chefs.find(c => c.id === selectedChef)?.name,
      preferences: {
        spiceLevel: spiceLevel[0],
        sweetness: sweetness[0],
        saltiness: saltiness[0],
        oilLevel: oilLevel[0],
      },
      notes: customNotes,
    };
    console.log("Custom Meal Order:", orderDetails);
    toast.success(`Order placed with ${orderDetails.chef}! Details in console.`);
  };

  const handleVoiceInstructions = () => {
    setIsSpeaking(true);
    const msg = new window.SpeechSynthesisUtterance(
      voiceLang === "hi-IN"
        ? "कृपया अपने स्वाद के अनुसार मसाले, मिठास, नमक और तेल का स्तर चुनें। फिर अपनी पसंदीदा शेफ चुनें और ऑर्डर करें।"
        : "Please adjust the spice, sweetness, salt, and oil levels to your taste. Then select your favorite chef and place your order."
    );
    msg.lang = voiceLang;
    msg.onend = () => setIsSpeaking(false);
    window.speechSynthesis.speak(msg);
  };

  // Convert real sellers to chef format for meal customization
  const chefs = realSellers.map(seller => ({
    id: seller.id,
    name: seller.displayName,
    location: seller.location,
    speciality: seller.speciality,
    rating: seller.rating,
    experience: seller.experience,
    orders: seller.ordersThisWeek * 4, // Approximate monthly orders
    image: seller.image,
    price: "₹150/meal", // Default price, could be customized per seller
    available: true
  }));

  return (
    <div className="min-h-screen bg-background">
      <Header />
      {/* Logo at the top */}
      <div className="flex justify-center mt-6 mb-2">
        <img src="/gruhani-logo.jpg" alt="गृहिणी Logo" className="h-16 w-auto rounded-full shadow-lg border-4 border-primary bg-white p-2" />
      </div>
      {/* Voice Instructions */}
      <div className="flex justify-center items-center gap-4 mb-6">
        <Button variant="ethnic" size="lg" onClick={handleVoiceInstructions} disabled={isSpeaking} className="flex items-center gap-2">
          <Volume2 className="h-5 w-5" />
          {isSpeaking ? "Speaking..." : "Voice Instructions"}
        </Button>
        <div className="flex items-center gap-2">
          <Languages className="h-5 w-5 text-muted-foreground" />
          <select value={voiceLang} onChange={e => setVoiceLang(e.target.value)} className="rounded px-2 py-1 border text-sm">
            <option value="hi-IN">Hindi</option>
            <option value="en-US">English</option>
          </select>
        </div>
        <span className="text-xs text-muted-foreground">(Elderly friendly)</span>
      </div>
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="text-center mb-8">
          <Badge variant="outline" className="bg-accent/20 text-ethnic-primary border-accent px-4 py-2 mb-4">
            🍛 Customize Your Perfect Meal
          </Badge>
          <h1 className="font-heading text-3xl md:text-4xl font-bold text-ethnic-primary mb-4">
            Made Exactly How You Like It
          </h1>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            Adjust taste preferences, choose your favorite chef, and get meals prepared just the way your mom used to make them.
          </p>
        </div>
        <div className="grid lg:grid-cols-2 gap-8">
          {/* Taste Customization */}
          <div className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  🌶️ Taste Preferences
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-5">
                {/* Spice Level */}
                <div>
                  <div className="flex justify-between items-center mb-2">
                    <label className="font-medium">Spice Level</label>
                    <Badge variant="secondary">
                      {spiceLevel[0] === 1 ? "Mild" : 
                       spiceLevel[0] === 2 ? "Medium" :
                       spiceLevel[0] === 3 ? "Medium-Hot" :
                       spiceLevel[0] === 4 ? "Hot" : "Extra Hot"}
                    </Badge>
                  </div>
                  <Slider
                    value={spiceLevel}
                    onValueChange={setSpiceLevel}
                    max={5}
                    min={1}
                    step={1}
                    className="w-full"
                  />
                  <div className="flex justify-between text-xs text-muted-foreground mt-1">
                    <span>Mild</span>
                    <span>Extra Hot</span>
                  </div>
                  <div className="text-xs text-muted-foreground mt-1">Adjust how spicy you want your meal.</div>
                </div>
                {/* Sweetness */}
                <div>
                  <div className="flex justify-between items-center mb-2">
                    <label className="font-medium">Sweetness</label>
                    <Badge variant="secondary">
                      {sweetness[0] === 1 ? "Less Sweet" :
                       sweetness[0] === 2 ? "Normal" :
                       sweetness[0] === 3 ? "Sweet" :
                       sweetness[0] === 4 ? "Extra Sweet" : "Very Sweet"}
                    </Badge>
                  </div>
                  <Slider
                    value={sweetness}
                    onValueChange={setSweetness}
                    max={5}
                    min={1}
                    step={1}
                    className="w-full"
                  />
                  <div className="flex justify-between text-xs text-muted-foreground mt-1">
                    <span>Less Sweet</span>
                    <span>Very Sweet</span>
                  </div>
                  <div className="text-xs text-muted-foreground mt-1">Set your preferred sweetness level.</div>
                </div>
                {/* Saltiness */}
                <div>
                  <div className="flex justify-between items-center mb-2">
                    <label className="font-medium">Salt Level</label>
                    <Badge variant="secondary">
                      {saltiness[0] === 1 ? "Low Salt" :
                       saltiness[0] === 2 ? "Light" :
                       saltiness[0] === 3 ? "Normal" :
                       saltiness[0] === 4 ? "Salty" : "Extra Salty"}
                    </Badge>
                  </div>
                  <Slider
                    value={saltiness}
                    onValueChange={setSaltiness}
                    max={5}
                    min={1}
                    step={1}
                    className="w-full"
                  />
                  <div className="flex justify-between text-xs text-muted-foreground mt-1">
                    <span>Low Salt</span>
                    <span>Extra Salty</span>
                  </div>
                  <div className="text-xs text-muted-foreground mt-1">Control the saltiness of your meal.</div>
                </div>
                {/* Oil Level */}
                <div>
                  <div className="flex justify-between items-center mb-2">
                    <label className="font-medium">Oil Content</label>
                    <Badge variant="secondary">
                      {oilLevel[0] === 1 ? "Oil Free" :
                       oilLevel[0] === 2 ? "Light" :
                       oilLevel[0] === 3 ? "Normal" :
                       oilLevel[0] === 4 ? "Rich" : "Extra Rich"}
                    </Badge>
                  </div>
                  <Slider
                    value={oilLevel}
                    onValueChange={setOilLevel}
                    max={5}
                    min={1}
                    step={1}
                    className="w-full"
                  />
                  <div className="flex justify-between text-xs text-muted-foreground mt-1">
                    <span>Oil Free</span>
                    <span>Rich</span>
                  </div>
                  <div className="text-xs text-muted-foreground mt-1">Choose how oily you want your food.</div>
                </div>
              </CardContent>
            </Card>
            {/* Special Instructions */}
            <Card>
              <CardHeader>
                <CardTitle>Special Notes for Seller</CardTitle>
              </CardHeader>
              <CardContent>
                <Textarea
                  value={customNotes}
                  onChange={e => setCustomNotes(e.target.value)}
                  placeholder="Any special instructions or preferences you'd like to share with the seller..."
                  className="min-h-[80px]"
                />
              </CardContent>
            </Card>
          </div>
          {/* Chef Selection & Order */}
          <div className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <ChefHat className="h-5 w-5 text-primary" /> Choose Your Chef
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {chefs.map(chef => (
                    <div
                      key={chef.id}
                      className={`rounded-lg border p-4 flex flex-col items-center cursor-pointer transition-all duration-200 hover:shadow-lg ${selectedChef === chef.id ? "border-primary bg-primary/10" : "border-border bg-white"} ${!chef.available ? "opacity-50 pointer-events-none" : ""}`}
                      onClick={() => chef.available && setSelectedChef(chef.id)}
                    >
                      <img 
                        src={chef.image} 
                        alt={chef.name} 
                        className="w-16 h-16 rounded-full mb-2 object-cover border-2 border-primary"
                        onError={(e) => {
                          e.currentTarget.src = "/placeholder.svg";
                        }}
                      />
                      <div className="font-semibold text-ethnic-primary">{chef.name}</div>
                      <div className="text-xs text-muted-foreground mb-1">{chef.speciality}</div>
                      <div className="flex items-center gap-1 text-xs mb-1">
                        <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                        {chef.rating} • {chef.experience}
                      </div>
                      <div className="text-xs text-muted-foreground">{chef.location}</div>
                      <div className="text-xs text-primary font-bold mt-1">{chef.price}</div>
                      {!chef.available && <span className="text-xs text-destructive mt-1">Not Available</span>}
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
            <Button variant="hero" size="lg" className="w-full" onClick={handlePlaceOrder}>
              Place Order
            </Button>
            <Button variant="outline" size="sm" className="w-full" onClick={saveTasteProfile}>
              Save Taste Profile
            </Button>
          </div>
        </div>
      </div>
      {/* Logo at the bottom */}
      <div className="flex justify-center mt-8 mb-4">
        <img src="/gruhani-logo.jpg" alt="गृहिणी Logo" className="h-12 w-auto rounded-full shadow border-2 border-primary bg-white p-1" />
      </div>
      <Footer />
    </div>
  );
};

export default CustomizeMeal;