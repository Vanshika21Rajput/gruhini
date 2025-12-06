import { useState, ChangeEvent } from "react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Search, User, Heart, Map, Mic, Trophy, Upload, Utensils, Gift, Users, ShoppingCart, Menu, X } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import useVoiceSearch from "@/hooks/useVoiceSearch";
import { toast } from "sonner";
import { useAuth } from "@/context/AuthContext";

const Header = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const navigate = useNavigate();
  const { user, logout, isAuthenticated } = useAuth();

  const handleSearchResult = (transcript: string) => {
    setSearchTerm(transcript);
    navigate(`/explore?q=${encodeURIComponent(transcript)}`);
    toast.success(`Voice search result: ${transcript}`);
  };

  const handleVoiceError = (error: string) => {
    toast.error(error);
  };

  const { isListening, isSupported, startListening } = useVoiceSearch({
    onResult: handleSearchResult,
    onError: handleVoiceError,
    lang: 'hi-IN',
  });

  const handleSearchInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  const handleSearchSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    if (searchTerm.trim()) {
      navigate(`/explore?q=${encodeURIComponent(searchTerm.trim())}`);
      setSearchTerm("");
    }
  };

  const handleVoiceSearchClick = () => {
    if (!isSupported) {
      toast.error("Voice search is not supported in your browser.");
      return;
    }
    startListening();
  };

  const navItems = [
    { to: "/explore", icon: Utensils, label: "Browse Products" },
    { to: "/customize-meal", icon: Users, label: "Customize Meal" },
    { to: "/voice-search", icon: Mic, label: "Voice Search", onClick: handleVoiceSearchClick },
    { to: "/festive-booking", icon: Gift, label: "Festive Booking" },
    { to: "/leaderboard", icon: Trophy, label: "Leaderboard" },
    { to: "/upload-items", icon: Upload, label: "Upload Items" },
  ];

  return (
    <header className="sticky top-0 z-50 bg-background/95 backdrop-blur-sm border-b border-border shadow-gentle">
      <div className="container mx-auto px-4 py-3">
        <div className="flex items-center justify-between">
          {/* Logo */}
          <Link to="/" className="flex items-center space-x-3">
            <img src="/gruhani-logo.jpg" alt="गृहिणी Logo" className="h-12 w-auto rounded-lg shadow-sm" />
            <div className="hidden sm:block">
              <h1 className="font-heading text-xl font-bold text-ethnic-primary">गृहिणी</h1>
              <p className="text-xs text-muted-foreground">घर का स्वाद</p>
            </div>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden lg:flex items-center space-x-1">
            {navItems.map((item) => (
              <div key={item.label}>
                {item.onClick ? (
                  <Button
                    variant="ghost"
                    size="sm"
                    className="flex items-center gap-2 px-3 py-2 text-sm font-medium hover:bg-accent/50 hover:text-accent-foreground transition-colors"
                    onClick={item.onClick}
                  >
                    <item.icon className="h-4 w-4" />
                    {item.label}
                  </Button>
                ) : (
                  <Link to={item.to}>
                    <Button
                      variant="ghost"
                      size="sm"
                      className="flex items-center gap-2 px-3 py-2 text-sm font-medium hover:bg-accent/50 hover:text-accent-foreground transition-colors"
                    >
                      <item.icon className="h-4 w-4" />
                      {item.label}
                    </Button>
                  </Link>
                )}
              </div>
            ))}
          </nav>

          {/* Search Bar - Desktop */}
          <form onSubmit={handleSearchSubmit} className="hidden md:flex flex-1 max-w-md mx-6">
            <div className="relative w-full">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
              <input
                type="text"
                placeholder={isListening ? "Listening..." : "Search for homemade delights..."}
                value={searchTerm}
                onChange={handleSearchInputChange}
                className="w-full pl-10 pr-12 py-2 rounded-lg border border-input bg-background focus:ring-2 focus:ring-primary focus:border-transparent transition-all text-sm"
              />
              {isSupported && (
                <Button
                  type="button"
                  variant="ghost"
                  size="icon"
                  className="absolute right-1 top-1/2 transform -translate-y-1/2 h-8 w-8"
                  onClick={handleVoiceSearchClick}
                  aria-label="Voice search"
                >
                  <Mic className={`h-4 w-4 ${isListening ? 'text-destructive animate-pulse' : 'text-muted-foreground hover:text-primary'}`} />
                </Button>
              )}
            </div>
          </form>

          {/* Right Side Actions */}
          <div className="flex items-center space-x-2">
            <Link to="/cart">
              <Button variant="outline" size="icon" className="h-9 w-9 relative">
                <ShoppingCart className="h-4 w-4" />
                <Badge className="absolute -top-2 -right-2 h-5 w-5 rounded-full p-0 flex items-center justify-center text-xs">
                  3
                </Badge>
              </Button>
            </Link>
            
            {user ? (
              <div className="flex items-center gap-2">
                <span className="text-sm font-medium text-ethnic-primary">
                  Hi, {user.name.split(' ')[0]}
                </span>
                <Button variant="outline" size="sm" onClick={logout} className="text-xs">
                  Logout
                </Button>
              </div>
            ) : (
              <Link to="/login">
                <Button variant="ethnic" size="sm" className="px-4 text-sm">
                  Login
                </Button>
              </Link>
            )}

            {/* Mobile Menu Button */}
            <Button
              variant="ghost"
              size="icon"
              className="lg:hidden h-9 w-9"
              onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
            >
              {isMobileMenuOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
            </Button>
          </div>
        </div>

        {/* Mobile Search */}
        <form onSubmit={handleSearchSubmit} className="md:hidden mt-3">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
            <input
              type="text"
              placeholder={isListening ? "Listening..." : "Search homemade products..."}
              value={searchTerm}
              onChange={handleSearchInputChange}
              className="w-full pl-10 pr-12 py-2 rounded-lg border border-input bg-background focus:ring-2 focus:ring-primary focus:border-transparent text-sm"
            />
            {isSupported && (
              <Button
                type="button"
                variant="ghost"
                size="icon"
                className="absolute right-1 top-1/2 transform -translate-y-1/2 h-8 w-8"
                onClick={handleVoiceSearchClick}
                aria-label="Voice search"
              >
                <Mic className={`h-4 w-4 ${isListening ? 'text-destructive animate-pulse' : 'text-muted-foreground hover:text-primary'}`} />
              </Button>
            )}
          </div>
        </form>

        {/* Mobile Navigation Menu */}
        {isMobileMenuOpen && (
          <div className="lg:hidden mt-4 pb-4 border-t border-border">
            <nav className="grid grid-cols-2 gap-2 mt-4">
              {navItems.map((item) => (
                <div key={item.label}>
                  {item.onClick ? (
                    <Button
                      variant="ghost"
                      size="sm"
                      className="w-full justify-start text-sm"
                      onClick={() => {
                        item.onClick();
                        setIsMobileMenuOpen(false);
                      }}
                    >
                      <item.icon className="h-4 w-4 mr-2" />
                      {item.label}
                    </Button>
                  ) : (
                    <Link to={item.to} onClick={() => setIsMobileMenuOpen(false)}>
                      <Button variant="ghost" size="sm" className="w-full justify-start text-sm">
                        <item.icon className="h-4 w-4 mr-2" />
                        {item.label}
                      </Button>
                    </Link>
                  )}
                </div>
              ))}
            </nav>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header;