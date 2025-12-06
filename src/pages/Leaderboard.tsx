import React, { useState } from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Trophy, Star, TrendingUp, Users, ShoppingBag, Crown, Medal, Award } from 'lucide-react';
import { realSellers } from "@/data/sellers";

const Leaderboard = () => {
  const [activeTab, setActiveTab] = useState("sellers");

  // Convert real sellers to leaderboard format
  const topSellers = realSellers
    .sort((a, b) => {
      if (b.ordersThisWeek !== a.ordersThisWeek) {
        return b.ordersThisWeek - a.ordersThisWeek;
      }
      return b.rating - a.rating;
    })
    .map((seller, index) => ({
      rank: index + 1,
      name: seller.displayName,
      location: seller.location,
      orders: seller.ordersThisWeek * 4, // Approximate monthly orders
      rating: seller.rating,
      revenue: `₹${(seller.ordersThisWeek * 150).toLocaleString()}`, // Estimated revenue
      image: seller.image,
      badge: seller.rewardLevel,
      growth: `+${Math.floor(seller.ordersThisWeek / 10)}%` // Mock growth percentage
    }));

  const topCustomers = [
    {
      rank: 1,
      name: "Amit Sharma",
      location: "Delhi",
      orders: 156,
      spent: "₹45,600",
      image: "https://source.unsplash.com/150x150/?indian,man,professional",
      badge: "💎 Diamond Member",
      favoriteCategory: "Homemade Food"
    },
    {
      rank: 2,
      name: "Priya Gupta",
      location: "Mumbai",
      orders: 134,
      spent: "₹38,900",
      image: "https://source.unsplash.com/150x150/?indian,woman,professional",
      badge: "🏆 Gold Member",
      favoriteCategory: "Natural Cosmetics"
    },
    {
      rank: 3,
      name: "Rajesh Kumar",
      location: "Bangalore",
      orders: 98,
      spent: "₹29,400",
      image: "https://source.unsplash.com/150x150/?indian,man,business",
      badge: "🥈 Silver Member",
      favoriteCategory: "Handmade Crafts"
    }
  ];

  const getRankIcon = (rank: number) => {
    switch (rank) {
      case 1:
        return <Crown className="h-6 w-6 text-yellow-500" />;
      case 2:
        return <Medal className="h-6 w-6 text-gray-400" />;
      case 3:
        return <Award className="h-6 w-6 text-amber-600" />;
      default:
        return <span className="text-lg font-bold text-muted-foreground">#{rank}</span>;
    }
  };

  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="text-center mb-12">
          <Badge variant="outline" className="bg-amber-100 text-amber-700 border-amber-300 px-4 py-2 mb-4">
            🏆 Community Champions
          </Badge>
          <h1 className="font-heading text-4xl md:text-5xl font-bold text-ethnic-primary mb-4">
            गृहिणी Leaderboard
          </h1>
          <p className="text-xl text-muted-foreground max-w-3xl mx-auto">
            Celebrating our top performers who make our community thrive with their dedication and excellence
          </p>
        </div>

        {/* Tabs */}
        <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
          <TabsList className="grid w-full grid-cols-2 max-w-md mx-auto mb-8">
            <TabsTrigger value="sellers" className="flex items-center gap-2">
              <Users className="h-4 w-4" />
              Top Sellers
            </TabsTrigger>
            <TabsTrigger value="customers" className="flex items-center gap-2">
              <ShoppingBag className="h-4 w-4" />
              Top Customers
            </TabsTrigger>
          </TabsList>

          {/* Top Sellers */}
          <TabsContent value="sellers" className="space-y-6">
            {/* Top 3 Podium */}
            <div className="grid md:grid-cols-3 gap-6 mb-8">
              {topSellers.slice(0, 3).map((seller, index) => (
                <Card key={seller.rank} className={`relative overflow-hidden ${
                  seller.rank === 1 ? 'border-2 border-yellow-400 bg-gradient-to-br from-yellow-50 to-amber-50' :
                  seller.rank === 2 ? 'border-2 border-gray-300 bg-gradient-to-br from-gray-50 to-slate-50' :
                  'border-2 border-amber-300 bg-gradient-to-br from-amber-50 to-orange-50'
                }`}>
                  <CardContent className="p-6 text-center">
                    <div className="absolute top-4 right-4">
                      {getRankIcon(seller.rank)}
                    </div>
                    
                    <Avatar className="h-20 w-20 mx-auto mb-4 border-4 border-white shadow-lg">
                      <AvatarImage src={seller.image} alt={seller.name} />
                      <AvatarFallback>{seller.name.substring(0, 2)}</AvatarFallback>
                    </Avatar>
                    
                    <h3 className="font-heading text-xl font-bold text-ethnic-primary mb-1">
                      {seller.name}
                    </h3>
                    <p className="text-sm text-muted-foreground mb-2">{seller.location}</p>
                    
                    <Badge variant="secondary" className="mb-3 text-xs">
                      {seller.badge}
                    </Badge>
                    
                    <div className="space-y-2 text-sm">
                      <div className="flex justify-between">
                        <span>Orders:</span>
                        <span className="font-bold">{seller.orders}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Rating:</span>
                        <span className="font-bold flex items-center">
                          <Star className="h-3 w-3 fill-yellow-400 text-yellow-400 mr-1" />
                          {seller.rating}
                        </span>
                      </div>
                      <div className="flex justify-between">
                        <span>Revenue:</span>
                        <span className="font-bold text-trust-green">{seller.revenue}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Growth:</span>
                        <span className="font-bold text-primary flex items-center">
                          <TrendingUp className="h-3 w-3 mr-1" />
                          {seller.growth}
                        </span>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>

            {/* Rest of the sellers */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Trophy className="h-5 w-5 text-amber-500" />
                  Complete Rankings
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {topSellers.slice(3).map((seller) => (
                    <div key={seller.rank} className="flex items-center justify-between p-4 rounded-lg border hover:bg-accent/50 transition-colors">
                      <div className="flex items-center gap-4">
                        <div className="flex items-center justify-center w-8 h-8 rounded-full bg-muted">
                          <span className="text-sm font-bold">#{seller.rank}</span>
                        </div>
                        <Avatar className="h-12 w-12">
                          <AvatarImage src={seller.image} alt={seller.name} />
                          <AvatarFallback>{seller.name.substring(0, 2)}</AvatarFallback>
                        </Avatar>
                        <div>
                          <h4 className="font-semibold text-ethnic-primary">{seller.name}</h4>
                          <p className="text-sm text-muted-foreground">{seller.location}</p>
                        </div>
                      </div>
                      <div className="text-right">
                        <div className="text-sm font-bold">{seller.orders} orders</div>
                        <div className="text-sm text-trust-green">{seller.revenue}</div>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </TabsContent>

          {/* Top Customers */}
          <TabsContent value="customers" className="space-y-6">
            <div className="grid md:grid-cols-3 gap-6">
              {topCustomers.map((customer) => (
                <Card key={customer.rank} className={`relative overflow-hidden ${
                  customer.rank === 1 ? 'border-2 border-purple-400 bg-gradient-to-br from-purple-50 to-pink-50' :
                  customer.rank === 2 ? 'border-2 border-blue-300 bg-gradient-to-br from-blue-50 to-cyan-50' :
                  'border-2 border-green-300 bg-gradient-to-br from-green-50 to-emerald-50'
                }`}>
                  <CardContent className="p-6 text-center">
                    <div className="absolute top-4 right-4">
                      {getRankIcon(customer.rank)}
                    </div>
                    
                    <Avatar className="h-20 w-20 mx-auto mb-4 border-4 border-white shadow-lg">
                      <AvatarImage src={customer.image} alt={customer.name} />
                      <AvatarFallback>{customer.name.substring(0, 2)}</AvatarFallback>
                    </Avatar>
                    
                    <h3 className="font-heading text-xl font-bold text-ethnic-primary mb-1">
                      {customer.name}
                    </h3>
                    <p className="text-sm text-muted-foreground mb-2">{customer.location}</p>
                    
                    <Badge variant="secondary" className="mb-3 text-xs">
                      {customer.badge}
                    </Badge>
                    
                    <div className="space-y-2 text-sm">
                      <div className="flex justify-between">
                        <span>Orders:</span>
                        <span className="font-bold">{customer.orders}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Total Spent:</span>
                        <span className="font-bold text-trust-green">{customer.spent}</span>
                      </div>
                      <div className="text-xs text-muted-foreground mt-2">
                        Loves: {customer.favoriteCategory}
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          </TabsContent>
        </Tabs>

        {/* Call to Action */}
        <div className="mt-16 text-center">
          <Card className="bg-gradient-warm p-8 max-w-2xl mx-auto">
            <h3 className="font-heading text-2xl font-semibold text-ethnic-primary mb-4">
              Want to Join the Leaderboard?
            </h3>
            <p className="text-muted-foreground mb-6">
              Start your journey with गृहिणी today and become part of our thriving community
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Button variant="hero" size="lg" className="rounded-full">
                👩‍🍳 Become a Seller
              </Button>
              <Button variant="outline" size="lg" className="rounded-full">
                🛒 Start Shopping
              </Button>
            </div>
          </Card>
        </div>
      </div>
      
      <Footer />
    </div>
  );
};

export default Leaderboard;