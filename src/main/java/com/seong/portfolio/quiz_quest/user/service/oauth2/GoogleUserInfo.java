package com.seong.portfolio.quiz_quest.user.service.oauth2;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("google")
public class GoogleUserInfo implements OAuth2UserInfo {
    private  Map<String, Object> attributes;


    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("sub"));
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("name"));
    }
    @Override
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
