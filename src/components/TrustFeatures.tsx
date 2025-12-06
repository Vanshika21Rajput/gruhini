import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { ShieldCheck, Truck, Users, Globe, Heart, Award } from "lucide-react";

const TrustFeatures = () => {
  const features = [
    {
      icon: <ShieldCheck className="h-8 w-8 text-trust-green" />,
      title: "Chemical-free products",
      description: "All natural ingredients, no harmful chemicals or preservatives"
    },
    {
      icon: <Users className="h-8 w-8 text-primary" />,
      title: "Local women entrepreneurs",
      description: "Supporting skilled women from your community and beyond"
    },
    {
      icon: <Globe className="h-8 w-8 text-accent" />,
      title: "International shipping",
      description: "Delivering authentic taste to homesick hearts worldwide"
    },
    {
      icon: <Truck className="h-8 w-8 text-ethnic-primary" />,
      title: "Fresh & Fast delivery",
      description: "Quick delivery with proper packaging to maintain freshness"
    },
    {
      icon: <Heart className="h-8 w-8 text-destructive" />,
      title: "Made with love",
      description: "Every product crafted with care and traditional methods"
    },
    {
      icon: <Award className="h-8 w-8 text-amber-500" />,
      title: "Quality assured",
      description: "Rigorous quality checks and verified seller standards"
    }
  ];

  return (
    <section className="py-16 bg-background">
      <div className="container mx-auto px-4">
        <div className="text-center mb-12">
          <Badge variant="outline" className="bg-trust-green/20 text-ethnic-primary border-trust-green mb-4">
            🤝 Why Choose गृहिणी
          </Badge>
          <h2 className="font-heading text-3xl md:text-4xl font-bold text-ethnic-primary mb-4">
            Trusted by Thousands Worldwide
          </h2>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            Experience the authentic taste of home with our carefully curated network of verified sellers
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {features.map((feature, index) => (
            <Card key={index} className="group hover:shadow-warm transition-all duration-300 border-0 bg-gradient-to-br from-background to-muted/30 hover:scale-105">
              <CardContent className="p-6 text-center">
                <div className="mb-4 flex justify-center">
                  {feature.icon}
                </div>
                <h3 className="font-semibold text-ethnic-primary mb-2 text-lg">
                  {feature.title}
                </h3>
                <p className="text-muted-foreground text-sm leading-relaxed">
                  {feature.description}
                </p>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </section>
  );
};

export default TrustFeatures;