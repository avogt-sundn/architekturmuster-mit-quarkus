package quarkitecture.fga.components;

import lombok.Builder;
import lombok.Data;
import quarkitecture.fga.shareddomain.BenutzerData;

@Data
@Builder
public class NeuerBenutzer {
    BenutzerData benutzer;
}
