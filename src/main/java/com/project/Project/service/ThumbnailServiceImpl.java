package com.project.Project.service;

import com.project.Project.aws.s3.ThumbnailImagePackageMetadata;
import com.project.Project.domain.Thumbnail;
import com.project.Project.domain.Uuid;
import com.project.Project.repository.ThumbnailRepository;
import com.project.Project.service.fileProcess.ThumbnailImageProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThumbnailServiceImpl implements ThumbnailImageService {

    private final ThumbnailImageProcess thumbnailImageProcess;
    private final ThumbnailRepository thumbnailRepository;

    @Override
    public List<Thumbnail> saveThumbnailList(List<MultipartFile> imageFileList) {

        /*
        todo: asynchronously
         */
        List<Thumbnail> thumbnailList = new ArrayList<>();
        for (MultipartFile multipartFile : imageFileList) {
            Uuid uuid = thumbnailImageProcess.createUUID();
            ThumbnailImagePackageMetadata thumbnailImagePackageMetadata = ThumbnailImagePackageMetadata.builder()
                    .createdAt(LocalDateTime.now())
                    .fileName(multipartFile.getOriginalFilename())
                    .uuid(uuid.getUuid())
                    .uuidEntity(uuid)
                    .build();
            Thumbnail thumbnail = thumbnailImageProcess.makeThumbnailAndUpload(multipartFile, thumbnailImagePackageMetadata);
            thumbnailList.add(thumbnail);
        }
        return thumbnailRepository.saveAll(thumbnailList);
    }
}
