package com.seong.portfolio.quiz_quest.courses.info.courseVisual.service;

import com.seong.portfolio.quiz_quest.courses.info.courseVisual.repo.CourseVisualRepository;
import com.seong.portfolio.quiz_quest.courses.vo.CourseVisualVO;
import com.seong.portfolio.quiz_quest.utils.file.MultipartFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseVisualServiceImpl implements CourseVisualService {
    private final CourseVisualRepository courseVisualRepository;
    @Value("${courses.image.upload.path}")
    private String imageFilesPath;
    private final String imageUrl = "/c/pic/";

    @Override
    public void saveFiles(MultipartFile[] files, long courseId, String fileName) throws IOException {

        if (files != null) {
            List<String> imageSrcResults = MultipartFileUtil.saveFiles(files, imageFilesPath, fileName);

            List<String> urlResults = getVideoUrls(files);

            courseVisualRepository.save(getCourseVisualVO(courseId, imageSrcResults, urlResults));

        }

    }

    @Override
    public Resource loadAsResource(String uuid) throws IOException {
        String visualUrl = imageUrl + uuid;
        String filePath = courseVisualRepository.findByVisualUrl(visualUrl);
        try{
            Path file = Paths.get(imageFilesPath+filePath);
            Resource resource = new UrlResource(file.toUri());

            if(!resource.exists()) {
                throw new NoSuchFileException("Cannot find image file" + uuid);
            }

            if(!resource.isReadable()) {
                throw new NoSuchFileException("Cannot read image file" + uuid);
            }


            return new UrlResource(file.toUri());

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    private CourseVisualVO getCourseVisualVO(long courseId, List<String> imageSrc, List<String> videoUrls)
    {
        return CourseVisualVO.builder().visualUrl(videoUrls).visualSrc(imageSrc).courseId(courseId).build();
    }

    private List<String> getVideoUrls(MultipartFile[] files) {
        List<String> filesUUID = MultipartFileUtil.extractPrefixesBeforeFirstUnderscore(files);

        return  filesUUID.stream()
                .map(uuid -> imageUrl + uuid)
                .collect(Collectors.toList());
    }
}
