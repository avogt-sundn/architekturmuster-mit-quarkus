package quarkitecture.fga.shareddomain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import quarkitecture.fga.arbeitsverteilung.Aufgabenart;

@Data
@Builder
public class Aufgabe {
    @ToString.Exclude
    Vorgang vorgang;
    Aufgabenart aufgabenart;
}
