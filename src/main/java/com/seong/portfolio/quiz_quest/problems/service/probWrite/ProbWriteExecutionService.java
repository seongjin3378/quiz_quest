package com.seong.portfolio.quiz_quest.problems.service.probWrite;


import com.seong.portfolio.quiz_quest.problems.problemVisual.vo.ProbVisualVO;
import com.seong.portfolio.quiz_quest.problems.service.probWrite.probWrite.ProbWrite;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProbWriteExecutionService {
    private final ProbWrite probWrite;

    //기본 어드민 문제 작성

    @Transactional
    public ResponseEntity<String> execute(ProbExecutionVO probExecutionVO, MultipartFile file, MultipartFile[] files, ProbVisualVO probVisualVO, String language) {
        try {
            probWrite.initializeProbExecutionVO(probExecutionVO, file, language);
            probWrite.validateProbExecution(probExecutionVO);
            long problemId = probWrite.executeProblem(probExecutionVO);
            if(files != null) {
                probWrite.saveProblemVisualAids(files, probVisualVO, problemId, "problem" + problemId + "-");
            }
            return ResponseEntity.ok("Yes");
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "Runtime error: Check if there are any syntax errors.";
            if (e.getMessage().contains("Illegal") || e.getMessage().contains("Please") || e.getMessage().contains("You")) {
                error = e.getMessage();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //사용자 문제 작성 - 미구현
    public ResponseEntity<String> execute(ProbExecutionVO probExecutionVO, MultipartFile file, MultipartFile[] files, ProbVisualVO probVisualVO, String language, String fileName) {
        return null;
    }
}

