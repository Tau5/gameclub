package uno.tau0.gameclub;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByGroupIdIs(Long groupId);
}
