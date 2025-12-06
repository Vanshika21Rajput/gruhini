import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Link } from "react-router-dom";
import heroImage from "@/assets/hero-homemade-food.jpg";

const HeroSection = () => {
  return (
    <section className="relative min-h-screen bg-gradient-warm overflow-hidden">
      {/* Background Image */}
      <div className="absolute inset-0">
        <img
          src={heroImage}
          alt="Homemade delicacies"
          className="w-full h-full object-cover opacity-40"
        />
        <div className="absolute inset-0 bg-gradient-to-br from-background/85 via-background/70 to-background/50"></div>
      </div>

      {/* Content */}
      <div className="relative container mx-auto px-6 lg:px-8 py-24 flex items-center justify-center min-h-screen">
        <div className="max-w-4xl mx-auto text-center animate-fadeIn">
          {/* Badge */}
          <div className="mb-12">
            <Badge variant="outline" className="bg-accent/30 text-ethnic-primary border-accent/50 px-6 py-3 text-sm font-medium rounded-full">
              🏠 Authentic • Homemade • Trusted
            </Badge>
          </div>

          {/* Main Heading */}
          <h1 className="font-heading text-5xl md:text-7xl lg:text-8xl font-bold text-ethnic-primary mb-8 leading-tight">
            घर की रसोई से,
            <br />
            <span className="text-primary">दिल तक का सफर</span>
          </h1>

          {/* Subtitle */}
          <p className="text-xl md:text-2xl text-muted-foreground mb-16 leading-relaxed max-w-3xl mx-auto">
            Connecting you with authentic homemade products from local women entrepreneurs. 
            From mom's recipes to artisan crafts - delivered fresh to your doorstep, 
            <span className="text-primary font-semibold"> anywhere in the world</span>.
          </p>

          {/* CTA Buttons */}
          <div className="flex flex-col sm:flex-row items-center justify-center gap-6 mb-20">
            <Link to="/explore">
              <Button variant="hero" size="lg" className="text-lg px-10 py-4 rounded-full">
                🛒 Explore Products
              </Button>
            </Link>
            <Button variant="trust" size="lg" className="text-lg px-10 py-4 rounded-full">
              👩‍🍳 Become a Seller
            </Button>
          </div>

          {/* Stats */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-12 pt-12 border-t border-border/30 max-w-2xl mx-auto">
            <div className="text-center">
              <div className="text-4xl font-bold text-ethnic-primary mb-2">1000+</div>
              <div className="text-base text-muted-foreground">Happy Customers</div>
            </div>
            <div className="text-center">
              <div className="text-4xl font-bold text-ethnic-primary mb-2">50+</div>
              <div className="text-base text-muted-foreground">Local Sellers</div>
            </div>
            <div className="text-center">
              <div className="text-4xl font-bold text-ethnic-primary mb-2">25+</div>
              <div className="text-base text-muted-foreground">Countries Served</div>
            </div>
          </div>
        </div>
      </div>

      {/* Floating Elements */}
      <div className="absolute top-32 right-16 w-24 h-24 bg-accent/15 rounded-full blur-xl animate-float"></div>
      <div className="absolute bottom-40 left-16 w-32 h-32 bg-trust-green/15 rounded-full blur-xl animate-float" style={{ animationDelay: '1s' }}></div>
      <div className="absolute top-1/2 right-8 w-16 h-16 bg-primary/15 rounded-full blur-xl animate-float" style={{ animationDelay: '2s' }}></div>
    </section>
  );
};

export default HeroSection;