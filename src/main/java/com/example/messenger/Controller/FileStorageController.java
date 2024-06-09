package com.example.messenger.Controller;

import com.example.messenger.Exceptions.StorageFileNotFoundException;
import com.example.messenger.POJO.User;
import com.example.messenger.Services.FileStorageSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/file/storage" ,produces = MediaType.APPLICATION_JSON_VALUE)
public class FileStorageController {
public final FileStorageSystem storageService;
    @GetMapping(value = "/files/{filename:.+}",produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<Resource> serveFile(@PathVariable String filename,Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();

        Resource file = storageService.loadAsResource(filename,user.getEmail());

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    @PostMapping("/")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, Principal principal) {
storageService.init();
        User user = (User) ((Authentication) principal).getPrincipal();

        storageService.store(file ,user.getEmail());
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return   ResponseEntity.ok().body(true);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
