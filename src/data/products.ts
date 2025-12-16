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

// ================= REAL PRODUCTS =================

export const realProducts: Product[] = [
  {
    id: 1,
    name: "Atte ke Laddu",
    seller: "Sunita's Kitchen",
    price: "₹249",
    originalPrice: "₹299",
    discount: "17% OFF",
    rating: 4.8,
    deliveryTime: "45 min",
    image: "/products/atte-ke-laddu.jpeg",
    badge: "Bestseller",
    verified: true,
    category: "Homemade Food",
    subcategory: "Traditional Sweets",
    description: "Authentic Atte ke Laddu cooked fresh at home."
  },
  {
    id: 2,
    name: "Besan Ladoo",
    seller: "Sharma Sweets",
    price: "₹199",
    rating: 4.6,
    deliveryTime: "30 min",
    image: "/products/besan-ladoo.jpg",
    badge: "Pure Ghee",
    verified: true,
    category: "Homemade Food",
    subcategory: "Traditional Sweets",
    description: "Traditional besan ladoos made with pure desi ghee."
  }
];
