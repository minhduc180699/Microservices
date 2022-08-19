package com.saltlux.deepsignal.adapter.config;

import com.saltlux.deepsignal.adapter.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import tech.jhipster.security.*;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
    }

    @Bean
    public AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }

    @Bean
    public AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler() {
        return new AjaxLogoutSuccessHandler();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**").antMatchers("/swagger-ui/**").antMatchers("/test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf().disable();
        //        .and()
        //            .addFilterBefore(corsFilter, CsrfFilter.class)
        //            .exceptionHandling()
        //                .authenticationEntryPoint(problemSupport)
        //                .accessDeniedHandler(problemSupport)
        //        .and()
        //            .formLogin()
        //            .loginProcessingUrl("/api/authentication")
        //            .successHandler(ajaxAuthenticationSuccessHandler())
        //            .failureHandler(ajaxAuthenticationFailureHandler())
        //            .permitAll()
        //        .and()
        //            .logout()
        //            .logoutUrl("/api/logout")
        //            .logoutSuccessHandler(ajaxLogoutSuccessHandler())
        //            .permitAll()
        //        .and()
        //            .headers()
        //            .contentSecurityPolicy("default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:")
        //        .and()
        //            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
        //        .and()
        //            .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; fullscreen 'self'; payment 'none'")
        //        .and()
        //            .frameOptions()
        //            .deny()
        //        .and()
        //            .authorizeRequests()
        //            .antMatchers("/api/authenticate").permitAll()
        //            .antMatchers("/swagger-ui").permitAll()
        //            .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
        //            .antMatchers("/api/**").permitAll()
        //            .antMatchers("/management/health").permitAll()
        //            .antMatchers("/management/health/**").permitAll()
        //            .antMatchers("/management/info").permitAll()
        //            .antMatchers("/management/prometheus").permitAll()
        //            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN);
        // @formatter:on
    }
}
