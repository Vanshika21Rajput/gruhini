Royal Experience — Quick install & integration

Files added:
- src/utils/royalMenuData.ts  — curated Thali data helper (uses your public asset paths)
- src/components/royal/HeroSection.tsx
- src/components/royal/TasteAlchemy.tsx
- src/components/royal/LivingThali.tsx
- src/pages/Index.tsx (master assembly — replace your existing Index with this)

What you must do next:
1) Add assets
   - Place your background image at: public/assets/royal-bg.jpg
   - Place chef/product images in public root as referenced in `royalMenuData.ts` (paths like `/Neelam Joshi/Daal Bhati Churma.jpeg`).

2) Dependencies
   - Install framer-motion and react-router-dom (if not already installed):

     npm install framer-motion react-router-dom

   or with yarn:

     yarn add framer-motion react-router-dom

3) Routing / Header/Footer
   - The new `Index.tsx` imports `@/components/Header`, `Footer`, and `TrustSection`. Keep your existing header/footer or adapt the imports.

4) Data wiring (optional)
   - `getCuratedThali()` returns a curated list using your public asset paths. If you prefer fetching from your API, replace the helper with a fetch to `/api/products` and map to the ThaliItem shape.

5) Run dev server
   - Start your dev server (Vite/CRA/Next) and open the index page. Check console for missing assets or TypeScript errors.

If you want, I can now:
- wire the helper to call your live API endpoints (/api/products, /api/sellers) instead of the curated list, or
- convert the imports to relative paths if your project isn\'t set up with the `@/` alias.

Tell me which of the two you prefer and I will update the files accordingly.