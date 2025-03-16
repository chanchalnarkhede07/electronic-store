package com.golu.electronic.store.services.impl;

import com.golu.electronic.store.exceptions.BadApiRequestException;
import com.golu.electronic.store.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;
        String fullPathWithFileName = path + File.separator + fileNameWithExtension;

        if (extension.equalsIgnoreCase(".png") ||
                extension.equalsIgnoreCase(".jpg") ||
                extension.equalsIgnoreCase(".jpeg")) {

            //save file

            File folder = new File(path);
            if (!folder.exists()) {
                //create the folder
                folder.mkdirs();
            }
            //upload the file

            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;

        } else {
            throw new BadApiRequestException("File type with " + extension + " is not supported ");
        }
    }

    @Override
    public InputStream getFile(String path, String filename) throws FileNotFoundException {
        String Path = path + File.separator + filename;
        InputStream inputStream = new FileInputStream(Path);
        return inputStream;
    }
}
