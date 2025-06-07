import { defineStore } from 'pinia'
import axios from 'axios' // Assuming axios is configured globally or create an instance here
import router from '../router' // To redirect after login/logout

// Define a type for the user profile (adjust based on your UserProfileDto)
interface UserProfile {
  username: string;
  email: string;
  nickname?: string;
  avatarUrl?: string;
  school?: string;
  studentId?: string;
}

// Define a type for the login response (adjust based on your LoginResponseDto)
interface LoginResponse {
  token: string;
  userProfile: UserProfile;
}

// Define a type for the registration payload (adjust based on your UserRegistrationDto)
interface RegistrationPayload {
    username: string;
    email: string;
    password?: string; // Password might not be part of UserProfile if not returned
    nickname?: string;
}


export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('authToken') || null,
    user: JSON.parse(localStorage.getItem('authUser') || 'null') as UserProfile | null,
    isAuthenticated: !!localStorage.getItem('authToken'),
    error: null as string | null,
    loading: false,
  }),
  getters: {
    // isAuthenticated: (state) => !!state.token, // Already in state, can be a getter too
    // getUser: (state) => state.user,
    // getToken: (state) => state.token,
    // getError: (state) => state.error,
    // isLoading: (state) => state.loading,
  },
  actions: {
    async login(payload: Record<string, string>) {
      this.loading = true;
      this.error = null;
      try {
        // Replace with your actual API endpoint and base URL
        const response = await axios.post<LoginResponse>('/api/users/login', payload);
        const { token, userProfile } = response.data;

        this.token = token;
        this.user = userProfile;
        this.isAuthenticated = true;

        localStorage.setItem('authToken', token);
        localStorage.setItem('authUser', JSON.stringify(userProfile));

        // Set authorization header for subsequent requests
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

        router.push('/home'); // Redirect to home or dashboard
      } catch (err: any) {
        this.error = err.response?.data?.error || err.response?.data?.message || 'Login failed. Please check your credentials.';
        this.isAuthenticated = false;
        this.token = null;
        this.user = null;
        localStorage.removeItem('authToken');
        localStorage.removeItem('authUser');
        // throw err; // Rethrow if component needs to handle it further
      } finally {
        this.loading = false;
      }
    },

    async register(payload: RegistrationPayload) {
      this.loading = true;
      this.error = null;
      try {
        // Replace with your actual API endpoint
        await axios.post('/api/users/register', payload);
        // Optionally, handle auto-login or just redirect to login
        // For now, just signal success and let user login manually
      } catch (err: any) {
        this.error = err.response?.data?.error || err.response?.data?.message || 'Registration failed.';
        // throw err;
      } finally {
        this.loading = false;
      }
    },

    logout() {
      this.token = null;
      this.user = null;
      this.isAuthenticated = false;
      this.error = null;
      localStorage.removeItem('authToken');
      localStorage.removeItem('authUser');
      delete axios.defaults.headers.common['Authorization'];
      router.push('/login');
    },

    // Action to initialize store from localStorage (e.g., on app load)
    // This is implicitly handled by state initialization, but an explicit action can be useful.
    // checkAuth() {
    //   const token = localStorage.getItem('authToken');
    //   const user = localStorage.getItem('authUser');
    //   if (token && user) {
    //     this.token = token;
    //     this.user = JSON.parse(user);
    //     this.isAuthenticated = true;
    //     axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    //   } else {
    //     this.logout(); // Ensure clean state if not fully authenticated
    //   }
    // }

    async fetchUserProfile() {
      if (!this.user?.username) {
        console.warn('Cannot fetch profile without username.');
        // this.error = 'Cannot fetch profile: User information is missing.';
        // this.logout(); // Optional: force logout if user state is inconsistent
        return;
      }
      this.loading = true;
      this.error = null;
      try {
        const response = await axios.get<UserProfile>(`/api/users/profile/${this.user.username}`);
        this.user = response.data;
        localStorage.setItem('authUser', JSON.stringify(this.user));
      } catch (err: any) {
        this.error = err.response?.data?.error || err.response?.data?.message || 'Failed to fetch user profile.';
        // console.error('Failed to fetch user profile:', err);
        // Optionally logout if profile fetch fails critically
        // if (err.response?.status === 401 || err.response?.status === 403) {
        //   this.logout();
        // }
      } finally {
        this.loading = false;
      }
    },

    async updateUserProfile(profileData: Partial<UserProfile>) {
      if (!this.user?.username) {
        this.error = 'Cannot update profile: User information is missing.';
        return false; // Indicate failure
      }
      this.loading = true;
      this.error = null;
      try {
        const response = await axios.put<UserProfile>(`/api/users/profile/${this.user.username}`, profileData);
        this.user = { ...this.user, ...response.data }; // Merge existing user data with response
        localStorage.setItem('authUser', JSON.stringify(this.user));
        return true; // Indicate success
      } catch (err: any) {
        this.error = err.response?.data?.error || err.response?.data?.message || 'Profile update failed.';
        return false; // Indicate failure
      } finally {
        this.loading = false;
      }
    }
  },
})
