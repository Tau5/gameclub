package uno.tau0.gameclub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uno.tau0.gameclub.entity.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, String> {
}
