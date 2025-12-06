import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Link } from "react-router-dom";

const CustomizationSection = () => {
  const customizationFeatures = [
    {
      icon: "🌶️",
      title: "Taste Customization",
      description: "Adjust spice levels, sweetness, and flavors to match your exact preferences",
      badge: "Your Way"
    },
    {
      icon: "👩‍🍳",
      title: "Personal Chef Service",
      description: "Connect with local aunties who cook exactly like your mom used to",
      badge: "Homestyle"
    },
    {
      icon: "📝",
      title: "Custom Recipes",
      description: "Share your family recipes and we'll find the perfect cook to make them",
      badge: "Bespoke"
    },
    {
      icon: "🎁",
      title: "Special Occasions",
      description: "Custom meal plans for festivals, birthdays, or homesick moments",
      badge: "Festive"
    }
  ];

  return (
    <section className="py-20 bg-gradient-warm">
      <div className="container mx-auto px-6">
        {/* Header */}
        <div className="text-center mb-16">
          <Badge variant="outline" className="bg-accent/20 text-ethnic-primary border-accent px-4 py-2 mb-6">
            ✨ Personalized Just for You
          </Badge>
          <h2 className="font-heading text-4xl md:text-5xl font-bold text-ethnic-primary mb-6">
            Made Exactly How You Like It
          </h2>
          <p className="text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed">
            Miss your mom's cooking? Want it less spicy or extra sweet? Our local chefs customize 
            every dish to match your taste preferences, dietary needs, and nostalgic cravings.
          </p>
        </div>

        {/* Features Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 mb-16">
          {customizationFeatures.map((feature, index) => (
            <Card 
              key={feature.title}
              className="group cursor-pointer hover:shadow-warm transition-all duration-300 border-0 bg-white/80 backdrop-blur hover:scale-105"
            >
              <CardContent className="p-8 text-center">
                <div className="text-5xl mb-4">{feature.icon}</div>
                <Badge variant="secondary" className="bg-primary/10 text-primary text-xs mb-4">
                  {feature.badge}
                </Badge>
                <h3 className="font-heading text-xl font-semibold text-ethnic-primary mb-3">
                  {feature.title}
                </h3>
                <p className="text-muted-foreground text-sm leading-relaxed">
                  {feature.description}
                </p>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* CTA */}
        <div className="text-center">
          <div className="bg-white/90 backdrop-blur rounded-2xl p-8 max-w-2xl mx-auto">
            <h3 className="font-heading text-2xl font-semibold text-ethnic-primary mb-4">
              Ready to Taste Home Again?
            </h3>
            <p className="text-muted-foreground mb-6">
              Tell us your preferences and we'll connect you with the perfect local chef
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link to="/customize-meal">
                <Button variant="hero" size="lg" className="rounded-full">
                  🍛 Customize Your Meal
                </Button>
              </Link>
              <Button variant="trust" size="lg" className="rounded-full">
                📞 Talk to a Chef
              </Button>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default CustomizationSection;