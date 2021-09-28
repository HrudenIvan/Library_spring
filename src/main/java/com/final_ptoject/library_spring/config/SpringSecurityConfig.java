package com.final_ptoject.library_spring.config;

import com.final_ptoject.library_spring.handlers.AppAuthenticationSuccessHandler;
import com.final_ptoject.library_spring.services.impl.AppUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    AppUserDetailsService appUserDetailsService;
    PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/","/registration", "/error", "/error_404").permitAll()
                    .antMatchers("/administrator/**").hasRole("ADMIN")
                    .antMatchers("/librarian/**").hasRole("LIBRARIAN")
                    .antMatchers("/user/**").hasRole("USER")
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .usernameParameter("login")
                    .passwordParameter("password")
                    .failureUrl("/login?error")
                    .successHandler(appAuthenticationSuccessHandler())
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .permitAll()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessUrl("/");
    }

    @Bean
    public AuthenticationSuccessHandler appAuthenticationSuccessHandler() {
        return new AppAuthenticationSuccessHandler();
    }
}