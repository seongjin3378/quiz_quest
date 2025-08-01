package com.seong.portfolio.quiz_quest.visual.service;


import com.seong.portfolio.quiz_quest.visual.dto.SaveProcessDTO;
import com.seong.portfolio.quiz_quest.visual.repo.VisualRepository;
import com.seong.portfolio.quiz_quest.visual.dto.VisualDTO;
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
public class VisualServiceImpl implements VisualService {
    @Value("${problem.image.upload.path}")
    private  String imageFilePath;
    private final VisualRepository visualRepository;
    private final VisualValidate  visualValidate;
    @Override
    public Resource loadAsResource(long problemVisualId, String boardType) throws MalformedURLException, NoSuchFileException {
        String lastPartOfImageUrl = visualRepository.findVisualSrcByProblemVisualId(problemVisualId);
        String pathName = imageFilePath+boardType+"\\"+lastPartOfImageUrl;
        Path path = Paths.get(pathName);

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
    public void saveProcess(SaveProcessDTO saveProcessDTO) throws IOException {
        MultipartFile[] files = saveProcessDTO.getFiles();
        VisualDTO visualDTO = saveProcessDTO.getVisualDTO();
        String fileName = saveProcessDTO.getFileName();


        visualValidate.validateVisual(files, saveProcessDTO.getVisualDTO());
        if(files != null && files.length > 0) {
            List<String> results = MultipartFileUtil.saveFiles(files, imageFilePath+visualDTO.getBoardType()+"\\", fileName);
            visualDTO.setVisualSrc(results);
        }

        if(visualDTO.getVisualSrc() != null || visualDTO.getVisualTables() != null) {
            visualRepository.save(visualDTO);
        }

    }

    @Override
    public List<VisualDTO> findAllVisual(long boardId, String boardType) {

        return  visualRepository.findAllVisualByBoardIdAndBoardType(boardId, boardType);
    }



}
