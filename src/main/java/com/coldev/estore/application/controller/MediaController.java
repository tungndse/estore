package com.coldev.estore.application.controller;

import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.domain.dto.ResponseObject;
import com.coldev.estore.domain.dto.media.response.MediaResponse;
import com.coldev.estore.domain.service.FirebaseService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("api/v1/media-files")
public class MediaController {

    final
    FirebaseService firebaseService;

    public MediaController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping(value = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> saveMediaList(@RequestPart MultipartFile[] files)
            throws IOException {
        List<MediaResponse> imageUrlList = new ArrayList<>();
         for (MultipartFile file : files) {
             imageUrlList.add(firebaseService.uploadMedia(file));
         }

        ResponseObject<List<MediaResponse>> responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.ACTION_SUCCESS);
        responseObject.setTotalItems(imageUrlList.size());
        responseObject.setData(imageUrlList);
        return ResponseEntity.ok(responseObject);
    }

    @GetMapping(value = "/file/{id}")
    public ResponseEntity<ResponseObject<MediaResponse>> getFileUrl(@PathVariable(name = "id") String fileId) throws FirebaseMessagingException {
//        firebaseService.sendNotification(new NotificationMessage().builder().content("nice").data(new HashMap<>()).build(), "e-KQYOLETMGDtwaCct1cNK:APA91bG4SyarlhuGIvTQUKYKWGtgtcRLSyYqBXD1qvO-czgjEpqMUY5fMnY5OBbfWbdP5Rjo25Ek8Gyq2H7TYow38pedbsGbWbXRQhHSi5N0TXbEFKArU-fWSzVZgin4jub7U454tgqs");
        ResponseObject<MediaResponse> responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.ACTION_SUCCESS);
        responseObject.setData(firebaseService.findMediaUrl(fileId));
        if(responseObject.getData() != null){
            responseObject.setTotalItems(1);
        } else {
            responseObject.setTotalItems(0);
        }
        return ResponseEntity.ok(responseObject);
    }
}
