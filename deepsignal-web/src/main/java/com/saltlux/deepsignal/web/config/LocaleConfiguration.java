package com.saltlux.deepsignal.web.config;

import com.saltlux.deepsignal.web.service.UserService;
import com.saltlux.deepsignal.web.util.HttpRequestResponseUtils;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import tech.jhipster.config.locale.AngularCookieLocaleResolver;

@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public LocaleResolver localeResolver(HttpServletRequest request) {
        //        AngularCookieLocaleResolver cookieLocaleResolver = new AngularCookieLocaleResolver();
        //        cookieLocaleResolver.setCookieName("NG_TRANSLATE_LANG_KEY");
        //        return cookieLocaleResolver;
        //        System.out.println(request.getRemoteUser());
        //        String username = HttpRequestResponseUtils.getCurrentUser().getUsername();
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        //        UserService userService = applicationContext.getBean(UserService.class);
        //        String locale = userService.getLocaleOfCurrentUser();
        //        if (locale != null) {
        //            sessionLocaleResolver.setDefaultLocale(new Locale(locale));
        //        } else {
        sessionLocaleResolver.setDefaultLocale(new Locale("vi"));
        //        }
        return sessionLocaleResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        registry.addInterceptor(localeChangeInterceptor);
    }
}
