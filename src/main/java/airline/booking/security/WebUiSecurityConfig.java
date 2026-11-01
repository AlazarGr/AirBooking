package airline.booking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Открывает только статику веб-интерфейса. Удалить файл + папку static/ui = убрать веб-часть.
@Configuration
public class WebUiSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain webUiFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/ui/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}