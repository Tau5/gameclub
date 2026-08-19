package uno.tau0.gameclub;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uno.tau0.gameclub.dto.AuthRequest;
import uno.tau0.gameclub.dto.AuthResponse;
import uno.tau0.gameclub.dto.RegisterRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws InvitationService.InvalidInvitationException, InvitationService.InvitationNotFoundException {
        // Register new user and return JWT
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        // Authenticate and return JWT
        IO.println(request.password());
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @ExceptionHandler(InvitationService.InvitationNotFoundException.class)
    public ResponseEntity<String> invitationNotFound(InvitationService.InvitationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation not found");
    }

    @ExceptionHandler(InvitationService.InvalidInvitationException.class)
    public ResponseEntity<String> invalidInvitation(InvitationService.InvalidInvitationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid invitation");
    }

    @ExceptionHandler(InvitationService.InvitationLimitReachedException.class)
    public ResponseEntity<String> invitationLimitReached(InvitationService.InvitationLimitReachedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invitation limit reached");
    }
}
