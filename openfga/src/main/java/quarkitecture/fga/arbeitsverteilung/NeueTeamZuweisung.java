package quarkitecture.fga.arbeitsverteilung;

import lombok.Builder;
import lombok.Data;
import quarkitecture.fga.shareddomain.Team;

@Data
@Builder
public class NeueTeamZuweisung {
    Team team;
    Aufgabenart aufgabenart;
}
