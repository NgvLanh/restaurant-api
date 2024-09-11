package org.edu.restaurantapi.util;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadUtil {
    private final static String uploadedFolder = "uploads/images/";

    public static String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        File directory = new File(uploadedFolder);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Path path = Paths.get(uploadedFolder + fileName);
        Files.write(path, multipartFile.getBytes());

        System.out.println("File saved to: " + path.toAbsolutePath().toString());
        return path.toString();
    }

}
