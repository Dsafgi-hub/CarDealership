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
        String roleUser = "USER";
        String roleManager = "MANAGER";
        String roleAdministrator = "ADMINISTRATOR";

        http
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/users/auth", "/users/register", "/vehicles/created").permitAll()
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAnyRole(roleAdministrator)
                .antMatchers(HttpMethod.GET, "/orders").hasAnyRole(roleAdministrator)
                .antMatchers(HttpMethod.GET, "/orders/**").hasAnyRole(roleUser, roleManager, roleAdministrator)
                .antMatchers(HttpMethod.POST, "/orders").hasAnyRole(roleUser)
                .antMatchers(HttpMethod.DELETE, "/orders/**").hasAnyRole(roleUser, roleAdministrator)
                .antMatchers(HttpMethod.PUT, "/orders/**").hasAnyRole(roleManager, roleAdministrator)
                .antMatchers(HttpMethod.GET, "/users/*/orders").hasAnyRole(roleUser)
                .antMatchers( "/orders/accept").hasAnyRole(roleManager)
                .antMatchers(HttpMethod.GET, "/vehicles/processed").hasAnyRole(roleManager)
                .antMatchers("/vehicles_models**").hasAnyRole(roleManager, roleAdministrator)
                .antMatchers("/types_of_equipment*").hasAnyRole(roleManager, roleAdministrator)
                .antMatchers("/vehicle_models*").hasAnyRole(roleManager, roleAdministrator)
                .antMatchers( "/invoices*").hasAnyRole(roleManager, roleAdministrator)
                .antMatchers("/user_roles*").hasAnyRole(roleAdministrator)
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtProvider));
    }
}
