package com.seong.portfolio.quiz_quest.notification.repo;


import com.seong.portfolio.quiz_quest.notification.vo.NotificationVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationRepository {
    List<NotificationVO> findAllBySenderId(long senderId);

    @Insert("INSERT INTO notification(receiver_Id, notification_type, url, content, is_notice) VALUES(#{receiverId}, #{notificationType}, #{url}, #{content}, #{isNotice})")
    void save(NotificationVO notificationVO);

}
