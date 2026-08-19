package uno.tau0.gameclub;

import lombok.experimental.StandardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uno.tau0.gameclub.entity.Invitation;
import uno.tau0.gameclub.entity.User;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvitationService {
    @StandardException
    public class InvalidInvitationException extends Exception { }

    @StandardException
    public class InvitationNotFoundException extends Exception { }

    @StandardException
    public class InvitationLimitReachedException extends Exception { }

    @Value("${invitation.expiration}")
    private long invitationExpiration;

    @Value("${invitation.maxInvitations}")
    private long maxInvitations;

    @Value("${invitation.defaultUses}")
    private int defaultUses;

    @Autowired
    InvitationRepository invitationRepository;

    private Optional<Invitation> consumeInvitation(Invitation invitation) {
        if (invitation.isValid()) {
            invitation.setUsesLeft(invitation.getUsesLeft() - 1);
            return Optional.of(
                    invitationRepository.save(invitation)
            );
        }

        return Optional.empty();
    }

    Invitation tryUseInvitation(UUID invitationCode) throws InvalidInvitationException, InvitationNotFoundException {
        Invitation invitation = invitationRepository.findById(invitationCode).orElseThrow(InvitationNotFoundException::new);
        return consumeInvitation(invitation).orElseThrow(InvalidInvitationException::new);
    }

    private boolean exceedsInvitationLimit(User user) {
        return List.of(invitationRepository.findValidInvitationsByUser(user.getName())).size() > maxInvitations;
    }

    public Iterable<Invitation> getInvitationsOfUser(User user) {
        return invitationRepository.findValidInvitationsByUser(user.getName());
    }

    Invitation generateInvitation(User user) throws InvitationLimitReachedException {
       if (exceedsInvitationLimit(user)) {
           throw new InvitationLimitReachedException();
       }

       var inv = new Invitation(
            user, Instant.now().plus(Duration.ofHours(invitationExpiration)), defaultUses
       );
       return invitationRepository.save(inv);
    }
}
