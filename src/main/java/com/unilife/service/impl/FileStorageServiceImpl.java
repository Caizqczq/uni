package com.unilife.service.impl;

import com.unilife.exception.FileStorageException;
import com.unilife.exception.ResourceNotFoundException;
import com.unilife.mapper.CourseInfoMapper;
import com.unilife.mapper.SharedFileMapper;
import com.unilife.mapper.UserMapper;
import com.unilife.model.dto.SharedFileResponseDto;
import com.unilife.model.entity.CourseInfo;
import com.unilife.model.entity.SharedFile;
import com.unilife.model.entity.User;
import com.unilife.service.FileStorageService;
import com.unilife.utils.PageResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.io.Resource;
// import org.springframework.core.io.UrlResource;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.util.StringUtils;
// import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;

// @Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;
    private final SharedFileMapper sharedFileMapper;
    private final UserMapper userMapper;
    private final CourseInfoMapper courseInfoMapper; // To validate courseId

    // @Value("${file.upload-dir}") // Injected from application.properties
    private String uploadDir = "./uploads/"; // Default, should be configured

    // @Autowired
    public FileStorageServiceImpl(SharedFileMapper sharedFileMapper, UserMapper userMapper, CourseInfoMapper courseInfoMapper
                                 /*, @Value("${file.upload-dir}") String uploadDir */) {
        this.sharedFileMapper = sharedFileMapper;
        this.userMapper = userMapper;
        this.courseInfoMapper = courseInfoMapper;
        // this.uploadDir = uploadDir; // If @Value is used
        this.fileStorageLocation = Paths.get(this.uploadDir).toAbsolutePath().normalize();
        initStorage(); // Initialize storage on service creation
    }

    @Override
    public void initStorage() {
        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println("Created upload directory: " + this.fileStorageLocation.toString());
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    @Override
    // @Transactional
    public SharedFileResponseDto storeFile(InputStream fileInputStream, String originalFileName, String contentType, long fileSize,
                                           Long courseId, String description, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        if (courseId != null) {
            CourseInfo course = courseInfoMapper.findById(courseId);
            if (course == null) {
                throw new ResourceNotFoundException("CourseInfo not found with id: " + courseId);
            }
        }

        // Validate file name (basic validation)
        // String fileName = StringUtils.cleanPath(originalFileName); // Spring specific
        String fileName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");


        if (fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        // Generate unique stored file name
        String fileExtension = "";
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            fileExtension = fileName.substring(lastDot);
        }
        String storedFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(fileInputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            SharedFile sharedFile = new SharedFile();
            sharedFile.setFileName(fileName);
            sharedFile.setStoredFileName(storedFileName);
            sharedFile.setFilePath(this.fileStorageLocation.relativize(targetLocation).toString()); // Store relative path
            sharedFile.setFileType(contentType);
            sharedFile.setFileSize(fileSize);
            sharedFile.setCourseId(courseId);
            sharedFile.setUploadedByUserId(user.getId());
            sharedFile.setDescription(description);
            // createdAt is set by entity default constructor

            sharedFileMapper.save(sharedFile);
            return sharedFileMapper.findById(sharedFile.getId());

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                // Log error or handle
                System.err.println("Error closing input stream: " + e.getMessage());
            }
        }
    }

    @Override
    public Path loadFileAsResourcePath(Long fileId, String username) {
        // Optional: Add permission check for username if needed
        SharedFile sharedFile = sharedFileMapper.findRawById(fileId);
        if (sharedFile == null) {
            throw new ResourceNotFoundException("File not found with id " + fileId);
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(sharedFile.getStoredFileName()).normalize();
            // Resource resource = new UrlResource(filePath.toUri()); // Spring specific
            if (Files.exists(filePath) && Files.isReadable(filePath)) {
                return filePath; // Return Path as a placeholder for Spring Resource
            } else {
                throw new FileStorageException("Could not read file: " + sharedFile.getFileName());
            }
        } /*catch (MalformedURLException ex) { // For UrlResource
            throw new FileStorageException("Could not read file: " + sharedFile.getFileName(), ex);
        }*/ catch (Exception ex) { // Generic exception for other file issues
             throw new FileStorageException("Could not read file: " + sharedFile.getFileName(), ex);
        }
    }

    @Override
    public SharedFileResponseDto getSharedFileDetails(Long fileId) {
        SharedFileResponseDto fileDetails = sharedFileMapper.findById(fileId);
        if (fileDetails == null) {
            throw new ResourceNotFoundException("File not found with id " + fileId);
        }
        return fileDetails;
    }

    @Override
    public PageResponse<SharedFileResponseDto> getSharedFilesByCourse(Long courseId, int page, int size) {
        CourseInfo course = courseInfoMapper.findById(courseId);
        if (course == null && courseId != null) { // Allow courseId to be null if we want a list of non-course files, but API implies specific course
            throw new ResourceNotFoundException("CourseInfo not found with id: " + courseId);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("courseId", courseId); // Mapper should handle null courseId if desired
        params.put("offset", page * size);
        params.put("limit", size);

        List<SharedFileResponseDto> files = sharedFileMapper.findByCourseId(params);
        long totalElements = sharedFileMapper.countByCourseId(courseId);
        return new PageResponse<>(files, page, size, totalElements);
    }

    @Override
    public PageResponse<SharedFileResponseDto> getAllSharedFiles(int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);

        List<SharedFileResponseDto> files = sharedFileMapper.findAll(params);
        long totalElements = sharedFileMapper.countAll(params);
        return new PageResponse<>(files, page, size, totalElements);
    }

    @Override
    // @Transactional
    public void deleteFile(Long fileId, String username) {
        SharedFile sharedFile = sharedFileMapper.findRawById(fileId);
        if (sharedFile == null) {
            throw new ResourceNotFoundException("File not found with id " + fileId);
        }

        User currentUser = userMapper.findByUsername(username);
        boolean isAdmin = currentUser != null && currentUser.getRoles() != null && currentUser.getRoles().contains("ADMIN");

        if (currentUser == null || (!Objects.equals(sharedFile.getUploadedByUserId(), currentUser.getId()) && !isAdmin)) {
            // throw new AccessDeniedException("User not authorized to delete this file");
            throw new RuntimeException("User not authorized to delete this file (placeholder for AccessDeniedException)");
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(sharedFile.getStoredFileName()).normalize();
            Files.deleteIfExists(filePath);
            sharedFileMapper.deleteById(fileId);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file " + sharedFile.getFileName() + ". Please try again!", ex);
        }
    }

    // For Spring @Value injection if not done in constructor
    // public void setUploadDir(String uploadDir) {
    //     this.uploadDir = uploadDir;
    //     this.fileStorageLocation = Paths.get(this.uploadDir).toAbsolutePath().normalize();
    // }
}
