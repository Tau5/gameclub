package uno.tau0.gameclub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uno.tau0.gameclub.entity.Invitation;
import uno.tau0.gameclub.entity.User;

import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    @Query("select i from Invitation i where i.generatedBy.name = ?1")// and i.usesLeft > 0 and i.expiry > CURRENT_TIMESTAMP")
    Iterable<Invitation> findValidInvitationsByUser(String username);
}
