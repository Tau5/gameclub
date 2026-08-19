package uno.tau0.gameclub.dto;

import uno.tau0.gameclub.entity.Invitation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public record InvitationDto(
        UUID id,
        String generatedBy,
        Instant expiry,
        Integer usesLeft
) {
    static public InvitationDto from(Invitation invitation) {
        return new InvitationDto(
                invitation.getId(),
                invitation.getGeneratedBy().getName(),
                invitation.getExpiry(),
                invitation.getUsesLeft()
        );
    }

    static public Iterable<InvitationDto> from(Iterable<Invitation> invitations) {
        var result = new ArrayList<InvitationDto>();
        invitations.forEach(i -> result.add(InvitationDto.from(i)));
        return result;
    }
}