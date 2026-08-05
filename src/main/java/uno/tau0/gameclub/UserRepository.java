package uno.tau0.gameclub;

import org.springframework.data.jpa.repository.JpaRepository;
import uno.tau0.gameclub.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}
