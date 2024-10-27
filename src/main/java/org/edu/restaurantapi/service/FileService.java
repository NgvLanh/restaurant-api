package org.edu.restaurantapi.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final static String uploadedFolder = "uploads/images/";

    public static String saveFile(MultipartFile file) throws IOException {
       if (file != null && !file.isEmpty()) {
           File directory = new File(uploadedFolder);
           if (!directory.exists()) {
               directory.mkdirs();
           }
           String fileName = file.getOriginalFilename();
           Path path = Paths.get(uploadedFolder + fileName);
           Files.write(path, file.getBytes());
           System.out.println("File saved to: " + path.toAbsolutePath());
           return fileName;
       }
        return null;
    }

    public static Resource getFile(String fileName) throws IOException {
        Path path = Paths.get(uploadedFolder + fileName);
        return new UrlResource(path.toUri());
    }
}
