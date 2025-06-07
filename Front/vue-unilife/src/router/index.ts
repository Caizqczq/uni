import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import HomeView from '../views/HomeView.vue'
import ProfileView from '../views/ProfileView.vue'
import ForumHomeView from '../views/ForumHomeView.vue'
import PostDetailView from '../views/PostDetailView.vue'
import CreatePostView from '../views/CreatePostView.vue'
import EditPostView from '../views/EditPostView.vue'
import CampusNewsView from '../views/CampusNewsView.vue'
// import NewsArticleDetailView from '../views/NewsArticleDetailView.vue';

// Learning Resources Imports
import SharedDocumentsView from '../views/learningresources/SharedDocumentsView.vue'
import DocumentDetailView from '../views/learningresources/DocumentDetailView.vue'
import DocumentEditorFormView from '../views/learningresources/DocumentEditorFormView.vue'
import SharedFilesView from '../views/learningresources/SharedFilesView.vue'
import ScheduleView from '../views/schedule/ScheduleView.vue' // New


const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/login', // Redirect root to login for now
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView,
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterView,
  },
  {
    path: '/home',
    name: 'Home',
    component: HomeView,
    meta: { requiresAuth: true } // Example for protected route
  },
  {
    path: '/profile',
    name: 'Profile',
    component: ProfileView,
    meta: { requiresAuth: true }
  },
  {
    path: '/forum',
    name: 'ForumHome',
    component: ForumHomeView,
    // meta: { requiresAuth: true } // Decide if forum home is public or protected
  },
  {
    path: '/forum/post/:postId', // Use postId as param
    name: 'PostDetail',
    component: PostDetailView,
    props: true // Pass route params as props to the component
    // meta: { requiresAuth: true } // Decide if post detail is public or protected
  },
  {
    path: '/forum/create-post',
    name: 'CreatePost',
    component: CreatePostView,
    meta: { requiresAuth: true }
  },
  {
    path: '/forum/edit-post/:postId',
    name: 'EditPost',
    component: EditPostView,
    props: true,
    meta: { requiresAuth: true }
  },
  {
    path: '/campus-news',
    name: 'CampusNews',
    component: CampusNewsView,
    // meta: { requiresAuth: false } // Assuming news is public
  },
  // { // Optional: Route for individual news article detail page
  //   path: '/campus-news/:articleId',
  //   name: 'NewsArticleDetail',
  //   component: NewsArticleDetailView, // To be created if detail view is implemented
  //   props: true,
  //   // meta: { requiresAuth: false }
  // },
  // Learning Resources Routes
  {
    path: '/learning-resources/shared-documents',
    name: 'SharedDocuments',
    component: SharedDocumentsView,
    meta: { requiresAuth: true } // Assuming viewing shared docs requires login
  },
  {
    path: '/learning-resources/shared-documents/course/:courseId',
    name: 'SharedDocumentsByCourse',
    component: SharedDocumentsView,
    props: true,
    meta: { requiresAuth: true }
  },
  {
    path: '/learning-resources/document/:docId',
    name: 'DocumentDetail',
    component: DocumentDetailView,
    props: true,
    meta: { requiresAuth: true }
  },
  {
    path: '/learning-resources/document/:docId/edit',
    name: 'EditDocument',
    component: DocumentEditorFormView, // Using a view wrapper for the form component
    props: true,
    meta: { requiresAuth: true }
  },
  {
    path: '/learning-resources/course/:courseId/create-document',
    name: 'CreateDocument',
    component: DocumentEditorFormView, // Using a view wrapper for the form component
    props: true, // courseId will be passed as prop
    meta: { requiresAuth: true }
  },
  {
    path: '/learning-resources/shared-files',
    name: 'SharedFiles',
    component: SharedFilesView,
    meta: { requiresAuth: true }
  },
  {
    path: '/learning-resources/shared-files/course/:courseId',
    name: 'SharedFilesByCourse',
    component: SharedFilesView, // Same view, will filter based on courseId prop
    props: true,
    meta: { requiresAuth: true }
  },
  {
    path: '/schedule',
    name: 'Schedule',
    component: ScheduleView,
    meta: { requiresAuth: true }
  },
  // Add other routes here
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL), // Use BASE_URL from Vite env
  routes,
})

// Basic navigation guard (example)
router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem('authToken'); // Simple check

  if (to.meta.requiresAuth && !isAuthenticated) {
    next({ name: 'Login' });
  } else {
    next();
  }
});

export default router
