package quarkitecture.fga.shareddomain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BenutzerFromToken {
    String mandantId;
    String name;
    String benutzerId;

}
