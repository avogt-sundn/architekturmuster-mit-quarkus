package quarkitecture.fga.prozesse;

import lombok.Builder;
import lombok.Data;
import quarkitecture.fga.shareddomain.Aufgabe;

@Data
@Builder
public class NeueAufgabe {
    Aufgabe aufgabe;
}
