package com.seong.portfolio.quiz_quest.admin.problems.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seong.portfolio.quiz_quest.problems.info.testCases.service.TestCasesValidate;
import com.seong.portfolio.quiz_quest.problems.info.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.service.probDockerExecution.ProbDockerExecutionService;
import com.seong.portfolio.quiz_quest.problems.service.probValidate.ProbValidate;
import com.seong.portfolio.quiz_quest.problems.service.probWrite.ProbWriteExecutionService;
import com.seong.portfolio.quiz_quest.problems.service.probWrite.probService.ProbWriteService;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import com.seong.portfolio.quiz_quest.visual.dto.VisualDTO;
import com.seong.portfolio.quiz_quest.visual.service.VisualService;
import junit.framework.TestCase;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@Import(MultipartAutoConfiguration.class)
public class AdminRestProblemControllerTest extends TestCase {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    ProbWriteExecutionService probWriteExecutionService;


    @Mock
    private ProbValidate probValidate;

    @Mock
    private TestCasesValidate testCasesValidate;
    @Mock
    private ProbDockerExecutionService probDockerExecutionService;

    @Mock
    private ProbWriteService probWriteService;

    @Mock
    private VisualService visualService;


    @Test
    @WithMockUser(username = "admin",  roles = "ADMIN")
    public void testWrite_problem_테스트_1() throws Exception {
        // 1. 단일 코드 파일
        ClassPathResource script = new ClassPathResource("codes/test2.py");
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "file.py", "text/x-python", script.getInputStream());

        // 2. 이미지 파일 (files[])
        ClassPathResource img = new ClassPathResource("static/img/google-logo.svg");

        MockMultipartFile singleFile = new MockMultipartFile(
                "files", "google-logo.svg", "image/svg+xml", img.getInputStream());
        MockMultipartFile[] mockFiles = new MockMultipartFile[] {
                singleFile
        };

        // 3. probExecutionVO
        ProbExecutionVO vo = new ProbExecutionVO();
        vo.setMemoryLimit(512L);
        vo.setTimeLimit(1000);
        vo.setProblemTitle("두 수의 합");
        vo.setProblemType("알고리즘");
        vo.setProblemContent("두 정수를 입력받아 합을 출력하세요.");
        vo.setXp(50);
        vo.setLanguage("python");
        TestCasesVO tc = new TestCasesVO();
        tc.setInputValue("2 3");
        tc.setOutputValue("5");
        vo.setTestCases(List.of(tc));

        MockMultipartFile probExecutionPart = new MockMultipartFile(
                "probExecutionVO", "", "application/json", objectMapper.writeValueAsBytes(vo));


        // 4. probVisualVO
        VisualDTO visual = new VisualDTO();
        visual.setVisualCaptions(List.of());
        visual.setVisualTables("[]");

        MockMultipartFile visualPart = new MockMultipartFile(
                "probVisualVO", "", "application/json", objectMapper.writeValueAsBytes(visual));

        // 5. 요청 수행

        //@RequestPart("file") MultipartFile file,  @RequestPart(value="files",  required = false) MultipartFile[] files, @RequestPart("probExecutionVO")ProbExecutionVO probExecutionVO, @RequestPart("probVisualVO") VisualDTO visualDTO



        ResponseEntity<String> result = probWriteExecutionService.execute(
                vo, mockFile, mockFiles, visual, "python"
        );

        assertEquals("Yes", result.getBody());

        verify(probWriteService).saveProblemAndTestCases(any());


    }
}