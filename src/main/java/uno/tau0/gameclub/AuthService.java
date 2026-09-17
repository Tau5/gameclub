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
import uno.tau0.gameclub.entity.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final InitialSetupService initialSetupService;

    @Autowired
    UserService userService;

    public AuthResponse register(RegisterRequest request) throws InvitationService.InvalidInvitationException, InvitationService.InvitationNotFoundException {
        // Create new user with encoded password

        var maybeUser = initialSetupService.registerUser(
                request.username(),
                request.displayName(),
                request.password()
        );

        if (maybeUser.isEmpty()) {
            maybeUser = Optional.of(userService.createUser(
                    request.username(),
                    request.displayName(),
                    request.password(),
                    request.invitationCode().orElse(null)
            ));
        }

        var user = maybeUser.get();

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
