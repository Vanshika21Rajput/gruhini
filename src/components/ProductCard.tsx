import React from 'react';
import { Product } from '../types/Product';
import { Check, Package, Calendar, DollarSign, Tag } from 'lucide-react';

interface ProductCardProps {
  product: Product;
  isSelected: boolean;
  onSelect: (productId: string) => void;
}

const ProductCard: React.FC<ProductCardProps> = ({ product, isSelected, onSelect }) => {
  const handleCheckboxChange = () => {
    onSelect(product.id);
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(price);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'approved':
        return 'bg-green-100 text-green-800';
      case 'rejected':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-yellow-100 text-yellow-800';
    }
  };

  return (
    <div
      className={`relative bg-white rounded-lg shadow-md hover:shadow-lg transition-all duration-200 border-2 ${
        isSelected ? 'border-blue-500 ring-2 ring-blue-200' : 'border-gray-200'
      }`}
    >
      {/* Selection Checkbox */}
      <div className="absolute top-4 left-4 z-10">
        <label className="flex items-center cursor-pointer">
          <input
            type="checkbox"
            checked={isSelected}
            onChange={handleCheckboxChange}
            className="sr-only"
          />
          <div
            className={`w-6 h-6 rounded-full border-2 flex items-center justify-center transition-all duration-200 ${
              isSelected
                ? 'bg-blue-500 border-blue-500 text-white'
                : 'bg-white border-gray-300 hover:border-blue-400'
            }`}
          >
            {isSelected && <Check size={16} />}
          </div>
        </label>
      </div>

      {/* Status Badge */}
      <div className="absolute top-4 right-4 z-10">
        <span
          className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(
            product.status
          )}`}
        >
          {product.status}
        </span>
      </div>

      {/* Product Image */}
      <div className="aspect-w-16 aspect-h-9 bg-gray-100 rounded-t-lg overflow-hidden">
        <img
          src={product.image}
          alt={product.name}
          className="w-full h-48 object-cover"
          onError={(e) => {
            const target = e.target as HTMLImageElement;
            target.src = 'https://images.pexels.com/photos/230544/pexels-photo-230544.jpeg?auto=compress&cs=tinysrgb&w=400';
          }}
        />
      </div>

      {/* Product Details */}
      <div className="p-4">
        <h3 className="text-lg font-semibold text-gray-900 mb-2 line-clamp-2">
          {product.name}
        </h3>

        <p className="text-gray-600 text-sm mb-3 line-clamp-3">
          {product.description}
        </p>

        <div className="space-y-2">
          <div className="flex items-center text-sm text-gray-700">
            <DollarSign size={16} className="mr-2 text-green-600" />
            <span className="font-semibold">{formatPrice(product.price)}</span>
          </div>

          <div className="flex items-center text-sm text-gray-700">
            <Tag size={16} className="mr-2 text-blue-600" />
            <span>{product.category}</span>
          </div>

          {product.brand && (
            <div className="flex items-center text-sm text-gray-700">
              <Package size={16} className="mr-2 text-purple-600" />
              <span>{product.brand}</span>
            </div>
          )}

          {product.stock !== undefined && (
            <div className="flex items-center text-sm text-gray-700">
              <Package size={16} className="mr-2 text-orange-600" />
              <span>{product.stock} in stock</span>
            </div>
          )}

          <div className="flex items-center text-sm text-gray-500">
            <Calendar size={16} className="mr-2" />
            <span>{formatDate(product.createdAt)}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductCard;