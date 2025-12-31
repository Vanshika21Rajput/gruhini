import React, { useState, useEffect } from 'react';
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Search, ShoppingBag, Filter, Star, Heart } from 'lucide-react';
import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';

interface Product {
    id: number;
    name: string;
    price: number;
    category: string;
    subcategory: string;
    description: string;
    image: string; // Base64 or URL
    stock: number;
    status: string;
    rating: number;
    discount: string;
    verified: boolean;
    quantity: number;
    chef?: string; // Optional if not in backend but used in UI
}

const ExploreProducts = () => {
    const [products, setProducts] = useState<Product[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('All');
    const { addToCart } = useCart();

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        setIsLoading(true);
        try {
            const response = await fetch('https://gruhini2.onrender.com/explore');
            if (response.ok) {
                const data = await response.json();
                setProducts(data);
            } else {
                console.error("Failed to fetch products");
            }
        } catch (error) {
            console.error("Error fetching products:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const categories = ['All', 'Thalis', 'Snacks', 'Desserts', 'Pickles', 'Crafts'];

    const filteredProducts = products.filter(product => {
        const matchesSearch = product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
            product.description.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesCategory = selectedCategory === 'All' || product.category === selectedCategory;
        return matchesSearch && matchesCategory;
    });

    return (
        <div className="min-h-screen bg-neutral-50 p-6 md:p-8 space-y-8">
            {/* Header Section */}
            <div className="flex flex-col md:flex-row justify-between items-center gap-4">
                <div>
                    <h1 className="text-3xl font-heading font-bold text-ethnic-primary">Explore Products</h1>
                    <p className="text-muted-foreground mt-1">Discover authentic homemade treasures from across India</p>
                </div>

                <div className="flex items-center gap-2">
                    {/* Search and Filters could go here */}
                </div>
            </div>

            {/* Search & Filter Bar */}
            <div className="bg-white p-4 rounded-xl shadow-sm border sticky top-4 z-30">
                <div className="flex flex-col md:flex-row gap-4 items-center justify-between">
                    <div className="relative w-full md:w-96">
                        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                        <Input
                            placeholder="Search for dishes, snacks, or crafts..."
                            className="pl-10 bg-neutral-50"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>

                    <div className="flex gap-2 overflow-x-auto w-full md:w-auto pb-2 md:pb-0 no-scrollbar">
                        {categories.map(category => (
                            <Button
                                key={category}
                                variant={selectedCategory === category ? "default" : "outline"}
                                className={`rounded-full ${selectedCategory === category ? 'bg-ethnic-primary hover:bg-ethnic-primary/90' : ''}`}
                                onClick={() => setSelectedCategory(category)}
                            >
                                {category}
                            </Button>
                        ))}
                    </div>
                </div>
            </div>

            {/* Products Grid */}
            {isLoading ? (
                <div className="text-center py-20">Loading products...</div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                    {filteredProducts.map((product) => (
                        <Card key={product.id} className="group overflow-hidden hover:shadow-lg transition-all duration-300 border-none bg-white">
                            <div className="relative aspect-[4/3] overflow-hidden">
                                <img
                                    src={`data:image/jpeg;base64,${product.image}`}
                                    alt={product.name}
                                    className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
                                    onError={(e) => {
                                        (e.target as HTMLImageElement).src = '/api/placeholder/400/320';
                                    }}
                                />
                                <div className="absolute top-2 right-2">
                                    <Button size="icon" variant="secondary" className="rounded-full bg-white/80 backdrop-blur-sm hover:bg-white text-red-500 h-8 w-8">
                                        <Heart className="h-4 w-4" />
                                    </Button>
                                </div>
                                {product.verified && (
                                    <Badge className="absolute top-2 left-2 bg-trust-green/90 text-white border-none flex items-center gap-1">
                                        <Badge className="h-3 w-3 fill-current" /> Verified
                                    </Badge>
                                )}
                            </div>

                            <CardContent className="p-4">
                                <div className="flex justify-between items-start mb-2">
                                    <div>
                                        <h3 className="font-heading font-semibold text-lg text-gray-900 line-clamp-1">{product.name}</h3>
                                        <p className="text-sm text-muted-foreground">{product.chef || 'Home Chef'}</p>
                                    </div>
                                    <div className="flex items-center bg-green-50 px-2 py-1 rounded text-green-700 text-xs font-bold">
                                        {product.rating} <Star className="h-3 w-3 fill-current ml-1" />
                                    </div>
                                </div>

                                <p className="text-sm text-gray-600 line-clamp-2 mb-4 h-10">
                                    {product.description}
                                </p>

                                <div className="flex items-center justify-between mt-4">
                                    <div className="flex flex-col">
                                        <span className="text-xs text-muted-foreground mr-1">Price</span>
                                        <span className="font-bold text-xl text-ethnic-primary">₹{product.price}</span>
                                    </div>
                                    <Button
                                        className="rounded-full bg-ethnic-secondary hover:bg-ethnic-secondary/90 text-white shadow-md shadow-orange-200"
                                        onClick={() => addToCart({
                                            productid: product.id,
                                            name: product.name,
                                            price: product.price,
                                            quantity: 1,
                                            image: product.image
                                        })}
                                    >
                                        <ShoppingBag className="h-4 w-4 mr-2" />
                                        Add
                                    </Button>
                                </div>
                            </CardContent>
                        </Card>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ExploreProducts;
