package com.seong.portfolio.quiz_quest.courses.info.courseLikes.service;

import com.seong.portfolio.quiz_quest.courses.info.courseLikes.repo.CourseLikesRepository;
import com.seong.portfolio.quiz_quest.courses.vo.CourseLikesVO;
import com.seong.portfolio.quiz_quest.courses.vo.CourseTotalLikesInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CourseLikesServiceImpl implements CourseLikesService {
    private final CourseLikesRepository courseLikesRepository;

    @Override
    public CourseTotalLikesInfoVO likeStatusProcess(CourseLikesVO vo) {
        long userNum   = vo.getUserNum();
        long courseId  = vo.getCourseId();
        int  likeCount = vo.getLikeCount();

        if (likeCount != 0) {
            courseLikesRepository.save(vo);
        } else {
            courseLikesRepository.deleteByUserNumAndCourseId(vo);
        }


        CourseTotalLikesInfoVO result = courseLikesRepository.findTotalLikesInfoByCourseIdAndUserNum(userNum, courseId);


        if (result == null) {
            return CourseTotalLikesInfoVO.builder()
                    .courseId(courseId)
                    .totalLikes(0)
                    .totalDislikes(0)
                    .currentState(0)
                    .build();
        }

        return result;
    }
}
