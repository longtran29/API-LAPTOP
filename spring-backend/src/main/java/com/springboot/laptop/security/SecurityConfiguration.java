package com.springboot.laptop.security;


import com.springboot.laptop.security.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {


    @Autowired
    private UserDetailServiceImpl userDetailsService;


    private static String[] resources = new String[] {

    };


    @Autowired
    void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("Vao authentication");
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();


        http.authorizeRequests()
                .antMatchers("/index", "/home", "/").authenticated()
                .antMatchers("/change-password*","/forgot-password*","/changePassword*").permitAll()
                .antMatchers("/admin/orders/**").hasRole("ADMIN")
                .antMatchers("/admin/users/**").hasRole("ADMIN")
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "EDITOR")
                .anyRequest().permitAll()
                .and()
                .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/index")
//                .failureUrl("/login?error=true")
//                .successHandler(myAuthenticationSuccessHandler)
//                .failureHandler(authenticationFailureHandler)
                .permitAll()
                .and()
                // logout setup and its logoutSuccessUrl
                .logout()
                .logoutUrl("/logout");
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomize() throws Exception {
        return (web) -> web.ignoring().antMatchers(HttpMethod.GET, resources);
    }
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
