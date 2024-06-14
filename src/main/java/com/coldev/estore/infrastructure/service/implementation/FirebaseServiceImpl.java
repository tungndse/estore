package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.config.firebase.FirebaseProperties;
import com.coldev.estore.domain.dto.media.response.MediaResponse;
import com.coldev.estore.domain.entity.Media;
import com.coldev.estore.domain.service.FirebaseService;
import com.coldev.estore.infrastructure.repository.MediaRepository;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class FirebaseServiceImpl implements FirebaseService {

    final FirebaseProperties properties;

    final MediaRepository mediaRepository;


    public FirebaseServiceImpl(FirebaseProperties properties, MediaRepository mediaRepository) {
        this.properties = properties;
        this.mediaRepository = mediaRepository;
    }

    @Override
    public String getMediaUrl(String mediaName) {
        return String.format(properties.getImageUrl(), mediaName);
    }

    @Override
    public String getMediaFolder(String folderName) {
        return String.format(properties.getFolderUrl(), folderName);
    }

    @Override
    public MediaResponse uploadMedia(MultipartFile file) throws IOException {
        if (file == null) {
            return null;
        }
        Bucket bucket = StorageClient.getInstance().bucket();
        String name = generateFileName(file.getOriginalFilename());
        bucket.create(name, file.getBytes(), file.getContentType());
        Media media = new Media();
        media.setKey(name);
        media.setMediaType(file.getContentType());
        media.setUrl(this.getMediaUrl(name));
        media = mediaRepository.save(media);
        return new MediaResponse(media.getKey(), getMediaUrl(media.getKey()), media.getMediaType());
    }

    @Override
    public MediaResponse uploadMedia(String fileName, byte[] file, String mediaType) throws IOException {
        if (file == null) {
            return null;
        }
        Bucket bucket = StorageClient.getInstance().bucket();
        String name = generateFileName(fileName);
        bucket.create(name, file, mediaType);
        Media media = Media.builder()
                .key(name).mediaType(mediaType)
                .url(getMediaUrl(name))
                .build();

        media = mediaRepository.save(media);

        return new MediaResponse(media.getKey(), getMediaUrl(media.getKey()), media.getMediaType());
    }

    @Override
    public String save(MultipartFile file, String filePath) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        String fileName = generateFileName(file.getOriginalFilename());
        String finalFileName = filePath + "/" + fileName;
        bucket.create(finalFileName, file.getBytes(), file.getContentType());
        return getMediaUrl(finalFileName);
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {
        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));
        Bucket bucket = StorageClient.getInstance().bucket();
        String name = generateFileName(originalFileName);
        bucket.create(name, bytes);
        return name;
    }

    @Override
    public MediaResponse findMediaUrl(String mediaName) {
        MediaResponse response = null;
        Media media;
        media = mediaRepository.getMediaByKey(mediaName);
        if (media != null) {
            response = new MediaResponse();
            response.setMediaKeys(media.getKey());
            response.setMediaType(media.getMediaType());
            response.setMediaUrls(getMediaUrl(media.getKey()));
        }
        return response;
    }

}
