package ru.bachinin.cardealership.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import ru.bachinin.cardealership.jwt.JwtConfigurer;
import ru.bachinin.cardealership.jwt.JwtProvider;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;

    @Autowired
    public SecurityConfiguration(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/users/auth", "/users/register").permitAll()
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAnyRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.GET, "/orders").hasAnyRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.GET, "/orders/**").hasAnyRole("USER", "MANAGER", "ADMINISTRATOR")
                .antMatchers(HttpMethod.POST, "/orders").hasAnyRole("USER")
                .antMatchers(HttpMethod.DELETE, "/orders/**").hasAnyRole("USER", "ADMINISTRATOR")
                .antMatchers(HttpMethod.PUT, "/orders/**").hasAnyRole("MANAGER", "ADMINISTRATOR")
                .antMatchers(HttpMethod.GET, "/users/*/orders").hasAnyRole("USER")
                .antMatchers( "/orders/accept").hasAnyRole("MANAGER", "ADMINISTRATOR")
                .antMatchers(HttpMethod.GET, "/vehicles/**").hasAnyRole("USER", "MANAGER", "ADMINISTRATOR")
                .antMatchers("/vehicles_models/**").hasAnyRole("MANAGER", "ADMINISTRATOR")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtProvider));
    }
}
