package quarkitecture.fga.prozesse;

import lombok.Builder;
import lombok.Data;
import quarkitecture.fga.shareddomain.Vorgang;

@Data
@Builder
public class NeuerVorgang {
    public Vorgang vorgang;
}
