package com.example.messenger.Controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.example.messenger.POJO.User;
import com.example.messenger.Services.BlobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/file" ,produces = MediaType.APPLICATION_JSON_VALUE)

public class BlobController {
    private final BlobService blobService;
    @PostMapping(path = "/")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file , Principal principal)  {
       String url;
        try {
            User user=(User)((Authentication)principal).getPrincipal();
           url= blobService.uploadFile(file,user.getEmail());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("unknown error");
        }
        return   ResponseEntity.ok().body(url);
    }

}
