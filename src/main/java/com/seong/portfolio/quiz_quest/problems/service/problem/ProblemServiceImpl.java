package com.seong.portfolio.quiz_quest.problems.service.problem;

import com.seong.portfolio.quiz_quest.comments.problem.repo.ProblemCommentsRepository;
import com.seong.portfolio.quiz_quest.comments.problem.vo.ProblemCommentsVO;
import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.testCases.utils.TestCasesFormatterUtil;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final ProblemCommentsRepository problemCommentsRepository;
    @Override
    public ProblemVO findByProblemIdAndReplace(long id) {
        ProblemVO problemVO = problemRepository.findByProblemId(ProblemVO.builder().problemId(id).isVisible(1).build());
        List<TestCasesVO> formatTestCases = TestCasesFormatterUtil.getTestCasesWithReplace(problemVO.getTestCases(), "\n", "↵");
        problemVO.setTestCases(formatTestCases);
        problemVO.setProblemContent(problemVO.getProblemContent().replaceAll("\n", "↵"));

        return problemVO;
    }

    @Override
    public List<ProblemCommentsVO> findAllProblemComments(long id) {
        return problemCommentsRepository.findAllByProblemId(id, "DESC", "0");
    }


    @Override
    public String findCursor(List<ProblemCommentsVO> problemCommentsVO) {
        int lastIndex = !problemCommentsVO.isEmpty() ? problemCommentsVO.size() - 1 : 0;
        return lastIndex != 0 ? problemCommentsVO.get(lastIndex).getCursor() : "0";
    }

    @Override
    public ProblemVO findByProblemId(long id) {
        return problemRepository.findByProblemId(ProblemVO.builder().problemId(id).isVisible(1).build());
    }
}
