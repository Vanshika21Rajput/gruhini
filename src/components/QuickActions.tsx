import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Mic, ShoppingBag, Settings, Gift, Trophy, Upload, Languages } from "lucide-react";

const actions = [
  {
    title: "Browse Products",
    description: "Explore fresh, homemade delights from local sellers.",
    icon: <ShoppingBag className="h-8 w-8 text-primary" />,
    to: "/explore",
    color: "from-green-200 to-green-100"
  },
  {
    title: "Customise Your Meal",
    description: "Personalize your order to your taste & needs.",
    icon: <Settings className="h-8 w-8 text-ethnic-primary" />,
    to: "/customize-meal",
    color: "from-yellow-200 to-yellow-100"
  },
  {
    title: "Voice Search",
    description: "Search for items by speaking in your language.",
    icon: <Mic className="h-8 w-8 text-red-500" />,
    to: "#voice-search",
    color: "from-rose-200 to-rose-100",
    isVoice: true
  },
  {
    title: "Festive Booking",
    description: "Book special meals & gifts for festivals.",
    icon: <Gift className="h-8 w-8 text-pink-500" />,
    to: "/festive-booking",
    color: "from-pink-200 to-pink-100"
  },
  {
    title: "Leaderboard",
    description: "See top sellers and customers in your area.",
    icon: <Trophy className="h-8 w-8 text-amber-500" />,
    to: "/leaderboard",
    color: "from-amber-200 to-amber-100"
  },
  {
    title: "Upload Your Items",
    description: "Upload a photo of your list and auto-add to cart!",
    icon: <Upload className="h-8 w-8 text-blue-500" />,
    to: "/upload-items",
    color: "from-blue-200 to-blue-100"
  }
];

const QuickActions = () => {
  return (
    <section className="relative z-10 py-10 bg-gradient-to-b from-background to-white/80">
      <div className="container mx-auto px-4">
        {/* Logo at the top */}
        <div className="flex justify-center mb-8">
          <img src="/gruhani-logo.jpg" alt="गृहिणी Logo" className="h-16 w-auto rounded-full shadow-lg border-4 border-primary bg-white p-2" />
        </div>
        <h2 className="text-3xl md:text-4xl font-heading font-bold text-center text-ethnic-primary mb-6">Quick Actions</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {actions.map((action, idx) => (
            <Link to={action.to} key={action.title} className={`group rounded-2xl p-6 bg-gradient-to-br ${action.color} shadow-md hover:scale-105 transition-transform duration-200 flex flex-col items-center text-center relative`}>
              <div className="mb-4">{action.icon}</div>
              <h3 className="text-xl font-semibold text-ethnic-primary mb-2 group-hover:text-primary transition-colors">{action.title}</h3>
              <p className="text-muted-foreground mb-4 text-sm">{action.description}</p>
              {action.isVoice && (
                <div className="flex items-center gap-2 justify-center mt-2">
                  <Languages className="h-5 w-5 text-muted-foreground" />
                  <select className="rounded px-2 py-1 border text-sm">
                    <option value="hi-IN">Hindi</option>
                    <option value="en-US">English</option>
                    <option value="pa-IN">Punjabi</option>
                    <option value="mr-IN">Marathi</option>
                    <option value="bn-IN">Bengali</option>
                    <option value="ta-IN">Tamil</option>
                    <option value="te-IN">Telugu</option>
                    <option value="gu-IN">Gujarati</option>
                  </select>
                  <span className="text-xs text-muted-foreground">(Elderly friendly)</span>
                </div>
              )}
              <Button variant="ethnic" size="sm" className="mt-4 w-full">Go</Button>
            </Link>
          ))}
        </div>
      </div>
    </section>
  );
};

export default QuickActions; 