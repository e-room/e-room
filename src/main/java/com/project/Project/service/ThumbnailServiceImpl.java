package com.project.Project.service;

import com.project.Project.common.aws.s3.ThumbnailImagePackageMetadata;
import com.project.Project.domain.Thumbnail;
import com.project.Project.domain.Uuid;
import com.project.Project.repository.ThumbnailRepository;
import com.project.Project.service.fileProcess.ThumbnailImageProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThumbnailServiceImpl implements ThumbnailImageService {

    private final ThumbnailImageProcess thumbnailImageProcess;
    private final ThumbnailRepository thumbnailRepository;

    @Override
    public List<Thumbnail> saveThumbnailList(List<MultipartFile> imageFileList) {

        List<Thumbnail> thumbnailList = imageFileList.parallelStream().map(multipartFile -> {
            Uuid uuid = thumbnailImageProcess.createUUID();
            ThumbnailImagePackageMetadata thumbnailImagePackageMetadata = ThumbnailImagePackageMetadata.builder()
                    .createdAt(LocalDateTime.now())
                    .fileName(multipartFile.getOriginalFilename())
                    .uuid(uuid.getUuid())
                    .uuidEntity(uuid)
                    .build();
            return thumbnailImageProcess.makeThumbnailAndUpload(multipartFile, thumbnailImagePackageMetadata);
        }).collect(Collectors.toList());

        return thumbnailRepository.saveAll(thumbnailList);
    }
}
