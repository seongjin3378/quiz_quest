package com.seong.portfolio.quiz_quest.problems.service.problem;

import com.seong.portfolio.quiz_quest.comments.dto.CommentsRepoDTO;
import com.seong.portfolio.quiz_quest.comments.repo.CommentsRepository;
import com.seong.portfolio.quiz_quest.comments.dto.CommentsDTO;
import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.info.testCases.utils.TestCasesFormatterUtil;
import com.seong.portfolio.quiz_quest.problems.info.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final CommentsRepository commentsRepository;
    @Override
    public ProblemVO findByProblemIdAndReplace(long id) {
        ProblemVO problemVO = problemRepository.findByProblemId(ProblemVO.builder().problemId(id).isVisible(1).build());
        List<TestCasesVO> formatTestCases = TestCasesFormatterUtil.getTestCasesWithReplace(problemVO.getTestCases(), "\n", "↵");
        problemVO.setTestCases(formatTestCases);
        problemVO.setProblemContent(problemVO.getProblemContent().replaceAll("\n", "↵"));

        return problemVO;
    }

    @Override
    public List<CommentsDTO> findAllProblemComments(long id) {
        return commentsRepository.findAllByBoardIdAndBoardTypeSortTypeAndCursor(getCommentsRepoDTO(id));
    }

    private CommentsRepoDTO getCommentsRepoDTO(long id){

        return CommentsRepoDTO.builder()
                .boardId(id)
                .sortType("DESC")
                .cursor("0")
                .boardType("problem")
                .build();
    }


    @Override
    public String findCursor(List<CommentsDTO> commentsDTO) {
        int lastIndex = !commentsDTO.isEmpty() ? commentsDTO.size() - 1 : 0;
        return lastIndex != 0 ? commentsDTO.get(lastIndex).getCursor() : "0";
    }

    @Override
    public ProblemVO findByProblemId(long id) {
        return problemRepository.findByProblemId(ProblemVO.builder().problemId(id).isVisible(1).build());
    }
}
