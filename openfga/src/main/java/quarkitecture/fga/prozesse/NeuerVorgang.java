package quarkitecture.fga.prozesse;

import lombok.Builder;
import lombok.Data;
import quarkitecture.fga.shareddomain.VorgangData;

@Data
@Builder
public class NeuerVorgang {
    public VorgangData vorgang;
}
