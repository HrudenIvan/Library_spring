package com.final_ptoject.library_spring.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Local configuration class.
 */

@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * Bean for {@link BCryptPasswordEncoder}
     * @return instance of {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for Locale resolver. In particular case used {@link CookieLocaleResolver}. By default, Locale.US was used.
     * @return instance of {@link CookieLocaleResolver}.
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    /**
     * Bean for {@link LocaleChangeInterceptor}. Parameter that contains a locale specification in a locale change request set to "lang".
     * @return configured instance of {@link LocaleChangeInterceptor}.
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /**
     * Bean for {@link ReloadableResourceBundleMessageSource}. Basename sets to "classpath:messages", default encoding to "UTF-8".
     * @return configured instance of {@link ReloadableResourceBundleMessageSource}.
     */
    @Bean
    public MessageSource getMessageResource() {
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
        messageResource.setBasename("classpath:messages");
        messageResource.setDefaultEncoding("UTF-8");
        return messageResource;
    }

    /**
     * Registers configured localeChangeInterceptor in {@link InterceptorRegistry}
     * @param registry {@link InterceptorRegistry}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}
