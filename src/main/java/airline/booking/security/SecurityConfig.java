package airline.booking.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Автоматически внедрит финальные поля через конструктор
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // 1. Внедряем ваш JWT-фильтр

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        // чтение и поиск рейсов открыты всем
                        .requestMatchers(HttpMethod.GET, "/flights", "/flights/all", "/flights/{id}").permitAll()
                        // остальное по рейсам (создание, изменение, удаление, отмена) только админу
                        .requestMatchers("/flights/**").hasRole("ADMIN")
                        // брони только клиенту
                        .requestMatchers("/bookings/**").hasAnyRole("CLIENT", "ADMIN")
                        .anyRequest().denyAll())
                // 3. Добавляем JWT-фильтр перед стандартным фильтром проверки логина/пароля
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) ->
                                writeError(res, 401, "UNAUTHORIZED", "Требуется авторизация"))
                        .accessDeniedHandler((req, res, ex) ->
                                writeError(res, 403, "FORBIDDEN", "Недостаточно прав")));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static void writeError(HttpServletResponse res, int status, String code, String message)
            throws IOException {
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"code\":\"" + code + "\",\"message\":\"" + message + "\"}");
    }
}