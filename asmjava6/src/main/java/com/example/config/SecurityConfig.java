package com.example.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.entity.Account;
import com.example.jparepository.AccountRepository;

import jakarta.servlet.http.Cookie;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    AccountRepository accountDAO;

    @Bean
    BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Account account = accountDAO.findByUserName(username) != null ? accountDAO.findByUserName(username)
                        : null;
                if (account == null) {
                    throw new UsernameNotFoundException("User not found");
                }

                String userRole = account.isAdmin() ? "ADMIN" : "USER";
                System.out.println(userRole);
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole);
                return new User(account.getUserName(), getPasswordEncoder().encode(account.getPassword()),
                        Collections.singletonList(authority));
            }
        };
    }

    @Bean
    @Order(1)
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.cors().disable().csrf().disable();
        http.securityMatcher("/client/**", "/admin/**", "/client/detail/**", "/oauth2/authorization",
                "/oauth2/authorization/google",
                "/login/oauth2/code/google", "/oauth2/authorization/facebook", "/login/oauth2/code/facebook")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/client/cart/**")).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/client/favorite/**")).hasAnyRole("USER", "ADMIN")
                        .requestMatchers( new AntPathRequestMatcher("/client/**"))
                        .permitAll())
                .formLogin(
                        login -> {
                            try {
                                login.loginPage("/client/login")
                                        .loginProcessingUrl("/client/processinglogin")
                                        .defaultSuccessUrl("/client/login/success", true)
                                        .failureUrl("/client/signin/error")
                                        .and()
                                        .exceptionHandling(exception -> exception.accessDeniedPage("/client/denied"));
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        })
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/client/signin")
                        .defaultSuccessUrl("/client/social/success", true)
                        .failureUrl("/client/signin/error"))
                .logout(logout -> logout
                        .logoutUrl("/client/logout")
                        // .logoutSuccessUrl("/client/index")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/client/logout"))
                        .addLogoutHandler((request, response, authentication) -> {
                            // Xoá cookie bằng cách đặt thời gian hết hạn của nó trong quá khứ
                            Cookie cookie = new Cookie("username", "123");
                            System.out.println(cookie.getName() + cookie.getValue() + 1);
                            cookie.setMaxAge(0); // Đặt thời gian hết hạn là 0 để xoá cookie
                            response.addCookie(cookie);
                        })
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // Thực hiện bất kỳ xử lý nào sau khi đăng xuất thành công
                            try {
                                response.sendRedirect("/client/logout1");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }));

        return http.build();
    }
    //

    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = new ArrayList();
        registrations.add(this.googleClientRegistration());
        registrations.add(this.facebookClientRegistration());
        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("53037258373-bqo5dc5a0remtvbhq18t9ku3mk324q7j.apps.googleusercontent.com")
                .clientSecret("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")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email", "address", "phone")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }

    private ClientRegistration facebookClientRegistration() {
        return ClientRegistration.withRegistrationId("facebook")
                .clientId("776354867561051")
                .clientSecret("43b42a6f3c032746ce26915df52397d6")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("email", "public_profile")
                .authorizationUri("https://www.facebook.com/v12.0/dialog/oauth")
                .tokenUri("https://graph.facebook.com/v12.0/oauth/access_token")
                .userInfoUri("https://graph.facebook.com/v12.0/me?fields=id,name,email")
                .userNameAttributeName("id")
                .clientName("Facebook")
                .build();
    }
}