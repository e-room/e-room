package com.project.Project.service.fileProcess;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.Project.aws.s3.FileService;
import com.project.Project.aws.s3.ThumbnailImagePackageMetadata;
import com.project.Project.domain.Thumbnail;
import com.project.Project.repository.uuid.UuidCustomRepositoryImpl;
import com.project.Project.repository.uuid.UuidRepository;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ThumbnailImageProcess extends FileProcessServiceImpl<ThumbnailImagePackageMetadata> {
    @Autowired
    public ThumbnailImageProcess(FileService amazonS3Service, UuidCustomRepositoryImpl uuidCustomRepository, UuidRepository uuidRepository) throws IOException {
        super(amazonS3Service, uuidCustomRepository, uuidRepository);
    }

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
        try {
            String thumbnailSavePath = getResourcesFolder();
            String fileName = this.createFileName(thumbnailImagePackageMetadata.getUuid(), file.getOriginalFilename());
            FileOutputStream thumbnail = null;
            File thumbnailImage = new File(thumbnailSavePath, fileName);

            thumbnailImage.createNewFile();
            thumbnail = new FileOutputStream(thumbnailImage);
            Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, 512, 512);
            thumbnail.close();
            return fileName;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String uploadThumbnail(MultipartFile file, ThumbnailImagePackageMetadata thumbnailImagePackageMetadata, String fileName) {
        String filePath = this.getFilePath(file, thumbnailImagePackageMetadata);
        File thumbNail = new File(getResourcesFolder(), fileName);
        ObjectMetadata objectMetadata = this.generateObjectMetadata(thumbNail);
        String url = null;
        try {
            url = this.uploadImage(new FileInputStream(thumbNail), objectMetadata, filePath, fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    private ObjectMetadata generateObjectMetadata(File file) {
        try {
            Path source = Paths.get(file.getPath());
            String mimeType = Files.probeContentType(source);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.length());
            objectMetadata.setContentType(mimeType);
            return objectMetadata;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getResourcesFolder() {
        try {
            return new ClassPathResource("thumbnail").getFile().getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
