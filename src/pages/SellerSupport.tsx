import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Button } from "@/components/ui/button";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Link } from "react-router-dom";

const SellerSupport = () => {
  const faqs = [
    {
      question: "How do I register as a seller?",
      answer: "You can register by visiting our 'Become a Seller' page and filling out the application form. We'll guide you through the process.",
    },
    {
      question: "What are the commission rates?",
      answer: "Our commission rates vary by product category. Please refer to our seller agreement or contact us for detailed information.",
    },
    {
      question: "How do I list my products?",
      answer: "Once your seller account is approved, you'll get access to a seller dashboard where you can easily add and manage your product listings.",
    },
    {
      question: "How does shipping work for sellers?",
      answer: "We offer various shipping solutions. You can choose to handle shipping yourself or opt into our partnered logistics services. More details are available in the seller portal.",
    },
    {
      question: "How and when do I get paid?",
      answer: "Payments are processed on a regular cycle (e.g., weekly or bi-weekly) directly to your registered bank account after deducting applicable fees.",
    },
  ];

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <div className="container mx-auto px-4 py-12">
        <section className="text-center mb-16">
          <h1 className="text-4xl font-heading font-bold text-ethnic-primary mb-4">
            Seller Support Center
          </h1>
          <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
            Everything you need to know to start selling and grow your homemade business with Gruh Aangan.
          </p>
        </section>

        <section className="grid md:grid-cols-3 gap-8 mb-16">
          <div className="bg-card p-6 rounded-lg shadow-sm hover:shadow-warm transition-shadow">
            <h3 className="text-xl font-semibold text-ethnic-primary mb-2">Seller Onboarding</h3>
            <p className="text-sm text-muted-foreground mb-4">
              Ready to join our community? Find out how to register and set up your shop.
            </p>
            <Button variant="ethnic" asChild>
              <Link to="/seller-onboarding">Get Started</Link>
            </Button>
          </div>
          <div className="bg-card p-6 rounded-lg shadow-sm hover:shadow-warm transition-shadow">
            <h3 className="text-xl font-semibold text-ethnic-primary mb-2">Product Listing Guide</h3>
            <p className="text-sm text-muted-foreground mb-4">
              Tips and best practices for creating attractive and effective product listings.
            </p>
            <Button variant="outline">View Guidelines</Button>
          </div>
          <div className="bg-card p-6 rounded-lg shadow-sm hover:shadow-warm transition-shadow">
            <h3 className="text-xl font-semibold text-ethnic-primary mb-2">Payments & Fees</h3>
            <p className="text-sm text-muted-foreground mb-4">
              Understand our payment cycles, commission structure, and fee policies.
            </p>
            <Button variant="outline">Learn More</Button>
          </div>
        </section>

        <section className="mb-16">
          <h2 className="text-3xl font-heading font-bold text-center text-ethnic-primary mb-8">
            Frequently Asked Questions
          </h2>
          <Accordion type="single" collapsible className="w-full max-w-3xl mx-auto">
            {faqs.map((faq, index) => (
              <AccordionItem value={`item-${index}`} key={index}>
                <AccordionTrigger className="text-left hover:text-primary">{faq.question}</AccordionTrigger>
                <AccordionContent className="text-muted-foreground">
                  {faq.answer}
                </AccordionContent>
              </AccordionItem>
            ))}
          </Accordion>
        </section>

        <section className="text-center bg-gradient-to-r from-green-50 to-emerald-50 p-8 rounded-lg">
          <h2 className="text-2xl font-semibold text-ethnic-primary mb-3">Still Have Questions?</h2>
          <p className="text-muted-foreground mb-6">
            Our dedicated seller support team is here to help you every step of the way.
          </p>
          <Button variant="warm" size="lg" asChild>
            <Link to="/contact">Contact Seller Support</Link>
          </Button>
        </section>
      </div>
      <Footer />
    </div>
  );
};

export default SellerSupport;
