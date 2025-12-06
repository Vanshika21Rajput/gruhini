import React, { useMemo } from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Badge } from '@/components/ui/badge';
import { Star, TrendingUp, ShieldCheck } from 'lucide-react'; // Added ShieldCheck for verified
import { Link } from 'react-router-dom';
import { realSellers, getTopSellers } from "@/data/sellers";

const TopSellersPage: React.FC = () => {
  const topSellerIds = useMemo(() => getTopSellers(realSellers), []);

  // For displaying all sellers, perhaps with a highlight for top ones
  const sortedSellers = useMemo(() => {
    return [...realSellers].sort((a, b) => {
      if (topSellerIds.includes(a.id) && !topSellerIds.includes(b.id)) return -1;
      if (!topSellerIds.includes(a.id) && topSellerIds.includes(b.id)) return 1;
      if (b.ordersThisWeek !== a.ordersThisWeek) return b.ordersThisWeek - a.ordersThisWeek;
      return b.rating - a.rating;
    });
  }, [topSellerIds]);

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Header />
      <main className="flex-grow container mx-auto px-4 py-8">
        <div className="text-center mb-12">
          <Badge variant="outline" className="bg-amber-100 text-amber-700 border-amber-300 px-4 py-2 mb-4 text-sm">
            🏆 Weekly Stars 🏆
          </Badge>
          <h1 className="text-4xl md:text-5xl font-bold font-heading text-ethnic-primary mb-4">
            Top Sellers of the Week
          </h1>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            Meet our community's most loved and trusted sellers, recognized for their exceptional quality and service.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {sortedSellers.map((seller, index) => {
            const isTopSeller = topSellerIds.includes(seller.id);
            return (
              <Link to={seller.profileLink} key={seller.id} className="block group">
                <Card className={`h-full transition-all duration-300 hover:shadow-xl ${isTopSeller ? 'border-2 border-amber-400 bg-amber-50/30 shadow-lg' : 'border'}`}>
                  <CardHeader className="flex flex-row items-center gap-4 pb-3">
                    <Avatar className="h-16 w-16 border-2 border-primary/20">
                      <AvatarImage src={seller.image} alt={seller.displayName} />
                      <AvatarFallback>{seller.displayName.substring(0, 2).toUpperCase()}</AvatarFallback>
                    </Avatar>
                    <div className="flex-1">
                      <CardTitle className="text-xl text-ethnic-primary group-hover:text-primary transition-colors">
                        {seller.displayName}
                        {seller.isVerified && <ShieldCheck className="inline-block h-5 w-5 text-trust-green ml-2" />}
                      </CardTitle>
                      <p className="text-sm text-muted-foreground">{seller.speciality}</p>
                    </div>
                    {isTopSeller && (
                       <div className="absolute top-3 right-3 transform translate-x-1/2 -translate-y-1/2 rotate-12">
                         <Star className="h-10 w-10 fill-yellow-400 text-yellow-500 opacity-80" />
                         <span className="absolute inset-0 flex items-center justify-center text-xs font-bold text-white filter drop-shadow-sm">Top</span>
                       </div>
                    )}
                  </CardHeader>
                  <CardContent className="text-sm space-y-2">
                    <div className="flex justify-between items-center">
                      <span className="text-muted-foreground">Weekly Orders:</span>
                      <Badge variant={isTopSeller ? "default" : "secondary"} className={isTopSeller ? "bg-amber-500" : ""}>
                        {seller.ordersThisWeek}
                      </Badge>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-muted-foreground">Rating:</span>
                      <span className="font-semibold flex items-center">
                        <Star className="h-4 w-4 mr-1 fill-yellow-400 text-yellow-500" /> {seller.rating.toFixed(1)}
                      </span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-muted-foreground">On-time Delivery:</span>
                      <span className="font-semibold text-trust-green">{(seller.onTimeDeliveryRate * 100).toFixed(0)}%</span>
                    </div>
                     <p className="text-xs text-muted-foreground pt-1">Location: {seller.location}</p>
                  </CardContent>
                </Card>
              </Link>
            );
          })}
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default TopSellersPage;
