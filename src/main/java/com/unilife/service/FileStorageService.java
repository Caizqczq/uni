package com.unilife.service;

import com.unilife.model.dto.SharedFileResponseDto;
import com.unilife.utils.PageResponse;
// import org.springframework.core.io.Resource; // Placeholder for Spring's Resource
// import org.springframework.web.multipart.MultipartFile; // Placeholder

import java.io.InputStream; // Using InputStream as a generic representation
import java.nio.file.Path; // For path operations

public interface FileStorageService {

    /**
     * Stores a file.
     *
     * @param fileInputStream The InputStream of the file to store.
     * @param originalFileName The original name of the file.
     * @param contentType The MIME type of the file.
     * @param fileSize The size of the file in bytes.
     * @param courseId Optional ID of the course this file is associated with.
     * @param description Optional description of the file.
     * @param username The username of the uploader.
     * @return SharedFileResponseDto containing details of the stored file.
     */
    SharedFileResponseDto storeFile(InputStream fileInputStream, String originalFileName, String contentType, long fileSize,
                                    Long courseId, String description, String username);

    /**
     * Loads a file as a resource.
     *
     * @param fileId The ID of the file to load.
     * @param username The username of the user requesting the file (for permission checks).
     * @return A Resource object representing the file. The caller is responsible for closing the InputStream from the resource.
     *         (Using Path as a placeholder for Resource if Spring Core is not available)
     */
    Path loadFileAsResourcePath(Long fileId, String username); // Returning Path as placeholder for Spring Resource

    /**
     * Retrieves the metadata for a shared file.
     *
     * @param fileId The ID of the file.
     * @return SharedFileResponseDto containing file details.
     */
    SharedFileResponseDto getSharedFileDetails(Long fileId);

    /**
     * Retrieves a paginated list of shared files for a specific course.
     *
     * @param courseId The ID of the course.
     * @param page The page number (0-indexed).
     * @param size The page size.
     * @return PageResponse of SharedFileResponseDto.
     */
    PageResponse<SharedFileResponseDto> getSharedFilesByCourse(Long courseId, int page, int size);

    /**
     * Retrieves a paginated list of all shared files.
     *
     * @param page The page number (0-indexed).
     * @param size The page size.
     * @return PageResponse of SharedFileResponseDto.
     */
    PageResponse<SharedFileResponseDto> getAllSharedFiles(int page, int size);

    /**
     * Deletes a file.
     *
     * @param fileId The ID of the file to delete.
     * @param username The username of the user attempting deletion (for permission checks).
     */
    void deleteFile(Long fileId, String username);

    /**
     * Initializes the file storage directory.
     */
    void initStorage();
}
