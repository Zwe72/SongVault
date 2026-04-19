package harjoitustyo.songvault;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/songlist", "/api/**").permitAll()
                .requestMatchers("/deletesong/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
        .formLogin( formlogin -> formlogin
            .loginPage("/login")
            .defaultSuccessUrl("/songlist", true)
            .permitAll()
        )
        .logout( logout -> logout.permitAll());

        return http.build();
    } 

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

