import React from 'react';
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Link } from "react-router-dom";
import { Star } from 'lucide-react';
import { realProducts } from "@/data/products";

// Get top 5 products based on rating and category diversity
const getMostSoldProducts = () => {
  return realProducts
    .sort((a, b) => b.rating - a.rating)
    .slice(0, 5);
};

const MostSoldProducts: React.FC = () => {
  const mostSoldProducts = getMostSoldProducts();

  return (
    <section className="py-12 bg-amber-50">
      <div className="container mx-auto px-4">
        <h2 className="text-3xl font-bold font-heading text-center text-ethnic-primary mb-8">
          Our Most Loved Products
        </h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
          {mostSoldProducts.map((product) => (
            <Link to={`/explore?category=${encodeURIComponent(product.category)}&product_id=${product.id}`} key={product.id}> {/* Example Link */}
              <Card className="group hover:shadow-warm transition-all duration-300 overflow-hidden h-full flex flex-col">
                <CardContent className="p-0 flex flex-col flex-grow">
                  <div className="relative h-48 bg-gray-100">
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
                  <div className="p-4 flex flex-col flex-grow">
                    <h3 className="font-semibold text-ethnic-primary mb-1 text-md line-clamp-2">
                      {product.name}
                    </h3>
                    <p className="text-sm text-muted-foreground mb-1 text-xs">by {product.seller}</p>
                    <div className="flex items-center gap-1 mb-2">
                      <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                      <span className="text-xs font-medium">{product.rating}</span>
                    </div>
                    <p className="text-lg font-bold text-ethnic-primary mt-auto pt-2">
                      {product.price}
                    </p>
                    {/* Simplified: No Add to cart here, user clicks to explore */}
                  </div>
                </CardContent>
              </Card>
            </Link>
          ))}
        </div>
      </div>
    </section>
  );
};

export default MostSoldProducts;
