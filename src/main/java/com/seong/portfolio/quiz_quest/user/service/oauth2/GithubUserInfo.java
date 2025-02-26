package com.seong.portfolio.quiz_quest.user.service.oauth2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("github")
@Getter
public class GithubUserInfo implements OAuth2UserInfo {
    private  Map<String, Object> attributes;




    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "github";
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("html_url"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("login"));
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}

