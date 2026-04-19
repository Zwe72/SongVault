package harjoitustyo.songvault;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/login").permitAll()
                .requestMatchers("/songlist", "/api/**").hasAnyRole("USER","ADMIN")
                .requestMatchers("/deletesong/**").authenticated()
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
}

