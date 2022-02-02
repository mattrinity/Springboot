package com.example.filedemo.controller;

import com.example.filedemo.payload.UploadFileResponse;
import com.example.filedemo.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin("*")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);


    private final Set<String>  WORD_FILE_TYPES =  new HashSet<>(
            Arrays.asList("application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    ,"application/vnd.openxmlformats-officedocument.wordprocessingml.template"
                    ,"application/vnd.ms-word.document.macroEnabled.12"
                    ,"application/vnd.ms-word.template.macroEnabled.12"));
    @Autowired
    private FileStorageService fileStorageService;
    private static List<UploadFileResponse> uploadFileResponses = new ArrayList<>();
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {

        if (!WORD_FILE_TYPES.contains(file.getContentType())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"Invalid file type. Only word files are allowed.");
        }
        File theFile = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(theFile.getName())
                .toUriString();
        UploadFileResponse response = new UploadFileResponse(theFile.getName(), file.getName(), fileDownloadUri,
                "text/pdf", theFile.length(), "Uploaded");

        uploadFileResponses.add(response);
        return response;
    }
    @GetMapping("/listFiles")
    public ResponseEntity<List<UploadFileResponse>> downloadFile() {
        return new ResponseEntity<>(uploadFileResponses,HttpStatus.OK) ;
    }

    @GetMapping("/downloadFile/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
