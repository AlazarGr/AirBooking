package airline.booking.service;

import airline.booking.dto.request.LoginRequest;
import airline.booking.dto.request.RegisterRequest;
import airline.booking.dto.response.JwtResponse;
import airline.booking.dto.response.UserResponse;
import airline.booking.enums.Role;
import airline.booking.exception.BusinessException;
import airline.booking.model.AppUser;
import airline.booking.repository.AppUserRepository;
import airline.booking.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("USERNAME_TAKEN", "Пользователь с таким именем уже существует");
        }
        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.CLIENT);
        AppUser saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getUsername(), saved.getRole().name());
    }


    public JwtResponse login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "Пользователь с таким именем не найден"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException("INVALID_PASSWORD", "Неверный пароль");
        }

        String token = jwtTokenProvider.generateTokenFromUsername(user.getUsername());
        return new JwtResponse(token);
    }
}