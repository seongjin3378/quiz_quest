package com.seong.portfolio.quiz_quest.likes.service;

import com.seong.portfolio.quiz_quest.likes.repo.LikesRepository;
import com.seong.portfolio.quiz_quest.likes.dto.LikesDTO;
import com.seong.portfolio.quiz_quest.likes.dto.totalLikesInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {
    private final LikesRepository likesRepository;

    @Override
    public totalLikesInfoDTO likeStatusProcess(LikesDTO vo) {
        long userNum   = vo.getUserNum();
        long boardId  = vo.getBoardId();
        int  likeCount = vo.getLikeCount();
        String boardType = vo.getBoardType();

        if (likeCount != 0) {
            likesRepository.save(vo);
        } else {
            likesRepository.deleteByUserNumAndBoardIdAndBoardType(vo);
        }


        totalLikesInfoDTO result = likesRepository.findTotalLikesInfoByBoardIdAndUserNumAndBoardType(userNum, boardId, boardType);


        if (result == null) {
            return totalLikesInfoDTO.builder()
                    .boardId(boardId)
                    .totalLikes(0)
                    .totalDisLikes(0)
                    .currentState(0)
                    .build();
        }

        return result;
    }
}
