package quarkitecture.fga.shareddomain;

import lombok.Builder;
import lombok.Data;
import quarkitecture.fga.arbeitsverteilung.Aufgabenart;

@Data
@Builder
public class Anfrage {
    BenutzerData wer;
    Aufgabenart was;
    VorgangData wo;
}
