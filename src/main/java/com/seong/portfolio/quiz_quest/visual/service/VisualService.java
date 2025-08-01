package com.seong.portfolio.quiz_quest.visual.service;


import com.seong.portfolio.quiz_quest.visual.dto.SaveProcessDTO;
import com.seong.portfolio.quiz_quest.visual.dto.VisualDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.NoSuchFileException;
import java.util.List;

public interface VisualService {
    Resource  loadAsResource(long visualId, String boardType) throws MalformedURLException, NoSuchFileException;
    void saveProcess(SaveProcessDTO saveProcessDTO) throws IOException;
    List<VisualDTO> findAllVisual(long boardId, String boardType);
}
