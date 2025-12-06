import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Calendar } from "@/components/ui/calendar";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { CalendarIcon, MapPin, Clock, Star } from "lucide-react";
import { format } from "date-fns";
import Header from "@/components/Header";
import Footer from "@/components/Footer";

const RequestCustom = () => {
  const [selectedDate, setSelectedDate] = useState<Date>();
  const [category, setCategory] = useState("");
  const [urgency, setUrgency] = useState("");

  const categories = [
    { value: "homemade-food", label: "Homemade Food & Meals" },
    { value: "natural-cosmetics", label: "Natural Cosmetics & Beauty" },
    { value: "handmade-crafts", label: "Handmade Crafts & Decor" },
    { value: "festival-items", label: "Festival Special Items" },
    { value: "custom-recipe", label: "Custom Family Recipe" },
    { value: "bulk-orders", label: "Bulk Orders for Events" },
    { value: "other", label: "Other Custom Request" }
  ];

  const urgencyOptions = [
    { value: "flexible", label: "Flexible (7+ days)" },
    { value: "normal", label: "Normal (3-7 days)" },
    { value: "urgent", label: "Urgent (1-3 days)" },
    { value: "same-day", label: "Same Day (if possible)" }
  ];

  const successStories = [
    {
      request: "Custom Maharashtrian Wedding Menu",
      customer: "Priya M.",
      fulfilled: "Meera Didi from Pune",
      rating: 5,
      timeframe: "5 days"
    },
    {
      request: "Chemical-free Baby Products",
      customer: "Anita S.",
      fulfilled: "Sunita Devi from Indore",
      rating: 5,
      timeframe: "3 days"
    },
    {
      request: "100 Handmade Diyas for Office",
      customer: "Raj Enterprises",
      fulfilled: "Craftswomen Collective",
      rating: 4.8,
      timeframe: "7 days"
    }
  ];

  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="text-center mb-12">
          <Badge variant="outline" className="bg-accent/20 text-ethnic-primary border-accent px-4 py-2 mb-6">
            💬 Custom Request
          </Badge>
          <h1 className="font-heading text-4xl md:text-5xl font-bold text-ethnic-primary mb-6">
            Can't Find What You're Looking For?
          </h1>
          <p className="text-xl text-muted-foreground max-w-3xl mx-auto">
            No worries! Our network of talented women entrepreneurs can create exactly what you need. 
            Share your requirements and we'll connect you with the perfect maker.
          </p>
        </div>

        <div className="grid lg:grid-cols-3 gap-8">
          {/* Request Form */}
          <div className="lg:col-span-2">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  📝 Tell Us What You Need
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-6">
                {/* Category Selection */}
                <div className="space-y-2">
                  <label className="text-sm font-medium">Product Category *</label>
                  <Select value={category} onValueChange={setCategory}>
                    <SelectTrigger>
                      <SelectValue placeholder="Choose the closest category" />
                    </SelectTrigger>
                    <SelectContent>
                      {categories.map((cat) => (
                        <SelectItem key={cat.value} value={cat.value}>
                          {cat.label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>

                {/* Product Details */}
                <div className="space-y-2">
                  <label className="text-sm font-medium">Product Name/Title *</label>
                  <Input placeholder="e.g., Grandmother's Secret Pickle Recipe" />
                </div>

                {/* Detailed Description */}
                <div className="space-y-2">
                  <label className="text-sm font-medium">Detailed Description *</label>
                  <Textarea 
                    placeholder="Describe what you need in detail. Include ingredients, quantities, specific requirements, cooking style, etc."
                    rows={5}
                  />
                </div>

                {/* Quantity and Serving */}
                <div className="grid md:grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Quantity Needed</label>
                    <Input placeholder="e.g., 10 portions, 500g, 2 pieces" />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Budget Range</label>
                    <Select>
                      <SelectTrigger>
                        <SelectValue placeholder="Select budget range" />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="under-500">Under ₹500</SelectItem>
                        <SelectItem value="500-1000">₹500 - ₹1,000</SelectItem>
                        <SelectItem value="1000-2500">₹1,000 - ₹2,500</SelectItem>
                        <SelectItem value="2500-5000">₹2,500 - ₹5,000</SelectItem>
                        <SelectItem value="above-5000">Above ₹5,000</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                </div>

                {/* Delivery Details */}
                <div className="grid md:grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Delivery Location *</label>
                    <Input placeholder="City, State" />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">When do you need it? *</label>
                    <Popover>
                      <PopoverTrigger asChild>
                        <Button variant="outline" className="w-full justify-start text-left font-normal">
                          <CalendarIcon className="mr-2 h-4 w-4" />
                          {selectedDate ? format(selectedDate, "PPP") : "Pick a date"}
                        </Button>
                      </PopoverTrigger>
                      <PopoverContent className="w-auto p-0" align="start">
                        <Calendar
                          mode="single"
                          selected={selectedDate}
                          onSelect={setSelectedDate}
                          initialFocus
                        />
                      </PopoverContent>
                    </Popover>
                  </div>
                </div>

                {/* Urgency */}
                <div className="space-y-2">
                  <label className="text-sm font-medium">How urgent is this? *</label>
                  <Select value={urgency} onValueChange={setUrgency}>
                    <SelectTrigger>
                      <SelectValue placeholder="Select urgency level" />
                    </SelectTrigger>
                    <SelectContent>
                      {urgencyOptions.map((option) => (
                        <SelectItem key={option.value} value={option.value}>
                          {option.label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>

                {/* Special Instructions */}
                <div className="space-y-2">
                  <label className="text-sm font-medium">Special Instructions</label>
                  <Textarea 
                    placeholder="Any specific requirements, allergies, preferences, or special instructions for the maker?"
                    rows={3}
                  />
                </div>

                {/* Contact Information */}
                <div className="grid md:grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Your Name *</label>
                    <Input placeholder="Full name" />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Phone Number *</label>
                    <Input placeholder="+91 XXXXX XXXXX" />
                  </div>
                </div>

                {/* Submit Button */}
                <div className="pt-4">
                  <Button variant="hero" className="w-full text-lg" size="lg">
                    🚀 Submit Custom Request
                  </Button>
                  <p className="text-sm text-muted-foreground mt-2 text-center">
                    We'll connect you with suitable makers within 24 hours
                  </p>
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Sidebar */}
          <div className="space-y-6">
            {/* How It Works */}
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">How It Works</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex gap-3">
                  <div className="w-8 h-8 bg-primary rounded-full flex items-center justify-center text-white font-bold text-sm">
                    1
                  </div>
                  <div>
                    <div className="font-medium">Submit Request</div>
                    <div className="text-sm text-muted-foreground">Tell us exactly what you need</div>
                  </div>
                </div>
                <div className="flex gap-3">
                  <div className="w-8 h-8 bg-primary rounded-full flex items-center justify-center text-white font-bold text-sm">
                    2
                  </div>
                  <div>
                    <div className="font-medium">Get Matched</div>
                    <div className="text-sm text-muted-foreground">We find the perfect maker for you</div>
                  </div>
                </div>
                <div className="flex gap-3">
                  <div className="w-8 h-8 bg-primary rounded-full flex items-center justify-center text-white font-bold text-sm">
                    3
                  </div>
                  <div>
                    <div className="font-medium">Connect & Order</div>
                    <div className="text-sm text-muted-foreground">Discuss details and place order</div>
                  </div>
                </div>
                <div className="flex gap-3">
                  <div className="w-8 h-8 bg-primary rounded-full flex items-center justify-center text-white font-bold text-sm">
                    4
                  </div>
                  <div>
                    <div className="font-medium">Receive & Enjoy</div>
                    <div className="text-sm text-muted-foreground">Get your custom-made product</div>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Success Stories */}
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Recent Success Stories</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                {successStories.map((story, index) => (
                  <div key={index} className="border-b last:border-b-0 pb-4 last:pb-0">
                    <div className="font-medium text-sm mb-1">{story.request}</div>
                    <div className="text-xs text-muted-foreground mb-2">
                      Requested by {story.customer}
                    </div>
                    <div className="flex items-center justify-between text-xs">
                      <div className="flex items-center gap-1">
                        <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                        <span>{story.rating}</span>
                      </div>
                      <div className="flex items-center gap-1">
                        <Clock className="h-3 w-3 text-muted-foreground" />
                        <span>{story.timeframe}</span>
                      </div>
                    </div>
                    <div className="text-xs text-primary mt-1">
                      Fulfilled by {story.fulfilled}
                    </div>
                  </div>
                ))}
              </CardContent>
            </Card>

            {/* Contact Support */}
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Need Help?</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-sm text-muted-foreground mb-4">
                  Our team is here to help you find exactly what you need.
                </p>
                <div className="space-y-2">
                  <Button variant="outline" className="w-full text-sm">
                    📞 Call Us: +91 XXXXX XXXXX
                  </Button>
                  <Button variant="outline" className="w-full text-sm">
                    💬 WhatsApp Support
                  </Button>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
      
      <Footer />
    </div>
  );
};

export default RequestCustom;