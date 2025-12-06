import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Link } from "react-router-dom";

const Footer = () => {
  return (
    <footer className="bg-ethnic-primary text-primary-foreground">
      <div className="container mx-auto px-4 py-12">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 mb-8">
          {/* Brand */}
          <div className="space-y-4">
            <Link to="/" className="flex items-center space-x-2">
              <img src="/gruhani-logo.jpg" alt="गृहिणी Logo" className="h-10 w-auto rounded-lg" />
              {/* <h3 className="font-heading text-xl font-bold">गृहिणी</h3> */}
            </Link>
            <p className="text-sm opacity-80 leading-relaxed">
              Connecting hearts through authentic homemade products. 
              Supporting local women entrepreneurs globally.
            </p>
            <Badge variant="secondary" className="bg-accent text-ethnic-primary">
              🌍 Serving 25+ Countries
            </Badge>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="font-semibold mb-4">Quick Links</h4>
            <ul className="space-y-2 text-sm opacity-80">
              <li><Link to="/explore" className="hover:opacity-100 transition-opacity">Browse Products</Link></li>
              <li><Link to="/customize-meal" className="hover:opacity-100 transition-opacity">Customize Meal</Link></li>
              <li><Link to="/festive-booking" className="hover:opacity-100 transition-opacity">Festive Booking</Link></li>
              <li><Link to="/top-sellers" className="hover:opacity-100 transition-opacity">Top Sellers</Link></li> {/* Added link */}
              <li><Link to="/success-stories" className="hover:opacity-100 transition-opacity">Success Stories</Link></li>
              <li><Link to="/request-custom" className="hover:opacity-100 transition-opacity">Request Custom</Link></li>
              {/* <li><a href="#" className="hover:opacity-100 transition-opacity">Track Order</a></li> */}
            </ul>
          </div>

          {/* Categories */}
          <div>
            <h4 className="font-semibold mb-4">Categories</h4>
            <ul className="space-y-2 text-sm opacity-80">
              <li><Link to="/explore?category=ghar-ka-khana" className="hover:opacity-100 transition-opacity">Homemade Food</Link></li>
              <li><Link to="/explore?category=natural-cosmetics" className="hover:opacity-100 transition-opacity">Natural Cosmetics</Link></li>
              <li><Link to="/explore?category=handmade-crafts" className="hover:opacity-100 transition-opacity">Handmade Crafts</Link></li>
              <li><Link to="/explore?category=kitchen-essentials" className="hover:opacity-100 transition-opacity">Kitchen Essentials</Link></li>
              <li><Link to="/explore?category=student-tiffins" className="hover:opacity-100 transition-opacity">Student Tiffins</Link></li>
            </ul>
          </div>

          {/* Support */}
          <div>
            <h4 className="font-semibold mb-4">Support</h4>
            <ul className="space-y-2 text-sm opacity-80">
              <li><Link to="/help" className="hover:opacity-100 transition-opacity">Help Center</Link></li>
              <li><Link to="/shipping" className="hover:opacity-100 transition-opacity">Shipping Info</Link></li>
              <li><Link to="/returns" className="hover:opacity-100 transition-opacity">Return Policy</Link></li>
              <li><Link to="/contact" className="hover:opacity-100 transition-opacity">Contact Us</Link></li>
              <li><Link to="/seller-support" className="hover:opacity-100 transition-opacity">Seller Support</Link></li>
            </ul>
          </div>
        </div>

        {/* Newsletter */}
        <div className="border-t border-primary-foreground/20 pt-8 mb-8">
          <div className="max-w-md mx-auto text-center">
            <h4 className="font-semibold mb-2">Stay Connected</h4>
            <p className="text-sm opacity-80 mb-4">
              Get updates on new products and special offers for international students
            </p>
            <div className="flex gap-2">
              <input
                type="email"
                placeholder="Enter your email"
                className="flex-1 px-3 py-2 rounded-lg bg-primary-foreground/10 border border-primary-foreground/20 text-primary-foreground placeholder:text-primary-foreground/60 focus:outline-none focus:ring-2 focus:ring-accent"
              />
              <Button variant="warm" size="sm">
                Subscribe
              </Button>
            </div>
          </div>
        </div>

        {/* Bottom */}
        <div className="border-t border-primary-foreground/20 pt-6 flex flex-col md:flex-row justify-between items-center text-sm opacity-80">
          {/* <p>© 2024 Gruh Aangan. All rights reserved.</p> */}
          <div className="flex space-x-4 mt-4 md:mt-0">
            <a href="#" className="hover:opacity-100 transition-opacity">Privacy Policy</a>
            <a href="#" className="hover:opacity-100 transition-opacity">Terms of Service</a>
            <a href="#" className="hover:opacity-100 transition-opacity">Cookie Policy</a>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;