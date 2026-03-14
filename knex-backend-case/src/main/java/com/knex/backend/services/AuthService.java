package com.knex.backend.services;

import com.knex.backend.dtos.auth.AuthResponseDto;
import com.knex.backend.dtos.user.UserLoginDto;
import com.knex.backend.dtos.user.UserRegisterDto;
import com.knex.backend.dtos.user.UserResponseDto;
import com.knex.backend.entities.User;
import com.knex.backend.mappers.UserMapper;
import com.knex.backend.repositories.UserRepository;
import com.knex.backend.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    public AuthResponseDto register(UserRegisterDto registerDto) {
        if (userRepository.findByEmail(registerDto.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User(
                registerDto.email(),
                passwordEncoder.encode(registerDto.password())
        );

        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(savedUser.getId(), savedUser.getEmail());
        UserResponseDto userResponse = userMapper.toResponseDto(savedUser);

        return new AuthResponseDto(token, userResponse);
    }

    public AuthResponseDto login(UserLoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.email(),
                            loginDto.password()
                    )
            );

            User user = userRepository.findByEmail(loginDto.email())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
            UserResponseDto userResponse = userMapper.toResponseDto(user);

            return new AuthResponseDto(token, userResponse);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Email ou senha inválidos");
        }
    }
}
