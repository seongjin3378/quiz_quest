package com.seong.portfolio.quiz_quest.settings.security.basic;


import com.seong.portfolio.quiz_quest.settings.security.basic.customFilter.OauthAutoLoginFilter;
import com.seong.portfolio.quiz_quest.settings.security.basic.customFilter.RateLimitingFilter;
import com.seong.portfolio.quiz_quest.settings.security.basic.entryPoint.CustomAuthenticationEntrySpot;
import com.seong.portfolio.quiz_quest.settings.security.basic.handler.CustomAccessDeniedHandler;
import com.seong.portfolio.quiz_quest.settings.security.basic.handler.CustomLogOutSuccessHandler;
import com.seong.portfolio.quiz_quest.settings.security.basic.handler.OAuth2LoginSuccessHandler;
import com.seong.portfolio.quiz_quest.settings.security.basic.resolver.CustomAuthorizationRequestResolver;
import com.seong.portfolio.quiz_quest.user.service.principalDetails.service.CustomOauthUserService;
import com.seong.portfolio.quiz_quest.user.service.principalDetails.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final RateLimitingFilter rateLimitingFilter;
    private final CustomOauthUserService customOauthUserService;
    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;
    private final OauthAutoLoginFilter oauthAutoLoginFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;


    @Bean
    SecurityFilterChain adminProcessFilterChain(HttpSecurity http, SessionRegistry sessionRegistry) throws Exception {
        http
                .securityMatchers((auth) -> auth.requestMatchers("/admin/**"))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/admin/p/editor"))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().denyAll());


        return http.build();

    }

    
    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http, SqlSessionFactory sqlSessionFactory, SessionRegistry sessionRegistry, ClientRegistrationRepository clientRegistrationRepository) throws Exception {

        http
                .addFilterAfter(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(oauthAutoLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/img/**","/css/**", "/js/**", "/", "/login", "/favicon.ico", "/p/{number}",  "/test/**", "/test", "/api/v1/notification/**"))
                .logout((auth) -> auth
                        .logoutUrl("/logoutProc")
                        .logoutSuccessHandler(new CustomLogOutSuccessHandler(sessionRegistry))
                        .permitAll()
                )
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/loginProc", "/join", "/joinProc", "/api/v1/users/**", "/api/v1/rankings/**", "/logoutProc", "/favicon.ico", "/api/v1/problems/**").permitAll()
                        .requestMatchers("/", "/p/{index}/s/{sortType}", "/p/n/{index}", "/c/{index}/s/{sortType}", "/c/n/{index}", "/c/write", "/api/v1/courses/**", "/c/pic/{UUID}", "/p/pic/{problemVisualId}/{boardType}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/login").anonymous()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/api/v1/notification/**", "/test", "/test/**").permitAll()

                        .anyRequest().denyAll()
                )                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntrySpot(authorizedClientManager))
                );


        http

                .oauth2Login((login) -> login

                .loginPage("/login")

                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                .userService(customOauthUserService)

                ).authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                        .authorizationRequestResolver(customAuthorizationRequestResolver)
                                )
                .successHandler(oAuth2LoginSuccessHandler)
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
                        .key("remember-me")
                        .rememberMeParameter("remember")
                        .tokenValiditySeconds(3600 * 24 * 365)
                        .alwaysRemember(false) //로그인할때마다 자동로그인 할것인지 유무
                        .userDetailsService(customUserDetailsService));


        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                );


      /*세션 고정 보호를 해줌*/
        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId()
                        .sessionCreationPolicy(IF_REQUIRED)
                );








        return http.build();
    }





}
