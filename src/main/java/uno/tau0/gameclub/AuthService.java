package uno.tau0.gameclub;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uno.tau0.gameclub.dto.AuthRequest;
import uno.tau0.gameclub.dto.AuthResponse;
import uno.tau0.gameclub.dto.RegisterRequest;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    public AuthResponse register(RegisterRequest request) {
        // Create new user with encoded password

        var user = userService.createUserWithoutInvitation(
                request.username(),
                request.displayName(),
                request.password()
        );

        // Save to database
        userRepository.save(user);

        // Generate JWT for immediate login after registration
        var jwt = jwtService.generateToken(user);

        return new AuthResponse(jwt);
    }

    public AuthResponse authenticate(AuthRequest request) {
        // Let Spring Security validate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // If we get here, credentials are valid
        var user = userRepository.findById(request.username())
                .orElseThrow();

        // Generate and return JWT
        var jwt = jwtService.generateToken(user);

        return new AuthResponse(jwt);
    }
}
