package com.gmail.ezlotnikova.web.controller.config;

import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/reviews/**", "/users/**")
                .hasRole(String.valueOf(UserRoleEnum.ADMINISTRATOR))
                .antMatchers("/articles/**")
                .hasRole(String.valueOf(UserRoleEnum.CUSTOMER_USER))
                .antMatchers("/profile/**")
                .hasAnyRole(
                        String.valueOf(UserRoleEnum.ADMINISTRATOR),
                        String.valueOf(UserRoleEnum.CUSTOMER_USER),
                        String.valueOf(UserRoleEnum.SECURE_API_USER))
                .antMatchers("/api/**")
                .hasRole(String.valueOf(UserRoleEnum.SECURE_API_USER))
                .antMatchers("login")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .csrf()
                .disable();
    }

}