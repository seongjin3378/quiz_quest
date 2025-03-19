package com.seong.portfolio.quiz_quest.user.service.user;

import java.util.List;
import java.util.Set;

public interface ActiveUserService {
    int getAllCount();
    void setIdWithExpiry();
    void delete(Set<String> keys );
    Set<String> findAll();
    Set<String> findAllInactiveUsers();
}
