package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uno.tau0.gameclub.dto.InitialRegisterRequest;
import uno.tau0.gameclub.entity.User;

import java.util.Optional;

@RestController("/init-setup")
public class InitialSetupController {
    @Autowired
    InitialSetupService service;

    @GetMapping("/available")
    boolean initialSetupAvailable() {
        return service.isAvailable();
    }

    Optional<User> registerInitialUser(InitialRegisterRequest request) {
        return service.registerUser(request.username(), request.displayName(), request.password());
    }
}
