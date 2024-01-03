package organisationen.bearbeiten;

import lombok.Builder;
import lombok.Data;
import organisationen.suchen.modell.Organisation;

@Data
@Builder
public class Arbeitsversion {
    Long id;
    Long organisationId;
    Organisation organisation;
}
