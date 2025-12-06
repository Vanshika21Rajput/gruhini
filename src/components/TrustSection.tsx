import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import trustedSellerImage from "@/assets/trusted-seller.jpg";

const TrustSection = () => {
  return (
    <section className="py-16 bg-gradient-warm">
      <div className="container mx-auto px-4">
        <div className="grid lg:grid-cols-2 gap-12 items-center">
          {/* Content */}
          <div className="space-y-6">
            <Badge variant="outline" className="bg-trust-green/20 text-ethnic-primary border-trust-green">
              🤝 Built on Trust
            </Badge>
            
            <h2 className="font-heading text-3xl md:text-4xl font-bold text-ethnic-primary">
              Supporting Local Women Entrepreneurs
            </h2>
            
            <p className="text-lg text-muted-foreground leading-relaxed">
              Every purchase supports local women who pour their heart into creating authentic, 
              homemade products. From busy students abroad craving home-cooked meals to families 
              seeking chemical-free alternatives - we bridge the gap with trust and quality.
            </p>

            <div className="space-y-4">
              <div className="flex items-start space-x-3">
                <div className="w-6 h-6 bg-trust-green rounded-full flex items-center justify-center flex-shrink-0 mt-0.5">
                  <span className="text-white text-sm">✓</span>
                </div>
                <div>
                  <h4 className="font-semibold text-ethnic-primary">Verified Sellers</h4>
                  <p className="text-muted-foreground text-sm">Every seller is personally verified and trained on quality standards</p>
                </div>
              </div>
              
              <div className="flex items-start space-x-3">
                <div className="w-6 h-6 bg-primary rounded-full flex items-center justify-center flex-shrink-0 mt-0.5">
                  <span className="text-white text-sm">✓</span>
                </div>
                <div>
                  <h4 className="font-semibold text-ethnic-primary">Quality Assured</h4>
                  <p className="text-muted-foreground text-sm">Fresh ingredients, traditional methods, and hygienic packaging</p>
                </div>
              </div>
              
              <div className="flex items-start space-x-3">
                <div className="w-6 h-6 bg-accent rounded-full flex items-center justify-center flex-shrink-0 mt-0.5">
                  <span className="text-white text-sm">✓</span>
                </div>
                <div>
                  <h4 className="font-semibold text-ethnic-primary">Global Delivery</h4>
                  <p className="text-muted-foreground text-sm">Secure packaging for international shipping to homesick students</p>
                </div>
              </div>
            </div>

            <div className="flex flex-col sm:flex-row gap-4 pt-4">
              <Button variant="ethnic" size="lg">
                Meet Our Sellers
              </Button>
              <Button variant="outline" size="lg">
                Read Success Stories
              </Button>
            </div>
          </div>

          {/* Image and Stats */}
          <div className="space-y-6">
            <Card className="overflow-hidden border-0 shadow-warm">
              <CardContent className="p-0">
                <div className="relative">
                  <img
                    src={trustedSellerImage}
                    alt="Trusted local seller"
                    className="w-full h-80 object-cover"
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-black/40 via-transparent to-transparent"></div>
                  <div className="absolute bottom-4 left-4 text-white">
                    <h4 className="font-semibold text-lg">Meera Aunty</h4>
                    <p className="text-sm opacity-90">Famous for her mango pickles</p>
                    <Badge variant="secondary" className="bg-white/20 text-white border-0 mt-2">
                      ⭐ 4.9 • 500+ orders
                    </Badge>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Quick Stats */}
            <div className="grid grid-cols-2 gap-4">
              <Card className="bg-trust-green/10 border-trust-green/20">
                <CardContent className="p-4 text-center">
                  <div className="text-2xl font-bold text-ethnic-primary">₹2L+</div>
                  <div className="text-sm text-muted-foreground">Earned by sellers</div>
                </CardContent>
              </Card>
              <Card className="bg-accent/10 border-accent/20">
                <CardContent className="p-4 text-center">
                  <div className="text-2xl font-bold text-ethnic-primary">98%</div>
                  <div className="text-sm text-muted-foreground">Customer satisfaction</div>
                </CardContent>
              </Card>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default TrustSection;