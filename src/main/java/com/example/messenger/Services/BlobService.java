package com.example.messenger.Services;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.UserDelegationKey;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.sas.AccountSasPermission;
import com.azure.storage.common.sas.AccountSasResourceType;
import com.azure.storage.common.sas.AccountSasService;
import com.azure.storage.common.sas.AccountSasSignatureValues;
import com.example.messenger.POJO.Enums.MessageType;
import com.example.messenger.POJO.Response.ChatroomMessageResponse;
import com.example.messenger.POJO.Response.ContainerTokens;
import com.example.messenger.Reposotories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class BlobService {
    private final UserRepository userRepository;
    public final SimpMessagingTemplate simpMessagingTemplate;

    @Value("${storage-account-name}")
    private String storageAccountName;

    private BlobContainerClient containerImg;
    private BlobContainerClient containerImgMsg;
    private BlobContainerClient containerUserImgMsg;
    private BlobContainerClient containerRoomAudioMsg;
    private BlobContainerClient containerUserAudioMsg;
    private BlobServiceClient blobServiceClient;
    private String fileName;
    private HashMap<String,String> containerTokens;
    private HashMap expiryTokens;
    private  RetryTemplate retryTemplate;


    public HashMap<String, String> getContainerTokens() {
        return containerTokens;
    }

    public BlobService(UserRepository userRepository,SimpMessagingTemplate simpMessagingTemplate) {
        this.userRepository = userRepository;
        this.simpMessagingTemplate= simpMessagingTemplate;

        expiryTokens=new HashMap<>();
        containerTokens=new HashMap<>();
        this.retryTemplate=new RetryTemplate();


    }

    @PostConstruct
    void init() {
        this.blobServiceClient=generateBlobToken();

        containerImg = createContainerWithSas("images");
        containerImgMsg = createContainerWithSas("imgmsg");
        containerUserImgMsg = createContainerWithSas("one-to-one-user-image");
        containerRoomAudioMsg = createContainerWithSas("room-audios");
        containerUserAudioMsg = createContainerWithSas("one-to-one-user-audio");

    }



    public String uploadFile(MultipartFile file, String id) throws IOException {
        String url = this.containerImg.getBlobContainerUrl();
        fileName = id + file.getOriginalFilename();
        BlobClient blob = this.containerImg.getBlobClient(fileName);
        blob.upload(file.getInputStream(), file.getSize(), true);
        url = url + "/" + fileName;  // Changed to include "/" before fileName
        this.userRepository.updateImageUrl(url, id);
        return url;
    }

    public String uploadImgMessage(MultipartFile file, long id) throws IOException {
        String url = this.containerImgMsg.getBlobContainerUrl() + "/";
        fileName = LocalDateTime.now() + String.valueOf(id) + file.getOriginalFilename();
        BlobClient blob = this.containerImgMsg.getBlobClient(fileName);
        blob.upload(file.getInputStream(), file.getSize(), true);
        url = url + fileName;
        return url;
    }

    public String uploadUserMessage(MultipartFile file, String id) throws IOException {
        String url = this.containerUserImgMsg.getBlobContainerUrl() + "/";
        fileName = LocalDateTime.now() + id + file.getOriginalFilename();
        BlobClient blob = this.containerUserImgMsg.getBlobClient(fileName);
        blob.upload(file.getInputStream(), file.getSize(), true);
        url = url + fileName;
        return url;
    }

    public String uploadRoomAudioMessage(MultipartFile file, long id) throws IOException {
        String url = this.containerRoomAudioMsg.getBlobContainerUrl() + "/";
        fileName = LocalDateTime.now() + String.valueOf(id) + file.getOriginalFilename();
        BlobClient blob = this.containerRoomAudioMsg.getBlobClient(fileName);
        blob.upload(file.getInputStream(), file.getSize(), true);
        url = url + fileName;
        return url;
    }

    public String uploadUserAudioMessage(MultipartFile file, String id) throws IOException {
        String url = this.containerUserAudioMsg.getBlobContainerUrl() + "/";
        fileName = LocalDateTime.now() + id + file.getOriginalFilename();
        BlobClient blob = this.containerUserAudioMsg.getBlobClient(fileName);
        blob.upload(file.getInputStream(), file.getSize(), true);
        url = url + fileName;
        return url;
    }

    // Generate token for storage blob
    BlobServiceClient generateBlobToken() {
        BlobServiceClient blobServiceClient= new BlobServiceClientBuilder()
                .endpoint("https://" + storageAccountName + ".blob.core.windows.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        return blobServiceClient;
    }

    public UserDelegationKey requestUserDelegationKey(BlobServiceClient blobServiceClient) {
        return blobServiceClient.getUserDelegationKey(
                OffsetDateTime.now().minusMinutes(5),
                OffsetDateTime.now().plusDays(1));
    }

    public String createUserDelegationSASBlob(BlobContainerClient blobClient, UserDelegationKey userDelegationKey) {
        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);
        BlobContainerSasPermission sasPermission = new BlobContainerSasPermission()
                .setReadPermission(true)
                .setWritePermission(true)
                .setCreatePermission(true)
                .setAddPermission(true)
                .setListPermission(true); // Added list permission

        BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                .setStartTime(OffsetDateTime.now().minusMinutes(5));
 this.expiryTokens.put(blobClient.getBlobContainerName(),expiryTime);
        return blobClient.generateUserDelegationSas(sasSignatureValues, userDelegationKey);
    }


    public String authorizeContainerWithSas(String containerName) {
        BlobContainerClient blobClient = this.blobServiceClient.getBlobContainerClient(containerName); // Fixed to use containerName
        UserDelegationKey userDelegationKey = requestUserDelegationKey(blobServiceClient);
       String sas=createUserDelegationSASBlob(blobClient, userDelegationKey);
       containerTokens.put(containerName,sas);
        return sas;
    }

    BlobContainerClient createContainerWithSas(String containerName) {

        String sasToken = authorizeContainerWithSas(containerName);
        System.out.println(sasToken);
        String containerUrl = String.format("https://%s.blob.core.windows.net/%s?%s",
                this.storageAccountName,
                containerName,
                sasToken);  // Fixed URL formatting to append SAS token correctly

        return new BlobContainerClientBuilder()
                .endpoint(containerUrl)
                .buildClient();
    }
    void refreshTokens(){
        init();

    }




    @Bean
    @Scheduled(fixedDelay = 1,timeUnit = TimeUnit.DAYS)
    @Retryable(maxAttempts = 3, value = {Exception.class})
    public void cleanPasswordCodes() throws Exception {
        retryTemplate.execute(context -> {
            System.out.println("scheduled run");

this.init();
            this.simpMessagingTemplate.convertAndSend("/topic/containerTokens" , this.containerTokens);

            System.out.println("Cleaning succeeded");
            return null;
        });
    }
}
