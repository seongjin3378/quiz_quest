package com.seong.portfolio.quiz_quest.user.service.oauth2;

import java.util.Map;

public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();

    void setAttributes(Map<String, Object> attributes);

}


