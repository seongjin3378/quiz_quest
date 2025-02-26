package com.seong.portfolio.quiz_quest.user.service.principalDetails.service;

import com.seong.portfolio.quiz_quest.user.repo.UserRepository;
import com.seong.portfolio.quiz_quest.user.service.oauth2.GoogleUserInfo;
import com.seong.portfolio.quiz_quest.user.service.oauth2.OAuth2UserInfo;
import com.seong.portfolio.quiz_quest.user.service.principalDetails.vo.PrincipalDetails;
import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CustomOauthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    private final ApplicationContext applicationContext;
    @Autowired
    public CustomOauthUserService(UserRepository userRepository,
                                  BCryptPasswordEncoder bCryptPasswordEncoder,
                                   ApplicationContext applicationContext) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.applicationContext = applicationContext;
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("getClientRegistration {}", userRequest.getClientRegistration());
        log.info("getAccessToken {}", userRequest.getAccessToken());
        log.info("getAttributes: {}", delegate.loadUser(userRequest).getAttributes());

        OAuth2User oauth2User = delegate.loadUser(userRequest);
        OAuth2UserInfo oauth2UserInfo = (OAuth2UserInfo) applicationContext
                                        .getBean(userRequest.getClientRegistration()
                                        .getRegistrationId());
        oauth2UserInfo.setAttributes(delegate.loadUser(userRequest).getAttributes());

        UserVO userVO = findUserOrCreateUser(oauth2UserInfo);
        log.info("userVO: {}", userVO);
        return new PrincipalDetails(userVO, oauth2User.getAttributes());
    }

    private UserVO findUserOrCreateUser(OAuth2UserInfo oAuth2UserInfo)
    {
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String userName = provider + "_" + providerId;

        log.info("userName: {}", userName);
        String password = bCryptPasswordEncoder.encode(UUID.randomUUID().toString()); // 비밀번호는 외부에서 처리하기 때문에 필요없음
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";
        UserVO userVO = userRepository.findByUserId(userName);

        if(userVO == null) {
            userVO = UserVO.builder().userId(userName).password(password).email(email).role(role).build();
            userRepository.save(userVO);
        }

        return userVO;
    }
}
