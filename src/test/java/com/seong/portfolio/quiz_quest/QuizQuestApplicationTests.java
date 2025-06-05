package com.seong.portfolio.quiz_quest;


import com.seong.portfolio.quiz_quest.notification.repo.NotificationRepository;
import com.seong.portfolio.quiz_quest.notification.vo.NotificationVO;
import com.seong.portfolio.quiz_quest.problems.info.problemHistory.service.ProblemHistoryService;
import com.seong.portfolio.quiz_quest.problems.service.probDockerExecution.ProbDockerExecutionService;
import com.seong.portfolio.quiz_quest.problems.info.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import com.seong.portfolio.quiz_quest.quartz.interfaces.JobService;
import com.seong.portfolio.quiz_quest.rankings.repo.RedisRankingRepository;
import com.seong.portfolio.quiz_quest.user.repo.UserRepository;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.*;
import java.util.*;


@SpringBootTest(classes = QuizQuestApplication.class)
class QuizQuestApplicationTests {

    @Autowired
    @Qualifier("ProbWriteDockerExecution")
    private ProbDockerExecutionService probDockerExecutionService;

    @Autowired
    private RedisRankingRepository redisRankingRepository;

    private Logger logger = LoggerFactory.getLogger(QuizQuestApplicationTests.class);

    @Autowired
    private  RedisTemplate<String, Object> redisTemplate;


    @Qualifier("InitializeUsageTimeJob")
            @Autowired
    private JobService initializeUsageTimeJob;
    //

    @Autowired
    private ProblemHistoryService problemHistoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;



    @Test
    public void messageDB_test()
    {

   /*     List<MessageVO> messageVOList = messageRepository.findAllBySenderId(1L);

        for(MessageVO messageVO : messageVOList)
        {
            logger.info(messageVO.getSender().getUserId());
        }*/

        NotificationVO message = new NotificationVO();
        message.setSenderId(1L);
        message.setReceiverId(2L);
        message.setMessageContent("안녕하세요. 테스트 메시지입니다.");
        message.setNotice(false);

        notificationRepository.save(message);

    }

    public void history_test()
    {
        /*problemHistoryService.saveProblem(25, "test0");*/
        int exists = problemHistoryService.isProblemSolved(25, "test0");

        if(exists==1)
        {
            logger.info("Problem Solved");

        }

    }
    public void quartzServiceTest()
    {
    initializeUsageTimeJob.execute();
    }
    public void RedisRankingTest() throws IOException {
     /*   RankingKeyEnumVO rankingEnumVO = RankingKeyEnumVO.fromString("usage_time");
    redisRankingRepository.addToSortedSet(RedisRankingVO.builder().key(rankingEnumVO.getRankingKey().getKey()).pk(39L).build(), score);*/
/*
        RedisRankingVO redisRankingVO = new RedisRankingVO();

        redisRankingVO.setKey(RankingKey.USAGE_TIME.getKey());
        redisRankingVO.setMinScore(60);
        redisRankingVO.setMaxScore(9999999);
        redisRankingVO.setOffsetStart(0);
        redisRankingVO.setCount(10);
        redisRankingVO.setSortSetKey(RankingSortSetKey.USAGE_TIME.getKey());
        Set<RankingVO> result = redisRankingRepository.getSortedSetByScoreDesc(redisRankingVO, DateUtils::isThisYear);
        for(RankingVO rankingVO : result) {
            logger.info("{}, {}, {}, {}", rankingVO.getUserId(), rankingVO.getRankingType(), rankingVO.getRankingScore(), rankingVO.getCreatedAt());
        }*/

        Double a = 0.66;
        System.out.println(a.intValue());
/*        RedisRankingVO redisRankingVO = new RedisRankingVO();
        redisRankingVO.setKey(RankingKeyEnumVO.fromString("usage_time").getRankingKey().getKey());
        redisRankingVO.setOffsetStart(0);
        redisRankingVO.setCount(10);
        Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores = redisRankingRepository.findAllByTimeUnitOrderByScoreDesc(redisRankingVO, "year");
        assert reverseRangeWithScores != null;
        for (ZSetOperations.TypedTuple<Object> tuple : reverseRangeWithScores) {
            System.out.println("Value: " + tuple.getValue() + ", Score: " + tuple.getScore());
        }*/
    }


    public void setUp() {
        // 사용자 정보를 설정
        UserDetails userDetails = User.withUsername("testUser")
                .password("password")
                .roles("USER") // 역할 설정
                .build();

        // Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // SecurityContextHolder에 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    public void ProbDockerExecutionTest() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.py",
                "text/x-python",
                """
                a, b = input().split()
                
                # 입력 받은 값을 정수로 변환합니다.
                a = int(a)
                b = int(b)
                
                # 두 숫자를 더합니다.
                result = a + b
                
                # 결과를 출력합니다.
                print(result)
                """.getBytes()
        );

        // 파일 업로드 요청
        List<TestCasesVO> testCasesVOList = new ArrayList<>();
        testCasesVOList.add(TestCasesVO.builder().inputValue("2 3").outputValue("5").isVisible(1).build());
        testCasesVOList.add(TestCasesVO.builder().inputValue("3 4").outputValue("7").isVisible(1).build());
        testCasesVOList.add(TestCasesVO.builder().inputValue("5 7").outputValue("12").isVisible(0).build());
        probDockerExecutionService.execute(ProbExecutionVO
                .builder()
                .problemId(-1)
                .language("python")
                .file(file)
                        .memoryLimit(128)
                        .timeLimit(1000)
                        .testCases(testCasesVOList)
                        .problemTitle("아무개여")
                        .problemType("자료구조")
                        .problemContent("몰라여 그냥 짓는거임")
                .build());
    }

        // Clean up: Dockerfile 삭제

}




