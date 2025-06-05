package com.seong.portfolio.quiz_quest.courses.info.courseVisual.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface CourseVisualService {
    void saveFiles(MultipartFile[] files, long courseId, String fileName) throws IOException;
    Resource loadAsResource(String uuid) throws IOException;
}
