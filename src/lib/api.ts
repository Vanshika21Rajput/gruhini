// API configuration and utilities
const API_BASE_URL = 'http://localhost:8080'; // Your Spring Boot backend URL

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message?: string;
  user?: {
    id: string;
    name: string;
    email: string;
    type: 'customer' | 'seller';
  };
}

export interface RegisterRequest {
  name: string;
  email: string;
  phone: string;
  password: string;
  confirmPassword: string;
}

// API client with error handling
class ApiClient {
  private baseURL: string;

  constructor(baseURL: string) {
    this.baseURL = baseURL;
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${this.baseURL}${endpoint}`;
    
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      credentials: 'include', // Important for Spring Security sessions
      ...options,
    };

    try {
      const response = await fetch(url, config);
      
      // Handle different response types
      if (response.ok) {
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
          return await response.json();
        } else {
          // For successful non-JSON responses (like redirects)
          return { success: true } as T;
        }
      } else {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  // Login method that works with Spring Security
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    try {
      // Create form data for Spring Security login
      const formData = new FormData();
      formData.append('username', credentials.username);
      formData.append('password', credentials.password);

      const response = await fetch(`${this.baseURL}/login`, {
        method: 'POST',
        body: formData,
        credentials: 'include',
        redirect: 'manual', // Handle redirects manually
      });

      // Spring Security redirects on successful login
      if (response.status === 0 || response.status === 302 || response.ok) {
        // Login successful - extract user info from response or make another call
        return {
          success: true,
          user: {
            id: '1',
            name: credentials.username,
            email: credentials.username,
            type: 'customer'
          }
        };
      } else {
        return {
          success: false,
          message: 'Invalid username or password'
        };
      }
    } catch (error) {
      console.error('Login error:', error);
      return {
        success: false,
        message: 'Login failed. Please try again.'
      };
    }
  }

  // Register method
  async register(data: RegisterRequest): Promise<LoginResponse> {
    try {
      const response = await this.request<any>('/register', {
        method: 'POST',
        body: JSON.stringify({
          name: data.name,
          address: '', // You might want to add address field
          contactNumber: data.phone
        }),
      });

      return {
        success: true,
        user: {
          id: Date.now().toString(),
          name: data.name,
          email: data.email,
          type: 'customer'
        }
      };
    } catch (error) {
      return {
        success: false,
        message: 'Registration failed. Please try again.'
      };
    }
  }

  // Seller registration method
  async registerSeller(data: any): Promise<LoginResponse> {
    try {
      const response = await this.request<any>('/register', {
        method: 'POST',
        body: JSON.stringify({
          name: data.name,
          address: data.address || '',
          contactNumber: data.phone
        }),
      });

      return {
        success: true,
        user: {
          id: Date.now().toString(),
          name: data.name,
          email: data.email,
          type: 'seller'
        }
      };
    } catch (error) {
      return {
        success: false,
        message: 'Seller registration failed. Please try again.'
      };
    }
  }

  // Check authentication status
  async checkAuth(): Promise<{ authenticated: boolean; user?: any }> {
    try {
      const response = await this.request<any>('/home', {
        method: 'GET',
      });
      
      return {
        authenticated: true,
        user: response.user
      };
    } catch (error) {
      return {
        authenticated: false
      };
    }
  }

  // Logout method
  async logout(): Promise<void> {
    try {
      await this.request('/logout', {
        method: 'POST',
      });
    } catch (error) {
      console.error('Logout error:', error);
    }
  }
}

// Export singleton instance
export const apiClient = new ApiClient(API_BASE_URL);

// Utility functions
export const isValidEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

export const isValidPhone = (phone: string): boolean => {
  const phoneRegex = /^[+]?[\d\s\-\(\)]{10,}$/;
  return phoneRegex.test(phone);
};