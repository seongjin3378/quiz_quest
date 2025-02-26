package com.seong.portfolio.quiz_quest.admin.problems.service;

import com.seong.portfolio.quiz_quest.admin.vo.AdminProblemVO;
import com.seong.portfolio.quiz_quest.utils.file.MultipartFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@Service
public class AdminProblemServiceImpl implements AdminProblemService {
    @Value("${problem.image.upload.path}")
    private String ImageFilesPath;
    @Override
    public void saveProblemImage(MultipartFile[] files,  String fileName) throws IOException {
        List<String> results = MultipartFileUtil.saveFiles(files, ImageFilesPath, fileName);
        log.info(results.toString());
        //db 작업
    }
}
