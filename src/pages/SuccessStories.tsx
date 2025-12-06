import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Star, MapPin, Calendar, Users, TrendingUp } from "lucide-react";
import Header from "@/components/Header";
import Footer from "@/components/Footer";

const successStories = [
  {
    id: 1,
    name: "Sunita Devi",
    location: "Indore, Madhya Pradesh",
    association: "Mahila Swayam Sahayata Samuh",
    joinedDate: "March 2023",
    rating: 4.9,
    totalOrders: 500,
    monthlyEarning: "₹15,000",
    story: "Started with just ₹500 and now earning enough to support my family and children's education. Gruh Aangan gave me the platform to showcase my cooking skills.",
    products: ["Home-style Dal", "Fresh Rotis", "Pickle Combo"],
    image: "/placeholder.svg",
    featured: true,
    growth: "+300%"
  },
  {
    id: 2,
    name: "Meera Sharma",
    location: "Pune, Maharashtra", 
    association: "Women's Self Help Group",
    joinedDate: "January 2023",
    rating: 4.8,
    totalOrders: 350,
    monthlyEarning: "₹12,000",
    story: "From selling door-to-door to having customers across the city. My natural beauty products are now trusted by hundreds of families.",
    products: ["Herbal Face Pack", "Natural Soap", "Hair Oil"],
    image: "/placeholder.svg",
    featured: false,
    growth: "+250%"
  },
  {
    id: 3,
    name: "Radha Kumari",
    location: "Jaipur, Rajasthan",
    association: "Resident Welfare Association",
    joinedDate: "May 2023",
    rating: 4.7,
    totalOrders: 280,
    monthlyEarning: "₹10,500",
    story: "My Rajasthani sweets and snacks are now reaching customers nationwide. The festival orders have completely changed my life.",
    products: ["Ghewar", "Pyaaz Kachori", "Rajasthani Thali"],
    image: "/placeholder.svg",
    featured: true,
    growth: "+180%"
  },
  {
    id: 4,
    name: "Kavita Patel",
    location: "Ahmedabad, Gujarat",
    association: "Community Kitchen Group",
    joinedDate: "February 2023", 
    rating: 4.9,
    totalOrders: 450,
    monthlyEarning: "₹18,000",
    story: "Started as a hobby, now it's my passion and livelihood. My Gujarati snacks are loved by customers from different states.",
    products: ["Dhokla", "Fafda", "Gujarati Thali"],
    image: "/placeholder.svg",
    featured: false,
    growth: "+320%"
  },
  {
    id: 5,
    name: "Priya Singh",
    location: "Lucknow, Uttar Pradesh",
    association: "Women Entrepreneur Network",
    joinedDate: "April 2023",
    rating: 4.8,
    totalOrders: 380,
    monthlyEarning: "₹14,000",
    story: "My handmade crafts and festive decorations are now part of celebrations in homes across India. Each order brings me joy.",
    products: ["Handmade Diyas", "Rakhi Sets", "Festive Decor"],
    image: "/placeholder.svg",
    featured: true,
    growth: "+275%"
  }
];

const SuccessStories = () => {
  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="text-center mb-12">
          <Badge variant="outline" className="bg-accent/20 text-ethnic-primary border-accent px-4 py-2 mb-6">
            🌟 Success Stories
          </Badge>
          <h1 className="font-heading text-4xl md:text-5xl font-bold text-ethnic-primary mb-6">
            Meet Our Star Entrepreneurs
          </h1>
          <p className="text-xl text-muted-foreground max-w-3xl mx-auto">
            Inspiring journeys of women who transformed their passion into successful businesses 
            through Gruh Aangan platform.
          </p>
        </div>

        {/* Featured Success Story */}
        <div className="mb-12">
          <Card className="overflow-hidden border-2 border-primary/20 shadow-ethnic">
            <CardContent className="p-0">
              <div className="bg-gradient-ethnic p-8 text-white text-center">
                <Badge variant="secondary" className="bg-white/20 text-white mb-4">
                  🏆 Success Story of the Month
                </Badge>
                <div className="w-24 h-24 bg-white rounded-full flex items-center justify-center mx-auto mb-4">
                  <span className="text-4xl">👩‍🍳</span>
                </div>
                <h2 className="text-3xl font-heading font-bold mb-2">Sunita Devi</h2>
                <p className="text-white/90 mb-4">From ₹500 to ₹15,000/month</p>
                <div className="flex justify-center items-center gap-4 text-sm">
                  <div className="flex items-center gap-1">
                    <Star className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                    <span>4.9 Rating</span>
                  </div>
                  <div className="flex items-center gap-1">
                    <TrendingUp className="h-4 w-4" />
                    <span>+300% Growth</span>
                  </div>
                  <div className="flex items-center gap-1">
                    <Users className="h-4 w-4" />
                    <span>500+ Orders</span>
                  </div>
                </div>
              </div>
              <div className="p-8">
                <blockquote className="text-lg text-center text-muted-foreground italic mb-6">
                  "Started with just ₹500 and now earning enough to support my family and children's education. 
                  Gruh Aangan gave me the platform to showcase my cooking skills."
                </blockquote>
                <div className="text-center">
                  <Button variant="hero">
                    View Products by Sunita Devi
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* All Success Stories */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
          {successStories.map((story) => (
            <Card key={story.id} className="group hover:shadow-warm transition-all duration-300 cursor-pointer">
              <CardContent className="p-6">
                {/* Profile */}
                <div className="text-center mb-6">
                  <div className="w-20 h-20 bg-gradient-ethnic rounded-full flex items-center justify-center mx-auto mb-4">
                    <span className="text-3xl">👩‍🍳</span>
                  </div>
                  <h3 className="font-heading text-xl font-semibold text-ethnic-primary mb-1">
                    {story.name}
                  </h3>
                  <div className="flex items-center justify-center gap-1 text-muted-foreground text-sm mb-2">
                    <MapPin className="h-4 w-4" />
                    <span>{story.location}</span>
                  </div>
                  <Badge variant="outline" className="text-xs">
                    {story.association}
                  </Badge>
                </div>

                {/* Stats */}
                <div className="grid grid-cols-2 gap-4 mb-6">
                  <div className="text-center">
                    <div className="text-2xl font-bold text-primary">{story.rating}</div>
                    <div className="text-xs text-muted-foreground">Rating</div>
                  </div>
                  <div className="text-center">
                    <div className="text-2xl font-bold text-primary">{story.totalOrders}</div>
                    <div className="text-xs text-muted-foreground">Orders</div>
                  </div>
                  <div className="text-center">
                    <div className="text-lg font-bold text-trust-green">{story.monthlyEarning}</div>
                    <div className="text-xs text-muted-foreground">Monthly</div>
                  </div>
                  <div className="text-center">
                    <div className="text-lg font-bold text-accent">{story.growth}</div>
                    <div className="text-xs text-muted-foreground">Growth</div>
                  </div>
                </div>

                {/* Story */}
                <blockquote className="text-sm text-muted-foreground mb-4 line-clamp-3">
                  "{story.story}"
                </blockquote>

                {/* Products */}
                <div className="mb-4">
                  <div className="text-sm font-medium mb-2">Popular Products:</div>
                  <div className="flex flex-wrap gap-1">
                    {story.products.slice(0, 2).map((product, index) => (
                      <Badge key={index} variant="secondary" className="text-xs">
                        {product}
                      </Badge>
                    ))}
                    {story.products.length > 2 && (
                      <Badge variant="outline" className="text-xs">
                        +{story.products.length - 2} more
                      </Badge>
                    )}
                  </div>
                </div>

                {/* Join Date */}
                <div className="flex items-center gap-2 text-xs text-muted-foreground mb-4">
                  <Calendar className="h-3 w-3" />
                  <span>Joined {story.joinedDate}</span>
                </div>

                {/* Action Button */}
                <Button variant="outline" className="w-full">
                  View Products
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* Call to Action */}
        <div className="mt-16 text-center">
          <Card className="bg-gradient-warm p-8 max-w-2xl mx-auto">
            <h3 className="font-heading text-2xl font-semibold text-ethnic-primary mb-4">
              Ready to Start Your Success Story?
            </h3>
            <p className="text-muted-foreground mb-6">
              Join thousands of women entrepreneurs who are building their dreams with Gruh Aangan
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Button variant="hero" size="lg" className="rounded-full">
                👩‍🍳 Become a Seller
              </Button>
              <Button variant="outline" size="lg" className="rounded-full">
                📞 Talk to Our Team
              </Button>
            </div>
          </Card>
        </div>
      </div>
      
      <Footer />
    </div>
  );
};

export default SuccessStories;