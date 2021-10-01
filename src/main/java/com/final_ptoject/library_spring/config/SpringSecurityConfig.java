package com.final_ptoject.library_spring.config;

import com.final_ptoject.library_spring.entities.AppUserDetails;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Configuration class for Spring security
 */
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    AppUserDetailsService appUserDetailsService;
    PasswordEncoder passwordEncoder;

    /**
     * Configuration for user authentication. Overridden to work with {@link AppUserDetails} - custom implementation of {@link UserDetails}.
     * Password encoder sets to {@link BCryptPasswordEncoder} via bean.
     * @param auth {@link AuthenticationManagerBuilder}
     * @throws Exception if something goes wrong
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * Configuration of {@link HttpSecurity}
     * @param http {@link HttpSecurity}
     * @throws Exception if something goes wrong
     */
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

    /**
     * Bean for custom realisation {@link AppAuthenticationSuccessHandler} of {@link AuthenticationSuccessHandler}.
     * @return instance of {@link AppAuthenticationSuccessHandler}
     */
    @Bean
    public AuthenticationSuccessHandler appAuthenticationSuccessHandler() {
        return new AppAuthenticationSuccessHandler();
    }
}