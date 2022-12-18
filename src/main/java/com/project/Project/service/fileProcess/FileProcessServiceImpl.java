package com.project.Project.service.fileProcess;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.Project.aws.s3.AmazonS3PackageCommand;
import com.project.Project.aws.s3.FilePackageMeta;
import com.project.Project.aws.s3.FileService;
import com.project.Project.repository.uuid.UuidCustomRepositoryImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public abstract class FileProcessServiceImpl<T extends FilePackageMeta> {
    private FileService amazonS3Service;

    private UuidCustomRepositoryImpl uuidRepository;

    public FileProcessServiceImpl(FileService amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    public String uploadImage(MultipartFile file, T imagePackage) {
        AmazonS3PackageCommand command = imagePackage.createCommand();
        String uuid = imagePackage.getUuid();
        String filePath = amazonS3Service.getFileFolder(command) + createFileName(uuid, file.getOriginalFilename());
        ObjectMetadata objectMetadata = generateObjectMetadata(file);
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Service.uploadFile(inputStream, objectMetadata, filePath);
        } catch (IOException ioe) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생했습니다 (%s)", file.getOriginalFilename()));
        }

        return amazonS3Service.getFileUrl(filePath);
    }

    private ObjectMetadata generateObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        return objectMetadata;
    }

    private String createFileName(String uuid, String originalFileName) {
        return uuid.concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String createUUID() {
        String candidate = UUID.randomUUID().toString();
        if (uuidRepository.exist(candidate)) {
            candidate = createUUID();
        }
        return candidate;
    }

//    public void deleteImage(String url) {
//        amazonS3Service.deleteFile(getFileName(url));
//    }
//
//    private String getFileName(String url) {
//        String[] paths = url.split("/");
//        return paths[paths.length - 2] + "/" + paths[paths.length - 1];
//    }
}
