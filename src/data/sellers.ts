// Real seller data based on the provided folders
export interface Seller {
  id: string;
  name: string;
  displayName: string;
  location: string;
  speciality: string;
  rating: number;
  experience: string;
  ordersThisWeek: number;
  onTimeDeliveryRate: number;
  image: string;
  isVerified: boolean;
  profileLink: string;
  bio: string;
  shgAssociation: string;
  hygieneStatus: string;
  rewardLevel: string;
  memberSince: string;
  reviews: Array<{
    id: number;
    user: string;
    rating: number;
    comment: string;
  }>;
}

export const realSellers: Seller[] = [
  {
    id: "dolly-aunty",
    name: "Dolly Aunty",
    displayName: "Dolly Aunty",
    location: "Mumbai, MH",
    speciality: "Handmade Crafts & Natural Products",
    rating: 4.9,
    experience: "12 years",
    ordersThisWeek: 85,
    onTimeDeliveryRate: 0.96,
    image: "/dolly aunty/Owner pic.jpeg",
    isVerified: true,
    profileLink: "/seller/dolly-aunty",
    bio: "Expert in creating beautiful handmade crafts and natural products. Specializes in traditional diyas, herbal soaps, and decorative items. Member of local women's craft collective.",
    shgAssociation: "Mumbai Craft Women's Group",
    hygieneStatus: "Certified Clean Workshop",
    rewardLevel: "Gold Seller",
    memberSince: "January 2022",
    reviews: [
      { id: 1, user: "Priya M.", rating: 5, comment: "Beautiful handmade diyas! Perfect for festivals." },
      { id: 2, user: "Raj S.", rating: 4.8, comment: "Natural soaps are amazing, very gentle on skin." }
    ]
  },
  {
    id: "sakshi-tolani",
    name: "Sakshi Tolani",
    displayName: "Sakshi Tolani",
    location: "Pune, MH",
    speciality: "Homemade Cakes & Desserts",
    rating: 4.8,
    experience: "8 years",
    ordersThisWeek: 120,
    onTimeDeliveryRate: 0.98,
    image: "/Sakshi Tolani/Sakshi Tolani/Owner pic.jpeg",
    isVerified: true,
    profileLink: "/seller/sakshi-tolani",
    bio: "Passionate baker specializing in fresh, homemade cakes and desserts. Uses only premium ingredients and traditional baking methods. Perfect for celebrations and gifts.",
    shgAssociation: "Pune Bakers Association",
    hygieneStatus: "FSSAI Certified Kitchen",
    rewardLevel: "Star Seller",
    memberSince: "March 2021",
    reviews: [
      { id: 1, user: "Anita K.", rating: 5, comment: "Best homemade cakes in Pune! Fresh and delicious." },
      { id: 2, user: "Rohit P.", rating: 4.7, comment: "Ordered for birthday, everyone loved the vanilla-chocolate cake." }
    ]
  },
  {
    id: "neelam-joshi",
    name: "Neelam Joshi",
    displayName: "Neelam Joshi",
    location: "Indore, MP",
    speciality: "Traditional Meals & Vrat Food",
    rating: 4.9,
    experience: "15 years",
    ordersThisWeek: 150,
    onTimeDeliveryRate: 0.99,
    image: "/Neelam Joshi/Owner pic.png",
    isVerified: true,
    profileLink: "/seller/neelam-joshi",
    bio: "Master of traditional Indian cuisine and special vrat (fasting) foods. Known for authentic Dal Bati Churma and festival specialties. Maintains traditional cooking methods passed down through generations.",
    shgAssociation: "Indore Traditional Cooks Collective",
    hygieneStatus: "Traditional Kitchen Certified",
    rewardLevel: "Diamond Seller",
    memberSince: "August 2020",
    reviews: [
      { id: 1, user: "Sunita D.", rating: 5, comment: "Authentic taste! Dal Bati Churma reminds me of my grandmother's cooking." },
      { id: 2, user: "Vikram J.", rating: 4.9, comment: "Best vrat food in the city. Always fresh and properly prepared." }
    ]
  },
  {
    id: "manju-vijayvargiye",
    name: "Manju Vijayvargiye",
    displayName: "Manju Vijayvargiye",
    location: "Bhopal, MP",
    speciality: "Snacks & Traditional Sweets",
    rating: 4.8,
    experience: "20 years",
    ordersThisWeek: 200,
    onTimeDeliveryRate: 0.97,
    image: "/Manju vijayvargiye/OWNER PIC.jpeg",
    isVerified: true,
    profileLink: "/seller/manju-vijayvargiye",
    bio: "Renowned for her extensive variety of traditional snacks and sweets. Specializes in namkeen, ladoos, and festival treats. Uses family recipes perfected over generations.",
    shgAssociation: "Bhopal Women Entrepreneurs Group",
    hygieneStatus: "FSSAI Licensed Kitchen",
    rewardLevel: "Platinum Seller",
    memberSince: "June 2019",
    reviews: [
      { id: 1, user: "Meera S.", rating: 5, comment: "Huge variety of snacks! Besan ladoo is absolutely divine." },
      { id: 2, user: "Amit G.", rating: 4.8, comment: "Best namkeen in Bhopal. Always fresh and perfectly spiced." }
    ]
  }
];

// Helper function to get top sellers
export const getTopSellers = (sellers: Seller[], count = 3) => {
  return [...sellers]
    .sort((a, b) => {
      if (b.ordersThisWeek !== a.ordersThisWeek) {
        return b.ordersThisWeek - a.ordersThisWeek;
      }
      return b.rating - a.rating;
    })
    .slice(0, count)
    .map(seller => seller.id);
};