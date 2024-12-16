package com.seong.portfolio.quiz_quest.user.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {


    private final SessionRegistry sessionRegistry;
    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);
    @Override
    public String getSessionId() {

        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)/*Authentication 객체가 존재하면 getName 반환*/
                .orElse("Anonymous");/*아닐경우 어아니머스로 반환*/
    }


}
