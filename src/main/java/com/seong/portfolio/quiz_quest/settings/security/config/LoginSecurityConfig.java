package com.seong.portfolio.quiz_quest.settings.security.config;


import com.seong.portfolio.quiz_quest.settings.security.config.customFilter.CustomAutoLoginFilter;
import com.seong.portfolio.quiz_quest.settings.security.config.customFilter.RateLimitingFilter;
import com.seong.portfolio.quiz_quest.settings.security.config.entryPoint.CustomAuthenticationEntrySpot;
import com.seong.portfolio.quiz_quest.settings.security.config.handler.CustomAccessDeniedHandler;
import com.seong.portfolio.quiz_quest.settings.security.config.handler.CustomLogOutSuccessHandler;
import com.seong.portfolio.quiz_quest.user.service.CustomUserDetailsService;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.session.SessionRegistry;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class LoginSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final SessionService sessionService;
    private final CustomAutoLoginFilter customAutoLoginFilter;
    private final RateLimitingFilter rateLimitingFilter;



    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http, SqlSessionFactory sqlSessionFactory, SessionRegistry sessionRegistry) throws Exception {
        http
                .addFilterBefore(customAutoLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/css/**", "/js/**", "/"))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/loginProc", "/join", "/joinProc", "/loggedInUserCount", "/invalidateUser", "/user-usage-time", "/", "/saveUsageTimer").permitAll()
                        .requestMatchers("/").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/login").anonymous()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/css/**", "/js/**").authenticated()

                        .anyRequest().denyAll()
                )                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntrySpot())
                );


        http

                .formLogin((auth) -> auth
                        .loginPage("/login")
                        .usernameParameter("userId")
                        .passwordParameter("password")
                        .loginProcessingUrl("/loginProc")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/", true)
                )
                .exceptionHandling((exceptions) -> exceptions
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                );


        http
                .rememberMe((auth) -> auth
                        .key("key")
                        .rememberMeParameter("remember")
                        .tokenValiditySeconds(3600 * 24 * 365)
                        .alwaysRemember(true)
                        .userDetailsService(customUserDetailsService));



        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                );

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId()
                        .sessionCreationPolicy(IF_REQUIRED)
                );


        http
                .logout((auth) -> auth
                        .logoutUrl("/logoutProc")
                        .logoutSuccessHandler(new CustomLogOutSuccessHandler(sessionRegistry, sessionService))
                        .permitAll()
                );


        return http.build();
    }



}
