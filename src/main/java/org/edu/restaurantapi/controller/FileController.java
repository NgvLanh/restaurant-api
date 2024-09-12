package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    private ResponseEntity<?> uploadFile(@RequestParam(name = "file", required = false) MultipartFile[] file) {
        try {
            String urlFile = FileService.saveFile(file);
            return ResponseEntity.status(urlFile == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK)
                    .body(ApiResponse.SUCCESS(urlFile == null ? "Only one file is allowed" : urlFile));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.BAD_REQUEST("Upload failed"));
        }
    }

    @GetMapping("/{fileName}")
    private ResponseEntity<?> getFile(@PathVariable String fileName)
            throws IOException {
        try {
            Resource resource = FileService.getFile(fileName);
            if (resource.exists() || resource.isReadable()) {
                MediaType mediaType = MediaType.IMAGE_JPEG;
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Not found the image: " + fileName));
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}