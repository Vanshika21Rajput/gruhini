import React, { useState } from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Checkbox } from '@/components/ui/checkbox';
import { Textarea } from '@/components/ui/textarea';
import { Calendar } from '@/components/ui/calendar';
import { Label } from '@/components/ui/label';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { toast } from 'sonner';
import { CalendarDays, Gift, Star, Clock, Truck, Heart } from 'lucide-react';

const FestiveBooking = () => {
  const [selectedOccasion, setSelectedOccasion] = useState<string | undefined>();
  const [selectedHamperSize, setSelectedHamperSize] = useState<string | undefined>();
  const [selectedItems, setSelectedItems] = useState<Record<string, boolean>>({});
  const [specialNotes, setSpecialNotes] = useState("");
  const [deliveryDate, setDeliveryDate] = useState<Date | undefined>();
  const [recipientName, setRecipientName] = useState("");
  const [recipientAddress, setRecipientAddress] = useState("");

  // Upcoming Festivals (based on reference images)
  const upcomingFestivals = [
    {
      name: "Diwali",
      date: "November 12, 2024",
      daysLeft: 25,
      popularItems: ["Sweets Box", "Dry Fruits", "Diyas & Rangoli"],
      image: "🪔"
    },
    {
      name: "Karva Chauth",
      date: "November 1, 2024", 
      daysLeft: 14,
      popularItems: ["Sargi Thali", "Mehendi", "Traditional Sweets"],
      image: "🌙"
    },
    {
      name: "Dhanteras",
      date: "November 10, 2024",
      daysLeft: 23,
      popularItems: ["Silver Items", "Sweets", "Decorative Items"],
      image: "💰"
    }
  ];

  // Pre-designed hampers (based on reference images)
  const festiveHampers = [
    {
      id: "diwali-premium",
      name: "Diwali Premium Hamper",
      originalPrice: 1500,
      salePrice: 1200,
      discount: 300,
      image: "🎁",
      badge: "Early Bird",
      items: ["Assorted Sweets (500g)", "Dry Fruits Mix (250g)", "Decorative Diyas (Set of 6)", "Rangoli Stencils", "Greeting Card"]
    },
    {
      id: "traditional-sweets",
      name: "Traditional Sweets Box",
      originalPrice: 1000,
      salePrice: 800,
      discount: 200,
      image: "🍬",
      badge: "Early Bird",
      items: ["Gulab Jamun (250g)", "Rasgulla (250g)", "Kaju Katli (200g)", "Motichoor Laddu (300g)", "Gift Box"]
    },
    {
      id: "karva-chauth-special",
      name: "Karva Chauth Special",
      originalPrice: 750,
      salePrice: 600,
      discount: 150,
      image: "🌙",
      badge: "Early Bird",
      items: ["Sargi Thali Set", "Mehendi Cone (2 pcs)", "Traditional Sweets (200g)", "Karva Chauth Story Book", "Decorative Plate"]
    }
  ];

  const customizableItems = [
    { id: "mithai_mix", name: "Assorted Mithai (250g)", category: "Sweets", price: 200 },
    { id: "dry_fruits", name: "Premium Dry Fruits Mix (200g)", category: "Healthy", price: 300 },
    { id: "diyas_set", name: "Handmade Diyas (Set of 8)", category: "Decorations", price: 150 },
    { id: "chocolates", name: "Artisanal Chocolates (Box of 12)", category: "Sweets", price: 250 },
    { id: "namkeen", name: "Festival Namkeen Mix (300g)", category: "Savories", price: 180 },
    { id: "candles", name: "Scented Candles (Set of 4)", category: "Decorations", price: 200 },
    { id: "rangoli", name: "Rangoli Making Kit", category: "Decorations", price: 120 },
    { id: "greeting_cards", name: "Handmade Greeting Cards (Set of 5)", category: "Stationery", price: 100 }
  ];

  const handleItemSelection = (itemId: string) => {
    setSelectedItems(prev => ({ ...prev, [itemId]: !prev[itemId] }));
  };

  const handleSubmitBooking = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!selectedOccasion || (!selectedHamperSize && Object.keys(selectedItems).length === 0)) {
      toast.error("Please select an occasion and at least one hamper or custom items.");
      return;
    }

    const bookingDetails = {
      occasion: selectedOccasion,
      hamperSize: selectedHamperSize,
      customItems: Object.entries(selectedItems).filter(([,isSelected]) => isSelected).map(([id]) => id),
      notes: specialNotes,
      deliveryDate: deliveryDate?.toISOString().split('T')[0],
      recipient: {
        name: recipientName,
        address: recipientAddress
      }
    };
    
    console.log("Festive Booking:", bookingDetails);
    toast.success(`Festive hamper for ${selectedOccasion} booked successfully!`);
  };

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Header />
      
      <main className="flex-grow">
        {/* Hero Section */}
        <div className="bg-gradient-to-br from-orange-50 via-red-50 to-pink-50 py-12">
          <div className="container mx-auto px-4 text-center">
            <Badge variant="outline" className="bg-orange-100 text-orange-700 border-orange-300 px-4 py-2 mb-4">
              🎉 Festival Pre-Booking
            </Badge>
            <h1 className="text-4xl md:text-5xl font-bold font-heading text-ethnic-primary mb-4">
              Festival Pre-Booking
            </h1>
            <p className="text-xl text-muted-foreground max-w-3xl mx-auto">
              Pre-order your festival essentials from trusted Gruhinis. Get early bird discounts and ensure availability!
            </p>
          </div>
        </div>

        {/* Festival Notifications */}
        <div className="bg-amber-50 border-l-4 border-amber-400 p-4 mx-4 mt-6 rounded-r-lg">
          <div className="flex items-center justify-between">
            <div className="flex items-center">
              <span className="text-amber-600 mr-2">🔔</span>
              <span className="text-amber-800 font-medium">Festival Notifications</span>
            </div>
            <Button variant="outline" size="sm" className="bg-orange-500 text-white border-orange-500 hover:bg-orange-600">
              Enabled
            </Button>
          </div>
          <p className="text-amber-700 text-sm mt-1">
            Get notified about new festival offers and booking reminders
          </p>
        </div>

        <div className="container mx-auto px-4 py-8">
          {/* Upcoming Festivals */}
          <section className="mb-12">
            <div className="flex items-center gap-2 mb-6">
              <CalendarDays className="h-6 w-6 text-primary" />
              <h2 className="text-2xl font-bold font-heading text-ethnic-primary">Upcoming Festivals</h2>
            </div>
            
            <div className="grid md:grid-cols-3 gap-6">
              {upcomingFestivals.map((festival, index) => (
                <Card key={index} className="hover:shadow-warm transition-all duration-300">
                  <CardContent className="p-6">
                    <div className="text-center mb-4">
                      <div className="text-4xl mb-2">{festival.image}</div>
                      <h3 className="text-xl font-bold text-ethnic-primary">{festival.name}</h3>
                      <Badge variant="secondary" className="mt-2">
                        {festival.daysLeft} days left
                      </Badge>
                    </div>
                    
                    <div className="space-y-2 text-sm">
                      <p className="text-muted-foreground">{festival.date}</p>
                      <div>
                        <p className="font-medium mb-1">Popular Items:</p>
                        <div className="flex flex-wrap gap-1">
                          {festival.popularItems.map((item, idx) => (
                            <Badge key={idx} variant="outline" className="text-xs">
                              {item}
                            </Badge>
                          ))}
                        </div>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          </section>

          {/* Festival Hampers & Pre-Orders */}
          <section className="mb-12">
            <div className="flex items-center gap-2 mb-6">
              <Gift className="h-6 w-6 text-primary" />
              <h2 className="text-2xl font-bold font-heading text-ethnic-primary">Festival Hampers & Pre-Orders</h2>
            </div>
            
            <div className="grid md:grid-cols-3 gap-6">
              {festiveHampers.map((hamper) => (
                <Card key={hamper.id} className="hover:shadow-warm transition-all duration-300 overflow-hidden">
                  <div className="relative">
                    <div className="h-48 bg-gradient-to-br from-orange-100 to-red-100 flex items-center justify-center">
                      <span className="text-6xl">{hamper.image}</span>
                    </div>
                    <Badge className="absolute top-3 left-3 bg-red-500 text-white">
                      {hamper.badge}
                    </Badge>
                  </div>
                  
                  <CardContent className="p-6">
                    <h3 className="text-lg font-bold text-ethnic-primary mb-2">{hamper.name}</h3>
                    
                    <div className="flex items-center gap-2 mb-3">
                      <span className="text-xl font-bold text-ethnic-primary">₹{hamper.salePrice}</span>
                      <span className="text-sm text-muted-foreground line-through">₹{hamper.originalPrice}</span>
                      <Badge variant="secondary" className="text-xs">Save ₹{hamper.discount}</Badge>
                    </div>
                    
                    <div className="space-y-1 text-xs text-muted-foreground mb-4">
                      {hamper.items.map((item, idx) => (
                        <div key={idx}>• {item}</div>
                      ))}
                    </div>
                    
                    <Button 
                      className="w-full" 
                      onClick={() => {
                        setSelectedHamperSize(hamper.id);
                        toast.success(`${hamper.name} selected!`);
                      }}
                    >
                      Pre-Order Now
                    </Button>
                  </CardContent>
                </Card>
              ))}
            </div>
          </section>

          {/* Custom Festival Order Form */}
          <section>
            <Card className="max-w-4xl mx-auto">
              <CardHeader>
                <CardTitle className="text-2xl text-ethnic-primary">Custom Festival Order</CardTitle>
                <CardDescription>
                  Create your personalized festival hamper or place a custom order
                </CardDescription>
              </CardHeader>
              
              <CardContent>
                <form onSubmit={handleSubmitBooking} className="space-y-6">
                  {/* Occasion Selection */}
                  <div className="grid md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <Label>Select Festival/Occasion</Label>
                      <Select onValueChange={setSelectedOccasion} value={selectedOccasion}>
                        <SelectTrigger>
                          <SelectValue placeholder="Choose occasion" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="diwali">Diwali</SelectItem>
                          <SelectItem value="karva-chauth">Karva Chauth</SelectItem>
                          <SelectItem value="dhanteras">Dhanteras</SelectItem>
                          <SelectItem value="holi">Holi</SelectItem>
                          <SelectItem value="rakhi">Raksha Bandhan</SelectItem>
                          <SelectItem value="navratri">Navratri</SelectItem>
                          <SelectItem value="dussehra">Dussehra</SelectItem>
                          <SelectItem value="other">Other</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>

                    <div className="space-y-2">
                      <Label>Budget Range</Label>
                      <Select>
                        <SelectTrigger>
                          <SelectValue placeholder="Select budget" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="500-1000">₹500 - ₹1,000</SelectItem>
                          <SelectItem value="1000-2000">₹1,000 - ₹2,000</SelectItem>
                          <SelectItem value="2000-5000">₹2,000 - ₹5,000</SelectItem>
                          <SelectItem value="5000+">₹5,000+</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                  </div>

                  {/* Custom Items Selection */}
                  <div className="space-y-4">
                    <Label className="text-lg">Customize Your Hamper</Label>
                    <div className="grid md:grid-cols-2 gap-4">
                      {customizableItems.map((item) => (
                        <div key={item.id} className="flex items-center space-x-3 p-3 border rounded-lg hover:bg-accent/50 transition-colors">
                          <Checkbox
                            id={item.id}
                            checked={selectedItems[item.id] || false}
                            onCheckedChange={() => handleItemSelection(item.id)}
                          />
                          <div className="flex-1">
                            <Label htmlFor={item.id} className="font-medium cursor-pointer">
                              {item.name}
                            </Label>
                            <div className="flex items-center gap-2 mt-1">
                              <Badge variant="outline" className="text-xs">{item.category}</Badge>
                              <span className="text-sm font-medium text-trust-green">₹{item.price}</span>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>

                  {/* Delivery Details */}
                  <div className="grid md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <Label>Recipient Name</Label>
                      <Input
                        placeholder="Enter recipient name"
                        value={recipientName}
                        onChange={(e) => setRecipientName(e.target.value)}
                      />
                    </div>
                    <div className="space-y-2">
                      <Label>Delivery Address</Label>
                      <Input
                        placeholder="Enter delivery address"
                        value={recipientAddress}
                        onChange={(e) => setRecipientAddress(e.target.value)}
                      />
                    </div>
                  </div>

                  {/* Special Instructions */}
                  <div className="space-y-2">
                    <Label>Special Instructions</Label>
                    <Textarea
                      placeholder="Any special requests, dietary restrictions, or custom messages..."
                      value={specialNotes}
                      onChange={(e) => setSpecialNotes(e.target.value)}
                      rows={3}
                    />
                  </div>

                  {/* Delivery Date */}
                  <div className="space-y-2">
                    <Label>Preferred Delivery Date</Label>
                    <div className="flex justify-center">
                      <Calendar
                        mode="single"
                        selected={deliveryDate}
                        onSelect={setDeliveryDate}
                        className="rounded-md border"
                        disabled={(date) => date < new Date()}
                      />
                    </div>
                  </div>

                  <Button type="submit" size="lg" className="w-full">
                    <Gift className="h-4 w-4 mr-2" />
                    Book Festival Order
                  </Button>
                </form>
              </CardContent>
            </Card>
          </section>
        </div>
      </main>
      
      <Footer />
    </div>
  );
};

export default FestiveBooking;