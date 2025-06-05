package com.seong.portfolio.quiz_quest.problems.info.problemVisual.service;


import com.seong.portfolio.quiz_quest.problems.info.problemVisual.repo.ProblemVisualRepository;
import com.seong.portfolio.quiz_quest.problems.info.problemVisual.vo.ProbVisualVO;
import com.seong.portfolio.quiz_quest.problems.service.probValidate.ProbValidate;
import com.seong.portfolio.quiz_quest.utils.file.MultipartFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProblemVisualServiceImpl implements ProblemVisualService {
    @Value("${problem.image.upload.path}")
    private  String imageFilePath;
    private final ProblemVisualRepository problemVisualRepository;
    private final ProbValidate  probValidate;
    @Override
    public Resource loadAsResource(long problemVisualId) throws MalformedURLException, NoSuchFileException {
        String lastPartOfImageUrl = problemVisualRepository.findVisualSrcByProblemVisualId(problemVisualId);
        Path path = Paths.get(imageFilePath+lastPartOfImageUrl);

        Resource resource = new UrlResource(path.toUri());
        if(!resource.exists()) {
            throw new NoSuchFileException("Cannot find image file" + lastPartOfImageUrl);
        }

        if(!resource.isReadable()) {
            throw new NoSuchFileException("Cannot read image file" + lastPartOfImageUrl);
        }


        return resource;
    }

    @Override
    public void saveProblemVisualAids(MultipartFile[] files, ProbVisualVO probVisualVO, String fileName) throws IOException {
        probValidate.validateVisual(files, probVisualVO);
        if(files != null && files.length > 0) {
            List<String> results = MultipartFileUtil.saveFiles(files, imageFilePath, fileName);
            probVisualVO.setVisualSrc(results);
        }

        if(probVisualVO.getVisualSrc() != null || probVisualVO.getVisualTables() != null) {
            problemVisualRepository.save(probVisualVO);
        }

    }

    @Override
    public List<ProbVisualVO> findAllProblemVisual(long problemId) {


        return  problemVisualRepository.findAllVisualByProblemId(problemId);
    }


}
