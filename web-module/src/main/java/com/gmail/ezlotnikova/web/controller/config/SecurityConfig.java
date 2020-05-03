package com.gmail.ezlotnikova.web.controller.config;

import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Order(2)
    @Configuration
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final UserDetailsService userDetailsService;
        private final PasswordEncoder bCryptPasswordEncoder;

        public WebSecurityConfig(
                UserDetailsService userDetailsService,
                PasswordEncoder bCryptPasswordEncoder
        ) {
            this.userDetailsService = userDetailsService;
            this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        }

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
                    .hasRole(UserRoleEnum.ADMINISTRATOR.name())
                    .antMatchers("/articles/add")
                    .hasRole(
                            UserRoleEnum.SALE_USER.name())
                    .antMatchers("/articles/**")
                    .hasAnyRole(
                            UserRoleEnum.CUSTOMER_USER.name(),
                            UserRoleEnum.SALE_USER.name())
                    .antMatchers("/items/**")
                    .hasAnyRole(
                            UserRoleEnum.SALE_USER.name(),
                            UserRoleEnum.CUSTOMER_USER.name())
                    .antMatchers("/profile/**")
                    .hasAnyRole(
                            UserRoleEnum.ADMINISTRATOR.name(),
                            UserRoleEnum.CUSTOMER_USER.name(),
                            UserRoleEnum.SALE_USER.name(),
                            UserRoleEnum.SECURE_API_USER.name())
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
                    .and().exceptionHandling()
                    .accessDeniedPage("/forbidden")
                    .and()
                    .csrf()
                    .disable();
        }

    }

    @Order(1)
    @Configuration
    public static class APISecurityConfig extends WebSecurityConfigurerAdapter {

        private final UserDetailsService userDetailsService;
        private final PasswordEncoder bCryptPasswordEncoder;

        public APISecurityConfig(
                UserDetailsService userDetailsService,
                PasswordEncoder bCryptPasswordEncoder
        ) {
            this.userDetailsService = userDetailsService;
            this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(bCryptPasswordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**")
                    .httpBasic()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/**")
                    .hasRole(UserRoleEnum.SECURE_API_USER.name())
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
                    //                    .and()
                    //                    .exceptionHandling()
                    //                    .accessDeniedPage("/api/forbidden")
                    .and()
                    .csrf()
                    .disable();
        }

    }

}