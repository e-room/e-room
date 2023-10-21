package com.project.Project.service.fileProcess;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.Project.common.aws.s3.FileService;
import com.project.Project.common.aws.s3.metadata.ThumbnailMetadata;
import com.project.Project.config.properties.FileProperties;
import com.project.Project.domain.Thumbnail;
import com.project.Project.repository.uuid.UuidCustomRepositoryImpl;
import com.project.Project.repository.uuid.UuidRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ThumbnailImageProcess extends FileProcessServiceImpl<ThumbnailMetadata> {

    private FileProperties fileProperties;

    @Autowired
    public ThumbnailImageProcess(FileService amazonS3Service, UuidCustomRepositoryImpl uuidCustomRepository, UuidRepository uuidRepository, FileProperties fileProperties) throws IOException {
        super(amazonS3Service, uuidCustomRepository, uuidRepository);
        this.fileProperties = fileProperties;
    }

    public Thumbnail makeThumbnailAndUpload(MultipartFile file, ThumbnailMetadata thumbnailMetadata) {
        String fileName = createThumbnail(file, thumbnailMetadata);
        String url = this.uploadThumbnail(file, thumbnailMetadata, fileName);
        return Thumbnail.builder()
                .url(url)
                .fileName(fileName)
                .uuid(thumbnailMetadata.getUuidEntity())
                .build();
    }

    private String createThumbnail(MultipartFile file, ThumbnailMetadata thumbnailMetadata) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            String thumbnailSavePath = "./thumbnail";
            String fileName = this.createFileName(thumbnailMetadata.getUuid(), file.getOriginalFilename());
            File thumbnailImage = new File(thumbnailSavePath, fileName);
            thumbnailImage.createNewFile();
            FileOutputStream thumbnail = new FileOutputStream(thumbnailImage);
            if (needToResize(file)) {
                if (width > height) {
                    Thumbnails.of(file.getInputStream())
                            .height(fileProperties.getThumbnailSize())
                            .outputQuality(1.0)
                            .toOutputStream(thumbnail);

                } else {
                    Thumbnails.of(file.getInputStream())
                            .width(fileProperties.getThumbnailSize())
                            .outputQuality(1.0)
                            .toOutputStream(thumbnail);
                }
//                Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, fileProperties.getThumbnailSize(), fileProperties.getThumbnailSize());
            } else {
                Thumbnails.of(file.getInputStream())
                        .scale(1.0)
                        .toOutputStream(thumbnail);
//                Thumbnailator.createThumbnail(file.getInputStream(), thumbnail,width , height);
            }
            thumbnail.close();
            return fileName;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Boolean needToResize(MultipartFile file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (width > fileProperties.getThumbnailSize() || height > fileProperties.getThumbnailSize()) {
            return true;
        }
        return false;
    }

    private String uploadThumbnail(MultipartFile file, ThumbnailMetadata thumbnailMetadata, String fileName) {
        try {
            String filePath = this.getFilePath(file, thumbnailMetadata);
            File thumbNail = new File("./thumbnail", fileName);
            ObjectMetadata objectMetadata = this.generateObjectMetadata(thumbNail);
            String url = this.uploadImage(new FileInputStream(thumbNail), objectMetadata, filePath);
            thumbNail.delete();
            return url;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private ClassPathResource getResourcesFolder() {
        return new ClassPathResource("thumbnail");
    }

    @Override
    public String getFilePath(MultipartFile file, ThumbnailMetadata imagePackage) {
        return super.getFilePath(file, imagePackage);
    }

    @Override
    protected String createFileName(String uuid, String originalFileName) {
        StringBuilder stringBuilder = new StringBuilder();
        String validOriginalFileName = super.createValidOriginFileName(originalFileName);
        return stringBuilder.append("s_").append(uuid).append("_").append(validOriginalFileName).toString();
    }

    @Override
    protected String getFileExtension(String fileName) {
        return super.getFileExtension(fileName);
    }
}
