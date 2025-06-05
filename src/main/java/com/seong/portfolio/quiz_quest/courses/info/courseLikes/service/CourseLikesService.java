package com.seong.portfolio.quiz_quest.courses.info.courseLikes.service;

import com.seong.portfolio.quiz_quest.courses.vo.CourseLikesVO;
import com.seong.portfolio.quiz_quest.courses.vo.CourseTotalLikesInfoVO;

public interface CourseLikesService {
    CourseTotalLikesInfoVO likeStatusProcess(CourseLikesVO vo);
}
