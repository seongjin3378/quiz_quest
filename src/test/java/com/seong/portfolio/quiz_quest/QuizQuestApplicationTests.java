package com.seong.portfolio.quiz_quest;


import com.seong.portfolio.quiz_quest.ranking.enums.RankingType;
import com.seong.portfolio.quiz_quest.ranking.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.ranking.vo.RankingVO;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest

class QuizQuestApplicationTests {
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private RankingRepository rankingRepository;
	/*
	@Transactional
	@Rollback(false) // 트랜잭션 롤백 방지
	@Test
	public void testBatchTransactionalInsert() {
		String insertDataSql = "INSERT INTO your_table (ranking_type, ranking_rank, user_id, ranking_score) VALUES (?, ?, ?, ?)";
		long startTime = System.currentTimeMillis();

		List<Object[]> batchArgs = new ArrayList<>();
		for (int i = 0; i < 100000; i++) {
			batchArgs.add(new Object[]{
					"usage_time", // ranking_type
					i + 1,       // ranking_rank
					"user_" + i, // user_id
					100 + i      // ranking_score
			});
		}

		jdbcTemplate.batchUpdate(insertDataSql, batchArgs); // 배치 삽입

		long endTime = System.currentTimeMillis();
		System.out.println("Total time taken: " + (endTime - startTime) + " ms");
	}

	 /*
	//@Test
	public void DBTest()
	{
	 RankingVO ranking = RankingVO.builder().userId("test0").rankingType("usage_time").build();
	 rankingRepository.saveOrUpdateRanking(ranking);
	}

	 */

	@Test
	public void EnumTest()
	{
		for(RankingType rankingType : RankingType.values()) {
			System.out.println(rankingType.label());
		}
	}
}
