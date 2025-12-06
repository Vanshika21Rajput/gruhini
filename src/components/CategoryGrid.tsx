import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Link } from "react-router-dom";
import craftsImage from "@/assets/crafts-collection.jpg";
import cosmeticsImage from "@/assets/natural-cosmetics.jpg";

const categories = [
  {
    title: "Ghar ka Khana",
    subtitle: "Complete Home-style Meals",
    icon: "🏠",
    count: "500+ dishes",
    description: "Full meals just like mom makes - customizable taste & portions",
    image: "/placeholder.svg",
    gradient: "from-orange-100 to-red-100",
    badge: "Most Loved",
    featured: true
  },
  {
    title: "Homemade Food",
    subtitle: "Pickles, Sweets, Tiffins",
    icon: "🍛",
    count: "200+ items",
    description: "Authentic recipes passed down generations",
    image: "/placeholder.svg",
    gradient: "from-amber-100 to-orange-100",
    badge: "Most Popular"
  },
  {
    title: "Natural Cosmetics",
    subtitle: "Soaps, Balms, Skincare",
    icon: "🧴",
    count: "80+ items",
    description: "Chemical-free beauty essentials",
    image: cosmeticsImage,
    gradient: "from-green-100 to-emerald-100",
    badge: "Chemical Free"
  },
  {
    title: "Handmade Crafts",
    subtitle: "Rakhis, Diyas, Decor",
    icon: "🎨",
    count: "150+ items",
    description: "Artistic creations by skilled artisans",
    image: craftsImage,
    gradient: "from-purple-100 to-pink-100",
    badge: "Artisan Made"
  },
  {
    title: "Festival Specials",
    subtitle: "Seasonal Collections",
    icon: "🪔",
    count: "50+ items",
    description: "Celebrate with authentic traditions",
    image: "/placeholder.svg",
    gradient: "from-rose-100 to-red-100",
    badge: "Limited Time"
  },
  {
    title: "Kitchen Essentials",
    subtitle: "Masalas, Incense",
    icon: "🌶️",
    count: "60+ items",
    description: "Daily needs from trusted sources",
    image: "/placeholder.svg",
    gradient: "from-yellow-100 to-amber-100",
    badge: "Fresh Daily"
  },
  {
    title: "Student Tiffins",
    subtitle: "Healthy Meal Plans",
    icon: "🍱",
    count: "30+ plans",
    description: "Nutritious meals for busy students",
    image: "/placeholder.svg",
    gradient: "from-blue-100 to-cyan-100",
    badge: "Worldwide"
  }
];

const CategoryGrid = () => {
  return (
    <section className="py-16 bg-background">
      <div className="container mx-auto px-4">
        {/* Header */}
        <div className="text-center mb-12">
          <h2 className="font-heading text-3xl md:text-4xl font-bold text-ethnic-primary mb-4">
            Discover Authentic Categories
          </h2>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            From traditional recipes to artistic crafts, explore our carefully curated collection 
            of homemade products by local women entrepreneurs.
          </p>
        </div>

        {/* Categories Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {categories.map((category, index) => (
            <Card 
              key={category.title}
              className={`group cursor-pointer hover:shadow-warm transition-all duration-300 border-0 bg-gradient-to-br from-background to-muted/30 hover:scale-105 ${
                category.featured ? 'md:col-span-2 lg:col-span-2 ring-2 ring-primary/20' : ''
              }`}
            >
              <CardContent className="p-0">
                {/* Image Section */}
                <div className={`relative overflow-hidden rounded-t-lg ${
                  category.featured ? 'h-64' : 'h-48'
                }`}>
                  <div className={`absolute inset-0 bg-gradient-to-br ${category.gradient} opacity-80`}></div>
                  {category.image !== "/placeholder.svg" && (
                    <img
                      src={category.image}
                      alt={category.title}
                      className="w-full h-full object-cover"
                    />
                  )}
                  <div className="absolute inset-0 flex items-center justify-center">
                    <span className={`opacity-60 ${category.featured ? 'text-8xl' : 'text-6xl'}`}>
                      {category.icon}
                    </span>
                  </div>
                  
                  {/* Badge */}
                  <div className="absolute top-3 left-3">
                    <Badge 
                      variant={category.featured ? "default" : "secondary"} 
                      className={category.featured ? 
                        "bg-primary text-primary-foreground text-xs animate-pulse" : 
                        "bg-white/90 text-ethnic-primary text-xs"
                      }
                    >
                      {category.badge}
                    </Badge>
                  </div>
                  
                  {/* Count */}
                  <div className="absolute bottom-3 right-3">
                    <Badge variant="outline" className="bg-white/90 text-ethnic-primary border-0 text-xs">
                      {category.count}
                    </Badge>
                  </div>
                </div>

                {/* Content Section */}
                <div className="p-6">
                  <h3 className="font-heading text-xl font-semibold text-ethnic-primary mb-2">
                    {category.title}
                  </h3>
                  <p className="text-primary font-medium text-sm mb-2">
                    {category.subtitle}
                  </p>
                  <p className="text-muted-foreground text-sm leading-relaxed">
                    {category.description}
                  </p>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* CTA */}
        <div className="text-center mt-12">
          <p className="text-muted-foreground mb-4">
            Can't find what you're looking for?
          </p>
          <Link to="/request-custom">
            <Badge variant="outline" className="bg-accent/20 text-ethnic-primary border-accent px-4 py-2 cursor-pointer hover:bg-accent/30 transition-colors">
              💬 Request Custom Products
            </Badge>
          </Link>
        </div>
      </div>
    </section>
  );
};

export default CategoryGrid;