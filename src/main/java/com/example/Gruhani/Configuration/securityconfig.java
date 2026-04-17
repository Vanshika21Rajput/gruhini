package com.example.Gruhani.Configuration;

import com.example.Gruhani.jwtfilter;
import com.example.Gruhani.models.userdetailsServices;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity//to enable @preauthorize on methods and proxy around method is created to check role before access of the method
public class securityconfig {
    @Autowired
private userdetailsServices userDetailsService;
@Autowired
jwtfilter jf;


    @Autowired
    public void bindAuthManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public SecurityFilterChain secure(HttpSecurity hs) throws Exception {
        return hs.csrf(o->o.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(h->h.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(o->o.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll().requestMatchers("/logins","/register","/register-seller","/explore","/forgot-password","/verify-otp-forgetPassword","/error").permitAll()
                 ///logins","/register","/home","/api/**","/register-seller","/seller-login","/view-pending","/get-all-products","/add-product","/got-message","/upload"
                    .anyRequest().authenticated())
                .addFilterBefore(jf, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "https://gruhini-app1.onrender.com",
                "https://gruhani-app.onrender.com"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
//"https://gruhini-app1.onrender.com","https://gruhani-app.onrender.com"
// Optional: Allow exposing headers if needed (e.g., Authorization)
// configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

@Bean
public BCryptPasswordEncoder bCryptPasswordEncoder()
{
    return new BCryptPasswordEncoder();
}
}
