package com.customer.shubham.config;

import com.customer.shubham.Filter.JwtFilter;
import com.customer.shubham.controller.CustomSuccessHandler;
import com.customer.shubham.serviceimpl.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {


    @Autowired
    private CustomUserDetailService userDetailService;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                     .antMatchers("/getAuthToken").permitAll()
//                      .anyRequest().authenticated()
//                .and()
//                .formLogin() // Specify login page URL (if using form login)
//                .permitAll()
//                .successHandler((request, response, authentication) -> response.sendRedirect("/welcome"))  // Redirect to /default-page
//                .and()
//                .logout()  // Enable logout (optional)
//                .logoutUrl("/logout")  // Specify logout URL (if using logout)
//                 .and().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//               http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http = configurePermittedUrls(http);
        http.cors().and().csrf().disable() // Disable CSRF for now, but consider enabling later
                .authorizeRequests()
                .antMatchers("/getAuthToken").permitAll()  // Allow access to login page
                .anyRequest().authenticated()       // Require authentication for other requests
                .and()
                .formLogin()
                .permitAll()
                .successHandler(new CustomSuccessHandler()) // Use custom handler for token generation
                .and().sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
               http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
       return NoOpPasswordEncoder.getInstance();
    }




   @Bean(name="AuthenticationManager")
    public AuthenticationManager authenticationManager() throws Exception {
        return  super.authenticationManager();
    }

    private HttpSecurity configurePermittedUrls(final HttpSecurity http) throws Exception {
		      return http.authorizeRequests().antMatchers("/dashBoard").permitAll().and();

    }

}
