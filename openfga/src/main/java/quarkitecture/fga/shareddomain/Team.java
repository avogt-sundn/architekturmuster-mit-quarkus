package quarkitecture.fga.shareddomain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Team {
    Team parent;
    String bezeichnung;
}
