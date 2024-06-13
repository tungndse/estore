package com.coldev.estore.domain.service;

import com.coldev.estore.config.firebase.FirebaseProperties;
import com.coldev.estore.domain.dto.media.response.MediaResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public interface FirebaseService {

    default String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString();
    }

    default byte[] getByteArrays(BufferedImage bufferedImage, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, format, baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            baos.close();
        }
    }

    default String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }


    String getMediaUrl(String mediaName);

    String getMediaFolder(String folderName);

    MediaResponse uploadMedia(MultipartFile file) throws IOException;

    MediaResponse uploadMedia(String fileName, byte[] file, String mediaType) throws IOException;

    String save(MultipartFile file, String filePath) throws IOException;

    String save(BufferedImage bufferedImage, String originalFileName) throws IOException;


    MediaResponse findMediaUrl(String mediaName);
}
