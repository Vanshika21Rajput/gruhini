import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";

const ContactUs = () => {
  return (
    <div className="min-h-screen bg-background">
      <Header />
      <div className="container mx-auto px-4 py-12">
        <h1 className="text-4xl font-heading font-bold text-center text-ethnic-primary mb-8">
          Contact Us
        </h1>
        <p className="text-center text-muted-foreground mb-12 max-w-2xl mx-auto">
          We'd love to hear from you! Whether you have a question about our products,
          your order, or just want to share your feedback, please reach out.
        </p>

        <div className="grid md:grid-cols-2 gap-12 items-start">
          {/* Contact Form */}
          <form className="space-y-6 p-8 border rounded-lg shadow-sm bg-card">
            <div>
              <Label htmlFor="name" className="text-ethnic-primary">Full Name</Label>
              <Input id="name" type="text" placeholder="Your Name" className="mt-1" />
            </div>
            <div>
              <Label htmlFor="email" className="text-ethnic-primary">Email Address</Label>
              <Input id="email" type="email" placeholder="your.email@example.com" className="mt-1" />
            </div>
            <div>
              <Label htmlFor="subject" className="text-ethnic-primary">Subject</Label>
              <Input id="subject" type="text" placeholder="Reason for contacting" className="mt-1" />
            </div>
            <div>
              <Label htmlFor="message" className="text-ethnic-primary">Message</Label>
              <Textarea id="message" placeholder="Write your message here..." rows={5} className="mt-1" />
            </div>
            <Button type="submit" variant="ethnic" className="w-full">
              Send Message
            </Button>
          </form>

          {/* Contact Information */}
          <div className="space-y-8 p-8 bg-gradient-to-br from-accent/20 to-primary/20 rounded-lg">
            <div>
              <h3 className="text-xl font-semibold text-ethnic-primary mb-2">Our Address</h3>
              <p className="text-muted-foreground">
                123 Gruh Aangan Lane,<br />
                Homemade Heaven, India 400001
              </p>
            </div>
            <div>
              <h3 className="text-xl font-semibold text-ethnic-primary mb-2">Email Us</h3>
              <p className="text-muted-foreground">
                <a href="mailto:support@gruhaangan.com" className="hover:text-primary">
                  support@gruhaangan.com
                </a>
              </p>
            </div>
            <div>
              <h3 className="text-xl font-semibold text-ethnic-primary mb-2">Call Us</h3>
              <p className="text-muted-foreground">
                +91 98765 43210 (Mon-Fri, 9am-6pm IST)
              </p>
            </div>
            <div>
              <h3 className="text-xl font-semibold text-ethnic-primary mb-2">Follow Us</h3>
              <div className="flex space-x-4">
                {/* Placeholder for social media icons */}
                <a href="#" className="text-muted-foreground hover:text-primary">Facebook</a>
                <a href="#" className="text-muted-foreground hover:text-primary">Instagram</a>
                <a href="#" className="text-muted-foreground hover:text-primary">Twitter</a>
              </div>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default ContactUs;
