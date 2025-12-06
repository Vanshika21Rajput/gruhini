// Real product data based on the provided images
export interface Product {
  id: number;
  name: string;
  seller: string;
  price: string;
  originalPrice?: string;
  discount?: string;
  rating: number;
  deliveryTime: string;
  image: string;
  badge: string;
  verified: boolean;
  category: string;
  subcategory: string;
  description: string;
  kitchenVideoUrl?: string;
}

// Categories and subcategories
export const categories = [
  {
    name: "All",
    icon: "🏠",
    subcategories: []
  },
  {
    name: "Homemade Food",
    icon: "🍛",
    subcategories: [
      "Traditional Meals",
      "Cakes & Desserts",
      "Traditional Sweets",
      "Namkeen & Snacks",
      "Gujarati Snacks",
      "Vrat Food",
      "Festival Sweets"
    ]
  },
  {
    name: "Natural Cosmetics",
    icon: "🧴",
    subcategories: [
      "Herbal Soaps",
      "Natural Skincare",
      "Hair Care Products"
    ]
  },
  {
    name: "Handmade Crafts",
    icon: "🎨",
    subcategories: [
      "Festival Diyas",
      "Festival Items",
      "Home Decor",
      "Traditional Crafts"
    ]
  }
];