package com.seong.portfolio.quiz_quest.docker.vo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

//timeMap 사용 메서드 AlgoDockerExec - readContainerOutput



@Component
@Slf4j
public class DockerValidationData {

    private final HashMap<String, Integer> timeMap = new HashMap<>();  //ms 단위 시간


    public void setTimeMap(String userId, int Time) {
        log.info("userId {}, time: {}", userId, Time);
        timeMap.put(userId, Time);
    }
    public int getTimeMap(String userId) {
        return timeMap.get(userId);
    }
    public void removeTimeMap(String userId) {
        timeMap.remove(userId);
    }
}
