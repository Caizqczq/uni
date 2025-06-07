import { defineStore } from 'pinia';
import axios from 'axios';
import { ref, Ref } from 'vue';
import router from '../router'; // For navigation

// Simplified DTOs (replace with actual imports if available)
interface CourseInfoDto {
  id: number;
  courseCode: string;
  courseName: string;
  description?: string;
}

interface SharedDocumentResponseDto {
  id: number;
  title: string;
  content: string;
  courseInfo?: CourseInfoDto; // Embedded or just courseId
  courseId: number; // Ensure this is present if courseInfo is not fully populated
  createdByUsername?: string;
  lastUpdatedByUsername?: string;
  createdAt: string;
  updatedAt: string;
  version: number;
}

interface SharedDocumentRequestDto {
  title: string;
  content: string;
  courseId: number;
}

interface SharedFileResponseDto {
  id: number;
  fileName: string;
  fileType: string;
  fileSize: number; // Assuming long is serialized as number in JS/TS
  courseInfo?: CourseInfoDto | null; // Can be null
  uploadedByUsername?: string;
  createdAt: string;
  description?: string;
}

interface FilePaginationData {
  currentPage: number;
  pageSize: number;
  totalItems: number;
  totalPages: number;
}


export const useLearningResourcesStore = defineStore('learningResources', () => {
  // State
  const courses: Ref<CourseInfoDto[]> = ref([]);
  const documents: Ref<SharedDocumentResponseDto[]> = ref([]);
  const currentDocument: Ref<SharedDocumentResponseDto | null> = ref(null);
  const files: Ref<SharedFileResponseDto[]> = ref([]); // New state for files

  const isLoadingCourses = ref(false);
  const isLoadingDocuments = ref(false);
  const isLoadingCurrentDocument = ref(false);
  const isSubmittingDocument = ref(false);
  const isLoadingFiles = ref(false); // New loading state for files
  const isUploadingFile = ref(false); // New state for upload process
  const error: Ref<string | null> = ref(null);
  const fileError: Ref<string | null> = ref(null); // Specific error for file operations

  const filePagination: Ref<FilePaginationData> = ref({ // New pagination for files
    currentPage: 0,
    pageSize: 10,
    totalItems: 0,
    totalPages: 0,
  });


  // Actions
  async function fetchCourses() {
    isLoadingCourses.value = true;
    error.value = null;
    try {
      const response = await axios.get<CourseInfoDto[]>('/api/courses'); // Adjust endpoint as needed
      courses.value = response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || 'Failed to fetch courses.';
      courses.value = [];
    } finally {
      isLoadingCourses.value = false;
    }
  }

  async function fetchDocumentsByCourse(courseId: number) {
    isLoadingDocuments.value = true;
    error.value = null;
    try {
      // Assuming endpoint returns a list, not paginated for now for simplicity in this DTO
      const response = await axios.get<SharedDocumentResponseDto[]>(`/api/courses/${courseId}/shared-documents`);
      documents.value = response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || `Failed to fetch documents for course ${courseId}.`;
      documents.value = [];
    } finally {
      isLoadingDocuments.value = false;
    }
  }

  async function fetchDocumentById(docId: number) {
    isLoadingCurrentDocument.value = true;
    error.value = null;
    currentDocument.value = null;
    try {
      const response = await axios.get<SharedDocumentResponseDto>(`/api/shared-documents/${docId}`);
      currentDocument.value = response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || `Failed to fetch document ${docId}.`;
    } finally {
      isLoadingCurrentDocument.value = false;
    }
  }

  async function createDocument(docData: SharedDocumentRequestDto) {
    isSubmittingDocument.value = true;
    error.value = null;
    try {
      const response = await axios.post<SharedDocumentResponseDto>('/api/shared-documents', docData);
      // Optionally add to local state or refetch
      // documents.value.push(response.data); // If relevant to current view
      return response.data; // Return created document
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || 'Failed to create document.';
      throw err; // Re-throw for component to handle
    } finally {
      isSubmittingDocument.value = false;
    }
  }

  async function updateDocument(docId: number, docData: SharedDocumentRequestDto) {
    isSubmittingDocument.value = true;
    error.value = null;
    try {
      const response = await axios.put<SharedDocumentResponseDto>(`/api/shared-documents/${docId}`, docData);
      // Update currentDocument if it's the one being edited
      if (currentDocument.value && currentDocument.value.id === docId) {
        currentDocument.value = response.data;
      }
      // Update in the documents list if present
      const index = documents.value.findIndex(d => d.id === docId);
      if (index !== -1) {
        documents.value[index] = response.data;
      }
      return response.data; // Return updated document
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || `Failed to update document ${docId}.`;
      throw err; // Re-throw for component to handle
    } finally {
      isSubmittingDocument.value = false;
    }
  }

  function clearCurrentDocument() {
    currentDocument.value = null;
  }

  function clearDocuments() {
    documents.value = [];
  }

  async function fetchFilesByCourse(courseId: number, page: number = 0, size: number = filePagination.value.pageSize) {
    isLoadingFiles.value = true;
    fileError.value = null;
    try {
      const response = await axios.get<any>(`/api/files/course/${courseId}`, { // Endpoint from FileController
        params: { page, size }
      });
      files.value = response.data.content || [];
      filePagination.value = {
        currentPage: response.data.pageNumber ?? response.data.number ?? page,
        pageSize: response.data.pageSize ?? response.data.size ?? size,
        totalItems: response.data.totalElements ?? 0,
        totalPages: response.data.totalPages ?? 0,
      };
    } catch (err: any) {
      fileError.value = err.response?.data?.error || err.message || `Failed to fetch files for course ${courseId}.`;
      files.value = [];
    } finally {
      isLoadingFiles.value = false;
    }
  }

  async function fetchAllFiles(page: number = 0, size: number = filePagination.value.pageSize) {
    isLoadingFiles.value = true;
    fileError.value = null;
    try {
      const response = await axios.get<any>('/api/files', { // Endpoint from FileController
        params: { page, size }
      });
      files.value = response.data.content || [];
      filePagination.value = {
        currentPage: response.data.pageNumber ?? response.data.number ?? page,
        pageSize: response.data.pageSize ?? response.data.size ?? size,
        totalItems: response.data.totalElements ?? 0,
        totalPages: response.data.totalPages ?? 0,
      };
    } catch (err: any) {
      fileError.value = err.response?.data?.error || err.message || 'Failed to fetch all files.';
      files.value = [];
    } finally {
      isLoadingFiles.value = false;
    }
  }

  async function uploadFile(formData: FormData): Promise<SharedFileResponseDto | null> {
    isUploadingFile.value = true;
    fileError.value = null;
    try {
      const response = await axios.post<SharedFileResponseDto>('/api/files/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      // Optionally refresh current list or add to it
      // For simplicity, we might just let the user see it on next full fetch or if on specific course page
      if (response.data.courseInfo?.id) {
        if (files.value.length > 0 && files.value[0].courseInfo?.id === response.data.courseInfo.id) {
            // files.value.unshift(response.data); // Add to start if current view is for this course
        }
      } else {
          // If viewing "all files" and not course specific, could add here too.
      }
      return response.data;
    } catch (err: any) {
      fileError.value = err.response?.data?.error || err.message || 'File upload failed.';
      throw err;
    } finally {
      isUploadingFile.value = false;
    }
  }

  async function deleteFile(fileId: number): Promise<boolean> {
    // Consider adding a specific loading state e.g. isDeletingFile
    fileError.value = null;
    try {
      await axios.delete(`/api/files/${fileId}`);
      // Remove from local state
      files.value = files.value.filter(f => f.id !== fileId);
      // Adjust pagination if needed, though often a full refetch is simpler
      if (filePagination.value.totalItems > 0) {
          filePagination.value.totalItems--;
          // Potentially refetch current page if it becomes empty
      }
      return true;
    } catch (err: any) {
      fileError.value = err.response?.data?.error || err.message || `Failed to delete file ${fileId}.`;
      throw err; // Let component handle UI update on error
    }
  }


  return {
    courses,
    documents,
    currentDocument,
    isLoadingCourses,
    isLoadingDocuments,
    isLoadingCurrentDocument,
    isSubmittingDocument,
    isLoadingFiles,
    isUploadingFile,
    error, // General error for documents
    fileError, // Specific error for files
    filePagination, // Pagination for files
    fetchCourses,
    fetchDocumentsByCourse,
    fetchDocumentById,
    createDocument,
    updateDocument,
    clearCurrentDocument,
    clearDocuments,
    fetchFilesByCourse,
    fetchAllFiles,
    uploadFile,
    deleteFile,
  };
});
