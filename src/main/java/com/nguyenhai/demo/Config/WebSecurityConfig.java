package com.nguyenhai.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig<S extends Session> extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    private AuthenticationFailureHandler authenticationFailureHandler;
    private FindByIndexNameSessionRepository<S> sessionRepository;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService,
                             AuthenticationSuccessHandler authenticationSuccessHandler,
                             AuthenticationFailureHandler authenticationFailureHandler,
                             FindByIndexNameSessionRepository<S> sessionRepository) {

        this.userDetailsService             = userDetailsService;
        this.authenticationSuccessHandler   = authenticationSuccessHandler;
        this.authenticationFailureHandler   = authenticationFailureHandler;
        this.sessionRepository              = sessionRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    public SpringSessionBackedSessionRegistry<S> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/audio/**", "/css/**", "/app/**", "/fonts/**", "/fullcalendar/**", "/images/**", "/js/**").permitAll() // for resource
                    .antMatchers("/page/audio/**", "/page/css/**", "/page/app/**", "/page/fonts/**", "/page/fullcalendar/**", "/page/images/**", "/page/js/**").permitAll() // for sub page
                    .antMatchers("/sign-up/**", "/login/**", "/data/**", "/file/**", "/logout/**", "/error", "/me/verify", "/page/change-password", "/email/change-password").permitAll()
                    .antMatchers(HttpMethod.POST,"/setting/password").permitAll()
                    .antMatchers("/swagger-ui.html").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                        .usernameParameter("email").passwordParameter("password")
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                    .permitAll()
                .and()
                    .logout()
                        .logoutSuccessUrl("/login")
                        .logoutUrl("/logout")
                        .clearAuthentication(true).invalidateHttpSession(true).deleteCookies("SESSION")
                    .permitAll()
                .and()
                    .rememberMe().rememberMeServices(rememberMeServices())
                .and()
                    .csrf().disable()
                .sessionManagement()
                    .maximumSessions(4)
                    .expiredUrl("/logout")
                    .sessionRegistry(sessionRegistry());
    }
}
