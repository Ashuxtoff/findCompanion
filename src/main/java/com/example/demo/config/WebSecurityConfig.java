package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration // конфигурация - источник определения бинов
@EnableWebSecurity // в сочетании с @Configuration показывает, что класс отвечает за настройки безопасности
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // выключить защиту от csrf атак, иначе не получалось запрос к странице с фронта сделать
            .authorizeRequests() //Это строкой мы говорим предоставить разрешения для следующих url.
                .antMatchers("/resources/**", "/registration").permitAll()
                .anyRequest().authenticated() // все http запросы к нашему приложению должны быть аутентифицированы
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .permitAll();
    }


    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception // используем в SecurityServiceImpl для аутентификации
    {
        return authenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return bCryptPasswordEncoder;
    }
}


