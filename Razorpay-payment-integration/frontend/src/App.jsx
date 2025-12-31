import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import PremiumPaymentUI from './components/PremiumPaymentUI';
import CartPage from './components/CartPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<CartPage />} />
        <Route path="/payment" element={<PremiumPaymentUI />} />
      </Routes>
    </Router>
  );
}

export default App;
