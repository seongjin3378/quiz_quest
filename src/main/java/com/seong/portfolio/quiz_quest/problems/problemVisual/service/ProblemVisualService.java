package com.seong.portfolio.quiz_quest.problems.problemVisual.service;


import com.seong.portfolio.quiz_quest.problems.problemVisual.vo.ProbVisualVO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.NoSuchFileException;
import java.util.List;

public interface ProblemVisualService {
    Resource  loadAsResource(long problemVisualId) throws MalformedURLException, NoSuchFileException;
    void saveProblemVisualAids(MultipartFile[] files, ProbVisualVO probVisualVO, String fileName) throws IOException;
    List<ProbVisualVO> findAllProblemVisual(long problemId);
}
