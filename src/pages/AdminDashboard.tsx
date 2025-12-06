import React, { useState, useEffect } from 'react';
import { Check, X, RefreshCw, Star, Package, Tag, Image, Clock } from 'lucide-react';

// Updated Order interface to match backend fields exactly
interface Order {
  id: string;
  name: string;
  description: string;
  verified: boolean;
  price: number;
  rating: number;
  badge: string;
  deliverytime: string;
  image: string;
  status: string;
  category: string;
  subcategory: string;
  stock: number;
  discount: string;
}

const AdminDashboard: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [selectedOrders, setSelectedOrders] = useState<Set<string>>(new Set());
  const [loading, setLoading] = useState(false);
  const [actionLoading, setActionLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [toast, setToast] = useState<{ message: string; type: 'success' | 'error' | 'info' } | null>(null);

  const API_BASE_URL = 'https://gruhani3.onrender.com';

  useEffect(() => {
    fetchPendingOrders();
  }, []);

  const fetchPendingOrders = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/view-pending`, {
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      if (!response.ok) throw new Error('Failed to fetch pending orders');
      const data = await response.json();
      setOrders(data);
    } catch (err) {
      setError((err as Error).message);
      showToast((err as Error).message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleOrderSelect = (orderId: string) => {
    const newSelected = new Set(selectedOrders);
    if (newSelected.has(orderId)) {
      newSelected.delete(orderId);
    } else {
      newSelected.add(orderId);
    }
    setSelectedOrders(newSelected);
  };

  const handleSelectAll = () => {
    if (selectedOrders.size === orders.length) {
      setSelectedOrders(new Set());
    } else {
      setSelectedOrders(new Set(orders.map(o => o.id)));
    }
  };

  const handleBulkAction = async (action: 'accept' | 'reject') => {
    if (selectedOrders.size === 0) {
      showToast('Please select at least one order', 'error');
      return;
    }
    setActionLoading(true);
    try {
      const endpoint = action === 'accept'
        ? `${API_BASE_URL}/accept-item`
        : `${API_BASE_URL}/reject-item`;
      const requestBody = {
        selectedOrders: Array.from(selectedOrders).map(id => ({ id }))
      };
      const response = await fetch(endpoint, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestBody)
      });
      if (!response.ok) throw new Error(`Failed to ${action} orders`);
      showToast(`Successfully ${action === 'accept' ? 'accepted' : 'rejected'} ${selectedOrders.size} order(s)`, 'success');
      // Remove processed orders from local state
      setOrders(prev => prev.filter(order => !selectedOrders.has(order.id)));
      setSelectedOrders(new Set());
    } catch (err) {
      showToast((err as Error).message, 'error');
    } finally {
      setActionLoading(false);
    }
  };

  const showToast = (message: string, type: 'success' | 'error' | 'info') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3000);
  };

  const renderStars = (rating: number) => {
    return (
      <div className="flex items-center">
        {[1, 2, 3, 4, 5].map((star) => (
          <Star
            key={star}
            size={16}
            className={star <= rating ? 'text-yellow-400 fill-current' : 'text-gray-300'}
          />
        ))}
        <span className="ml-1 text-sm text-gray-600">({rating})</span>
      </div>
    );
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-white shadow">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="py-6 flex items-center justify-between">
            <div>
              <h2 className="text-2xl font-bold leading-7 text-gray-900 sm:text-3xl sm:truncate">
                Admin Dashboard
              </h2>
              <p className="mt-1 text-sm text-gray-500">Manage and approve pending product orders</p>
            </div>
            <button
              onClick={fetchPendingOrders}
              disabled={loading}
              className="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50"
            >
              <RefreshCw className={`mr-2 h-4 w-4${loading ? ' animate-spin' : ''}`} />
              Refresh
            </button>
          </div>
        </div>
      </div>

      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div className="bg-white shadow rounded-lg mb-6">
          <div className="px-6 py-4 flex items-center justify-between">
            <div className="flex items-center">
              <button
                onClick={handleSelectAll}
                className="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
              >
                <Check className="mr-2 h-4 w-4" />
                {selectedOrders.size === orders.length ? 'Deselect All' : 'Select All'}
              </button>
              <span className="ml-4 text-sm text-gray-600">
                {selectedOrders.size} of {orders.length} selected
              </span>
            </div>
            <div className="flex space-x-3">
              <button
                onClick={() => handleBulkAction('accept')}
                disabled={selectedOrders.size === 0 || actionLoading}
                className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {actionLoading ? (
                  <RefreshCw className="mr-2 h-4 w-4 animate-spin" />
                ) : (
                  <Check className="mr-2 h-4 w-4" />
                )}
                Accept
              </button>
              <button
                onClick={() => handleBulkAction('reject')}
                disabled={selectedOrders.size === 0 || actionLoading}
                className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {actionLoading ? (
                  <RefreshCw className="mr-2 h-4 w-4 animate-spin" />
                ) : (
                  <X className="mr-2 h-4 w-4" />
                )}
                Reject
              </button>
            </div>
          </div>
        </div>

        {loading ? (
          <div className="flex items-center justify-center py-12">
            <RefreshCw className="h-8 w-8 animate-spin text-blue-500" />
            <span className="ml-2 text-blue-500">Loading pending orders...</span>
          </div>
        ) : error ? (
          <div className="text-center py-12 text-red-500">{error}</div>
        ) : orders.length === 0 ? (
          <div className="text-center py-12 text-gray-500">No pending orders found.</div>
        ) : (
          <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
            {orders.map((order) => (
              <div
                key={order.id}
                className={`relative bg-white rounded-lg shadow-md border-2 transition-all duration-200 ${selectedOrders.has(order.id) ? 'border-blue-500 ring-2 ring-blue-200' : 'border-gray-200 hover:border-gray-300'}`}
              >
                {/* Selection Checkbox */}
                <div className="absolute top-4 left-4 z-10">
                  <label className="flex items-center cursor-pointer">
                    <input
                      type="checkbox"
                      checked={selectedOrders.has(order.id)}
                      onChange={() => handleOrderSelect(order.id)}
                      className="sr-only"
                    />
                    <div
                      className={`w-6 h-6 rounded-full border-2 flex items-center justify-center transition-all duration-200 ${selectedOrders.has(order.id)
                        ? 'bg-blue-500 border-blue-500 text-white'
                        : 'bg-white border-gray-300 hover:border-blue-400'
                        }`}
                    >
                      {selectedOrders.has(order.id) && <Check size={16} />}
                    </div>
                  </label>
                </div>

                {/* Verified Badge */}
                {order.verified && (
                  <div className="absolute top-4 right-4 z-10">
                    <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">
                      <Check size={12} className="mr-1" />
                      Verified
                    </span>
                  </div>
                )}

                {/* Product Image */}
                <div className="relative h-48 bg-gray-100 rounded-t-lg overflow-hidden">
                  {order.image ? (
                    <img
                      src={order.image}
                      alt={order.name}
                      className="w-full h-full object-cover"
                      onError={(e) => {
                        const target = e.target as HTMLImageElement;
                        target.style.display = 'none';
                        target.nextElementSibling?.classList.remove('hidden');
                      }}
                    />
                  ) : null}
                  <div className={`${order.image ? 'hidden' : ''} flex items-center justify-center h-full`}>
                    <Image size={48} className="text-gray-400" />
                  </div>

                  {/* Discount Badge */}
                  {order.discount && order.discount !== '0' && (
                    <div className="absolute bottom-2 left-2">
                      <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-red-100 text-red-800">
                        {order.discount}% OFF
                      </span>
                    </div>
                  )}

                  {/* Badge */}
                  {order.badge && (
                    <div className="absolute top-2 left-2">
                      <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                        {order.badge}
                      </span>
                    </div>
                  )}
                </div>

                {/* Product Details */}
                <div className="p-6">
                  <div className="flex items-start justify-between mb-2">
                    <h3 className="text-lg font-semibold text-gray-900 line-clamp-2">{order.name}</h3>
                  </div>

                  {/* Rating */}
                  <div className="mb-3">
                    {renderStars(order.rating)}
                  </div>

                  {/* Category & Subcategory */}
                  <div className="flex items-center gap-2 mb-3">
                    <Tag size={16} className="text-gray-400" />
                    <span className="text-sm text-gray-600">
                      {order.category} → {order.subcategory}
                    </span>
                  </div>

                  {/* Delivery Time */}
                  {order.deliverytime && (
                    <div className="flex items-center gap-2 mb-3">
                      <Clock size={16} className="text-gray-400" />
                      <span className="text-sm text-gray-600">
                        Delivery: {order.deliverytime}
                      </span>
                    </div>
                  )}

                  {/* Price */}
                  <div className="flex items-center justify-between mb-3">
                    <div className="text-2xl font-bold text-gray-900">₹{order.price.toLocaleString()}</div>
                    <div className="flex items-center text-sm text-gray-500">
                      <Package size={16} className="mr-1" />
                      Stock: {order.stock}
                    </div>
                  </div>

                  {/* Description */}
                  <p className="text-gray-600 text-sm mb-3 line-clamp-3">{order.description}</p>

                  {/* Status */}
                  <div className="flex items-center justify-between">
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      order.status.toLowerCase() === 'pending'
                        ? 'bg-yellow-100 text-yellow-800'
                        : order.status.toLowerCase() === 'approved'
                        ? 'bg-green-100 text-green-800'
                        : 'bg-gray-100 text-gray-800'
                    }`}>
                      {order.status}
                    </span>
                    <span className="text-xs text-gray-500">ID: {order.id}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Toast Notification */}
      {toast && (
        <div className={`fixed bottom-6 right-6 z-50 px-4 py-2 rounded shadow-lg text-white ${toast.type === 'success' ? 'bg-green-600' : toast.type === 'error' ? 'bg-red-600' : 'bg-blue-600'}`}>
          {toast.message}
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;