package com.project.Project.service.fileProcess;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.Project.aws.s3.FileService;
import com.project.Project.aws.s3.ThumbnailImagePackageMetadata;
import com.project.Project.domain.Thumbnail;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class ThumbnailImageProcess extends FileProcessServiceImpl<ThumbnailImagePackageMetadata> {
    @Autowired
    public ThumbnailImageProcess(FileService amazonS3Service) {
        super(amazonS3Service);
    }

    private final String thumbnailSavePath = "classpath:/thumbnail";

    public Thumbnail makeThumbnailAndUpload(MultipartFile file, ThumbnailImagePackageMetadata thumbnailImagePackageMetadata) {
        String fileName = createThumbnail(file, thumbnailImagePackageMetadata);
        String url = this.uploadThumbnail(file, thumbnailImagePackageMetadata, fileName);
        return Thumbnail.builder()
                .url(url)
                .fileName(fileName)
                .uuid(thumbnailImagePackageMetadata.getUuidEntity())
                .build();
    }

    private String createThumbnail(MultipartFile file, ThumbnailImagePackageMetadata thumbnailImagePackageMetadata) {
        String fileName = this.createFileName(thumbnailImagePackageMetadata.getUuid(), file.getOriginalFilename());
        FileOutputStream thumbnail = null;
        try {
            thumbnail = new FileOutputStream(new File(thumbnailSavePath, fileName));
            Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, 200, 200);
            thumbnail.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }

    private String uploadThumbnail(MultipartFile file, ThumbnailImagePackageMetadata thumbnailImagePackageMetadata, String fileName) {
        String filePath = this.getFilePath(file, thumbnailImagePackageMetadata);
        ObjectMetadata objectMetadata = super.generateObjectMetadata(file);
        File thumbNail = new File(thumbnailSavePath, fileName);
        String url = null;
        try {
            url = this.uploadImage(new FileInputStream(thumbNail), objectMetadata, filePath, fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    @Override
    public String getFilePath(MultipartFile file, ThumbnailImagePackageMetadata imagePackage) {
        return super.getFilePath(file, imagePackage);
    }

    @Override
    protected String createFileName(String uuid, String originalFileName) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append(uuid).append("s_").append(originalFileName).toString();
    }

    @Override
    protected String getFileExtension(String fileName) {
        return super.getFileExtension(fileName);
    }
}
