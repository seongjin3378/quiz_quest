package com.seong.portfolio.quiz_quest.admin.problems.service;

import com.seong.portfolio.quiz_quest.admin.vo.AdminProblemVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminProblemService {
    void saveProblemImage(MultipartFile[] files, String fileName) throws IOException;
}
