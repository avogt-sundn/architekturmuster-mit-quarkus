package quarkitecture.fga.shareddomain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BenutzerData {

    public Team istTeilVonTeam;
    public String name;

}
