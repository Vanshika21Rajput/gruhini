import { useParams, Link } from "react-router-dom";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Star, ShieldCheck, MapPin, Users, ShoppingBag } from "lucide-react";
import { realProducts } from "@/data/products";
import { realSellers } from "@/data/sellers";


const SellerProfile = () => {
  const { sellerId } = useParams();
  // Find seller by id or a modified name from product.seller for now
  const seller = realSellers.find(s => s.id === sellerId || s.name.toLowerCase().replace(/\s+/g, '-') === sellerId);

  const sellerProducts = realProducts.filter(p => p.seller === seller?.name);

  if (!seller) {
    return (
      <>
        <Header />
        <div className="container mx-auto px-4 py-12 text-center">
          <h1 className="text-2xl font-bold">Seller not found</h1>
          <Link to="/explore">
            <Button variant="link">Go back to products</Button>
          </Link>
        </div>
        <Footer />
      </>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-50 via-emerald-50 to-teal-50">
      <Header />
      <div className="container mx-auto px-4 py-8">
        {/* Seller Header */}
        <Card className="mb-8 shadow-lg bg-card overflow-hidden">
          <div className="relative h-48 bg-gradient-to-r from-ethnic-primary to-ethnic-secondary">
            {/* Optional: Seller cover photo */}
          </div>
          <CardContent className="p-6 pt-0">
            <div className="flex flex-col sm:flex-row items-center sm:items-end -mt-16 sm:-mt-20">
              <img
                src={seller.image}
                alt={seller.displayName}
                className="w-32 h-32 sm:w-40 sm:h-40 rounded-full border-4 border-background shadow-xl object-cover"
                onError={(e) => {
                  e.currentTarget.src = "/placeholder.svg";
                }}
              />
              <div className="mt-4 sm:ml-6 text-center sm:text-left">
                <h1 className="text-3xl font-bold text-ethnic-primary">{seller.displayName}</h1>
                <div className="flex items-center justify-center sm:justify-start gap-2 mt-1">
                  {seller.isVerified && (
                    <Badge className="bg-trust-green text-white text-xs">
                      <ShieldCheck className="w-3 h-3 mr-1" /> Gruhini Verified
                    </Badge>
                  )}
                  {seller.rewardLevel && (
                    <Badge variant="warm" className="text-xs">
                      <Star className="w-3 h-3 mr-1" /> {seller.rewardLevel}
                    </Badge>
                  )}
                </div>
                <p className="text-sm text-muted-foreground mt-2 flex items-center justify-center sm:justify-start">
                  <MapPin className="w-4 h-4 mr-1 text-ethnic-secondary" /> {seller.location}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>

        <div className="grid lg:grid-cols-3 gap-8">
          {/* Left Column: About & Info */}
          <div className="lg:col-span-1 space-y-6">
            <Card>
              <CardHeader>
                <CardTitle className="text-ethnic-primary">About {seller.displayName}</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-sm text-muted-foreground leading-relaxed">{seller.bio}</p>
                <div className="mt-4 space-y-2 text-sm">
                  <div className="flex items-center">
                    <Users className="w-4 h-4 mr-2 text-ethnic-secondary" />
                    <span>SHG: <strong>{seller.shgAssociation}</strong></span>
                  </div>
                  <div className="flex items-center">
                    <ShieldCheck className="w-4 h-4 mr-2 text-ethnic-secondary" />
                    <span>Hygiene: <strong>{seller.hygieneStatus}</strong></span>
                  </div>
                   <div className="flex items-center">
                    <ShoppingBag className="w-4 h-4 mr-2 text-ethnic-secondary" />
                    <span>Member Since: <strong>{seller.memberSince}</strong></span>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle className="text-ethnic-primary">Customer Reviews ({seller.reviews.length})</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                {seller.reviews.map(review => (
                  <div key={review.id} className="border-b pb-2 last:border-b-0">
                    <div className="flex items-center mb-1">
                      {[...Array(Math.floor(review.rating))].map((_, i) => (
                        <Star key={i} className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                      ))}
                      {review.rating % 1 !== 0 && <Star key="half" className="w-4 h-4 fill-yellow-400 text-yellow-400 opacity-50" />}
                      {/* Crude half star */}
                      <span className="ml-2 text-sm font-medium">{review.user}</span>
                    </div>
                    <p className="text-xs text-muted-foreground">{review.comment}</p>
                  </div>
                ))}
                {seller.reviews.length === 0 && <p className="text-sm text-muted-foreground">No reviews yet.</p>}
              </CardContent>
            </Card>
          </div>

          {/* Right Column: Products by this Seller */}
          <div className="lg:col-span-2">
            <h2 className="text-2xl font-semibold text-ethnic-primary mb-4">
              Products by {seller.displayName} ({sellerProducts.length})
            </h2>
            {sellerProducts.length > 0 ? (
              <div className="grid sm:grid-cols-2 xl:grid-cols-3 gap-4">
                {sellerProducts.map(product => (
                  <Card key={product.id} className="group hover:shadow-warm transition-all duration-300">
                    <CardContent className="p-0">
                      <div className="relative h-40 bg-gradient-to-br from-accent/20 to-primary/20 rounded-t-lg overflow-hidden">
                        <img
                          src={product.image}
                          alt={product.name}
                          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                          onError={(e) => {
                            e.currentTarget.src = "/placeholder.svg";
                          }}
                        />
                        {product.badge && (
                          <Badge variant="secondary" className="absolute top-2 left-2 bg-primary text-primary-foreground text-xs">
                            {product.badge}
                          </Badge>
                        )}
                      </div>
                      <div className="p-3">
                        <h3 className="font-semibold text-ethnic-primary text-sm mb-1 truncate group-hover:whitespace-normal">
                          {product.name}
                        </h3>
                        <div className="flex items-center gap-1 mb-1">
                          <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                          <span className="text-xs font-medium">{product.rating}</span>
                        </div>
                        <div className="flex items-center gap-1">
                          <span className="text-md font-bold text-ethnic-primary">
                            {product.price}
                          </span>
                          {product.originalPrice && (
                            <span className="text-xs text-muted-foreground line-through">
                              {product.originalPrice}
                            </span>
                          )}
                        </div>
                         <Button variant="ethnic" size="sm" className="w-full mt-2 text-xs">
                            View Product
                          </Button>
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </div>
            ) : (
              <p className="text-muted-foreground">This seller has no products listed yet.</p>
            )}
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default SellerProfile;
