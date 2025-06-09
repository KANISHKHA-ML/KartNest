package com.cts.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.cts.service.CustomUserDetailsService;


@Configuration
public class SecurityConfiguration {


	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	 @Autowired
	 private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler; 


    SecurityConfiguration(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    new AntPathRequestMatcher("/cart/update", "POST"),
                    new AntPathRequestMatcher("/cart/delete", "POST"),
                    new AntPathRequestMatcher("/registerUser", HttpMethod.POST.name())
                    
                )
            )
            .authorizeHttpRequests(configurer ->
                configurer
                    .requestMatchers("/showLogin", "/registerUser").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/home").authenticated()
                    .requestMatchers("/showProfile", "/updateProfile").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()
            )
            .formLogin(form ->
                form
                    .loginPage("/showLogin")
                    .loginProcessingUrl("/login")
                    .successHandler(customAuthenticationSuccessHandler)
                    .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .logoutSuccessUrl("/showLogin?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        http.userDetailsService(userDetailsService);

        return http.build();
    }

	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

}

