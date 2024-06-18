package quarkitecture.fga.shareddomain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BenutzerData {

    Team istTeilVonTeam;
    String name;

    public String toUpdate() {
        return "BenutzerData(team:" + istTeilVonTeam + ",name" + name + ")";
    }
}
