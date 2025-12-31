import realProducts from "../../real-products.json";

// Helper to encode paths with spaces safely
const safePath = (path: string) => encodeURI(path);

export interface ThaliItem {
  id: string;
  title: string;
  price: string;
  desc: string;
  time: string;
  img: string;
  chefName: string;
  chefLoc: string;
  chefAvatar: string;
  trustStat: string;
}

// Maps your REAL public images and sellers to the Thali
export const getCuratedThali = (): ThaliItem[] => {
  return realProducts.map((product: any) => ({
    id: product.id.toString(),
    title: product.title,
    price: product.price,
    desc: product.desc,
    time: product.time,
    img: product.img,
    chefName: product.chef,
    chefLoc: product.loc,
    chefAvatar: product.avatar,
    trustStat: product.badge
  }));
};
