import React, { useState, useEffect } from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import ImageUpload from '@/components/ImageUpload';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Label } from '@/components/ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Plus, Edit, Trash2, Eye, Package, TrendingUp, Users, Star, Loader2 } from 'lucide-react';
import { toast } from 'sonner';

interface Product {
  id: string;
  name: string;
  price: number;
  category: string;
  subcategory: string;
  description: string;
  image: string;
  status: 'pending' | 'approved' | 'rejected';
  stock: number;
  rating: number;
  orders: number;
  discount: string;
  verified: boolean;
}

const SellerDashboard = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [isAddingProduct, setIsAddingProduct] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isFetchingProducts, setIsFetchingProducts] = useState(true);
  const [newProduct, setNewProduct] = useState({
    name: '',
    price: '',
    category: '',
    subcategory: '',
    description: '',
    stock: '',
    image: '',
    rating: '',
    discount: '',
    verified: false
  });

  const categories = [
    'Homemade Food',
    'Natural Cosmetics',
    'Handmade Crafts',
    'Condiments',
    'Bread',
    'Sweets',
    'Snacks',
    'Beverages',
    'Dairy Products',
    'Spices'
  ];

  const subcategories: Record<string, string[]> = {
    'Homemade Food': ['Curries', 'Rice Dishes', 'Snacks', 'Sweets', 'Beverages'],
    'Natural Cosmetics': ['Skincare', 'Haircare', 'Body Care', 'Face Masks', 'Oils'],
    'Handmade Crafts': ['Decorative Items', 'Jewelry', 'Textiles', 'Pottery', 'Wood Work'],
    'Condiments': ['Pickles', 'Chutneys', 'Sauces', 'Spice Mixes', 'Preserves'],
    'Bread': ['Indian Bread', 'Western Bread', 'Buns', 'Rolls', 'Specialty Bread'],
    'Sweets': ['Traditional Sweets', 'Modern Desserts', 'Chocolates', 'Candies', 'Dry Fruits'],
    'Snacks': ['Fried Snacks', 'Baked Snacks', 'Healthy Snacks', 'Namkeen', 'Chips'],
    'Beverages': ['Traditional Drinks', 'Juices', 'Teas', 'Health Drinks', 'Smoothies'],
    'Dairy Products': ['Milk Products', 'Cheese', 'Yogurt', 'Butter', 'Paneer'],
    'Spices': ['Whole Spices', 'Ground Spices', 'Spice Blends', 'Herbs', 'Masalas']
  };

  // Fetch products from backend
  const fetchProducts = async () => {
    try {
      setIsFetchingProducts(true);

      const response = await fetch('https://gruhani5.onrender.com/get-seller-products', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
      });

      if (!response.ok) {
        if (response.status === 401) {
          // User not authenticated, show empty state or redirect
          console.log('User not authenticated, showing empty products');
          setProducts([]);
          return;
        }
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();

      // Transform backend data to match frontend Product interface
      const transformedProducts: Product[] = data.products?.map((product: any) => ({
        id: product.id || product._id,
        name: product.name,
        price: product.price,
        category: product.category,
        subcategory: product.subcategory || '',
        description: product.description || '',
        image: product.image,
        status: product.status || 'pending',
        stock: product.stock || 0,
        rating: product.rating || 0,
        orders: product.orders || 0,
        discount: product.discount || '',
        verified: product.verified || false
      })) || [];

      setProducts(transformedProducts);

    } catch (error) {
      console.error('Error fetching products:', error);
      // Don't show error toast on initial load if user is not authenticated
      if (error.message !== 'HTTP error! status: 401') {
        toast.error('Failed to load products. Please try again.');
      }
      setProducts([]);
    } finally {
      setIsFetchingProducts(false);
    }
  };

  // Load products when component mounts
  useEffect(() => {
    fetchProducts();
  }, []);

  const handleAddProduct = async () => {
    if (!newProduct.name || !newProduct.price || !newProduct.category) {
      toast.error('Please fill in all required fields');
      return;
    }

    if (!newProduct.image) {
      toast.error('Please upload a product image');
      return;
    }

    setIsLoading(true);

    try {
      // Prepare product data for backend
      const productData = {
        name: newProduct.name,
        price: parseFloat(newProduct.price),
        category: newProduct.category,
        subcategory: newProduct.subcategory,
        description: newProduct.description,
        image: newProduct.image, // This is now the Cloudinary URL
        stock: parseInt(newProduct.stock) || 0,
        status: 'pending',
        rating: parseFloat(newProduct.rating) || 0.0,
        discount: newProduct.discount,
        verified: newProduct.verified
      };

      console.log("Submitting product:", productData);

      // Make API call to backend
      const response = await fetch('https://gruhani5.onrender.com/add-product', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: "include",
        body: JSON.stringify(productData)
      });

      if (!response.ok) {
        if (response.status === 401) {
          toast.error('Please log in again');
          return;
        }
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();

      // Create product object for local state with backend response
      const product: Product = {
        id: result.id || result._id || Date.now().toString(),
        name: productData.name,
        price: productData.price,
        category: productData.category,
        subcategory: productData.subcategory,
        description: productData.description,
        image: productData.image,
        status: 'pending',
        stock: productData.stock,
        rating: productData.rating,
        orders: 0,
        discount: productData.discount,
        verified: productData.verified
      };

      // Update local state
      const updatedProducts = [...products, product];
      setProducts(updatedProducts);

      // Clear form
      setNewProduct({
        name: '',
        price: '',
        category: '',
        subcategory: '',
        description: '',
        stock: '',
        image: '',
        rating: '',
        discount: '',
        verified: false
      });
      setIsAddingProduct(false);

      toast.success('Product added successfully! It will be reviewed by our team.');

    } catch (error) {
      console.error('Error adding product:', error);
      toast.error('Failed to add product. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteProduct = async (id: string) => {
    try {
      // Make API call to backend to delete from database
      const response = await fetch(`https://gruhani5.onrender.com/delete-product/${id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      // Remove from local state
      const updatedProducts = products.filter(p => p.id !== id);
      setProducts(updatedProducts);

      toast.success('Product deleted successfully');

    } catch (error) {
      console.error('Error deleting product:', error);
      toast.error('Failed to delete product. Please try again.');
    }
  };

  const handleImageChange = (imageUrl: string) => {
    setNewProduct({ ...newProduct, image: imageUrl });
  };

  const stats = {
    totalProducts: products.length,
    approvedProducts: products.filter(p => p.status === 'approved').length,
    pendingProducts: products.filter(p => p.status === 'pending').length,
    totalOrders: products.reduce((sum, p) => sum + p.orders, 0),
    totalRevenue: products.reduce((sum, p) => sum + (p.price * p.orders), 0),
    averageRating: products.filter(p => p.rating > 0).reduce((sum, p) => sum + p.rating, 0) / products.filter(p => p.rating > 0).length || 0
  };

  return (
    <div className="min-h-screen bg-background">
      <Header />

      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-3xl font-bold font-heading text-ethnic-primary">Seller Dashboard</h1>
            <p className="text-muted-foreground">Manage your products and track your business</p>
          </div>
          <Button onClick={() => setIsAddingProduct(true)} className="flex items-center gap-2">
            <Plus className="h-4 w-4" />
            Add New Product
          </Button>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">Total Products</p>
                  <p className="text-2xl font-bold text-ethnic-primary">{stats.totalProducts}</p>
                </div>
                <Package className="h-8 w-8 text-primary" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">Total Orders</p>
                  <p className="text-2xl font-bold text-ethnic-primary">{stats.totalOrders}</p>
                </div>
                <TrendingUp className="h-8 w-8 text-trust-green" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">Total Revenue</p>
                  <p className="text-2xl font-bold text-ethnic-primary">₹{stats.totalRevenue.toLocaleString()}</p>
                </div>
                <Users className="h-8 w-8 text-accent" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">Avg. Rating</p>
                  <p className="text-2xl font-bold text-ethnic-primary">{stats.averageRating.toFixed(1)}</p>
                </div>
                <Star className="h-8 w-8 text-amber-500" />
              </div>
            </CardContent>
          </Card>
        </div>

        <Tabs defaultValue="products" className="space-y-6">
          <TabsList>
            <TabsTrigger value="products">My Products</TabsTrigger>
            <TabsTrigger value="orders">Orders</TabsTrigger>
            <TabsTrigger value="analytics">Analytics</TabsTrigger>
          </TabsList>

          <TabsContent value="products" className="space-y-6">
            {/* Add Product Form */}
            {isAddingProduct && (
              <Card>
                <CardHeader>
                  <CardTitle>Add New Product</CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="grid md:grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label>Product Name *</Label>
                      <Input
                        placeholder="Enter product name"
                        value={newProduct.name}
                        onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
                        disabled={isLoading}
                      />
                    </div>
                    <div className="space-y-2">
                      <Label>Price (₹) *</Label>
                      <Input
                        type="number"
                        placeholder="Enter price"
                        value={newProduct.price}
                        onChange={(e) => setNewProduct({ ...newProduct, price: e.target.value })}
                        disabled={isLoading}
                      />
                    </div>
                    <div className="space-y-2">
                      <Label>Category *</Label>
                      <Select onValueChange={(value) => setNewProduct({ ...newProduct, category: value })} disabled={isLoading}>
                        <SelectTrigger>
                          <SelectValue placeholder="Select category" />
                        </SelectTrigger>
                        <SelectContent>
                          {categories.map((category) => (
                            <SelectItem key={category} value={category}>
                              {category}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    </div>
                    <div className="space-y-2">
                      <Label>Subcategory</Label>
                      <Select
                        onValueChange={(value) => setNewProduct({ ...newProduct, subcategory: value })}
                        disabled={isLoading || !newProduct.category}
                      >
                        <SelectTrigger>
                          <SelectValue placeholder="Select subcategory" />
                        </SelectTrigger>
                        <SelectContent>
                          {newProduct.category && subcategories[newProduct.category]?.map((subcategory) => (
                            <SelectItem key={subcategory} value={subcategory}>
                              {subcategory}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    </div>
                    <div className="space-y-2">
                      <Label>Stock Quantity</Label>
                      <Input
                        type="number"
                        placeholder="Enter stock quantity"
                        value={newProduct.stock}
                        onChange={(e) => setNewProduct({ ...newProduct, stock: e.target.value })}
                        disabled={isLoading}
                      />
                    </div>
                    <div className="space-y-2">
                      <Label>Initial Rating</Label>
                      <Input
                        type="number"
                        step="0.1"
                        min="0"
                        max="5"
                        placeholder="Enter rating (0-5)"
                        value={newProduct.rating}
                        onChange={(e) => setNewProduct({ ...newProduct, rating: e.target.value })}
                        disabled={isLoading}
                      />
                    </div>
                    <div className="space-y-2">
                      <Label>Discount</Label>
                      <Input
                        placeholder="e.g., 10% or ₹50 off"
                        value={newProduct.discount}
                        onChange={(e) => setNewProduct({ ...newProduct, discount: e.target.value })}
                        disabled={isLoading}
                      />
                    </div>
                  </div>

                  <div className="space-y-2">
                    <Label>Description</Label>
                    <Textarea
                      placeholder="Describe your product..."
                      value={newProduct.description}
                      onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })}
                      rows={3}
                      disabled={isLoading}
                    />
                  </div>

                  <div className="flex items-center space-x-2">
                    <input
                      id="verified"
                      type="checkbox"
                      checked={newProduct.verified}
                      onChange={(e) => setNewProduct({ ...newProduct, verified: e.target.checked })}
                      disabled={isLoading}
                      className="rounded border-gray-300"
                    />
                    <Label htmlFor="verified">Mark as verified seller product</Label>
                  </div>

                  <div className="space-y-2">
                    <Label>Product Image *</Label>
                    <ImageUpload
                      currentImage={newProduct.image}
                      onImageChange={handleImageChange}
                      disabled={isLoading}
                    />
                  </div>

                  <div className="flex gap-2">
                    <Button onClick={handleAddProduct} disabled={isLoading || !newProduct.image}>
                      {isLoading && <Loader2 className="h-4 w-4 mr-2 animate-spin" />}
                      {isLoading ? 'Adding Product...' : 'Add Product'}
                    </Button>
                    <Button variant="outline" onClick={() => setIsAddingProduct(false)} disabled={isLoading}>
                      Cancel
                    </Button>
                  </div>
                </CardContent>
              </Card>
            )}

            {/* Loading State */}
            {isFetchingProducts && (
              <div className="flex items-center justify-center py-12">
                <div className="flex items-center gap-2">
                  <Loader2 className="h-6 w-6 animate-spin" />
                  <span>Loading your products...</span>
                </div>
              </div>
            )}

            {/* Empty State */}
            {!isFetchingProducts && products.length === 0 && (
              <div className="flex items-center justify-center py-12">
                <div className="text-center">
                  <Package className="h-16 w-16 text-muted-foreground mx-auto mb-4" />
                  <h3 className="text-lg font-semibold mb-2">No Products Yet</h3>
                  <p className="text-muted-foreground mb-4">Start by adding your first product to get started!</p>
                  <Button onClick={() => setIsAddingProduct(true)}>
                    <Plus className="h-4 w-4 mr-2" />
                    Add Your First Product
                  </Button>
                </div>
              </div>
            )}

            {/* Products List */}
            {!isFetchingProducts && products.length > 0 && (
              <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                {products.map((product) => (
                  <Card key={product.id} className="overflow-hidden">
                    <div className="relative h-48">
                      <img src={product.image} alt={product.name} className="w-full h-full object-cover" />
                      <Badge
                        className={`absolute top-2 right-2 ${
                          product.status === 'approved' ? 'bg-trust-green' :
                          product.status === 'pending' ? 'bg-amber-500' : 'bg-destructive'
                        }`}
                      >
                        {product.status}
                      </Badge>
                    </div>

                    <CardContent className="p-4">
                      <h3 className="font-semibold text-ethnic-primary mb-1">{product.name}</h3>
                      <p className="text-sm text-muted-foreground mb-2">{product.category}</p>
                      {product.subcategory && (
                        <p className="text-xs text-muted-foreground mb-2">• {product.subcategory}</p>
                      )}
                      <p className="text-lg font-bold text-trust-green mb-2">₹{product.price}</p>
                      {product.discount && (
                        <Badge variant="secondary" className="mb-2 text-xs">
                          {product.discount} OFF
                        </Badge>
                      )}

                      {product.status === 'approved' && (
                        <div className="flex items-center gap-4 text-sm text-muted-foreground mb-3">
                          <span>Stock: {product.stock}</span>
                          <span>Orders: {product.orders}</span>
                          {product.rating > 0 && (
                            <span className="flex items-center gap-1">
                              <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                              {product.rating}
                            </span>
                          )}
                          {product.verified && (
                            <Badge variant="outline" className="text-xs bg-trust-green/10 text-trust-green border-trust-green">
                              Verified
                            </Badge>
                          )}
                        </div>
                      )}

                      <div className="flex gap-2">
                        <Button variant="outline" size="sm" className="flex-1">
                          <Eye className="h-3 w-3 mr-1" />
                          View
                        </Button>
                        <Button variant="outline" size="sm" className="flex-1">
                          <Edit className="h-3 w-3 mr-1" />
                          Edit
                        </Button>
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleDeleteProduct(product.id)}
                          className="text-destructive hover:text-destructive"
                        >
                          <Trash2 className="h-3 w-3" />
                        </Button>
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </div>
            )}
          </TabsContent>

          <TabsContent value="orders">
            <Card>
              <CardHeader>
                <CardTitle>Recent Orders</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">Order management coming soon...</p>
              </CardContent>
            </Card>
          </TabsContent>

          <TabsContent value="analytics">
            <Card>
              <CardHeader>
                <CardTitle>Business Analytics</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">Analytics dashboard coming soon...</p>
              </CardContent>
            </Card>
          </TabsContent>
        </Tabs>
      </div>

      <Footer />
    </div>
  );
};

export default SellerDashboard;