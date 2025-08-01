package com.seong.portfolio.quiz_quest.likes.service;

import com.seong.portfolio.quiz_quest.likes.dto.LikesDTO;
import com.seong.portfolio.quiz_quest.likes.dto.totalLikesInfoDTO;

public interface LikesService {
    totalLikesInfoDTO likeStatusProcess(LikesDTO vo);
}
