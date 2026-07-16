package uno.tau0.gameclub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRespository extends JpaRepository<Group, Long> {
}
