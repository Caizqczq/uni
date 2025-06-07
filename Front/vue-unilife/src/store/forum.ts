import { defineStore } from 'pinia';
import axios from 'axios';
import { ref, Ref } from 'vue';

// Assuming DTOs are defined elsewhere and imported, or define simplified versions here
// For now, using 'any' for simplicity in DTO types, replace with actual DTOs
interface UserProfile {
  username: string;
  nickname?: string;
  avatarUrl?: string;
}

interface TopicDto {
  id: number;
  name: string;
  description?: string;
}

interface PostResponseDto {
  id: number;
  title: string;
  content: string;
  authorUsername: string;
  authorNickname?: string;
  authorAvatarUrl?: string;
  topicName: string;
  topicId: number; // Added topicId for filtering
  createdAt: string; // Assuming ISO string date
  updatedAt: string; // Assuming ISO string date
  likesCount: number;
  // commentsCount?: number; // Add if available from backend
  topicId: number;
}

interface PostRequestPayload { // For creating/updating posts
  title: string;
  content: string;
  topicId: number;
}


interface CommentResponseDto {
  id: number;
  content: string;
  authorUsername: string;
  authorNickname?: string;
  authorAvatarUrl?: string;
  postId: number;
  parentCommentId?: number | null;
  createdAt: string; // Assuming ISO string date
}

interface PaginationState {
  currentPage: number;
  pageSize: number;
  totalItems: number;
  totalPages: number;
}

export const useForumStore = defineStore('forum', () => {
  // State
  const posts: Ref<PostResponseDto[]> = ref([]);
  const currentPost: Ref<PostResponseDto | null> = ref(null);
  const comments: Ref<CommentResponseDto[]> = ref([]);
  const topics: Ref<TopicDto[]> = ref([]);

  const isLoadingPosts = ref(false);
  const isLoadingCurrentPost = ref(false);
  const isLoadingTopics = ref(false);
  const isLoadingComments = ref(false);
  const error: Ref<string | null> = ref(null);

  const pagination: Ref<PaginationState> = ref({
    currentPage: 0,
    pageSize: 10,
    totalItems: 0,
    totalPages: 0,
  });

  // Actions
  async function fetchTopics() {
    isLoadingTopics.value = true;
    error.value = null;
    try {
      const response = await axios.get<TopicDto[]>('/api/topics'); // Adjust endpoint if needed
      topics.value = response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || 'Failed to fetch topics.';
    } finally {
      isLoadingTopics.value = false;
    }
  }

  async function fetchPosts(page: number = 0, size: number = 10, topicId?: number, userId?: number) {
    isLoadingPosts.value = true;
    error.value = null;
    try {
      const params: Record<string, any> = { page, size };
      if (topicId) params.topicId = topicId;
      if (userId) params.userId = userId; // For fetching posts by a specific user

      const response = await axios.get<any>('/api/posts', { params }); // Adjust endpoint and response type
      // Assuming backend returns a PageResponse-like structure
      posts.value = response.data.content || [];
      pagination.value = {
        currentPage: response.data.pageNumber ?? response.data.number ?? page,
        pageSize: response.data.pageSize ?? response.data.size ?? size,
        totalItems: response.data.totalElements ?? 0,
        totalPages: response.data.totalPages ?? 0,
      };
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || 'Failed to fetch posts.';
      posts.value = []; // Clear posts on error
    } finally {
      isLoadingPosts.value = false;
    }
  }

  async function fetchPostById(postId: string | number) {
    isLoadingCurrentPost.value = true;
    error.value = null;
    currentPost.value = null; // Reset before fetching
    try {
      const response = await axios.get<PostResponseDto>(`/api/posts/${postId}`);
      currentPost.value = response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || `Failed to fetch post ${postId}.`;
    } finally {
      isLoadingCurrentPost.value = false;
    }
  }

  async function fetchCommentsByPostId(postId: string | number) {
    isLoadingComments.value = true;
    error.value = null;
    comments.value = []; // Reset before fetching
    try {
      const response = await axios.get<CommentResponseDto[]>(`/api/posts/${postId}/comments`);
      comments.value = response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || `Failed to fetch comments for post ${postId}.`;
    } finally {
      isLoadingComments.value = false;
    }
  }

  async function likePost(postId: number) {
    error.value = null;
    try {
      await axios.post(`/api/posts/${postId}/like`);
      // Update likesCount locally or refetch post/posts
      if (currentPost.value && currentPost.value.id === postId) {
        currentPost.value.likesCount++;
      }
      const postInList = posts.value.find(p => p.id === postId);
      if (postInList) {
        postInList.likesCount++;
      }
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || `Failed to like post ${postId}.`;
    }
  }

  async function unlikePost(postId: number) {
    error.value = null;
    try {
      await axios.delete(`/api/posts/${postId}/like`);
      // Update likesCount locally or refetch post/posts
      if (currentPost.value && currentPost.value.id === postId) {
        currentPost.value.likesCount--;
      }
      const postInList = posts.value.find(p => p.id === postId);
      if (postInList) {
        postInList.likesCount--;
      }
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || `Failed to unlike post ${postId}.`;
    }
  }

  // Placeholder for createComment
  async function createComment(payload: { postId: number; content: string; parentCommentId?: number | null }) {
    isLoadingComments.value = true;
    error.value = null;
    try {
      // Assuming endpoint /api/comments
      const response = await axios.post<CommentResponseDto>('/api/comments', payload);
      comments.value.push(response.data);
    } catch (err: any) {
        error.value = err.response?.data?.error || err.message || 'Failed to create comment.';
        throw err; // Re-throw so component can handle it
    } finally {
        isLoadingComments.value = false;
    }
  }

  async function createPost(postData: PostRequestPayload) {
    isLoadingPosts.value = true; // Or a specific isLoadingCreatePost
    error.value = null;
    try {
      const response = await axios.post<PostResponseDto>('/api/posts', postData);
      // Optionally add to posts list or invalidate/refetch
      // For now, just return the created post data
      // After creating a post, you might want to add it to the local 'posts' array or refetch.
      // Example: posts.value.unshift(response.data);
      // Or, more robustly, trigger a refetch of the current view's posts.
      return response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || 'Failed to create post.';
      throw err; // Re-throw for component to handle
    } finally {
      isLoadingPosts.value = false;
    }
  }

  async function updatePost(postId: string | number, postData: PostRequestPayload) {
    isLoadingCurrentPost.value = true; // Or a specific isLoadingUpdatePost
    error.value = null;
    try {
      const response = await axios.put<PostResponseDto>(`/api/posts/${postId}`, postData);
      // Update currentPost if it's the one being edited
      if (currentPost.value && currentPost.value.id === Number(postId)) {
        currentPost.value = response.data;
      }
      // Optionally update in the posts list as well
      const index = posts.value.findIndex(p => p.id === Number(postId));
      if (index !== -1) {
        posts.value[index] = response.data;
      }
      return response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || `Failed to update post ${postId}.`;
      throw err; // Re-throw for component to handle
    } finally {
      isLoadingCurrentPost.value = false;
    }
  }

  return {
    posts,
    currentPost,
    comments,
    topics,
    isLoadingPosts,
    isLoadingCurrentPost,
    isLoadingTopics,
    isLoadingComments,
    error,
    pagination,
    fetchTopics,
    fetchPosts,
    fetchPostById,
    fetchCommentsByPostId,
    likePost,
    unlikePost,
    createComment,
    createPost,
    updatePost,
  };
});
