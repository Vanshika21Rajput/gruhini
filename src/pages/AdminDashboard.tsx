import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { ScrollArea } from "@/components/ui/scroll-area";
import { CheckCircle2, XCircle, Package, Clock, ShieldCheck, User, ChefHat } from 'lucide-react';
import { toast } from "sonner";
import { apiClient } from '@/lib/api'; // Use centralized API client if desired, or fetch directly

const AdminDashboard = () => {
    const [pendingItems, setPendingItems] = useState([]);
    const [selectedItems, setSelectedItems] = useState<string[]>([]); // Store IDs as strings

    useEffect(() => {
        fetchPendingItems();
    }, []);

    const fetchPendingItems = async () => {
        try {
            // Using strict backend URL
            const response = await fetch('https://gruhini2.onrender.com/view-pending');
            if (response.ok) {
                const data = await response.json();
                setPendingItems(data);
            }
        } catch (error) {
            console.error("Error fetching pending items", error);
            toast.error("Failed to load pending items");
        }
    };

    const handleSelect = (id: string) => {
        setSelectedItems(prev =>
            prev.includes(id) ? prev.filter(item => item !== id) : [...prev, id]
        );
    };

    const handleAction = async (action: 'accept' | 'reject') => {
        if (selectedItems.length === 0) {
            toast.error("Please select items first");
            return;
        }

        const endpoint = action === 'accept' ? '/accept-item' : '/reject-item';

        try {
            const response = await fetch(`https://gruhini2.onrender.com${endpoint}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    selectedOrders: selectedItems // Send array of strings
                })
            });

            if (response.ok) {
                toast.success(`Items ${action}ed successfully`);
                setSelectedItems([]);
                fetchPendingItems();
            } else {
                const result = await response.json();
                toast.error(result.message || `Failed to ${action} items`);
            }
        } catch (error) {
            toast.error(`Error processing ${action}`);
        }
    };

    return (
        <div className="p-8 bg-neutral-50 min-h-screen">
            <div className="max-w-6xl mx-auto">
                <header className="mb-8 flex justify-between items-center">
                    <div>
                        <h1 className="text-3xl font-heading font-bold text-ethnic-primary">Admin Dashboard</h1>
                        <p className="text-muted-foreground">Manage product approvals and seller requests</p>
                    </div>
                    <div className="flex gap-2">
                        {selectedItems.length > 0 && (
                            <>
                                <Button variant="destructive" onClick={() => handleAction('reject')}>
                                    <XCircle className="w-4 h-4 mr-2" /> Reject ({selectedItems.length})
                                </Button>
                                <Button className="bg-green-600 hover:bg-green-700" onClick={() => handleAction('accept')}>
                                    <CheckCircle2 className="w-4 h-4 mr-2" /> Approve ({selectedItems.length})
                                </Button>
                            </>
                        )}
                    </div>
                </header>

                <Tabs defaultValue="pending">
                    <TabsList>
                        <TabsTrigger value="pending">Pending Approvals</TabsTrigger>
                        <TabsTrigger value="sellers">Sellers</TabsTrigger>
                    </TabsList>
                    <TabsContent value="pending" className="mt-6">
                        <div className="grid gap-4">
                            {pendingItems.length === 0 ? (
                                <p className="text-center py-10 text-muted-foreground">No pending items.</p>
                            ) : (
                                pendingItems.map((item: any) => ( // Use 'any' or proper type
                                    <Card key={item.id} className={`cursor-pointer border-2 transition-all ${selectedItems.includes(item.id.toString()) ? 'border-primary' : 'border-transparent hover:border-gray-200'}`}
                                        onClick={() => handleSelect(item.id.toString())}>
                                        <div className="flex p-4 gap-4">
                                            <div className="h-24 w-24 bg-gray-100 rounded-md overflow-hidden flex-shrink-0">
                                                <img src={`data:image/jpeg;base64,${item.image}`} className="h-full w-full object-cover" alt={item.name} />
                                            </div>
                                            <div className="flex-1">
                                                <div className="flex justify-between">
                                                    <h3 className="font-bold text-lg">{item.name}</h3>
                                                    <Badge variant="outline">{item.category}</Badge>
                                                </div>
                                                <p className="text-sm text-gray-500 line-clamp-1">{item.description}</p>
                                                <div className="mt-2 flex items-center gap-4 text-sm">
                                                    <span className="font-semibold text-ethnic-primary">₹{item.price}</span>
                                                    <span className="text-gray-400">|</span>
                                                    <span>Stock: {item.stock}</span>
                                                </div>
                                            </div>
                                            <div className="flex items-center">
                                                <div className={`w-6 h-6 rounded-full border flex items-center justify-center ${selectedItems.includes(item.id.toString()) ? 'bg-primary border-primary text-white' : 'border-gray-300'}`}>
                                                    {selectedItems.includes(item.id.toString()) && <CheckCircle2 className="w-4 h-4" />}
                                                </div>
                                            </div>
                                        </div>
                                    </Card>
                                ))
                            )}
                        </div>
                    </TabsContent>
                </Tabs>
            </div>
        </div>
    );
};

export default AdminDashboard;
