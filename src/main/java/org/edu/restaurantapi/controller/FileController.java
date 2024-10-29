package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.Base64;


@RestController
@RequestMapping("/api/files")
public class FileController {

    //    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    private ResponseEntity<?> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file) {
        try {
            String urlFile = FileService.saveFile(file);
            return ResponseEntity.status(urlFile == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK)
                    .body(ApiResponse.SUCCESS(urlFile == null ? "Only one file is allowed" : urlFile));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
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

    // API lấy hình ảnh và trả về dạng Base64
    @GetMapping("/image/{fileName}")
    public ResponseEntity<String> getImageBase64(@PathVariable String fileName) throws IOException {
        // Đường dẫn đến thư mục chứa file ảnh
        String imagePath = "uploads/images/" + fileName;

        // Kiểm tra xem file có tồn tại hay không
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found");
        }

        // Đọc file ảnh
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

        // Sử dụng java.util.Base64 để mã hóa thành chuỗi Base64
        String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);

        // Trả về chuỗi Base64 của ảnh
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(base64Image);
    }
}